package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.os.Bundle;

import com.cqj.test.wbd2_gwpy.YhzgCommitInfo;
import com.cqj.test.wbd2_gwpy.dao.SqliteOperator;

import java.util.List;

public class YhzgCommitActivity extends CheckCommitActivity<YhzgCommitInfo> {

    @Override
    protected List<YhzgCommitInfo> getData() {
        return SqliteOperator.INSTANCE.getYhzgCommitInfo(this).loadAll();
    }
}
