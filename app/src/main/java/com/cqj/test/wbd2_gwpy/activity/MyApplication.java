package com.cqj.test.wbd2_gwpy.activity;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.cqj.test.wbd2_gwpy.CompanyInfo;
import com.cqj.test.wbd2_gwpy.DaoMaster;
import com.cqj.test.wbd2_gwpy.DaoSession;
import com.cqj.test.wbd2_gwpy.UserInfo;

public class MyApplication extends Application {

	public static boolean isConnection =true;
	public static final String OFFLINE_DB ="offline_db";
	public final String CONFIG ="config";
	public final String LOGINNAME="loginname";
	public final String PWD ="pwd";
	public final String ISREM="isRem";
	private UserInfo user;
	private CompanyInfo comInfo;
	private String toActivity="";
	public boolean isRefresh =true;

	public String getToActivity() {
		return toActivity;
	}


	public void setToActivity(String toActivity) {
		this.toActivity = toActivity;
	}


	public CompanyInfo getComInfo() {
		return comInfo;
	}


	public void setComInfo(CompanyInfo comInfo) {
		this.comInfo = comInfo;
	}


	public UserInfo getUser() {
		return user;
	}


	public void setUser(UserInfo user) {
		this.user = user;
	}


	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Intent intent =new Intent(this,NetworkStateService.class);
		stopService(intent);
	}


}
