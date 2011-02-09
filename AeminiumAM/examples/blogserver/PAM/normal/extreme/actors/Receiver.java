package examples.blogserver.PAM.normal.extreme.actors;

import actor.Actor;

public class Receiver extends Actor{
	
	long workTime;
	
	@Override
	protected void react(Object obj) {
		//System.out.println(obj);
	}
	
	private void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

}
