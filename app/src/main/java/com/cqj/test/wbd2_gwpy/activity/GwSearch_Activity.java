package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.util.StringUtil;
import com.cqj.test.wbd2_gwpy.util.WebServiceUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class GwSearch_Activity extends Activity {

	private EditText gwbt, startDate, endDate;
	private Button searchBtn;
	private ArrayList<HashMap<String, Object>> data;//查询结果数据集
	private ArrayList<HashMap<String, Object>> comData;//公司数据集
	private Calendar calendar;
	private String orgid;
	private MyApplication myApp;
	private Spinner fbdwSp;
	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				changeButtonStyle(false, "");
				if (data.size() == 1 && data.get(0).size() < 2) {
					Toast.makeText(GwSearch_Activity.this, "查询无数据。",
							Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent();
					intent.putExtra("data", data);
					setResult(RESULT_OK, intent);
					finish();
				}
				break;
			case 2:
				SimpleAdapter ada = new SimpleAdapter(GwSearch_Activity.this,
						comData, android.R.layout.simple_spinner_item,
						new String[] { "relOrgName" },
						new int[] { android.R.id.text1 });
				fbdwSp.setAdapter(ada);
				ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
		setContentView(R.layout.gwsearch_view);
		myApp = (MyApplication) getApplication();
		orgid = myApp.getComInfo().getOrg_idstr();
		initComplement();
		getData();
	}

	private Thread myGetDataThread;

	private void getData() {

		myGetDataThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String keys[] = { "orgid", "relType", "includeself" };
					Object values[] = {
							Integer.parseInt(myApp.getComInfo().getOrg_id()),
							"", false };
					comData = WebServiceUtil.getWebServiceMsg(keys, values,
							"getRelationOrg",WebServiceUtil.HUIWEI_URL,WebServiceUtil.HUIWEI_NAMESPACE);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mHandler.sendEmptyMessage(2);
			}
		});
		myGetDataThread.start();
	}

	private void initComplement() {
		calendar = Calendar.getInstance();
		getActionBar().setTitle("公文信息筛选");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		gwbt = (EditText) findViewById(R.id.gwsearch_edt);
		startDate = (EditText) findViewById(R.id.gwsearch_startdate);
		endDate = (EditText) findViewById(R.id.gwsearch_enddate);
		searchBtn = (Button) findViewById(R.id.gwsearch_cxbtn);
		fbdwSp = (Spinner) findViewById(R.id.gwsearch_fbdw);
		setDate(endDate);
		setDate(startDate);
		comData = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("relOrgName", myApp.getComInfo().getCom_fullname());
		map.put("relOrgID", orgid);
		comData.add(map);
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

	private void setDate(final EditText edt) {
		edt.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
		edt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DateDialog(edt);
			}
		});
		edt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean focus) {
				// TODO Auto-generated method stub
				if (focus) {
					DateDialog(edt);
				}
			}
		});
	}

	private void DateDialog(final EditText edt) {
		new DatePickerDialog(GwSearch_Activity.this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				// roomxx_jzryxx_ddzzdrq.setText(String
				// .valueOf(year)
				// + "-"
				// + String.valueOf(monthOfYear)
				// + "-"
				// + String.valueOf(dayOfMonth));
				String smon = String.valueOf(monthOfYear + 1);
				String sday = String.valueOf(dayOfMonth);
				// if (smon.length() == 1) {
				// smon = "0" + smon;
				// }
				// if (sday.length() == 1) {
				// sday = "0" + sday;
				// }
				edt.setText(String.valueOf(year) + "-" + smon + "-" + sday);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	public void search(View view) {
		if (StringUtil.isEmpty(gwbt.getText().toString())) {
			Toast.makeText(GwSearch_Activity.this, "请输入查询关键字。",
					Toast.LENGTH_LONG).show();
			return;
		}
		changeButtonStyle(true, "查询中...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String keys[] = { "orgidstr", "pubDateStart", "pubDateEnd",
							"topCount", "docType", "infoTitle", "retstr" };
					// System.out.println("orgidstr" +
					// orgItem.get("Emid").toString());
					Object values[] = {
							comData.get(fbdwSp.getSelectedItemPosition()).get(
									"relOrgID"),
							startDate.getText().toString(),
							endDate.getText().toString(), 0, "",
							gwbt.getText().toString(), "" };
					data = WebServiceUtil.getWebServiceMsg(keys, values,
							"getWebInformFromOrgID_Android");
					for (int i = 0; i < data.size(); i++) {
						for (String key : data.get(i).keySet()) {
							System.out
									.println(key + "=" + data.get(i).get(key));
						}
					}
					mHandler.sendEmptyMessage(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void changeButtonStyle(boolean isPress, String alertMsg) {
		if (isPress) {
			searchBtn.setText(alertMsg);
			searchBtn.setBackgroundColor(Color.GRAY);
			searchBtn.setEnabled(false);
		} else {
			searchBtn.setEnabled(true);
			searchBtn.setText("查　　询");
			searchBtn.setBackgroundColor(getResources().getColor(
					R.color.login_bg));
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (myGetDataThread.isAlive()) {
			myGetDataThread.interrupt();
		}
	}
}
