package examples.blogserver.PAM.dist.actors;

import java.util.Hashtable;
import java.util.Random;

import examples.blogserver.PAM.dist.AddPost;
import examples.blogserver.PAM.dist.Gen;
import examples.blogserver.PAM.dist.ReadPost;

import actor.Actor;
import actor.Dispatcher;
import annotations.writable;

public class Posts extends Actor{
	Receiver[] receivers;
	
	@writable
	int lastIndex;
	
	Hashtable<Integer,String> hashPosts;
	Hashtable<Integer,String> hashUsersID;
	
	long workTime;
	int numCopies;
	
	Random ran;
	
	public Posts(Receiver[] receiverArray, int postsNum, long workTime, int numCopies){
		super();
		
		this.workTime = workTime;
		this.numCopies = numCopies;
		ran = new Random(10);
		
		hashPosts = new Hashtable<Integer, String>();
		hashUsersID = new Hashtable<Integer, String>();
		
		int i;
		Gen g = new Gen(postsNum);
		for( i=0; i<postsNum; i++){
			hashPosts.put(i, g.getSentences()[i]);
			hashUsersID.put(i, "system");
		}
		
		lastIndex = i--;
		
		this.receivers = receiverArray;
	}

	@Override
	protected void react(Object obj) {
		if(obj instanceof AddPost){
			hashPosts.put(++lastIndex, ((AddPost)obj).post);
			hashUsersID.put(lastIndex, ((AddPost)obj).user);
			
		} else if( obj instanceof ReadPost){
			//Dispatcher.handle(this, "sendNotification", (ReadPost)obj);
			sendNotification((ReadPost)obj);
		}
	}
	
	public void sendNotification(ReadPost rp){
		if(rp.id>=lastIndex){
			receivers[ran.nextInt(numCopies)].sendMessage("User "+rp.user+" requested post "+(lastIndex-1)+" by "+
					hashUsersID.get(lastIndex-1) + ": "+hashPosts.get(lastIndex-1));
		} else {
			receivers[ran.nextInt(numCopies)].sendMessage("User "+rp.user+" requested post "+rp.id+" by "+
					hashUsersID.get(rp.id) + ": "+hashPosts.get(rp.id));
		}
		
	}
	
	private void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}
	
}
