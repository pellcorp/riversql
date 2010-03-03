
package com.riversql;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class IDManager {
	protected IDManager(){}
	private static ThreadLocal<IDManager> threadlocalIDManager = new ThreadLocal<IDManager>();
	public static IDManager get()
	{
		IDManager ex = threadlocalIDManager.get();
		return ex;
	}
	public static void set(IDManager ex)
	{
		threadlocalIDManager.set(ex);
	}
	AtomicInteger ai=new AtomicInteger(0);
	AtomicInteger ai2=new AtomicInteger(0);
	HashMap<String, Object> map=new HashMap<String, Object>();
	protected  int nextInt(){
		return ai.incrementAndGet();
	}
	public  String nextID(){
		return "0000"+Integer.toHexString(nextInt());
	}
	synchronized public  void put(String id, Object schemaNode) {
		map.put(id,schemaNode);
		
	}
	synchronized public  Object get(String id) {
		return map.get(id);
	}
	public int nextSessionID(){
		return ai2.incrementAndGet();
	}
}
