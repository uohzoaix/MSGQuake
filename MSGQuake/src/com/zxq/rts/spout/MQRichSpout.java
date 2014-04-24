package com.zxq.rts.spout;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;

public abstract class MQRichSpout extends BaseRichSpout {

	private static final long serialVersionUID = -2612429460532470035L;

	public void createConnection() {
	}

	@Override
	public void nextTuple() {

	}

	@Override
	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context, SpoutOutputCollector collector) {
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {

	}

}
