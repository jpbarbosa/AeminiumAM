package benchmarks.blogServerBenchmark.dist;

import java.util.Random;

import actor.AeminiumRuntime;

import examples.blogserver.PAM.dist.extreme.PutRequest;
import examples.blogserver.PAM.dist.extreme.ReadPost;
import examples.blogserver.PAM.dist.extreme.WebPamDist;

public class DistPamExtBench {
	
	public static void main(String[] args) {
		
		for(int num=100000; num<=100000; num+=10000){
			long total=0;
			
			System.out.println();
			System.out.println(num);
			for(int x=0; x<15; x++){
				WebPamDist.art = new AeminiumRuntime();
				
				WebPamDist web = new WebPamDist(3,5000000,1000,true);
				
				Random randP = new Random(10);
				Random randMID = new Random((int) (num+0.2*num));
				Random ran = new Random(15);
				
				long start = System.nanoTime();
				for(int i = 0; i<num; i++){
					if(randP.nextInt(10)<2){
						web.addActorArray[ran.nextInt(web.numCopies)].sendMessage(new PutRequest("Ace","Post gerado na "+i+"iteracao."));
					} else {
						web.readersArray[ran.nextInt(web.numCopies)].sendMessage(new ReadPost(randMID.nextInt(110),"BenchUser"+i));
					}
				}
				WebPamDist.art.endAeminiumRuntime();
				total=(System.nanoTime()-start);
				System.out.println(total);
			}
		}
	}
}
