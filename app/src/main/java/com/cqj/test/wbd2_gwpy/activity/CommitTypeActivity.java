package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.dao.SqliteOperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommitTypeActivity extends Activity {

    public static final String NAME = "name";
    public static final String COUNT = "count";
    public static final String TYPE = "type";
    public static final String TYPE_AQJC = "aqjc";
    public static final String TYPE_YHFC = "yhfc";
    public static final String TYPE_AQZG = "aqzg";
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_type);
        getActionBar().setTitle("离线数据");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mListView = (ListView) findViewById(R.id.commit_type_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return true;
    }

    private void refresh() {
        final List<HashMap<String, String>> data = getData();
        SimpleAdapter adapter =new SimpleAdapter(this, data,R.layout.commit_type_item,new String[]{NAME,COUNT},
                new int[]{R.id.commit_item_name,R.id.commit_item_count});
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
                if (TYPE_AQJC.equals(data.get(pI).get(TYPE))) {
                    Intent intent =new Intent(CommitTypeActivity.this,AqjcCommitActivity.class);
                    startActivity(intent);
                }else if (TYPE_YHFC.equals(data.get(pI).get(TYPE))) {
                    Intent intent =new Intent(CommitTypeActivity.this,YhfcCommitActivity.class);
                    startActivity(intent);
                }else if (TYPE_AQZG.equals(data.get(pI).get(TYPE))) {
                    Intent intent =new Intent(CommitTypeActivity.this,YhzgCommitActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private List<HashMap<String,String>> getData(){
        List<HashMap<String,String>> datas =new ArrayList<HashMap<String, String>>();
        long aqjcCount = SqliteOperator.INSTANCE.getCommitInfo(this).queryBuilder().buildCount().count();
        long yhfcCount = SqliteOperator.INSTANCE.getYhfcCommitInfo(this).queryBuilder().buildCount().count();
        long yhzgCount = SqliteOperator.INSTANCE.getYhzgCommitInfo(this).queryBuilder().buildCount().count();
        if(aqjcCount>0){
            HashMap<String,String> aqjcData =new HashMap<String, String>();
            aqjcData.put(NAME,"安全检查");
            aqjcData.put(TYPE, TYPE_AQJC);
            aqjcData.put(COUNT,String.valueOf(aqjcCount));
            datas.add(aqjcData);
        }
        if(yhfcCount>0){
            HashMap<String,String> aqjcData =new HashMap<String, String>();
            aqjcData.put(NAME,"隐患复查");
            aqjcData.put(TYPE, TYPE_YHFC);
            aqjcData.put(COUNT,String.valueOf(yhfcCount));
            datas.add(aqjcData);
        }
        if(yhzgCount>0){
            HashMap<String,String> aqjcData =new HashMap<String, String>();
            aqjcData.put(NAME,"安全整改");
            aqjcData.put(TYPE, TYPE_AQZG);
            aqjcData.put(COUNT,String.valueOf(yhzgCount));
            datas.add(aqjcData);
        }
        return datas;
    }
}
