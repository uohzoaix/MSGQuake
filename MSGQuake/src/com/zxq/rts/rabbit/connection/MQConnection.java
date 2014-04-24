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
	public abstract class CustomeMQConnection extends MQConnection {
		private int prefetch;
		private String queueName;
		private boolean requeueOnFail;

		private boolean durable;
		private boolean exclusive;
		private boolean autoDelete;
		private boolean autoAck;

		public CustomeMQConnection() {
		}

		public CustomeMQConnection(CustomeMQConfig config) {
			this.prefetch = config.getPrefetch();
			this.queueName = config.getQueneName();
			this.requeueOnFail = config.getRequeneOnFail();

			this.durable = config.getDurable();
			this.exclusive = config.getExclusive();
			this.autoDelete = config.getAutoDelete();
			this.autoAck = config.getAutoAck();
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
	}

	private ConnectionFactory connectionFactory;
	private CallBacker callBacker;
	private Logger logger = LoggerFactory.getLogger(MQConnection.class);

	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;
	private String consumerTag;

	public MQConnection() {
		this.callBacker = new DefaultCallBacker();
	}

	public MQConnection(CallBacker callBacker) {
		this.callBacker = callBacker;
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

	@Override
	public void reCreateConnection() {

	}

	@Override
	public void onFail(Long msgID) {
		try {
			channel.basicReject(msgID, false);
		} catch (ShutdownSignalException sse) {
			reCreateConnection();
			callBacker.callBack(sse);
		} catch (Exception e) {
			callBacker.callBack(e);
		}
	}

}
