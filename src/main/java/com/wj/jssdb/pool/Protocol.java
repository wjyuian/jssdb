/*
* 2014-11-20 下午12:52:58
* 吴健 HQ01U8435
*/

package com.wj.jssdb.pool;

import com.wj.jssdb.PropertieUtil;

/**
 * ssdb连接池相关常量
 * @author 吴健 HQ01U8435
 *
 */
public class Protocol {

	public static int DEFAULT_PORT = 8888;
	public static int DEFAULT_TIMEOUT = 4500;
	public static int DEFAULT_DATABASE = 0;
	
	public static final String 	MASTER_HOST_PORT_TIME = PropertieUtil.getStrProp("resource", "MASTER_HOST_PORT_TIME");
	public static final String 	SLAVER_HOST_PORT_TIME = PropertieUtil.getStrProp("resource", "SLAVER_HOST_PORT_TIME");
}
