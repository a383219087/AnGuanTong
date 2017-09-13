package com.cqj.test.wbd2_gwpy.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cqj.test.wbd2_gwpy.adapter.MyListAdapter;
import com.cqj.test.wbd2_gwpy.util.StringUtil;
import com.cqj.test.wbd2_gwpy.util.WebServiceUtil;
import com.cqj.test.wbd2_gwpy.view.XListView;
import com.cqj.test.wbd2_gwpy.view.XListView.IXListViewListener;import com.cqj.test.wbd2_gwpy.R;


public class GwList_Activity extends Activity implements IXListViewListener,
		OnClickListener {
	private XListView mListView;
	private MyListAdapter mAdapter;
	private ArrayList<HashMap<String, Object>> data;
	private LinearLayout progressLn;
	private TextView pro_tv;
	private ProgressBar bar;
	private MyApplication myApp;

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				if (data.size() == 0) {
					bar.setVisibility(View.GONE);
					pro_tv.setText("无数据返回 \n 点击重试");
				} else {
					progressLn.setVisibility(View.GONE);
					setListViewAdapter(new String[] { "NoAuditCount",
							"InfoTitle", "PubComname", "PubDate" });
				}
				break;

			default:
				break;
			}
			return false;
		}
	});

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gwlist_view);
		initComplement();
		registListener();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// if (myApp.isRefresh) {
		progressLn.setVisibility(View.VISIBLE);
		geneItems();
		// myApp.isRefresh = false;
		// }
	}

	private void registListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				position = position - 1;
				String gwid = "";
				String DocId = "";
				if (StringUtil.isNotEmpty(StringUtil.noNull(data.get(position)
						.get("CDocID")))) {

					DocId = data.get(position).get("CDocID").toString();
				} else {
					DocId = data.get(position).get("obli_id").toString();

				}
				if (StringUtil.isNotEmpty(StringUtil.noNull(data.get(position)
						.get("InfoID")))) {
					gwid = data.get(position).get("InfoID").toString();
				} else {
					gwid = data.get(position).get("info_id").toString();
				}
				Intent intent = null;
				if ("0".equals(StringUtil.noNull(data.get(position).get(
						"NoAuditCount")))) {
					intent = new Intent(GwList_Activity.this,
							Gwpy_Activity.class);
				} else {
					intent = new Intent(GwList_Activity.this,
							Gwsh_Activity.class);
					intent.putExtra(
							"nac",
							StringUtil.noNull(data.get(position).get(
									"NoAuditCount")));
				}
				intent.putExtra(
						"AddCDocNamesStr",
						StringUtil.noNull(data.get(position).get(
								"AddCDocNamesStr")));
				intent.putExtra(
						"AddFilePathsStr",
						StringUtil.noNull(data.get(position).get(
								"AddFilePathsStr")));
				intent.putExtra("gwid", gwid);
				intent.putExtra("DocId", DocId);
				startActivity(intent);
			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	private void initComplement() {
		myApp = (MyApplication) getApplication();
		getActionBar().setTitle("公文信息列表");
		if (!myApp.getToActivity().equals("GwList")) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		data = new ArrayList<HashMap<String, Object>>();
		progressLn = (LinearLayout) findViewById(R.id.normal_progress);
		mListView = (XListView) findViewById(R.id.gw_list);
		pro_tv = (TextView) findViewById(R.id.gwlist_tv);
		bar = (ProgressBar) findViewById(R.id.gwlist_progressBar);
		// mListView.setPullLoadEnable(true);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this);
	}

	private void setListViewAdapter(String[] keys) {
		mAdapter = new MyListAdapter(data, GwList_Activity.this, keys);
		mListView.setAdapter(mAdapter);

	}

	public void click2Refresh(View view) {
		bar.setVisibility(View.VISIBLE);
		pro_tv.setText("");
		geneItems();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_search:
			Intent intent = new Intent(GwList_Activity.this,
					GwSearch_Activity.class);
			startActivityForResult(intent, 110);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent it) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, it);
		if (resultCode == RESULT_OK) {
			if (requestCode == 110) {
				data = (ArrayList<HashMap<String, Object>>) it
						.getSerializableExtra("data");
				setListViewAdapter(new String[] { "NoAuditCount", "oblititle",
						"sim_comname", "publish_start" });
			}
		}
	}

	private void geneItems() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String keys[] = { "Emid", "DayCount", "TopCount", "InfoID",
							"viewed" };
					// System.out.println("orgidstr" +
					// orgItem.get("Emid").toString());
					Object values[] = {
							Integer.parseInt(myApp.getComInfo().getEmid()),
							100, 10, 0, false };
					data = WebServiceUtil.getWebServiceMsg(keys, values,
							"getWebInformFroEmID", new String[] {
									"NoAuditCount", "InfoTitle", "PubComname",
									"PubDate", "AddCDocNamesStr",
									"AddFilePathsStr", "CDocID", "InfoID" });

					mHandler.sendEmptyMessage(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				geneItems();
				// mAdapter.notifyDataSetChanged();
				setListViewAdapter(new String[] { "NoAuditCount", "InfoTitle",
						"PubComname", "PubDate" });
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				geneItems();
				mAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onClick(View v) {
	}

}