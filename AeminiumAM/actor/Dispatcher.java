package actor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import byteCodeOperations.ByteCodeOpASM;

import aeminium.runtime.Body;
import aeminium.runtime.DataGroup;
import aeminium.runtime.Runtime;
import aeminium.runtime.Task;

public class Dispatcher {
	
	public static void handle(final Actor actor, String name, final Object msg) {
		final Method m;
		final Class<?> c = actor.getClass();
		
		if ((m = checkMethod(c, name)) != null) {
			
			HashMap<String,Boolean> varUsed=null;
			try{
				varUsed = getAllFieldsTypeByASM(m.getName(), actor);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(Constants.Constants.debug_varUsedInDispatcher){
				for(Entry<String,Boolean> entry : varUsed.entrySet()){
					System.out.println("key: "+entry.getKey()+" value: "+entry.getValue());
				}
			}
			
			//temos as variaveis escritas e temos a lista, � s� obter as deps e passar
			if (!methodIsWritable(actor,name,varUsed)) {
				System.out.println(m.getName()+" is going to be par from dispatcher");

				Task t1 = AeminiumRuntime.rt.createNonBlockingTask(new Body() {
					@Override
					public void execute(Runtime rt, Task current)
							throws Exception {
						try {
							
							try {
								m.setAccessible(true);

								m.invoke(actor, msg);

								// Handle any exceptions thrown by method to be
								// invoked.
							} catch (InvocationTargetException x) {
								x.getCause();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, Runtime.NO_HINTS);
				
				Collection<Task> bb = getFuncDependencies(actor, varUsed, t1);
				
				for(int j=0 ; j<bb.size(); j++){
					System.out.println(bb);
				}
				
				AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT,
						bb);
				System.out.println("after: "+t1);

			} else {
				System.out.println(m.getName()+" is going to be an Atomic task from dispatcher");

				/* Useless Datagroup created to pass as arg in createAtomicTask */
				DataGroup dg = AeminiumRuntime.rt.createDataGroup();

				Task t1 = AeminiumRuntime.rt.createAtomicTask(new Body() {
					@Override
					public void execute(Runtime rt, Task current)
							throws Exception {
						try {
							try {
								m.setAccessible(true);

								m.invoke(actor, msg);

								// Handle any exceptions thrown by method to be
								// invoked.
							} catch (InvocationTargetException x) {
								x.getCause();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, dg, Runtime.NO_HINTS);
				
				AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT,
						getFuncDependencies(actor, varUsed, t1));
			}
		} else {
			System.out.println("Inexistent method '"+name+"'.");
		}
	}

	private static HashMap<String, Boolean> getAllFieldsTypeByASM(String name,
			Actor actor) {
		HashMap<String, Boolean> hash = ByteCodeOpASM.getWritableFields(name, actor);
		HashMap<String, Boolean> temp;
		
		ArrayList<String> methodsInThisMethod = ByteCodeOpASM.getUsedMethods(name, actor);
		
		for(String s: methodsInThisMethod){
			if(actor.getMethodsName().contains(s)){
				temp = getAllFieldsTypeByASM(s, actor);
				
				for(Entry<String,Boolean> entry : temp.entrySet()){
					if(hash.containsKey(entry.getKey())){
						hash.put(entry.getKey(), entry.getValue() || hash.get(entry.getKey()));
					} else {
						hash.put(entry.getKey(), entry.getValue());
					}
				}
			}
		}
	
		return hash;
	}
	
	private static boolean methodIsWritable(Actor a, String methodName, HashMap<String,Boolean> varUsed) {
		for (Field f: a.getClass().getFields()) {
		    for (Entry<String, Boolean> entry : varUsed.entrySet()) {
				if(f.getName().equals(entry.getKey()) && entry.getValue() == true){
					return true;
				}
			}
		}
		return false;
	}

	private static Method checkMethod(Class<?> c, String name) {

		Method[] allMethods = c.getDeclaredMethods();
		for (Method m : allMethods) {
			if (m.getName() == name)
				return m;
		}

		return null;
	}
	
	/*TODO: check if this methods needs to be synchronized*/
	private static Collection<Task> getFuncDependencies(Actor actor, HashMap<String, Boolean> usedVars, Task task){
				
		ArrayList<Task> deps = new ArrayList<Task>();
		
		for (Entry<String, Boolean> entry : usedVars.entrySet()) {
			String varName = entry.getKey();
			
			/* Get all the current tasks that this task is dependent */
			if(actor.getvarDep().containsKey(varName)){
				if(entry.getValue() == true){
					getDepForWritable(actor.getvarDep().get(varName),deps);
				} else {
					getDepForReadable(actor.getvarDep().get(varName),deps);
				}
				refreshVarDeps(actor.getvarDep().get(varName), task, entry.getValue());
			}
		}
		
		return deps;
	}
	
	private static void getDepForWritable(Vector<DependencyTask> vector, ArrayList<Task> deps){
		if(!vector.isEmpty() && vector.get(vector.size()-1).isWritable){
			deps.add(vector.get(vector.size()-1).task);
			return;
		} else {
			for(int i=vector.size()-1; i>=0 && !vector.get(i).isWritable; i--){
				deps.add(vector.get(i).task);
			}
		}
		return;
	}
	
	private static void getDepForReadable(Vector<DependencyTask> vector, ArrayList<Task> deps){
		for(int i=vector.size()-1; i>=0; i--){
			if(vector.get(i).isWritable==true){
				deps.add(vector.get(i).task);
				return;
			}
		}
	}
	
	private static void refreshVarDeps(Vector<DependencyTask> v, Task task, Boolean isWritable){	
		if(isWritable){
			v.clear();
			v.add(new DependencyTask(task,isWritable));
		} else {
			v.add(new DependencyTask(task,isWritable));
		}
	}
	
}
