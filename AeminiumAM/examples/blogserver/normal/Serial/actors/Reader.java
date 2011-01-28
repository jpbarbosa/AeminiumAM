package examples.blogserver.normal.Serial.actors;

import actor.Actor;
import actor.annotations.*;

public class Reader extends Actor{
	Posts posts;
	long workTime=0;

	public Reader(Posts posts, long workTime){
		this.posts = posts;
		this.workTime = workTime;
	}
	
	@Write
	public void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

	@Write
	public void reqReadPost(int id, String user) {
		posts.readPost(id,user);
	}

}
