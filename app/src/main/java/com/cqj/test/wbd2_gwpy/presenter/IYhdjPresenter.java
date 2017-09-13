package com.cqj.test.wbd2_gwpy.presenter;

import android.graphics.Bitmap;

import com.cqj.test.wbd2_gwpy.AqjcCommitInfo;
import com.cqj.test.wbd2_gwpy.CsInfo;
import com.cqj.test.wbd2_gwpy.JcbDetailInfo;
import com.cqj.test.wbd2_gwpy.JcbInfo;
import com.cqj.test.wbd2_gwpy.RwInfo;
import com.cqj.test.wbd2_gwpy.SbInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by Administrator on 2016/3/8.
 */
public interface IYhdjPresenter {

    void getRwData();

    void getCsData();

    void getJcbData();

    void getSbData(String CsId);

    void getJcbDetail(String jcbId);

    void getPrePage();

    void getNextPage();

    void getJcbDetail();

    boolean isCommitting();

    void upload(AqjcCommitInfo info);

    void cancel();

    void getSssb(int pSbid,int comid);

    interface View{
        void getRwDataSuccess(List<RwInfo> data);
        void getCsDataSuccess(List<CsInfo> data);
        void getJcbDataSuccess(List<JcbInfo> data);
        void getSbDataSuccess(List<SbInfo> data);
        void changeJcbDetail(JcbDetailInfo info,int postion,int count);
        void commitStatus(boolean isSuccess);
        void toast(String toast);
        void toast(int toast);
        void pendingDialog(int resId);
        void setJcbDetail(String detail);
        void setJcbDetailGone();
        void cancelDialog();

        void setPlaceId(String pS);
    }
}
