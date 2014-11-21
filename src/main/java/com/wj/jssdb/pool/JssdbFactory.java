/*
* 2014-11-20 下午5:23:57
* 吴健 HQ01U8435
*/

package com.wj.jssdb.pool;

import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 * ssdb连接池工厂类
 */
public class JssdbFactory extends BasePoolableObjectFactory {
	private final String host;
	private final int port;
	private final int timeout;

	public JssdbFactory(final String host, final int port, final int timeout) {
		super();
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	}

	public Object makeObject() throws Exception {
		final Jssdb jssdb = new Jssdb(this.host, this.port, this.timeout);
		return jssdb;
	}

	public void destroyObject(final Object obj) throws Exception {
		if (obj instanceof Jssdb) {
			final Jssdb jssdb = (Jssdb) obj;
			if (jssdb.isOpen()) {
				jssdb.close();
			}
		}
	}

	public boolean validateObject(final Object obj) {
		if (obj instanceof Jssdb) {
			final Jssdb jedis = (Jssdb) obj;
			try {
				return jedis.isOpen();
			} catch (final Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}
}