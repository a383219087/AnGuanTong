package com.cqj.test.wbd2_gwpy.util;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class MyGetImageTask extends AsyncTask<String, String, HashMap<String, Object>> {

	private Context context;
	private String url;
	private ImageView iv;

	public MyGetImageTask(Context context, String url, ImageView iv) {
		this.context = context;
		this.url = url;
		this.iv = iv;
	}

	@Override
	protected HashMap<String, Object> doInBackground(String... params) {
		// TODO Auto-generated method stub
		return BitmapUtil.getBitmap(url, context);
	}

	@Override
	protected void onPostExecute(HashMap<String, Object> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		iv.setImageBitmap((Bitmap) result.get("bitmap"));
		iv.setTag((String)result.get("path"));
	}

}
