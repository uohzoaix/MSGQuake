package com.zxq.rts.rabbit.config;

import java.util.Map;

public class CustomeMQConfig extends BaseMQConfig {

	private Integer prefetch;
	private Boolean requeneOnFail;

	private Boolean durable;
	private Boolean exclusive;
	private Boolean autoDelete;
	private Boolean autoAck;

	private BaseMQConfig baseConfig;

	public CustomeMQConfig() {

	}

	public CustomeMQConfig(BaseMQConfig baseConfig, Integer prefetch, Boolean requeneOnFail) {
		this(baseConfig, prefetch, requeneOnFail, true, false, false, false);
	}

	public CustomeMQConfig(BaseMQConfig baseConfig, Boolean durable, Boolean exclusive, Boolean autoDelete, Boolean autoAck) {
		this(baseConfig, 1000, false, durable, exclusive, autoDelete, autoAck);
	}

	public CustomeMQConfig(BaseMQConfig baseConfig, Integer prefetch, Boolean requeneOnFail, Boolean durable, Boolean exclusive, Boolean autoDelete, Boolean autoAck) {
		this.baseConfig = baseConfig;
		this.prefetch = prefetch;
		this.requeneOnFail = requeneOnFail;
		this.durable = durable;
		this.exclusive = exclusive;
		this.autoDelete = autoDelete;
		this.autoAck = autoAck;
	}

	public BaseMQConfig getBaseConfig() {
		return baseConfig;
	}

	public void setBaseConfig(BaseMQConfig baseConfig) {
		this.baseConfig = baseConfig;
	}

	public Integer getPrefetch() {
		return prefetch;
	}

	public void setPrefetch(Integer prefetch) {
		this.prefetch = prefetch;
	}

	public Boolean getRequeneOnFail() {
		return requeneOnFail;
	}

	public void setRequeneOnFail(Boolean requeneOnFail) {
		this.requeneOnFail = requeneOnFail;
	}

	public Boolean getDurable() {
		return durable;
	}

	public void setDurable(Boolean durable) {
		this.durable = durable;
	}

	public Boolean getExclusive() {
		return exclusive;
	}

	public void setExclusive(Boolean exclusive) {
		this.exclusive = exclusive;
	}

	public Boolean getAutoDelete() {
		return autoDelete;
	}

	public void setAutoDelete(Boolean autoDelete) {
		this.autoDelete = autoDelete;
	}

	public Boolean getAutoAck() {
		return autoAck;
	}

	public void setAutoAck(Boolean autoAck) {
		this.autoAck = autoAck;
	}

}
