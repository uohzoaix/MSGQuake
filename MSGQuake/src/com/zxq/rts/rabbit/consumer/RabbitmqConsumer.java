package com.zxq.rts.rabbit.consumer;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;
import com.zxq.rts.Msg;
import com.zxq.rts.rabbit.config.CustomeMQConfig;
import com.zxq.rts.rabbit.connection.MQConnection;
import com.zxq.rts.rabbit.error.CallBacker;

public class RabbitmqConsumer {

	private long waitForMessage = 1000;

	private MQConnection connection;

	public RabbitmqConsumer(Map<String, Object> conf, CallBacker callBacker) {
		connection = new MQConnection(JSON.parseObject((String) conf.get("config"), CustomeMQConfig.class), callBacker);
	}

	public void ack(Long msgID) {
		connection.onAck(msgID);
	}

	public void fail(Long msgID) {
		connection.onFail(msgID);
	}

	public Msg nextMessage() {
		try {
			if (connection == null)
				connection.reCreateConnection();
			if (connection.getConsumerTag() == null || connection.getConsumer() == null || connection.getConsumer().nextDelivery(waitForMessage) == null)
				return Msg.NULL;
			return new Msg(connection.getConsumer().nextDelivery(waitForMessage));
		} catch (ShutdownSignalException sse) {
			connection.reCreateConnection();
			connection.getCallBacker().callBack(sse);
			return Msg.NULL;
		} catch (InterruptedException ie) {
			return Msg.NULL;
		} catch (ConsumerCancelledException cce) {
			connection.reCreateConnection();
			connection.getCallBacker().callBack(cce);
			return Msg.NULL;
		}
	}

	public MQConnection getConnection() {
		return connection;
	}

	public void setConnection(MQConnection connection) {
		this.connection = connection;
	}

}
