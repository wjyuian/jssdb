/*
* 2014-11-20 下午2:09:01
* 吴健 HQ01U8435
*/

package com.wj.jssdb.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.pool.impl.GenericObjectPool;

public class BasePool<T> {
	private GenericObjectPool internalPool;
	
	private final PoolableObjectFactoryManager factorysManager;
	private GenericObjectPool.Config config = null;
	public BasePool(final GenericObjectPool.Config poolConfig,
			final PoolableObjectFactoryManager fm) {
		
		this.factorysManager = fm;
		
		this.config = poolConfig;
		
		this.internalPool = new GenericObjectPool(factorysManager.getNext(), config);

		//守护进程
		startProtected();
	}
	
	private void startProtected() {
		//启动守护进程，监控连接是否失效，如果当前服务器失效，则连接下一台
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				if(internalPool != null) {
//					if(!isConnected()) {
//						try {
//							//先关闭已失效的连接池
//							internalPool.clear();
//							internalPool.close();
//							internalPool = new GenericObjectPool(factorysManager.getNext(), config);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
////						internalPool.setFactory(factorysManager.getNext());
//						System.out.println("connection refused!");
//					}
//				}
//			}
//		}, 1000, 1000);
		
		try {
			//启动守护进程，监控连接是否失效，如果当前服务器失效，则连接下一台；采用ScheduledExecutorService，避免系统时钟敏感性问题
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			executor.scheduleWithFixedDelay(new Runnable() {
				public void run() {
					if(internalPool != null) {
						if(!isConnected()) {
							try {
								//先关闭已失效的连接池
								internalPool.clear();
								internalPool.close();
								internalPool = new GenericObjectPool(factorysManager.getNext(), config);
							} catch (Exception e) {
								e.printStackTrace();
							}
//							internalPool.setFactory(factorysManager.getNext());
							System.out.println("connection refused!");
						}
					}
				}
			}, 0, 1000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public T getResource() {
		try {
			T temp = (T) internalPool.borrowObject();
			return temp ;
		} catch (Exception e) {
			return null;
		}
	}

	public void returnResourceObject(final Object resource) {

		try {
			internalPool.returnObject(resource);
		} catch (Exception e) {
//			throw new JedisException( "Could not return the resource to the pool", e);
		}
	}

	public void returnBrokenResource(final T resource) {
		returnBrokenResourceObject(resource);
	}

	public void returnResource(final T resource) {
		returnResourceObject(resource);
	}

	protected void returnBrokenResourceObject(final Object resource) {
		try {
			// 失效
			internalPool.invalidateObject(resource);
		} catch (Exception e) {
//			throw new JedisException("Could not return the resource to the pool", e);
		}

	}

	public void destroy() {
		try {
			internalPool.close();
		} catch (Exception e) {
//			throw new JedisException("Could not destroy the pool", e);
		}
	}
	
	public boolean isConnected() {
		T jds = null;
		try {
			jds = getResource();
			boolean isc = ((Jssdb)jds).testConnected();
			if(isc) {
				returnResource(jds);
			} else {
				returnBrokenResourceObject(jds);
			}
			return isc;
		} catch (Exception e) {
			return false;
		}
	}
}
