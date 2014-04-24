package com.zxq.rts.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.zxq.rts.rabbit.error.ServiceException;

public class PropUtil {

	public static Object getPropVal(String propKey) throws ServiceException {
		Properties prop = new Properties();
		InputStream fis = null;
		Object val = null;
		try {
			fis = PropUtil.class.getClassLoader().getResourceAsStream("conn.properties");
			prop.load(fis);
			val = prop.getProperty(propKey);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e2) {

				}
			}
		}
		if (val == null)
			throw new ServiceException("doesn't exist key with:" + propKey);
		return val;
	}
}
