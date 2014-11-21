/*
 * 2014-11-20 下午12:16:13
 * 吴健 HQ01U8435
 */

package com.wj.jssdb.pool;

import org.apache.commons.pool.impl.GenericObjectPool.Config;

/**
 * ssdb连接池
 */
public class JssdbPool extends BasePool<Jssdb> {
	public JssdbPool(final Config poolConfig, final String hostPortTimeout) {
		this(poolConfig, hostPortTimeout, Protocol.DEFAULT_DATABASE);
	}

	public JssdbPool(final Config poolConfig, final String hostPortTimeout, final int database) {
		super(poolConfig, new PoolableObjectFactoryManager(hostPortTimeout));
	}

	public void returnBrokenResource(final Jssdb resource) {
		returnBrokenResourceObject(resource);
	}

	public void returnResource(final Jssdb resource) {
		returnResourceObject(resource);
	}

}