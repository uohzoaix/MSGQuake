package com.zxq.rts.schema;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.alibaba.fastjson.JSON;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;

public class JsonSchema implements Scheme {

	private static final long serialVersionUID = 3224548531750963958L;

	@Override
	public List<Object> deserialize(byte[] bytes) {
		try {
			JSON.parseObject(new String(bytes, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Fields getOutputFields() {
		return null;
	}

}
