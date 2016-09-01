package my;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Client {
	private final ExecutorService executor = Executors.newFixedThreadPool(120);
	private Random random = new Random();
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		Client client = new Client();
//		client.render();
//		client.render2();
//		client.render3();
		client.render4();
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
					Thread.sleep(1000*random.nextInt(100));
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
	
	//为任务设置时限
	private void render3(){
		long endMillis = System.currentTimeMillis() + 1000*2;
		Future<String> f = executor.submit(new Callable<String>() {

			@Override
			public String call() throws Exception {
				// TODO Auto-generated method stub
				String name = Thread.currentThread().getName();
				System.out.println(name+"提交");
				Thread.sleep(1000*5);
				return name;
			}
			
		});
		//只等待指定长度时间
		long timeLeft = endMillis - System.currentTimeMillis();
		System.out.println("只等待指定长度时间"+timeLeft);
		try {
			String result = f.get(timeLeft, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("执行被中断");
//			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("执行过程出现异常");
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("执行超时");
		}finally{
			executor.shutdown();
		}
	}
	//为任务设置时限2
	private void render4(){
		
		List<QuoteTask> tasks = new ArrayList<Client.QuoteTask>();
		tasks.add(new QuoteTask("aaa"));
		tasks.add(new QuoteTask("bbb"));
		tasks.add(new QuoteTask("ccc"));
		tasks.add(new QuoteTask("ddd"));
		tasks.add(new QuoteTask("eee"));
		tasks.add(new QuoteTask("fff"));
		tasks.add(new QuoteTask("ggg"));
		tasks.add(new QuoteTask("hhh"));
		
	
		try {
			List<Future<String>> futures = executor.invokeAll(tasks, 2000, TimeUnit.MILLISECONDS);
			System.out.println("futures.size()="+futures.size());
			for(Future<String> future:futures){
				try {
					System.out.println(future.get());
				} catch (CancellationException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					System.out.println("执行超时");
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					System.out.println("执行过程中出现异常");
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("任务提交执行被中断");
		} finally{
			executor.shutdown();
		}

		
	}
	
	/**
	 * 睡眠时间 秒为单位
	 * @param n
	 * @throws InterruptedException 
	 */
	private void sleep(int n) throws InterruptedException{
		Thread.currentThread().sleep(1000*n);
	}
	
	
	class QuoteTask implements Callable<String>{
		
		private String name;
		
		
		
		public QuoteTask() {
			super();
		}

		public QuoteTask(String name) {
			super();
			this.name = name;
		}

		@Override
		public String call() throws Exception {
			// TODO Auto-generated method stub
			System.out.println(name+"开始执行。。。。。");
			sleep(random.nextInt(100));
			return name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		
		
	}
}
