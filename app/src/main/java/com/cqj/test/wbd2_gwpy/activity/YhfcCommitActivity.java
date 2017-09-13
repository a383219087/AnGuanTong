package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.os.Bundle;

import com.cqj.test.wbd2_gwpy.YhfcCommitInfo;
import com.cqj.test.wbd2_gwpy.dao.SqliteOperator;

import java.util.List;

public class YhfcCommitActivity extends CheckCommitActivity<YhfcCommitInfo> {


    @Override
    protected List<YhfcCommitInfo> getData() {
        return SqliteOperator.INSTANCE.getYhfcCommitInfo(this).loadAll();
    }
}
