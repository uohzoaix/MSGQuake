package com.zxq.rts.rabbit.connection;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.rabbitmq.client.ConnectionFactory;
import com.zxq.rts.rabbit.config.BaseMQConfig;
import com.zxq.rts.rabbit.error.ServiceException;

public abstract class BaseConnection {

	public void createConnection(Integer retry) throws ServiceException {

	}

	public abstract void close();

	public void closeSilently() {
		close();
	}

	public abstract void reCreateConnection();

	public abstract void onFail(Long msgID);

	public void getConnectionFactory(ConnectionFactory connectionFactory, BaseMQConfig config, Integer retry) throws ServiceException {
		Integer i = 0;
		while (connectionFactory == null && i < 5) {
			ConnectionFactory factory = new ConnectionFactory();
			if (config.getUri() != null) {
				try {
					factory.setUri(config.getUri());
				} catch (URISyntaxException e) {
					throw new RuntimeException(e);
				} catch (NoSuchAlgorithmException e) {
					throw new RuntimeException(e);
				} catch (KeyManagementException e) {
					throw new RuntimeException(e);
				}
			} else {
				factory.setHost(config.getHost());
				factory.setPort(config.getPort());
				factory.setUsername(config.getUserName());
				factory.setPassword(config.getPassword());
				factory.setVirtualHost(config.getVirtualHost());
				factory.setRequestedHeartbeat(config.getHeartBeat());
			}
			connectionFactory = factory;
		}
		if (connectionFactory == null) {
			throw new ServiceException("retry to getConnectionFactory for " + retry + " times,but failed!!!");
		}
	}

}
