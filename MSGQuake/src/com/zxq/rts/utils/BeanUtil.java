package com.zxq.rts.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.zxq.rts.rabbit.config.BaseMQConfig;
import com.zxq.rts.rabbit.config.CustomeMQConfig;
import com.zxq.rts.rabbit.error.ServiceException;

public class BeanUtil {

	private static String parGetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

	}

	private static boolean checkGetMet(Method[] methods, String fieldGetMet) {
		for (Method met : methods) {
			if (fieldGetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	public static Map<String, Object> getFieldValueMap(Map<String, Object> valMap, Object bean) {
		Class<?> cls = bean.getClass();
		Map<String, Object> valueMap = new HashMap<String, Object>();
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				String fieldGetName = parGetName(field.getName());
				if (!checkGetMet(methods, fieldGetName)) {
					continue;
				}
				Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
				Object obj = field.getType();
				if (!(obj == Integer.class || obj == Float.class || obj == Long.class || obj == String.class || obj == Boolean.class)) {
					getFieldValueMap(valueMap, fieldVal);
				} else {
					valueMap.put(field.getName(), fieldVal);
				}
			} catch (Exception e) {
				continue;
			}
		}
		return valueMap;
	}
	
	public static void main(String[] args) throws ServiceException, IllegalArgumentException, IllegalAccessException {
		BaseMQConfig baseConfig = new BaseMQConfig(PropUtil.getPropVal("mqhost").toString(), 5672, PropUtil.getPropVal("userName").toString(), PropUtil.getPropVal("password").toString(), PropUtil
				.getPropVal("virtualHost").toString(), 10, PropUtil.getPropVal("queneName").toString());

		CustomeMQConfig customeMQConfig = new CustomeMQConfig(baseConfig, Integer.valueOf(PropUtil.getPropVal("prefetch").toString()), Boolean.valueOf(PropUtil.getPropVal("requeueOnFail").toString()));
		Map<String, Object> valueMap = new HashMap<>();
		valueMap = getFieldValueMap(valueMap, customeMQConfig);
	}
}
