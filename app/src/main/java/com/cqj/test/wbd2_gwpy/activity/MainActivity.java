package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.CompanyInfo;
import com.cqj.test.wbd2_gwpy.CompanyInfoDao;
import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.UserInfo;
import com.cqj.test.wbd2_gwpy.UserInfoDao;
import com.cqj.test.wbd2_gwpy.dao.SqliteOperator;
import com.cqj.test.wbd2_gwpy.util.Code;
import com.cqj.test.wbd2_gwpy.util.MyThread;
import com.cqj.test.wbd2_gwpy.util.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

	private ImageView yzmIv;// 验证码
	private EditText zhanghao, mima, yzmEdt;
	private SharedPreferences sp;
	private boolean isRemenber;
	private ImageView jzmm;
	private MyApplication myApp;
	private String yzmStr = "";
	private ExecutorService pool;
	private Button loginBtn;
	private UserInfoDao mUserInfoDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initComplement();
		pool = Executors.newFixedThreadPool(1);
	}


	private void initComplement() {
		myApp = (MyApplication) getApplication();
		mUserInfoDao = SqliteOperator.INSTANCE.getUserInfoDao(this);
		yzmIv = (ImageView) findViewById(R.id.yzm_iv);
		zhanghao = (EditText) findViewById(R.id.zhanghao_edt);
		mima = (EditText) findViewById(R.id.mima_edt);
		loginBtn = (Button) findViewById(R.id.main_loginbtn);
		jzmm = (ImageView) findViewById(R.id.jzmm);
		yzmEdt = (EditText) findViewById(R.id.yzm_edt);
		yzmIv.setImageBitmap(Code.getInstance().createBitmap());
		yzmStr = Code.getInstance().getCode();
		sp = getSharedPreferences(myApp.CONFIG, MODE_PRIVATE);
		isRemenber = sp.getBoolean(myApp.ISREM, false);
		if (isRemenber) {
			jzmm.setBackgroundResource(R.drawable.bnt01);
		} else {
			jzmm.setBackgroundResource(R.drawable.bnt02);
		}
		// 显示密码
		if (isRemenber) {
			zhanghao.setText(sp.getString(myApp.LOGINNAME, ""));
			zhanghao.setSelection(sp.getString(myApp.LOGINNAME, "").length());
			mima.setText(sp.getString(myApp.PWD, ""));
		}
	}

	@SuppressWarnings("unchecked")
	public void login(View view) {
		try {
			String zhStr = zhanghao.getText().toString();
			String mmStr = mima.getText().toString();
			String yzm_ = yzmEdt.getText().toString();
			if (StringUtil.isEmpty(zhStr)) {
				Toast.makeText(MainActivity.this, "帐号不能为空！", Toast.LENGTH_LONG)
						.show();
				return;
			}
			if (StringUtil.isEmpty(mmStr)) {
				Toast.makeText(MainActivity.this, "密码不能为空！", Toast.LENGTH_LONG)
						.show();
				return;
			}
//			if (!yzmStr.equalsIgnoreCase(yzm_)) {
//				Toast.makeText(MainActivity.this, "非法验证码！", Toast.LENGTH_LONG)
//						.show();
//				return;
//			}
			Editor edit = sp.edit();
			edit.putBoolean(myApp.ISREM, isRemenber);
			// 记住密码
			if (isRemenber) {
				edit.putString(myApp.LOGINNAME, zhanghao.getText().toString());
				edit.putString(myApp.PWD, mima.getText().toString());
			}
			edit.commit();
			// getSinglePersonalUserFromLogin
			String[] keys = { "requestName", "mphone", "email", "pwd", "ret" };
			Object[] values = { zhStr, "", "", mmStr, -1 };
			changeButtonStyle(true, "登录中...");
			pool.execute(new MyThread(keys, values,
					"getSinglePersonalUserFromLogin", handler));

		} catch (Exception e) {
			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}

	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				ArrayList<HashMap<String, Object>> result1 = (ArrayList<HashMap<String, Object>>) msg.obj;
				if (result1 == null) {
					Toast.makeText(MainActivity.this, "无法连接至服务器。",
							Toast.LENGTH_LONG).show();
					changeButtonStyle(false, "");
					return false;
				}
				if (result1.size() > 0) {
					if (result1.get(0) != null) {
						int intStr = Integer.valueOf((String) result1.get(0)
								.get("ret"));
						if (intStr != 0) {
							changeButtonStyle(false, "");
							Toast.makeText(MainActivity.this,
									StringUtil.parseError(intStr),
									Toast.LENGTH_LONG).show();
						} else {
							putUserData(result1.get(0));
							String[] keys2 = { "uPersonalID", "sState" };
							Object[] values2 = { myApp.getUser().getUid(), "在职" };
							changeButtonStyle(true, "获取公司信息...");
							pool.execute(new MyThread(keys2, values2,
									"getMoreComs", handler));
						}
					}
				} else {
					Toast.makeText(MainActivity.this, "返回无数据，应该是出错了",
							Toast.LENGTH_LONG).show();
				}
				break;
			case 2:
				ArrayList<HashMap<String, Object>> result2 = (ArrayList<HashMap<String, Object>>) msg.obj;
				result2 = saveComInfos(result2);
				if (result2.size() > 1) {
					Intent intent = new Intent(MainActivity.this,
							ComList_Activity.class);
					myApp.setToActivity("ComList");
					intent.putExtra("data", result2);
					startActivity(intent);
					changeButtonStyle(false, "");
					finish();
				} else if (result2.size() == 1) {
					Intent intent = new Intent(MainActivity.this,
							HomePageActivity.class);
					myApp.setToActivity("GwList");
					intent.putExtra("orgItem", result2.get(0));
					startActivity(intent);
					changeButtonStyle(false, "");
					finish();
				} else {
					Toast.makeText(MainActivity.this, "该帐号没有关联公司，请更换帐号。",
							Toast.LENGTH_LONG).show();
					changeButtonStyle(false, "");
				}
				break;
			default:
				break;
			}
			return false;
		}
	});

	private ArrayList<HashMap<String, Object>> saveComInfos(ArrayList<HashMap<String, Object>> list){
		ArrayList<HashMap<String, Object>> result =new ArrayList<HashMap<String, Object>>();
		if(list ==null || list.isEmpty()){
			return result;
		}
		CompanyInfoDao dao = SqliteOperator.INSTANCE.getCompanyInfoDao(this);
		dao.deleteAll();
		for (int i=0;i<list.size();i++) {
			HashMap<String, Object> orgItem =list.get(i);
			CompanyInfo comInfo = new CompanyInfo();
			comInfo.setCom_fullname(StringUtil.noNull(orgItem.get("orgname")));
			comInfo.setCom_name(StringUtil.noNull(orgItem.get("comname")));
			comInfo.setOrg_idstr(StringUtil.noNull(orgItem.get("orgidstr")));
			comInfo.setOrg_id(StringUtil.noNull(orgItem.get("orgid")));
			comInfo.setEmid(StringUtil.noNull(orgItem.get("Emid")));
			comInfo.setCom_id(StringUtil.noNull(orgItem.get("comid")));
			comInfo.setIs_choose(false);
			dao.insert(comInfo);
			orgItem.put("id",comInfo.getId());
			result.add(orgItem);
		}
		return result;
	}

	/**
	 * 存储用户信息到全局变量
	 * 
	 * @param map
	 */
	private void putUserData(HashMap<String, Object> map) {
		UserInfo user = new UserInfo();
		user.setName((String) map.get("姓名"));
		user.setLogin_name((String) map.get("昵称"));
		user.setLogin_pwd((String) map.get("密码"));
		user.setUid(Integer.parseInt((String) map.get("Uid")));
		user.setPhone_number((String) map.get("手机"));
		user.setXuliehao((String) map.get("序列号"));
		user.setTongdao((String) map.get("通道"));
		user.setIs_login(true);
		user.setDate(new Date());
		myApp.setUser(user);
		mUserInfoDao.insert(user);
	}

	private void changeButtonStyle(boolean isPress, String alertMsg) {
		if (isPress) {
			loginBtn.setText(alertMsg);
//			loginBtn.setBackgroundColor(Color.GRAY);
			loginBtn.setEnabled(false);
		} else {
			loginBtn.setEnabled(true);
			loginBtn.setText("登　录");
//			loginBtn.setBackgroundColor(getResources().getColor(
//					R.color.login_bg));
		}
	}

	public void jzmm(View view) {
		if (isRemenber) {
			jzmm.setBackgroundResource(R.drawable.bnt02);
			isRemenber = false;
		} else {
			jzmm.setBackgroundResource(R.drawable.bnt01);
			isRemenber = true;
		}
	}

	/**
	 * 切换验证码
	 * 
	 * @param view
	 */
	public void changeYzm(View view) {
		yzmIv.setImageBitmap(Code.getInstance().createBitmap());
		yzmStr = Code.getInstance().getCode();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent =new Intent(this,NetworkStateService.class);
		stopService(intent);
	}
}
