package main;
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
			
			Dispatcher.dispatcToAM(this,"react1",obj);
						
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
		
		@writable
		public void react1(Object m){
			System.out.println("react1 em execu��o!");
		}
		
		public void react2(Object m){
			System.out.println("react2 em execu��o!");
		}

	}

	public static void main(String[] args) {

		
		TestActor a = new TestActor();
		
		a.sendMessage(3);

		art.endAeminiumRuntime();
	
		System.out.println("Testing result: "+a.result);

	}

}
