package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.adapter.NoCommitAdapter;
import com.cqj.test.wbd2_gwpy.myinterface.INoCommitItem;
import com.cqj.test.wbd2_gwpy.presenter.ICommitPresenter;
import com.cqj.test.wbd2_gwpy.presenter.compl.CommitPresenterImpl;

import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by Administrator on 2016/5/9.
 */
public abstract class CheckCommitActivity<T extends INoCommitItem> extends Activity implements View.OnClickListener, ICommitPresenter.View {

    private ICommitPresenter<T> mPresenter;
    private Button mAllCheck, mCommit;
    private LinearLayout mBtnLn;
    private NoCommitAdapter<T> mAdapter;
    private List<T> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_no_commit);
        initComplement();
    }

    private void initComplement() {
        getActionBar().setTitle("离线记录上传");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ListView listView = (ListView) findViewById(R.id.nocommit_list);
        mAllCheck = (Button) findViewById(R.id.no_commit_allcheck);
        mCommit = (Button) findViewById(R.id.no_commit_commitbtn);
        mBtnLn = (LinearLayout) findViewById(R.id.btn_ln);
        mAllCheck.setOnClickListener(this);
        mCommit.setOnClickListener(this);
        mPresenter =new CommitPresenterImpl<T>(this);
        mData =getData();
        mAdapter = new NoCommitAdapter<T>(this,mData);
        listView.setAdapter(mAdapter);
    }

    protected abstract List<T> getData();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void commitStatus(int position, boolean isSuccess) {
        if (isSuccess) {
            mAdapter.getItem(position).setCommitSuccess(true);
        }
        mAdapter.setCommitFail(position);
        mAdapter.notifyDataSetChanged();
        mBtnLn.setVisibility(mAdapter.isAllCommit() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void toast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }


    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void onClick(View pView) {
        int id = pView.getId();
        if (id == mCommit.getId()) {
            HashMap<Integer, Boolean> checkList = mAdapter.getIsCheckList();
            mPresenter.upload(mData, checkList.keySet());
        } else if (id == mAllCheck.getId()) {
            mAdapter.setAllCheck();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.cancel(mAdapter.getData());
    }

}
