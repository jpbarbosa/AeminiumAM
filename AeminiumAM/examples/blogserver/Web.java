package examples.blogserver;

import actor.AeminiumRuntime;
import examples.blogserver.actors.*;

public class Web {

	public static AeminiumRuntime art;
	
	Receiver receiver;
	Add adder;
	Posts posts;
	Users users;
	Reader reader;
	
	Web(int postsNum){
		receiver = new Receiver();
		users = new Users(adder);
		posts = new Posts(receiver,postsNum);
		adder = new Add(users, posts, receiver);
		reader = new Reader(posts);
		
		users.addActor = adder;
	}
	
	public static void main(String[] args) {
		art = new AeminiumRuntime();
		
		Web web = new Web(100);
		
		web.adder.sendMessage(new PutRequest("Ace","[Vamos l� por isto a funcionar]"));
		
		web.reader.sendMessage(new ReadPost(6,"USER1"));
		art.endAeminiumRuntime();
		
	}
	
}
