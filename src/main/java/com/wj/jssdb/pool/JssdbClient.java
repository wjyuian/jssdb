/*
* 2014-11-21 上午9:32:26
* 吴健 HQ01U8435
*/

package com.wj.jssdb.pool;

import java.io.Serializable;
import java.util.List;
/**
 * 多主多从，主从读写分离的连接池管理
 * @author 吴健 HQ01U8435
 *
 */
public class JssdbClient {
	//多主服务器连接池，负责写数据
	private JssdbPool masterJssdbPool;
	//多从服务器连接池，负责读数据
	private JssdbPool slaverJssdbPool;
	public JssdbClient() {
	}
	public JssdbPool getMasterJssdbPool() {
		return masterJssdbPool;
	}
	public void setMasterJssdbPool(JssdbPool masterJssdbPool) {
		this.masterJssdbPool = masterJssdbPool;
	}
	public JssdbPool getSlaverJssdbPool() {
		return slaverJssdbPool;
	}
	public void setSlaverJssdbPool(JssdbPool slaverJssdbPool) {
		this.slaverJssdbPool = slaverJssdbPool;
	}

	/**
	 * 初始化连接池管理对象
	 * @param mjp	多主连接池
	 * @param sjp	多从连接池
	 */
	public JssdbClient(JssdbPool mjp, JssdbPool sjp) {
		masterJssdbPool = mjp;
		slaverJssdbPool = sjp;
	}
	//从[多主服务器]连接池获得一个链接
	private Jssdb getMaster() throws JssdbException {
		try {
			return masterJssdbPool.getResource();
		} catch (Exception e) {
			throw new JssdbException("get master error");
		}
	}
	//归还主链接
	private void returnMaster(Jssdb jssdb) {
		try {
			if(jssdb != null) {
				masterJssdbPool.returnResource(jssdb);
			}
		} catch (Exception e) {
		}
	}
	//从[多从服务器]连接池获得一个链接
	private Jssdb getSlaver() throws JssdbException {
		try {
			return slaverJssdbPool.getResource();
		} catch (Exception e) {
			throw new JssdbException("get slaver error");
		}
	}
	//归还从连接
	private void returnSlaver(Jssdb jssdb) {
		try {
			if(jssdb != null) {
				slaverJssdbPool.returnResource(jssdb);
			}
		} catch (Exception e) {
		}
	}
	/**
	 * 在[主服务器]上保存指定key的value值
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String key, String value) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			jssdb.set(key, value);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			returnMaster(jssdb);
		}
	}
	/**
	 * 在[主服务器]上保存指定key的value值，数据有效期是seconds秒
	 * @param key		键
	 * @param value		值
	 * @param seconds	有效期，秒
	 * @return
	 */
	public boolean setExp(String key, String value, int seconds) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			jssdb.setExp(key, value, seconds);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			returnMaster(jssdb);
		}
	}
	/**
	 * 从[从服务器]读取指定key的value值；从服务器读数据有延迟
	 * @param key
	 * @return
	 */
	public String get(String key) {
		Jssdb jssdb = null;
		try {
			jssdb = getSlaver();
			return new String(jssdb.get(key));
		} catch (Exception e) {
			return null;
		} finally {
			returnSlaver(jssdb);
		}
	}
	/**
	 * 从[主服务器]读取指定key的value值
	 * @param key
	 * @return
	 */
	public String getFromMaster(String key) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			return new String(jssdb.get(key));
		} catch (Exception e) {
			return null;
		} finally {
			returnMaster(jssdb);
		}
	}
	/**
	 * 在[主服务器]上保存指定key的对象value
	 * @param key
	 * @param value
	 * @return
	 */
	public <T extends Serializable> boolean setPojo(String key, T value) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			jssdb.setPojo(key, value);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			returnMaster(jssdb);
		}
	}
	/**
	 * 在[主服务器]上保存指定key的对象value，有效期seconds
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public <T extends Serializable> boolean setPojoExp(String key, T value, int seconds) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			jssdb.setPojoExp(key, value, seconds);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			returnMaster(jssdb);
		}
	}
	/**
	 * 从[从服务器]获得指定key的pojo对象
	 * @param key
	 * @return
	 */
	public <T extends Serializable> T getPojo(String key) {
		Jssdb jssdb = null;
		try {
			jssdb = getSlaver();
			return jssdb.getPojo(key);
		} catch (Exception e) {
			return null;
		} finally {
			returnSlaver(jssdb);
		}
	}
	/**
	 * 从[主服务器]获得指定key的pojo对象
	 * @param key
	 * @return
	 */
	public <T extends Serializable> T getPojoFromMaster(String key) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			return jssdb.getPojo(key);
		} catch (Exception e) {
			return null;
		} finally {
			returnMaster(jssdb);
		}
	}
	/**
	 * 批量设置key-value的pojo对象信息
	 * @param keys		批量的key
	 * @param values	批量的pojo对象；和keys一一对应
	 * @throws Exception the size of keys and values is not equal..
	 */
	public <T extends Serializable> void mSetPojo(List<String> keys, List<T> values) throws Exception {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			jssdb.mSetPojo(keys, values);
		} catch (Exception e) {
			throw e;
		} finally {
			returnMaster(jssdb);
		}
	}
	/**
	 * 从[从服务器]批量获取指定key的pojo对象
	 * @param keys	指定的批量key值数组
	 * @return
	 */
	public <T extends Serializable> List<T> mGetPojo(List<String> keys) {
		Jssdb jssdb = null;
		try {
			jssdb = getSlaver();
			return jssdb.mGetPojo(keys);
		} catch (Exception e) {
			return null;
		} finally {
			returnSlaver(jssdb);
		}
	}

	/**
	 * 批量设置key-value的pojo对象信息
	 * @param keys		批量的key
	 * @param values	批量的String；和keys一一对应
	 * @throws Exception the size of keys and values is not equal..
	 */
	public <T extends Serializable> void mSet(List<String> keys, List<String> values) throws Exception {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			jssdb.mSet(keys, values);
		} catch (Exception e) {
			throw e;
		} finally {
			returnMaster(jssdb);
		}
	}
	/**
	 * 从[从服务器]批量获取指定key的String
	 * @param keys	指定的批量key值数组
	 * @return
	 */
	public List<String> mGet(List<String> keys) {
		Jssdb jssdb = null;
		try {
			jssdb = getSlaver();
			return jssdb.mGet(keys);
		} catch (Exception e) {
			return null;
		} finally {
			returnSlaver(jssdb);
		}
	}
	/**
	 * 从[主服务器]批量获取指定key的String
	 * @param keys	指定的批量key值数组
	 * @return
	 */
	public List<String> mGetMaster(List<String> keys) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			return jssdb.mGet(keys);
		} catch (Exception e) {
			return null;
		} finally {
			returnMaster(jssdb);
		}
	}
	
	/**
	 * 从[主服务器]批量获取指定key的pojo对象
	 * @param keys	指定的批量key值数组
	 * @return
	 */
	public <T extends Serializable> List<T> mGetFromMaster(List<String> keys) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			return jssdb.mGetPojo(keys);
		} catch (Exception e) {
			return null;
		} finally {
			returnMaster(jssdb);
		}
	}
