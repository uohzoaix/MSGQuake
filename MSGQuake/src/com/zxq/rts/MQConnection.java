package com.zxq.rts;

import com.rabbitmq.client.ConnectionFactory;

public class MQConnection {

	private String uri;

	private String host;
	private Integer port;
	private String userName;
	private String password;
	private String virtualHost;
	private Integer heartBeat;

	private String queneName;
	private Integer prefetch;
	private Boolean requeneOnFail;

	private Boolean durable;
	private Boolean exclusive;
	private Boolean autoDelete;
	private Boolean autoAck;

	public MQConnection(String queneName) {
		this("127.0.0.1", ConnectionFactory.DEFAULT_AMQP_PORT, ConnectionFactory.DEFAULT_USER, ConnectionFactory.DEFAULT_PASS, ConnectionFactory.DEFAULT_VHOST, ConnectionFactory.DEFAULT_HEARTBEAT,
				queneName);
	}

	public MQConnection(String uri, String queneName) {
		this.uri = uri;
		this.queneName = queneName;
	}

	public MQConnection(String host, String userName, String password, String queneName) {
		this(host, ConnectionFactory.DEFAULT_AMQP_PORT, userName, password, ConnectionFactory.DEFAULT_VHOST, ConnectionFactory.DEFAULT_HEARTBEAT, queneName);
	}

	public MQConnection(String host, Integer port, String userName, String password, String virtualHost, Integer heartBeat, String queneName) {
		super();
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.virtualHost = virtualHost;
		this.heartBeat = heartBeat;
		this.queneName = queneName;
	}

	public MQConnection setCustomeConfig() {
		return this;
	}

}
