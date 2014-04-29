package com.zxq.rts.rabbit.connection;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.zxq.rts.rabbit.config.CustomeMQConfig;
import com.zxq.rts.rabbit.error.CallBacker;
import com.zxq.rts.rabbit.error.CallBacker.DefaultCallBacker;
import com.zxq.rts.rabbit.error.ServiceException;

public class MQConnection extends BaseConnection {
	private ConnectionFactory connectionFactory;
	private CallBacker callBacker;
	private Logger logger = LoggerFactory.getLogger(MQConnection.class);

	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;
	private String consumerTag;

	private int prefetch;
	private String queueName;
	private boolean requeueOnFail;

	private boolean durable;
	private boolean exclusive;
	private boolean autoDelete;
	private boolean autoAck;

	public QueueingConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(QueueingConsumer consumer) {
		this.consumer = consumer;
	}

	public String getConsumerTag() {
		return consumerTag;
	}

	public void setConsumerTag(String consumerTag) {
		this.consumerTag = consumerTag;
	}

	public CallBacker getCallBacker() {
		return callBacker;
	}

	public void setCallBacker(CallBacker callBacker) {
		this.callBacker = callBacker;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public MQConnection() {
	}

	public MQConnection(CallBacker callBacker) {
		this.callBacker = callBacker;
	}

	public MQConnection(CustomeMQConfig config) {
		this(config, new DefaultCallBacker());
	}

	public MQConnection(CustomeMQConfig config, CallBacker callBacker) {
		this.callBacker = callBacker;
		this.prefetch = config.getPrefetch();
		this.queueName = config.getBaseConfig().getQueneName();
		this.requeueOnFail = config.getRequeneOnFail();

		this.durable = config.getDurable();
		this.exclusive = config.getExclusive();
		this.autoDelete = config.getAutoDelete();
		this.autoAck = config.getAutoAck();
		try {
			connectionFactory = getConnectionFactory(connectionFactory, config.getBaseConfig(), 5);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createConnection(Integer retry) throws ServiceException {
		Integer i = 0;
		while (connection == null && i < retry) {
			try {
				connection = connectionFactory.newConnection();
				if (connection == null) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (connection == null) {
			throw new ServiceException("retry to getConnection for " + retry + " times,but failed!!!");
		} else {
			connection.addShutdownListener(new ShutdownListener() {
				@Override
				public void shutdownCompleted(ShutdownSignalException cause) {
					logger.error("shutdown signal with EXCEPTION", cause);
					callBacker.callBack(cause);
					reCreateConnection();
				}
			});
			try {
				channel = connection.createChannel();
				channel.queueDeclare(queueName, durable, exclusive, autoDelete, null);
				if (prefetch > 0) {
					channel.basicQos(prefetch);
				}
				consumer = new QueueingConsumer(channel);
				consumerTag = channel.basicConsume(queueName, autoAck, consumer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void reCreateConnection() {
		closeSilently();
		try {
			createConnection(5);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public void onFail(Long msgID) {
		try {
			channel.basicReject(msgID, requeueOnFail);
		} catch (ShutdownSignalException sse) {
			reCreateConnection();
			callBacker.callBack(sse);
		} catch (Exception e) {
			callBacker.callBack(e);
		}
	}

	public void onAck(Long msgID) {
		try {
			channel.basicAck(msgID, false);
		} catch (ShutdownSignalException sse) {
			reCreateConnection();
			callBacker.callBack(sse);
		} catch (Exception e) {
			callBacker.callBack(e);
		}
	}

	@Override
	public void close() {
		try {
			if (channel != null && channel.isOpen()) {
				if (consumerTag != null)
					channel.basicCancel(consumerTag);
				channel.close();
			}
		} catch (Exception e) {
			logger.debug("close channel with EXCEPTION", e);
		}
		try {
			logger.info("closing connection to rabbitmq: " + connection);
			connection.close();
		} catch (Exception e) {
			logger.debug("close connection with EXCEPTION", e);
		}
		consumer = null;
		consumerTag = null;
		channel = null;
		connection = null;
	}
}
