package com.zxq.rts.topology.spout;

import java.util.Map;

import com.zxq.rts.rabbit.connection.MQConnection.CustomeMQConnection;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;

public class RabbitmqSpout extends BaseRichSpout {

	private static final long serialVersionUID = 5331197123979627377L;

	private transient CustomeMQConnection connection;
	private transient SpoutOutputCollector collector;

	@Override
	public void nextTuple() {

	}

	@Override
	public void open(Map arg0, TopologyContext arg1, SpoutOutputCollector arg2) {

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {

	}
}
