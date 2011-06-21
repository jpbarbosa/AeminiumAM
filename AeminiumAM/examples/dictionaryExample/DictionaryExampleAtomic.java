package examples.dictionaryExample;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import actor.Actor;
import actor.AeminiumRuntime;
import annotations.writable;

public class DictionaryExampleAtomic {
	
	public static long workTime;
	public static boolean useSpin;
	public static int noMsgs;
	public static int noMsgsRead;
	
	public static Dictionary dictionary;
	public static Reader reader;
	public static Receiver receiver;
	
	public DictionaryExampleAtomic(int num, int num2, boolean useSpin, long workTime){
		DictionaryExampleAtomic.workTime = workTime;
		DictionaryExampleAtomic. useSpin = useSpin;
		noMsgsRead = num;
		noMsgs = num2;
		art=new AeminiumRuntime();
		reader = new Reader();
		receiver = new Receiver();
		dictionary = new Dictionary();
	}
	
	
	public static class Reader extends Actor{
		@writable
		static public String [] words;
		
		Reader(){
			super();
			
			words = new String[noMsgs];
			
			try {
				FileInputStream fstream = new FileInputStream("500Word1.txt");
			    DataInputStream in = new DataInputStream(fstream);
			    BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				int index=0;
				String word;
				while (index<noMsgs && (word = br.readLine()) != null){
					words[index] = word;
					index++;
				}
				
				in.close();
				
				Random random = new Random(System.currentTimeMillis());
				int rand;
				String temp;
				
		        for (int i = 0; i < words.length; i++) {
		            rand = (random.nextInt() & 0x7FFFFFFF) % words.length;
		            temp = words[i];
		            words[i] = words[rand];
		            words[rand] = temp;
		        }
				
			
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		@Override
		protected void react(Object obj) {
			if(useSpin){
				long sleepTime = workTime; // convert to nanoseconds
			    long startTime = System.nanoTime();
			    while ((System.nanoTime() - startTime) < sleepTime) {}
			}
			for(int i=0; i<noMsgsRead; i++ ){
				dictionary.sendMessage(words[i]);
			}
		}
		
	}
	
	public static class Dictionary extends Actor{
		@writable
		static public String [] keyWords;
		@writable
		static public String [] valueWords;
		
		Dictionary(){
			super();
			
			keyWords = new String[noMsgs];
			valueWords = new String[noMsgs];
			
			try {
				FileInputStream fstream = new FileInputStream("500Word1.txt");
			    DataInputStream in = new DataInputStream(fstream);
			    BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				int index=0;
				String word;
				
				while (index<noMsgs && (word = br.readLine()) != null){
					keyWords[index] = word;
					valueWords[index] = word+"-V";
					index++;
				}
				
				in.close();
			
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		@Override
		protected void react(Object obj) {
			if(useSpin){
				long sleepTime = workTime; // convert to nanoseconds
			    long startTime = System.nanoTime();
			    while ((System.nanoTime() - startTime) < sleepTime) {}
			}
			try{
				String word = ((String)obj);
				
				for(int i=0; i<keyWords.length; i++){
					if(keyWords[i].equals(word)){
						receiver.sendMessage(valueWords[i]);
					}
				}
			}catch(Exception e){
				System.out.println("react Dictionary");
			}
		}
		
	}
	
	public static class Receiver extends Actor{
		@writable
		int x;
		@Override
		protected void react(Object obj) {
			if(useSpin){
				long sleepTime = workTime; // convert to nanoseconds
			    long startTime = System.nanoTime();
			    while ((System.nanoTime() - startTime) < sleepTime) {}
			}
		}
		
	}
	
	public static AeminiumRuntime art;

}
