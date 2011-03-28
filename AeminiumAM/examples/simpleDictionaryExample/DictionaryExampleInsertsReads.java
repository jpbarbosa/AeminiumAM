package examples.simpleDictionaryExample;

import actor.Actor;
import actor.annotations.*;
import aeminium.runtime.Runtime;

public class DictionaryExampleInsertsReads {
	public static boolean useSpin = false;
	public static boolean useFor = false;
	public static long workTime;
	
	public static Dictionary dictionary;
	public static Reader reader;
	public static Receiver receiver;
	public static Runtime rt;
	
	public DictionaryExampleInsertsReads(boolean useSpin, boolean useFor, long workTime){
		DictionaryExampleInsertsReads.useFor = useFor;
		DictionaryExampleInsertsReads.useSpin = useSpin;
		DictionaryExampleInsertsReads.workTime = workTime;
		reader = new Reader(rt);
		receiver = new Receiver(rt);
		dictionary = new Dictionary(rt);
	}
	
	public static class Reader extends Actor{
		
		public Reader(Runtime rt){
			super();
			this.rt = rt;
			
		}
		
		
		
		@Read
		public void makeReadRequest(Object obj) {
			if(useSpin){
				long sleepTime = workTime; // convert to nanoseconds
			    long startTime = System.nanoTime();
			    while ((System.nanoTime() - startTime) < sleepTime) {}
			} else if(useFor){
				for(int i=0; i< workTime; i++){}
			}
			dictionary.getVal("read request");
		}
		
		@Read
		public void makeWriteRequest(Object obj) {
			if(useSpin){
				long sleepTime = workTime; // convert to nanoseconds
			    long startTime = System.nanoTime();
			    while ((System.nanoTime() - startTime) < sleepTime) {}
			} else if(useFor){
				for(int i=0; i< workTime; i++){}
			}
			dictionary.insertEntry("write request");
		}
		
	}
	
	public static class Dictionary extends Actor{
		
		public Dictionary(Runtime rt){
			super();
			this.rt = rt;
		}

		@Read
		public void getVal(String word) {
			if(useSpin){
				long sleepTime = workTime; // convert to nanoseconds
			    long startTime = System.nanoTime();
			    while ((System.nanoTime() - startTime) < sleepTime) {}
			}  else if(useFor){
				for(int i=0; i< workTime; i++){}
			}
			
			receiver.sendMessage("value "+word);
		}
		
		@Write
		public void insertEntry(String word) {
			if(useSpin){
				long sleepTime = workTime; // convert to nanoseconds
			    long startTime = System.nanoTime();
			    while ((System.nanoTime() - startTime) < sleepTime) {}
			}  else if(useFor){
				for(int i=0; i< workTime; i++){}
			}
			
			receiver.sendMessage("confirmation "+word);
		}
		
	}
	
	public static class Receiver extends Actor{
		
		public Receiver(Runtime rt) {
			this.rt = rt;
		}

		@Read
		public void sendMessage(String value) {
			if(useSpin){
				long sleepTime = workTime; // convert to nanoseconds
			    long startTime = System.nanoTime();
			    while ((System.nanoTime() - startTime) < sleepTime) {}
			}  else if(useFor){
				for(int i=0; i< workTime; i++){}
			}
		}
		
	}

}