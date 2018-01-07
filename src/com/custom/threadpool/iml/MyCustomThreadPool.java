package com.custom.threadpool.iml;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MyCustomThreadPool 
{
	private static AtomicInteger poolSize = new AtomicInteger(0);
	private LinkedBlockingQueue<Runnable> taskQueue;

	private List<WorkerThread> workerThreads;

	private class WorkerThread extends Thread 
	{
		private LinkedBlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

		private WorkerThread(String threadName, LinkedBlockingQueue<Runnable> taskQueue) 
		{
			super(threadName);
			this.taskQueue = taskQueue;
		}

		@Override
		public void run() 
		{
			while(true)
			{
				Runnable runnable = taskQueue.poll();
				if (runnable != null)
				{
					System.out.println("Task has been taken by thread :" + Thread.currentThread().getName());
					runnable.run();
					System.out.println("Task completed by thread :" + Thread.currentThread().getName());
				}
			}
		}
	}

	private MyCustomThreadPool(int capacity)
	{
		poolSize.incrementAndGet();
		this.taskQueue = new LinkedBlockingQueue<>();
		this.workerThreads = new ArrayList<>();

		for (int index = 0; index < capacity; ++index)
		{
			WorkerThread thread = new WorkerThread("SimpleThreadpool-" + poolSize.get() + "-Thread-" + index, this.taskQueue);
			thread.start();
			this.workerThreads.add(thread); 
		}
	}
	public static MyCustomThreadPool getThreadPoolInstance()
	{
		return getThreadPoolInstance(Runtime.getRuntime().availableProcessors());
	}
	
	public static MyCustomThreadPool getThreadPoolInstance(int nThread) 
	{
		return new MyCustomThreadPool(nThread); 
	}

	// method to insert new arrived task to our blocking queue
	public void enqueueTask(Runnable runnable) 
	{
		taskQueue.add(runnable);
	}
}
