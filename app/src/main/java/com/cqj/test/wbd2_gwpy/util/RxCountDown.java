package com.cqj.test.wbd2_gwpy.util;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 * Created by Administrator on 2016/4/10.
 */
public class RxCountDown {

    public static Observable<Integer> countdown(int time,final boolean isUp) {
        if (time < 0) time = 0;

        final int countTime = time;
        final int upTime =0;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        if(isUp){
                            if(upTime<countTime){
                                return upTime+increaseTime.intValue();
                            }else{
                                return countTime;
                            }
                        }else {
                            return countTime - increaseTime.intValue();
                        }
                    }
                })
                .take(countTime + 1);

    }
}