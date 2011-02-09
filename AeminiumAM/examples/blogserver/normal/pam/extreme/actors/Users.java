package examples.blogserver.normal.pam.extreme.actors;

import java.util.ArrayList;

import actor.Actor;
import actor.annotations.Read;
import aeminium.runtime.Runtime;

public class Users extends Actor{
	boolean useSpin;
	int x;
	int numNames = 100;
	long workTime = 0;
	ArrayList<String> users;
	
	private Add addActor;
	
	public Users(Add addActor, long workTime, Runtime rt, boolean useSpin){
		this.useSpin = useSpin;
		this.rt = rt;
		this.addActor = addActor;
		this.workTime = workTime;
		
		users = new ArrayList<String>();
		
		for(int i=0; i<500; i++){
			users.add("auto-gen user");
		}
		users.add("Ace");
		
	}

	@Read
	public void requestPermission(String user, String msg) {
		if(useSpin){
			long sleepTime = workTime; // convert to nanoseconds
		    long startTime = System.nanoTime();
		    while ((System.nanoTime() - startTime) < sleepTime) {}
		}
		if(users.contains(user)){
			if(getAddActor()==null){
				System.out.println("addActor is null");
			}
			getAddActor().confirmReq(user,msg, true);
		} else {
			getAddActor().confirmReq(user,msg, false);
		}		
	}
	
	@Read
	private Add getAddActor(){
		return addActor;
	}

	@Read
	public void setAddActor(Add addActor) {
		this.addActor = addActor;
	}

}
