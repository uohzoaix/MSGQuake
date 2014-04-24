package com.zxq.rts;

import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Msg {

	private final byte[] body;
	private final long deliveryTag;

	public Msg(Delivery delivery) {
		this.body = delivery.getBody();
		this.deliveryTag = delivery.getEnvelope().getDeliveryTag();
	}

	public byte[] getBody() {
		return body;
	}

	public long getDeliveryTag() {
		return deliveryTag;
	}

	public static final Msg NULL = new NULL();

	public static class NULL extends Msg {
		private NULL() {
			super(null);
		}

		@Override
		public byte[] getBody() {
			throw new UnsupportedOperationException();
		};

		@Override
		public long getDeliveryTag() {
			throw new UnsupportedOperationException();
		}
	}
}
