package examples.blogserver.Serial.dist;

import java.util.Random;

import actor.AeminiumRuntime;

import examples.blogserver.Serial.dist.actors.*;

public class WebSerialDist {
	public int numCopies=20;
	boolean useSpin = false;
	public static AeminiumRuntime art;
	
	long workTime = 250000;
	
	Receiver [] receiverArray = new Receiver[numCopies];
	
	public Add [] addActorArray = new Add[numCopies];
	
	Posts posts;

	Users [] usersArray = new Users[numCopies];
	
	public Reader [] readersArray = new Reader[numCopies];
	
	public WebSerialDist(int numCopies, int workTime,int postsNum, boolean useSpin){
		this.numCopies = numCopies;
		this.workTime = workTime;
		this.useSpin = useSpin;
		int i;

		for(i=0;i<numCopies;i++){
			receiverArray[i] = new Receiver();
		}
		
		for(i=0;i<numCopies;i++){
			usersArray[i] = new Users(addActorArray,workTime, numCopies, useSpin);
		}
		
		posts = new Posts(receiverArray,postsNum,workTime, numCopies, useSpin);
		
		for(i=0;i<numCopies;i++){
			addActorArray[i] = new Add(usersArray, posts, receiverArray,workTime, numCopies, useSpin);
		}
		
		for(i=0;i<numCopies;i++){
			readersArray[i] = new Reader(posts ,workTime, numCopies, useSpin);
		}
		
		for(i=0;i<numCopies;i++){
			if(usersArray[i].addActorArray == null){
				usersArray[i].addActorArray = new Add[numCopies];
				usersArray[i].addActorArray = addActorArray;
			} else {
				usersArray[i].addActorArray = addActorArray;
			}
		}
	}
	
	public static void main(String[] args) {
		art = new AeminiumRuntime();
		
		WebSerialDist web = new WebSerialDist(3,200000,100,false);
		
		Random ran = new Random(20);
		
		web.addActorArray[ran.nextInt(web.numCopies)].sendMessage(new PutRequest("Ace","[Vamos l� por isto a funcionar]"));
		
		web.readersArray[ran.nextInt(web.numCopies)].sendMessage(new ReadPost(6,"USER1"));
		art.endAeminiumRuntime();
		
	}
	
}
