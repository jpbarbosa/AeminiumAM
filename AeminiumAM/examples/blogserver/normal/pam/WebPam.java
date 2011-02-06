package examples.blogserver.normal.pam;

import examples.blogserver.normal.pam.actors.*;

public class WebPam {
	
	boolean useSpin = false;
	
	public static aeminium.runtime.Runtime rt;
	
	long workTime = 250000;
	
	Receiver receiver;
	public Add adder;
	Posts posts;
	Users users;
	public Reader reader;
	
	public WebPam(int workTime, int postsNum, boolean useSpin){
		this.workTime = workTime;
		this.useSpin = useSpin;
		receiver = new Receiver(useSpin);
		users = new Users(adder,workTime,rt,useSpin);
		posts = new Posts(receiver,postsNum,workTime,rt,useSpin);
		adder = new Add(users, posts, receiver,workTime,rt,useSpin);
		reader = new Reader(posts,workTime,rt,useSpin);
		
		users.setAddActor(adder);
	}
	
	public static void main(String[] args) {
		WebPam.rt = aeminium.runtime.implementations.Factory.getRuntime();
		WebPam.rt.init();
		
		WebPam web = new WebPam(200000,100,false);
		
		web.adder.addMessage("Ace","[Vamos l� por isto a funcionar]");
		
		web.reader.reqReadPost(6,"USER1");
		
		WebPam.rt.shutdown();
		
	}
	
}
