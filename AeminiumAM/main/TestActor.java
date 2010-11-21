package main;

import actor.Actor;
import actor.Dispatcher;
import annotations.writable;

public class TestActor extends Actor{
		@writable
		static public int val=3;
		
		@writable
		static public int result;

		public TestActor() {
			super();
		}
		
		@Override
		protected void react(Object obj){
			
			result = 42 + ((Integer)obj) + val;			
			
			Dispatcher.handle(this,"react1",obj);
			
			Dispatcher.handle(this,"react2",obj);
					
		}
		
		@SuppressWarnings("unused")
		private void react1(Object m){
			result=result+2;
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("react1 em execu��o!");
		}
		
		@SuppressWarnings("unused")
		private void react2(Object m){
			System.out.println("react2 em execu��o! result="+result);
		}
}
