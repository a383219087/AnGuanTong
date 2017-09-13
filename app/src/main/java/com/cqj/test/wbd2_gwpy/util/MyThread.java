package com.cqj.test.wbd2_gwpy.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Handler;
import android.os.Message;

public class MyThread implements Runnable{

	String[] params;
	Object[] values;
	String wsMethod;
	Handler handler;

	public MyThread(String[] params, Object[] values, String wsMethod,Handler handler) {
		this.params = params;
		this.values = values;
		this.wsMethod = wsMethod;
		this.handler=handler;
	}

	public void run() {
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = WebServiceUtil.getWebServiceMsg(params, values,
					wsMethod);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (wsMethod.equals("getSinglePersonalUserFromLogin")) {
			Message msg = handler.obtainMessage();
			msg.obj = result;
			msg.what = 1;
			handler.sendMessage(msg);
		} else {
			Message msg = handler.obtainMessage();
			msg.obj = result;
			msg.what = 2;
			handler.sendMessage(msg);
		}
	}
}
