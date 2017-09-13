package com.cqj.test.wbd2_gwpy.myinterface;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by Administrator on 2016/3/10.
 */
public interface INoCommitItem {

    String getName();
    String getDetail();
    String getDate();
    boolean isCommitSuccess();
    void setCommitSuccess(boolean pIsSuccess);
    ArrayList<HashMap<String, Object>> upload() throws Exception;
    void deleteCommit(Context context);
}
