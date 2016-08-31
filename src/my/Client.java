package my;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Client {
	private final ExecutorService executor = Executors.newFixedThreadPool(2);
	private Random random = new Random(10);
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		Client client = new Client();
//		client.render();
		client.render2();
	}

	private void render() throws InterruptedException, ExecutionException{
		List<Future<String>> futrues = new ArrayList<Future<String>>();
		for(int i=0;i<100;i++){
			Future<String> future = executor.submit(new Callable<String>() {

				@Override
				public String call() throws Exception {
					// TODO Auto-generated method stub
					
					String name = Thread.currentThread().getName();
					System.out.println(name+"提交");
					Thread.sleep(1000);
					return name;
				}
				
			});
			futrues.add(future);
		}
		
		for(Future<String> each:futrues){
			System.out.println(each.get());
		}
		
		executor.shutdown();
	}
	
	private void render2() throws InterruptedException, ExecutionException{
		CompletionService<String>completionService = new ExecutorCompletionService<String>(executor);
		for(int i=0;i<100;i++){
			completionService.submit(new Callable<String>() {

				@Override
				public String call() throws Exception {
					// TODO Auto-generated method stub
					
					String name = Thread.currentThread().getName();
					System.out.println(name+"提交");
					Thread.sleep(1000);
					return name;
				}
				
			});
		}
		
		for(int i=0;i<100;i++){
			Future<String> future = completionService.take();
			String str = future.get();
			System.out.println(str);
		}
		System.out.println("任务执行完毕");
		executor.shutdown();
	}
}
