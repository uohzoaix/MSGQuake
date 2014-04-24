package com.zxq.rts.schema;

import java.util.List;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class StringSchema implements Scheme {

	private static final long serialVersionUID = 3278672641580803355L;

	@Override
	public List<Object> deserialize(byte[] bytes) {
		return new Values(bytes);
	}

	@Override
	public Fields getOutputFields() {
		return new Fields("rabbitMessage");
	}

}
