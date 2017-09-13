package com.cqj.test.wbd2_gwpy.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cqj.test.wbd2_gwpy.CompanyInfo;
import com.cqj.test.wbd2_gwpy.CompanyInfoDao;
import com.cqj.test.wbd2_gwpy.dao.SqliteOperator;
import com.cqj.test.wbd2_gwpy.util.BitmapUtil;
import com.cqj.test.wbd2_gwpy.view.SweetAlertDialog;
import com.cqj.test.wbd2_gwpy.view.SweetAlertDialog.OnSweetClickListener;
import com.cqj.test.wbd2_gwpy.view.XListView;
import com.cqj.test.wbd2_gwpy.view.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;import com.cqj.test.wbd2_gwpy.R;

import de.greenrobot.dao.query.QueryBuilder;


public class ComList_Activity extends Activity implements IXListViewListener {

	private XListView list;
	private MyApplication myApp;
	private Handler handler;
	private ArrayList<HashMap<String, Object>> data;
	private List<HashMap<String, Object>> tempData;
	private static final int PAGECOUNT = 1;
	private int page = 1;
	private final CompanyInfoDao mCompanyInfoDao = SqliteOperator.INSTANCE.getCompanyInfoDao(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comlist_view);
		myApp = (MyApplication) getApplication();
		initComplement();
		registListener();
	}

	private void registListener() {
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				List<CompanyInfo> comInfos =mCompanyInfoDao.loadAll();
				for (CompanyInfo companyInfo:comInfos){
					companyInfo.setIs_choose(false);
					mCompanyInfoDao.update(companyInfo);
				}
				Intent intent = new Intent(ComList_Activity.this,
						HomePageActivity.class);
				intent.putExtra("orgItem", tempData.get(position - 1));
				startActivity(intent);
				finish();
			}
		});
	}

	private void initComplement() {
		handler = new Handler();
		list = (XListView) findViewById(R.id.normal_list);
		list.setPullRefreshEnable(false);
		data = (ArrayList<HashMap<String, Object>>) getIntent()
				.getSerializableExtra("data");
		if(data ==null){
			tempData =CompanyInfo2NameMap(mCompanyInfoDao.loadAll());
		}else {
			tempData = data;
		}
		SimpleAdapter ada = new SimpleAdapter(ComList_Activity.this, tempData,
				R.layout.com_item, new String[] { "comname" },
				new int[] { R.id.comitem_comname });
		list.setAdapter(ada);
		list.setXListViewListener(this);
		list.setPullLoadEnable(false);
		myApp.isRefresh = true;
	}

	@Override
	public void onBackPressed() {

		// TODO Auto-generated method stub

		new SweetAlertDialog(ComList_Activity.this,
				SweetAlertDialog.WARNING_TYPE)
				.setTitleText(
						getResources().getString(R.string.dialog_default_title))
				.setContentText("你确定退出吗？").setCancelText("点错了")
				.setConfirmText("是的！").showCancelButton(true)
				.setCancelClickListener(new OnSweetClickListener() {

					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						sweetAlertDialog.cancel();
					}
				}).setConfirmClickListener(new OnSweetClickListener() {

					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						sweetAlertDialog.cancel();
						BitmapUtil.deleteFile(new File(BitmapUtil
								.getPath(ComList_Activity.this)));
						finish();
						Intent intent =new Intent(ComList_Activity.this,NetworkStateService.class);
						stopService(intent);
					}
				}).show();

	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
	}

	private ArrayList<HashMap<String, Object>> CompanyInfo2NameMap(List<CompanyInfo> companyInfoList){
		ArrayList<HashMap<String, Object>> result =new ArrayList<HashMap<String, Object>>();
		if(companyInfoList==null || companyInfoList.isEmpty()){
			return result;
		}
		for (CompanyInfo info :companyInfoList){
			HashMap<String, Object> map =new HashMap<String, Object>();
			map.put("comname",info.getCom_name());
			map.put("orgname",info.getCom_fullname());
			map.put("orgidstr",info.getOrg_idstr());
			map.put("orgid",info.getOrg_id());
			map.put("Emid",info.getEmid());
			map.put("comid",info.getCom_id());
			map.put("id",info.getId());
			result.add(map);

		}
		return result;
	}

}