//	/**
//	 * 从服务器上是否存在指定key的值
//	 * @param key
//	 * @return
//	 */
//	public boolean exists(String key) {
//		Jssdb jssdb = null;
//		try {
//			jssdb = getSlaver();
//			return jssdb.exists(key);
//		} catch (Exception e) {
//			return false;
//		} finally {
//			returnSlaver(jssdb);
//		}
//	}
	/**
	 * 在[主服务器]上，如果不存在指定key的数据，则保存value，否则不做操作
	 * @param key
	 * @param value
	 */
	public void setNotExsits(String key, String value) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			jssdb.setNx(key, value);
		} catch (Exception e) {
			
		} finally {
			returnMaster(jssdb);
		}
	}

	/**
	 * 在[主服务器]上，如果不存在指定key的pojo对象，则保存value，否则不做操作
	 * @param key
	 * @param value
	 */
	public <T extends Serializable> void setPojoNotExsits(String key, T value) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			jssdb.setPojoNx(key, value);
		} catch (Exception e) {
			
		} finally {
			returnMaster(jssdb);
		}
	}
	/**
	 * 在[主服务器]上，使 key 对应的值增加 delt. 参数 delt 可以为负数. 如果原来的值不是整数(字符串形式的整数), 它会被先转换成整数
	 * @param key
	 * @param delt
	 * @return
	 */
	public long increase(String key, long delt) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			return jssdb.increase(key, delt);
		} catch (Exception e) {
			return -12345678900l;
		} finally {
			returnMaster(jssdb);
		}
	}
	
	public void delete(String key) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			jssdb.delete(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnMaster(jssdb);
		}
	}
	
	public void hSet(String mapperName, String key, String value) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			jssdb.hSet(mapperName, key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnMaster(jssdb);
		}
	}
	public String hGet(String mapperName, String key) {
		Jssdb jssdb = null;
		try {
			jssdb = getSlaver();
			return jssdb.hGet(mapperName, key);
		} catch (Exception e) {
			return null;
		} finally {
			returnSlaver(jssdb);
		}
	}
	public String hGetFromMaster(String mapperName, String key) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			return jssdb.hGet(mapperName, key);
		} catch (Exception e) {
			return null;
		} finally {
			returnMaster(jssdb);
		}
	}

	public <T extends Serializable> void hSetPojo(String mapperName, String key, T value) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			jssdb.hSetPojo(mapperName, key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnMaster(jssdb);
		}
	}
	public <T extends Serializable> T hGetPojo(String mapperName, String key) {
		Jssdb jssdb = null;
		try {
			jssdb = getSlaver();
			return jssdb.hGetPojo(mapperName, key);
		} catch (Exception e) {
			return null;
		} finally {
			returnSlaver(jssdb);
		}
	}
	public <T extends Serializable> T hGetPojoFromMaster(String mapperName, String key) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			return jssdb.hGetPojo(mapperName, key);
		} catch (Exception e) {
			return null;
		} finally {
			returnMaster(jssdb);
		}
	}
	public void hDelete(String mapperName, String key) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			jssdb.hDelete(mapperName, key);
		} catch (Exception e) {
			
		} finally {
			returnMaster(jssdb);
		}
	}
	public void hClear(String mapperName) {
		Jssdb jssdb = null;
		try {
			jssdb = getMaster();
			jssdb.hClear(mapperName);
		} catch (Exception e) {
			
		} finally {
			returnMaster(jssdb);
		}
	}
}
