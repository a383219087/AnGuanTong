package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.os.Bundle;

import com.cqj.test.wbd2_gwpy.AqjcCommitInfo;
import com.cqj.test.wbd2_gwpy.AqjcCommitInfoDao;
import com.cqj.test.wbd2_gwpy.SbjcCommitInfo;
import com.cqj.test.wbd2_gwpy.dao.SqliteOperator;

import java.util.List;

public class SbjcCommitActivity extends CheckCommitActivity<SbjcCommitInfo> {

    @Override
    protected List<SbjcCommitInfo> getData() {
        return SqliteOperator.INSTANCE.getSbCommitDao(this).loadAll();
    }
}
