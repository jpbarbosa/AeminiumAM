package examples.blogserver.actors;

import java.util.Hashtable;

import examples.blogserver.AddPost;
import examples.blogserver.Gen;
import examples.blogserver.ReadPost;

import actor.Actor;
import actor.Dispatcher;
import annotations.writable;

public class Posts extends Actor{
	Receiver receiver;
	
	@writable
	int lastIndex;
	
	Hashtable<Integer,String> hashPosts;
	Hashtable<Integer,String> hashUsersID;
	
	public Posts(Receiver receiver, int postsNum){
		super();
		
		hashPosts = new Hashtable<Integer, String>();
		hashUsersID = new Hashtable<Integer, String>();
		
		int i;
		Gen g = new Gen(postsNum);
		for( i=0; i<postsNum; i++){
			hashPosts.put(i, g.getSentences()[i]);
			hashUsersID.put(i, "system");
		}
		
		lastIndex = i--;
		
		this.receiver = receiver;
	}

	@Override
	protected void react(Object obj) {
		if(obj instanceof AddPost){
			hashPosts.put(++lastIndex, ((AddPost)obj).post);
			hashUsersID.put(lastIndex, ((AddPost)obj).user);
			
			//System.out.println("Posts ln 38");
		} else if( obj instanceof ReadPost){
			//Dispatcher.handle(this, "sendNotification", (ReadPost)obj);
			sendNotification((ReadPost)obj);
		}
	}
	
	public void sendNotification(ReadPost rp){
		if(rp.id>=lastIndex){
			receiver.sendMessage("User "+rp.user+" requested post "+(lastIndex-1)+" by "+
					hashUsersID.get(lastIndex-1) + ": "+hashPosts.get(lastIndex-1));
		} else {
			receiver.sendMessage("User "+rp.user+" requested post "+rp.id+" by "+
					hashUsersID.get(rp.id) + ": "+hashPosts.get(rp.id));
		}
		
	}
	
}
