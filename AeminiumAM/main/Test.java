package main;
import java.util.ArrayList;

import actor.*;

public class Test {
	
	static AeminiumRuntime art = new AeminiumRuntime();

	public static class TestActor extends Actor {
		@writable
		public int val=3;


		public int result;

		public TestActor() {
			super();
		}
		
		@Override
		public void react(Object obj){
			
			result = 42 + ((Integer)obj) + val;
			
			Dispatcher.handle(this,"react1",obj);
			
			//Dispatcher.handle(this,"react2",obj);
						
			/* With this sleep, we are giving time to subActor performs his react*/
			/*
			try {
				Thread.currentThread();
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		}
		
		@SuppressWarnings("unused")
		@VarUsed(varNames = "result")
		private void react1(Object m){
			System.out.println("react1 em execu��o!");
		}
		
		@SuppressWarnings("unused")
		private void react2(Object m){
			System.out.println("react2 em execu��o!");
		}

	}

	public static void main(String[] args) {

		
		TestActor a = new TestActor();
		
		//a.sendMessage(3);

		
		ArrayList<String> c=BCEL.checkFields("TestActor", "react1");
		
		if(c==null){
			System.out.println("BCEL is returning null");
		} else {
			for(String s :c){
				System.out.println(s);
			}
		}
		
		
		art.endAeminiumRuntime();
	
		System.out.println("Testing result: "+a.result);

	}

}
