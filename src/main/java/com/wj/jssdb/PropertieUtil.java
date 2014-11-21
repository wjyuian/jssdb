package com.wj.jssdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author HQ01U8433
 * 读取配置文件信息
 */
public class PropertieUtil {
	/**
	 * 储存已加载的配置文件信息
	 */
	private static Map<String, Properties> _properties = new HashMap<String, Properties>(1);
	/**
	 * 加载指定名称的配置文件，
	 * 文件后缀为 properties
	 * @param propsName 文件名，不含后缀
	 * @return
	 */
	public static Properties getProps(String propsName) {
		if(_properties.get(propsName) != null) {
			return _properties.get(propsName);
		}
		Properties props = new Properties();
		FileInputStream fis = null;
		try {
			File f_file = new File(
					URLDecoder.decode(PropertieUtil.class.getClassLoader()
							.getResource(propsName+".properties").getFile(), "iso-8859-1"));
			fis = new FileInputStream(f_file);
			props.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
			_properties.put(propsName, props);
		}
		return props;
	}

	/**
	 * 获取指定配置文件中，指定字段的值
	 * @param propsName 文件名
	 * @param propName 字段
	 * @return
	 */
	public static String getStrProp(String propsName, String propName) {
		Properties props = getProps(propsName);
		if (props != null) {
			String propValue = props.getProperty(propName);
			if (propValue != null && !propValue.isEmpty()) {
				return propValue;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param propsName
	 * @param propName
	 * @return
	 */
	public static int getIntProp(String propsName, String propName) {
		Properties props = getProps(propsName);
		if (props != null) {
			String propValue = props.getProperty(propName);
			if (propValue != null && !propValue.isEmpty()) {
				return Integer.parseInt(propValue);
			}
		}
		return 0;
	}

	public static float getFloatProp(String propsName, String propName) {
		Properties props = getProps(propsName);
		if (props != null) {
			String propValue = props.getProperty(propName);
			if (propValue != null && !propValue.isEmpty()) {
				return Float.parseFloat(propValue);
			}
		}
		return 0.0f;
	}
	
	public static long getLongProp(String propsName, String propName) {
		Properties props = getProps(propsName);
		if (props != null) {
			String propValue = props.getProperty(propName);
			if (propValue != null && !propValue.isEmpty()) {
				return Long.parseLong(propValue);
			}
		}
		return 0l;
	}
}