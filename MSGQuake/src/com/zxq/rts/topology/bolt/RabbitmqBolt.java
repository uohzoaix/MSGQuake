package com.zxq.rts.topology.bolt;

import java.util.Map;

import backtype.storm.spout.Scheme;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class RabbitmqBolt extends BaseRichBolt {

	private static final long serialVersionUID = -7852906526849246118L;

	private transient Scheme schema;

	@Override
	public void execute(Tuple tuple) {
		schema.deserialize((byte[]) tuple.getValue(0));
	}

	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map conf, TopologyContext context, OutputCollector collector) {
		try {
			schema = (Scheme) Class.forName((String) conf.get("schema")).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {

	}

}
