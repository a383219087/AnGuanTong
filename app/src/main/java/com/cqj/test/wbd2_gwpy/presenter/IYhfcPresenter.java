package com.cqj.test.wbd2_gwpy.presenter;

import android.content.Context;

import com.cqj.test.wbd2_gwpy.AqjcCommitInfo;
import com.cqj.test.wbd2_gwpy.SbjcCommitInfo;
import com.cqj.test.wbd2_gwpy.YhfcInfo;
import com.cqj.test.wbd2_gwpy.activity.MyApplication;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public interface IYhfcPresenter {

    void getYhfcList(MyApplication pMyApplication,String startDate, String endDate,String name);

    interface View{
        void getYhfcListSuccess(List<YhfcInfo> pYhfcInfoList);
        void pendingDialog();
        void cancelDialog();
        void toast(String toast);
        void toast(int toast);
        Context getContext();
        int getReview();
        int getFinished();

        boolean isYhzg();
    }
}
