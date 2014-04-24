package com.zxq.rts.utils;

import java.lang.reflect.Field;
import java.util.Map;

import com.zxq.rts.rabbit.config.BaseMQConfig;
import com.zxq.rts.rabbit.config.CustomeMQConfig;
import com.zxq.rts.rabbit.error.ServiceException;

public class BeanUtil {

	public static Map<String, Object> toMap(Object bean) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			System.out.println(field.getModifiers() + "-" + field.getName() + "=" + field.get(bean.getClass()));
		}
		return null;
	}

	public static void main(String[] args) throws ServiceException, IllegalArgumentException, IllegalAccessException {
		BaseMQConfig baseConfig = new BaseMQConfig(PropUtil.getPropVal("mqhost").toString(), 5672, PropUtil.getPropVal("userName").toString(), PropUtil.getPropVal("password").toString(), PropUtil
				.getPropVal("virtualHost").toString(), 10, PropUtil.getPropVal("queneName").toString());

		CustomeMQConfig customeMQConfig = new CustomeMQConfig(baseConfig, Integer.valueOf(PropUtil.getPropVal("prefetch").toString()), Boolean.valueOf(PropUtil.getPropVal("requeueOnFail").toString()));
		toMap(customeMQConfig);
	}
}
