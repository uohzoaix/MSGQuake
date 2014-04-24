package com.zxq.rts.schema;

import java.io.UnsupportedEncodingException;
import java.util.List;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class StringSchema implements Scheme {

	private static final long serialVersionUID = 3278672641580803355L;

	@Override
	public List<Object> deserialize(byte[] bytes) {
		try {
			return new Values(new String(bytes, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Fields getOutputFields() {
		return new Fields("rabbitMessage");
	}

}
