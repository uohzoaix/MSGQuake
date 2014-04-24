package com.zxq.rts.rabbit.error;

public class CallBackerImpl implements CallBacker {

	@Override
	public void callBack(Throwable error) {
		if (error.getCause() instanceof ServiceException) {
			try {
				throw new ServiceException("rethrow " + error.getMessage() + " for service");
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
	}

}
