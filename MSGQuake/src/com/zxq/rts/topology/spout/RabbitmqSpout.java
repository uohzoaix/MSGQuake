package com.zxq.rts.topology.spout;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.spout.Scheme;

import com.zxq.rts.Msg;
import com.zxq.rts.rabbit.consumer.RabbitmqConsumer;
import com.zxq.rts.rabbit.error.CallBacker;
import com.zxq.rts.rabbit.error.ServiceException;

public class RabbitmqSpout extends BaseRichSpout {

	private static final long serialVersionUID = 5331197123979627377L;

	private transient Scheme schema;
	private transient RabbitmqConsumer consumer;
	private transient SpoutOutputCollector collector;
	private Logger logger;

	@Override
	public void ack(Object msgId) {
		if (msgId instanceof Long)
			consumer.ack((Long) msgId);
	}

	@Override
	public void fail(Object msgId) {
		if (msgId instanceof Long)
			consumer.fail((Long) msgId);
	}

	@Override
	public void nextTuple() {
		Msg msg;
		while ((msg = consumer.nextMessage()) != Msg.NULL) {
			List<Object> tuple = getTuple(msg);
			if (!tuple.isEmpty()) {
				emit(tuple, msg, collector);
			}
		}
	}

	private List<Object> getTuple(Msg msg) {
		return schema.deserialize(msg.getBody());
	}

	protected List<Integer> emit(List<Object> tuple, Msg msg, SpoutOutputCollector spoutOutputCollector) {
		return spoutOutputCollector.emit(tuple, msg.getDeliveryTag());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context, final SpoutOutputCollector collector) {
		CallBacker backer = new CallBacker() {
			@Override
			public void callBack(Throwable error) {
				collector.reportError(error);
			}
		};
		consumer = new RabbitmqConsumer(conf, backer);
		try {
			consumer.getConnection().createConnection(5);
		} catch (ServiceException e) {
			logger.error("create connection with EXCEPTION", e);
		}
		schema = (Scheme) conf.get("schema");
		logger = LoggerFactory.getLogger(RabbitmqSpout.class);
		this.collector = collector;
	}

	@Override
	public void close() {
		consumer.getConnection().close();
		super.close();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
		outputFieldsDeclarer.declare(new Fields("mqmsg"));
	}
}
