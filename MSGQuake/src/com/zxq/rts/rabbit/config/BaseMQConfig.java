package com.zxq.rts.rabbit.config;

import com.rabbitmq.client.ConnectionFactory;

public class BaseMQConfig extends MQConfig {

	private String uri;

	private String host;
	private Integer port;
	private String userName;
	private String password;
	private String virtualHost;
	private Integer heartBeat;

	private String queneName;

	public BaseMQConfig() {
	}

	public BaseMQConfig(String queneName) {
		this("127.0.0.1", ConnectionFactory.DEFAULT_AMQP_PORT, ConnectionFactory.DEFAULT_USER, ConnectionFactory.DEFAULT_PASS, ConnectionFactory.DEFAULT_VHOST, ConnectionFactory.DEFAULT_HEARTBEAT,
				queneName);
	}

	public BaseMQConfig(String uri, String queneName) {
		this.uri = uri;
		this.queneName = queneName;
	}

	public BaseMQConfig(String host, String userName, String password, String queneName) {
		this(host, ConnectionFactory.DEFAULT_AMQP_PORT, userName, password, ConnectionFactory.DEFAULT_VHOST, ConnectionFactory.DEFAULT_HEARTBEAT, queneName);
	}

	public BaseMQConfig(String host, Integer port, String userName, String password, String virtualHost, Integer heartBeat, String queneName) {
		super();
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.virtualHost = virtualHost;
		this.heartBeat = heartBeat;
		this.queneName = queneName;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	public Integer getHeartBeat() {
		return heartBeat;
	}

	public void setHeartBeat(Integer heartBeat) {
		this.heartBeat = heartBeat;
	}

	public String getQueneName() {
		return queneName;
	}

	public void setQueneName(String queneName) {
		this.queneName = queneName;
	}

}
