package com.zxq.rts.rabbit.error;

public interface CallBacker {

	void callBack(java.lang.Throwable error);

	public static class DefaultCallBacker implements CallBacker {

		@Override
		public void callBack(Throwable error) {

		}

	}
}
