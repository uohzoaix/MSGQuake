package com.zxq.rts.ssdb;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.udpwork.ssdb.SSDB;
import com.zxq.rts.rabbit.error.ServiceException;
import com.zxq.rts.utils.PropUtil;

public class SSDBConn {

	private static Logger logger = LoggerFactory.getLogger(SSDBConn.class);

	public static SSDB getSSDB(SSDB ssdb, Integer retry) throws ServiceException {
		try {
			int i = 0;
			ssdb = new SSDB(PropUtil.getPropVal("ssdbhost").toString(), Integer.valueOf(PropUtil.getPropVal("ssdbport").toString()));
			while (ssdb == null && i < retry) {
				i++;
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				logger.info("retry to get SSDB by " + i);
				ssdb = new SSDB(PropUtil.getPropVal("ssdbhost").toString(), Integer.valueOf(PropUtil.getPropVal("ssdbport").toString()));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ssdb == null) {
			throw new ServiceException("cann't get SSDB,please check the ssdb for more info");
		}
		return ssdb;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getSnapshotFromSSDB(SSDB ssdb, String key) {
		Map<String, Object> valMap = null;
		String bString = null;
		byte[] bytes = null;
		try {
			bytes = ssdb.get(key);
			if (bytes != null) {
				bString = new String(bytes);
				JSON.parseObject(bString, Map.class);
				valMap = JSON.parseObject(bString, Map.class);
			}
		} catch (Exception e) {
			try {
				bytes = ssdb.get(key);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (bytes != null) {
				bString = new String(bytes);
				valMap = JSON.parseObject(bString, Map.class);
			}
		}
		return valMap;
	}
}
