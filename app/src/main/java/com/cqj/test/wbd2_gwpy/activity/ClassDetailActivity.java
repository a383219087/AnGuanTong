package com.cqj.test.wbd2_gwpy.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.util.StringUtil;
import com.cqj.test.wbd2_gwpy.util.Utils;
import com.cqj.test.wbd2_gwpy.util.WebServiceUtil;
import com.zxing.activity.MipcaActivityCapture;

public class ClassDetailActivity extends Activity {

	private int currID;
	private Chronometer mChron;
	private TextView startDate, title, yqks, contentDetail, contentTitle;
	private WebView mWeb;
	private String mUrl;
	private EditText xxjl;
	private ScrollView scrollView;
	protected ArrayList<HashMap<String, Object>> mData;
	private TextView spckBtn, wbckBtn, commitBtn;

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message arg0) {
			// TODO Auto-generated method stub
			commitBtn.setEnabled(true);
			switch (arg0.what) {
				case 1:
					title.setText(StringUtil.noNull(mData.get(0).get("currTitle")));
					yqks.setText("要求课时："
							+ StringUtil.noNull(mData.get(0).get("currhour")));
					mUrl = StringUtil.noNull(mData.get(0).get("vlink"));
					if (TextUtils.isEmpty(mUrl)) {
						mWeb.setVisibility(View.GONE);
						spckBtn.setVisibility(View.GONE);
						changeBtnState(false);
					} else {
						spckBtn.setVisibility(View.VISIBLE);
						changeBtnState(true);
						// mWeb.loadUrl("http://v.youku.com/v_show/id_XNTIzMzE5OTgw.html?f=23182693&x&from=y11.3-idx-uhome-1519-20887.205916.1-1");//后缀
						// ?f=23182693&x&from=y11.3-idx-uhome-1519-20887.205916.1-1
						if (mUrl.endsWith("swf")) {
							if(android.os.Build.VERSION.SDK_INT>=19){
								mWeb.addJavascriptInterface(new AndroidBridge(),
										"android");
								mWeb.loadUrl("file:///android_asset/notplayflash.html");
								return false;
							}
							if (check()) {
								mWeb.loadUrl(mUrl);
							} else {
								mWeb.addJavascriptInterface(new AndroidBridge(),
										"android");
								mWeb.loadUrl("file:///android_asset/noflash.html");
							}
						} else {
							mWeb.getSettings().setLoadsImagesAutomatically(true);
							mWeb.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
							mWeb.loadUrl(mUrl
									+ "?f=23182693&x&from=y11.3-idx-uhome-1519-20887.205916.1-1");// 后缀
							// ?f=23182693&x&from=y11.3-idx-uhome-1519-20887.205916.1-1
							// mWeb.loadUrl("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>Untitled Document</title></head><body><object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0\" width=\"800\" height=\"600\" id=\"MyFlash\"><param name=\"movie\" value=\""+StringUtil.noNull(mData.get(0).get("vlink"))+"\"><param name=\"quality\" value=\"high\"><param name=\"fullscreen\" value=\"true\"><param name=\"scale\" value=\"exactfit\"><embed src=\"flash.swf\" quality=\"high\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" type=\"application/x-shockwave-flash\" width=\"800\" height=\"600\"></embed></object></body></html>");
							mWeb.scrollTo(0, 100);
						}
					}
					int obliid = -1;
					try {
						obliid = Integer.parseInt(StringUtil.noNull(mData.get(0)
								.get("obliid")));
					} catch (Exception e) {
					}
					if (obliid != -1) {
						new myGetDetailTask(obliid, contentDetail).execute("");
					}
					mChron.start();
					break;
				case 2:
					Toast.makeText(ClassDetailActivity.this, "连接服务器超时，请稍后再试...",
							Toast.LENGTH_LONG).show();
					break;
				case 3:
					Toast.makeText(ClassDetailActivity.this, "提交成功。",
							Toast.LENGTH_LONG).show();
					break;
				default:
					break;
			}
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classdetail_view);
		getActionBar().setTitle("课程学习");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		String currIDStr = getIntent().getStringExtra("currID");
		try {
			currID = Integer.parseInt(currIDStr);
		} catch (Exception e) {
			Toast.makeText(ClassDetailActivity.this, "不是正确的课程。",
					Toast.LENGTH_LONG).show();
			return;
		}
		initComplement();
		registListener();
		getData();
	}

	int startY, startX;

	@SuppressLint("SimpleDateFormat")
	private void registListener() {
		mWeb.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedSslError(WebView view,
										   SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				super.onReceivedSslError(view, handler, error);
				Log.e("urlError", error.getUrl());
			}



		});
		mWeb.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						startY = (int) event.getY();
						startX = (int) event.getX();
						System.out.println(".....truedd");
						return false;

					case MotionEvent.ACTION_UP:
						int endX = (int) event.getX();
						int endY = (int) event.getY();
						if (Math.abs(startX - endX) > 10
								|| Math.abs(startY - endY) > 10) {
							System.out.println(".....true");
							return true;
						} else {
							System.out.println(".....false");
							return false;
						}
					default:
						System.out.println(".....truedefa");
						return false;
				}

			}

		});
		spckBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeBtnState(true);
			}
		});
		wbckBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeBtnState(false);
			}
		});
		commitBtn.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(xxjl.getText().toString())) {
					Toast.makeText(ClassDetailActivity.this, "请输入学习记录。",
							Toast.LENGTH_LONG).show();
					return;
				}
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							float time = 0f;
							String timeStr = mChron.getText().toString();
							if (timeStr.length() > 5) {
								Date mDate = new SimpleDateFormat("H:mm:ss")
										.parse(timeStr);
								if (mDate.getHours() > 0) {
									time = mDate.getHours() * 60
											+ mDate.getMinutes();
								} else {
									time = mDate.getMinutes();
								}
							} else {
								Date mDate = new SimpleDateFormat("mm:ss")
										.parse(timeStr);
								int minutes = mDate.getMinutes();
								time = minutes;
							}
							String[] keys = { "StuID", "durMinute", "CRemark" };
							Object[] values = { currID, time,
									xxjl.getText().toString() };
							WebServiceUtil.getWebServiceMsg(keys, values,
									"setCourseReCord",
									WebServiceUtil.PART_DUTY_URL);
							handler.sendEmptyMessage(3);
						} catch (Exception e) {
							e.printStackTrace();
							handler.sendEmptyMessage(2);
						}
					}
				}).start();

			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	private void initComplement() {
		mChron = (Chronometer) findViewById(R.id.classdetail_chornometer);
		startDate = (TextView) findViewById(R.id.classdetail_startdate);
		Calendar cal = Calendar.getInstance();
		String startDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(cal.getTime());
		startDate.setText(startDateStr);
		scrollView = (ScrollView) findViewById(R.id.scroll_view);
		contentDetail = (TextView) findViewById(R.id.classdetail_textview);
		contentTitle = (TextView) findViewById(R.id.content_title);
		mWeb = (WebView) findViewById(R.id.classdetail_webview);
		ViewGroup.LayoutParams params = mWeb.getLayoutParams();
		params.height = (int) (Utils.getScreenWidth(this) * 2.8 / 5);
		mWeb.requestFocus();
		mWeb.setBackgroundColor(0);
		mWeb.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		WebSettings setting = mWeb.getSettings();
		setting.setPluginState(PluginState.ON);
		setting.setPluginsEnabled(true);
		setting.setAllowFileAccess(true);
		setting.setJavaScriptEnabled(true);
//		setting.setLoadsImagesAutomatically(true);
//		 setting.setJavaScriptCanOpenWindowsAutomatically(true);
		// setting.setAppCachePath("/data/data/" + getPackageName() +
		// "/app_path/");
		// setting.setAppCacheEnabled(true);
		// setting.setPluginState(PluginState.ON);
		title = (TextView) findViewById(R.id.classdetail_title);
		yqks = (TextView) findViewById(R.id.classdetail_teacher);
		spckBtn = (TextView) findViewById(R.id.classdetail_spbf);
		wbckBtn = (TextView) findViewById(R.id.classdetail_wbck);
		commitBtn = (TextView) findViewById(R.id.commit_btn);
		xxjl = (EditText) findViewById(R.id.classdetail_xxjl);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mWeb.destroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
			default:
				break;
		}
		return true;
	}

	public void toEwm(View view) {
		Intent intent = new Intent(ClassDetailActivity.this,
				CameraTestActivity.class);
		startActivityForResult(intent, 110);
	}

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String[] keys = { "Curid", "TitleKeyWord", "ComID", "CType" };
					Object[] values = { currID, "", 0, "" };// currID
					mData = WebServiceUtil.getWebServiceMsg(keys, values,
							"getCourse", new String[] { "currTitle",
									"currhour", "currType", "currid", "vlink",
									"obliid" }, WebServiceUtil.PART_DUTY_URL);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(2);
				}
			}
		}).start();

	}

	private void changeBtnState(boolean isSpbf) {
		if (isSpbf) {
			spckBtn.setBackgroundColor(getResources().getColor(R.color.gray));
			wbckBtn.setBackgroundColor(getResources().getColor(
					R.color.background));
			mWeb.setVisibility(View.VISIBLE);
			contentDetail.setVisibility(View.GONE);
			contentTitle.setText("视频播放");
		} else {
			spckBtn.setBackgroundColor(getResources().getColor(
					R.color.background));
			wbckBtn.setBackgroundColor(getResources().getColor(R.color.gray));
			mWeb.setVisibility(View.GONE);
			contentDetail.setVisibility(View.VISIBLE);
			contentTitle.setText("文本内容");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case 110:
					String ewmStr = data.getStringExtra("result");
					Toast.makeText(getApplicationContext(), ewmStr,
							Toast.LENGTH_LONG).show();
					break;

				default:
					break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	class myGetDetailTask extends
			AsyncTask<String, String, ArrayList<HashMap<String, Object>>> {

		private int obliid;
		private TextView detail;

		public myGetDetailTask(int obliid, TextView detail) {
			this.obliid = obliid;
			this.detail = detail;
		}

		@Override
		protected ArrayList<HashMap<String, Object>> doInBackground(
				String... params) {
			// TODO Auto-generated method stub
			ArrayList<HashMap<String, Object>> getDatas = new ArrayList<HashMap<String, Object>>();
			String keys2[] = { "orgIDstr", "cDocID", "titleKeyWord",
					"detailKeyWord", "carryPartID", "carryDutyID", "docType",
					"parentCDocID", "cDocDetailID", "retstr" };
			Object values2[] = { null, obliid, "", "", 0, 0, "", 0, 0, "" };
			try {
				getDatas = WebServiceUtil.getWebServiceMsg(keys2, values2,
						"getCapacityDocumentDetail", new String[] {
								"carryPartName", "dLevel", "cDocDetailID",
								"dSequence", "cDocTitle", "inTable", "inImage",
								"createcom", "cDocDetail", "info_additional",
								"info_additiondoc" });
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return getDatas;
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result.size() != 0) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < result.size(); i++) {
					String title = StringUtil.noNull(
							result.get(i).get("cDocTitle")).trim();
					sb.append("·");
					sb.append(title);
					sb.append(StringUtil.ENTER);
					String detail = StringUtil.noNull(
							result.get(i).get("cDocDetail")).trim();
					String sQe = StringUtil.noNull(result.get(i).get(
							"dSequence"));
					// int code = Integer.parseInt(sQe);
					detail = detail.replace("anyType{}", "");
					if (StringUtil.isNotEmpty(detail)) {
						String dLevel = StringUtil.noNull(result.get(i).get(
								"dLevel"));
						// int level = 2;
						// if (StringUtil.isNotEmpty(dLevel)) {
						// level = Integer.parseInt(dLevel) + 1;
						// sQe = StringUtil.parseNumberByLevel(level, code);
						// }
						sb.append(StringUtil.SPACE);
						if (!dLevel.equals("0")) {
							sb.append(dLevel + "." + sQe + ".");
						} else {
							sb.append(sQe + ".");
						}
						sb.append(detail);
						sb.append(StringUtil.ENTER);
					}
				}
				detail.setText(sb.toString());
			}
		}
	}

	private boolean check() {
		PackageManager pm = getPackageManager();
		List<PackageInfo> infoList = pm
				.getInstalledPackages(PackageManager.GET_SERVICES);
		for (PackageInfo info : infoList) {
			if ("com.adobe.flashplayer".equals(info.packageName)) {
				return true;
			}
		}
		return false;
	}

	private class AndroidBridge {
		public void goMarket() {
			handler.post(new Runnable() {
				public void run() {
					Intent installIntent = new Intent(
							"android.intent.action.VIEW");
					installIntent.setData(Uri
							.parse("market://details?id=com.adobe.flashplayer"));
					startActivity(installIntent);
				}
			});
		}

		public void goBrower() {
			handler.post(new Runnable() {
				public void run() {
					final Uri uri = Uri.parse(mUrl);
					final Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);
				}
			});
		}
	}
}
