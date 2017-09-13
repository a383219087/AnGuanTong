package com.cqj.test.wbd2_gwpy.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.adapter.PxksAdapter;
import com.cqj.test.wbd2_gwpy.util.StringUtil;
import com.cqj.test.wbd2_gwpy.util.WebServiceUtil;
import com.cqj.test.wbd2_gwpy.view.SweetAlertDialog;
import com.cqj.test.wbd2_gwpy.view.SweetAlertDialog.OnSweetClickListener;import com.cqj.test.wbd2_gwpy.R;


/**
 * 培训考试
 * 
 * @author Administrator
 * 
 */
public class PxksActivity extends Activity {

	private Spinner khyfSp, xxyfSp, nnxzSp;
	private ListView mListView;
	private Button shaiXuan;
	private PxksAdapter mAdapter;
	private Calendar mCal;
	private ArrayList<HashMap<String, Object>> mData;
	private MyApplication myApp;

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message arg0) {
			// TODO Auto-generated method stub
			shaiXuan.setEnabled(true);
			switch (arg0.what) {
			case 1:
				mAdapter = new PxksAdapter(PxksActivity.this, mData);
				mListView.setAdapter(mAdapter);
				break;
			case 2:
				Toast.makeText(PxksActivity.this, "连接服务器超时，请稍后再试...",
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
		setContentView(R.layout.pxks_view);
		myApp = (MyApplication) getApplication();
		initComplement();
		registListener();
		getData();
	}

	public void shaixuan(View view){
		getData();
	}
	
	private void getData() {
		shaiXuan.setEnabled(false);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String[] keys = { "Emid", "SStart", "SEnd", "IsStudyed",
							"IsExamed" };
					int isStrudyed = -1, IsExamed = -1;
					int emid = Integer.parseInt(myApp.getComInfo().getEmid());
					if (xxyfSp.getSelectedItemPosition() != 0) {
						isStrudyed = xxyfSp.getSelectedItemPosition();
					}
					if (khyfSp.getSelectedItemPosition() != 0) {
						IsExamed = khyfSp.getSelectedItemPosition();
					}
					String startDate = "2001-01-01T08:00:00.000";
					String endDate = "2035-01-01T08:00:00.000";
					if(nnxzSp.getSelectedItemPosition()!= 0){
						startDate =StringUtil.noNull(nnxzSp.getSelectedItem())+"-01-01T08:00:00.000";
						endDate =StringUtil.noNull(nnxzSp.getSelectedItem())+"-12-31T08:00:00.000";
					}
					Object[] values = { emid, startDate, endDate, isStrudyed,
							IsExamed };
					mData = WebServiceUtil.getWebServiceMsg(keys, values,
							"getLessonFromEm", new String[] { "currName",
									"studyid", "lesscore", "currid","leshour","teachemname" },
							WebServiceUtil.PART_DUTY_URL);
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(2);
				}
			}
		}).start();

	}

	private void initComplement() {
		getActionBar().setTitle("培训考试");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		khyfSp = (Spinner) findViewById(R.id.pxks_khyf);
		xxyfSp = (Spinner) findViewById(R.id.pxks_xxyf);
		nnxzSp = (Spinner) findViewById(R.id.pxks_ndxz);
		mListView = (ListView) findViewById(R.id.pxks_list);
		shaiXuan =(Button) findViewById(R.id.pxks_shaixuan);
		mCal = Calendar.getInstance();
		mData = new ArrayList<HashMap<String, Object>>();
		int year = mCal.get(mCal.YEAR);
		ArrayList<String> nnxzData = new ArrayList<String>();
		nnxzData.add("全部");
		for (int i = year - 5; i < year + 5; i++) {
			nnxzData.add(i + "");
		}
		ArrayAdapter<String> ndxzAda = new ArrayAdapter<String>(
				PxksActivity.this, android.R.layout.simple_spinner_item,
				nnxzData);
		nnxzSp.setAdapter(ndxzAda);
		ndxzAda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	private void registListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				final String title =StringUtil.noNull(mData.get(arg2).get("currName"));
				final String currID =StringUtil.noNull(mData.get(arg2).get("currid"));
				SweetAlertDialog dialog = new SweetAlertDialog(
						PxksActivity.this);
				dialog.setTitleText("课程简介");
				dialog.setContentText(title);
				dialog.setConfirmText("开始学习");
				dialog.setCancelText("暂不学习");
				dialog.setCanceledOnTouchOutside(true);
				dialog.setConfirmClickListener(new OnSweetClickListener() {

					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
						Intent intnet =new Intent(PxksActivity.this,ClassDetailActivity.class);
						intnet.putExtra("currID", currID);
						startActivity(intnet);
						sweetAlertDialog.dismiss();
					}
				});

				dialog.setCancelClickListener(new OnSweetClickListener() {

					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
						sweetAlertDialog.dismiss();
					}
				});
				dialog.show();
			}

		});
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
}
