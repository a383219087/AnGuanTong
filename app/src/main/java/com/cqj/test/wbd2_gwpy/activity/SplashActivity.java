package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cqj.test.wbd2_gwpy.CompanyInfo;
import com.cqj.test.wbd2_gwpy.CompanyInfoDao;
import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.UserInfo;
import com.cqj.test.wbd2_gwpy.UserInfoDao;
import com.cqj.test.wbd2_gwpy.dao.SqliteOperator;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * Created by Administrator on 2016/3/6.
 */
public class SplashActivity extends Activity {

    private UserInfoDao mUserInfoDao;
    private CompanyInfoDao mComInfoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_view);
        initComplement();
        waitToJump();
    }

    private void initComplement() {
        mUserInfoDao = SqliteOperator.INSTANCE.getUserInfoDao(this);
        mComInfoDao = SqliteOperator.INSTANCE.getCompanyInfoDao(this);
        Intent intent =new Intent(this,NetworkStateService.class);
        startService(intent);
    }

    private void waitToJump() {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<UserInfo> userInfos = mUserInfoDao.loadAll();
                if(userInfos ==null || userInfos.isEmpty()){
                    subscriber.onNext(true);
                }else{
                    UserInfo userInfo =userInfos.get(0);
                    if(userInfo.getIs_login()) {
                        ((MyApplication) getApplication()).setUser(userInfo);
                        List<CompanyInfo> comInfos = mComInfoDao.loadAll();
                        if (comInfos == null || comInfos.isEmpty()) {
                            subscriber.onNext(true);
                            return;
                        } else {
                            for (CompanyInfo companyInfo:comInfos){
                                if(companyInfo.getIs_choose()){
                                    ((MyApplication) getApplication()).setComInfo(companyInfo);
                                    break;
                                }
                            }
                        }
                        subscriber.onNext(false);
                    }else{
                        subscriber.onNext(true);
                    }
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                finish();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if(aBoolean){
                    Intent intent =new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent =new Intent(SplashActivity.this,HomePageActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }
}
