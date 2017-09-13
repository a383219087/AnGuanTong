package com.cqj.test.wbd2_gwpy.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.adapter.MySpinnerAdapter;
import com.cqj.test.wbd2_gwpy.util.WebServiceUtil;import com.cqj.test.wbd2_gwpy.R;


public class ChooseActivity extends Activity {

	private ListView choose_list;
	private int type;
	private MyApplication myApp;
	private ArrayList<HashMap<String, Object>> data2;
	private MySpinnerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_view);
		myApp = (MyApplication) getApplication();
		initComplement();
		registListener();
	}

	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message arg0) {
			// TODO Auto-generated method stub
			if (data2 != null && data2.size() == 0) {
				Toast.makeText(ChooseActivity.this, "获取无数据", Toast.LENGTH_LONG)
						.show();
				return false;
			}
//			switch (arg0.what) {
//			case 1:
//				mAdapter = new MySpinnerAdapter(data2, ChooseActivity.this,
//						"TaskTitle", "TaskID");
//				mAdapter.setCallBack(ChooseActivity.this);
//				choose_list.setAdapter(mAdapter);
//				break;
//			case 2:
//				mAdapter = new MySpinnerAdapter(data2, ChooseActivity.this,
//						"partname", "partid");
//				mAdapter.setCallBack(ChooseActivity.this);
//				choose_list.setAdapter(mAdapter);
//				break;
//			case 3:
//				mAdapter = new MySpinnerAdapter(data2, ChooseActivity.this,
//						"mplname", "mplid");
//				mAdapter.setCallBack(ChooseActivity.this);
//				choose_list.setAdapter(mAdapter);
//				break;
//			case 4:
//				mAdapter = new MySpinnerAdapter(data2, ChooseActivity.this,
//						"cDocTitle", "cDocid");
//				mAdapter.setCallBack(ChooseActivity.this);
//				choose_list.setAdapter(mAdapter);
//				break;
//			default:
//				Toast.makeText(ChooseActivity.this, "获取无数据", Toast.LENGTH_LONG)
//				.show();
//				break;
//			}
			return false;
		}
	});

	private void registListener() {
	}

	private void initComplement() {
		getActionBar().setTitle("选择内容");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		choose_list = (ListView) findViewById(R.id.choose_list);
		type = getIntent().getIntExtra("type", -1);
		switch (type) {
		case Yhdj_Activity.RW_CHOOSE:
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String keys2[] = { "ComID", "nStart", "eStart" };
					Object values2[] = {
							Integer.parseInt(myApp.getComInfo().getCom_id()),
							"2010-01-01T01:00:00.000",
							"2015-12-31T01:00:00.000" };
					data2 = new ArrayList<HashMap<String, Object>>();
					try {
						data2 = WebServiceUtil.getWebServiceMsg(keys2, values2,
								"getSafetyCheckTaskListFromCom", new String[] {
										"TaskTitle", "TaskID" },
								WebServiceUtil.SAFE_URL);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(5);
					}
					if (data2 != null) {
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(5);
					}
				}
			}).start();
			break;
		case Yhdj_Activity.BM_CHOOSE:
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String keys2[] = { "comid", "topLevel", "underLevel",
							"dState", "parentPartID", "partID", "retstr" };
					Object values2[] = {
							Integer.parseInt(myApp.getComInfo().getCom_id()),
							-1, -1, true, 0, 0, "" };
					data2 = new ArrayList<HashMap<String, Object>>();
					try {
						data2 = WebServiceUtil.getWebServiceMsg(keys2, values2,
								"getAllDepartment", new String[] { "partname",
										"partid" },
								WebServiceUtil.PART_DUTY_URL);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(5);
					}
					if (data2 != null) {
						handler.sendEmptyMessage(2);
					} else {
						handler.sendEmptyMessage(5);
					}
				}
			}).start();
			break;
		case Yhdj_Activity.CS_CHOOSE:
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String keys2[] = { "Comid" };
					Object values2[] = { Integer.parseInt(myApp.getComInfo()
							.getCom_id()) };
					data2 = new ArrayList<HashMap<String, Object>>();
					try {
						data2 = WebServiceUtil.getWebServiceMsg(keys2, values2,
								"getAllField", new String[] { "mplid",
										"mplname" }, WebServiceUtil.URL);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(5);
					}
					if (data2 != null) {
						handler.sendEmptyMessage(3);
					} else {
						handler.sendEmptyMessage(5);
					}
				}
			}).start();
			break;
		case Yhdj_Activity.JCB_LIST:
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						String keys[] = { "orgIDstr", "cDocID", "keyWord",
								"cStart", "cEnd", "onlyInTitle", "cState",
								"userId", "cType", "docTempId", "retstr" };
						// System.out.println("orgidstr" +
						// orgItem.get("Emid").toString());
						Object values[] = { myApp.getComInfo().getOrg_idstr(),
								0, "", "1900-01-01T00:00:00.850",
								"2049-12-31T00:00:00.850", true, true, 0,
								"安全检查表", 0, "" };
						data2 = WebServiceUtil.getWebServiceMsg(keys, values,
								"getCapacityDocument", new String[] { "cDocid",
										"cDocTitle" });
						if (data2 != null) {
							handler.sendEmptyMessage(4);
						} else {
							handler.sendEmptyMessage(5);
						}
					} catch (InterruptedException ex) {
						handler.sendEmptyMessage(5);
					} catch (Exception e) {
						e.printStackTrace();
						handler.sendEmptyMessage(5);
					}
				}
			}).start();
			break;
		default:
			break;
		}
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

//	@Override
//	public void clickBack(String name, String id) {
//		// TODO Auto-generated method stub
//		Intent intent = new Intent();
//		intent.putExtra("result", name);
//		intent.putExtra("resultID", id);
//		setResult(RESULT_OK, intent);
//		finish();
//	}
}
