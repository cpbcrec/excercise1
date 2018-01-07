package com.custom.threadpool.iml;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestApp 
{
	public static void main(String args[])
	{
		int runnableCount = 2;
		MyCustomThreadPool threadpool = MyCustomThreadPool.getThreadPoolInstance();
        final AtomicInteger count = new AtomicInteger(0);
        
        // Java 8 lambda expression, Runnable is a functional interface so it can be expressed as a lambda 
        Runnable r = () -> {
        	System.out.println(Thread.currentThread().getName() + " is now starting the task");
        	try 
        	{
        		count.getAndIncrement();
        		//pretend that thread is actually doing the task
				TimeUnit.SECONDS.sleep(10);
			} 
        	catch (InterruptedException e) 
        	{
				e.printStackTrace();
			}
        };
        
        for (int i = 0; i < runnableCount; i++) 
        {
            threadpool.enqueueTask(r);
        }
	}
}
