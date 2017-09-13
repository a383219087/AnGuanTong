package com.cqj.test.wbd2_gwpy.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.SbjcCommitInfo;
import com.cqj.test.wbd2_gwpy.SbjcCommitInfoDao;
import com.cqj.test.wbd2_gwpy.SbjcListInfo;
import com.cqj.test.wbd2_gwpy.SbjcListInfoDao;
import com.cqj.test.wbd2_gwpy.dao.SqliteOperator;
import com.cqj.test.wbd2_gwpy.util.StringUtil;
import com.cqj.test.wbd2_gwpy.util.WebServiceUtil;
import com.cqj.test.wbd2_gwpy.R;

import de.greenrobot.dao.query.QueryBuilder;


public class AqssDetailActivity extends Activity {

    private int SCPID;
    private Context context;
    private TextView sbmc, byry, byrq, sbzt, qtqk;
    private EditText edt;
    private Button detailCommit;
    private Spinner sbztSp;
    protected Calendar canl;
    protected MyApplication myApp;
    protected ArrayList<HashMap<String, Object>> dataList;
    protected Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message arg0) {
            // TODO Auto-generated method stub
            switch (arg0.what) {
                case 1:
                    if (dataList.size() > 0) {
                        SbjcListInfo info =parseSbjcInfo(dataList.get(0));
                        sbmc.setText(info.getProdname());
                        byry.setText(info.getSccheckname());
                        byrq.setText(info.getSccheckdate());
                        sbzt.setText(info.getSccheckstat());
                        qtqk.setText(info.getSccheckdetail());
                    }
                    break;
                case 5:
                    detailCommit.setEnabled(true);
                    Toast.makeText(context, "提交失败。", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    detailCommit.setEnabled(true);
                    Toast.makeText(context, "提交成功。", Toast.LENGTH_LONG).show();
                    edt.setText("");
                    finish();
                    break;
                case 7:
                    detailCommit.setEnabled(true);
                    Toast.makeText(context, "未联网，已保存！等待下次联网后再提示", Toast.LENGTH_LONG)
                            .show();
                    edt.setText("");
                    finish();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private SbjcListInfo parseSbjcInfo(HashMap<String, Object> map) {
        SbjcListInfoDao dao =SqliteOperator.INSTANCE.getSbjcListInfoDao(AqssDetailActivity.this);
        SbjcListInfo info = getSbjcListInfo(dao);
        String sbNum =StringUtil.noNull(map.get("scnumber"));
        if(!TextUtils.isEmpty(sbNum)){
            sbNum = String.format("(%s)", sbNum);
        }
        info.setProdname(StringUtil.noNull(map.get("prodname"))+sbNum);
        String date = StringUtil.noNull(map.get("sccheckdate"));
        if (date.length() > 10) {
            date = date.substring(0, 10);
        }
        info.setSccheckdate(date);
        info.setSccheckname(StringUtil.noNull(map.get("sccheckname")));
        info.setSccheckstat(StringUtil.noNull(map.get("sccheckstat")));
        info.setSccheckdetail(StringUtil.noNull(map.get("sccheckdetail")));
        info.setCpid(SCPID);
        dao.insertOrReplace(info);
        return info;
    }

    private SbjcListInfo getSbjcListInfo(SbjcListInfoDao pDao) {
        QueryBuilder<SbjcListInfo> qb = pDao.queryBuilder();
        qb.where(SbjcListInfoDao.Properties.Cpid.eq(SCPID));
        List<SbjcListInfo> listInfos = qb.build().list();
        SbjcListInfo info;
        if(listInfos!=null&& !listInfos.isEmpty()){
            info = listInfos.get(0);
        }else{
            info = new SbjcListInfo();

        }
        return info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aqss_detail);
        initComplement();
        if(MyApplication.isConnection) {
            getData();
        }else{
            SbjcListInfoDao dao =SqliteOperator.INSTANCE.getSbjcListInfoDao(AqssDetailActivity.this);
            SbjcListInfo info = getSbjcListInfo(dao);
            sbmc.setText(info.getProdname());
            byry.setText(info.getSccheckname());
            byrq.setText(info.getSccheckdate());
            sbzt.setText(info.getSccheckstat());
            qtqk.setText(info.getSccheckdetail());
        }
    }

    private void initComplement() {
        canl = Calendar.getInstance();
        myApp = (MyApplication) getApplication();
        getActionBar().setTitle("设备巡查");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        context = AqssDetailActivity.this;
        SCPID = getIntent().getIntExtra("SCPID", 0);
        sbmc = (TextView) findViewById(R.id.aqssdetail_sbmc);
        byry = (TextView) findViewById(R.id.aqssdetail_byry);
        byrq = (TextView) findViewById(R.id.aqssdetail_byrq);
        sbzt = (TextView) findViewById(R.id.aqssdetail_sbzt);
        qtqk = (TextView) findViewById(R.id.aqssdetail_qtqk);
        edt = (EditText) findViewById(R.id.aqssdetail_edt);
        sbztSp = (Spinner) findViewById(R.id.aqssdetail_sbztsp);
        detailCommit = (Button) findViewById(R.id.aqssdetail_commit);
    }

    public void commit(View view) {
        if (TextUtils.isEmpty(edt.getText().toString())) {
            return;
        }
        detailCommit.setEnabled(false);
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    int month = canl.get(Calendar.MONTH) + 1;
                    String monthStr = "";
                    if (month < 10) {
                        monthStr = "0" + month;
                    } else {
                        monthStr = month + "";
                    }
                    int day = canl.get(Calendar.DAY_OF_MONTH);
                    String dayStr = "";
                    if (day < 10) {
                        dayStr = "0" + day;
                    } else {
                        dayStr = day + "";
                    }
                    String nowDate = canl.get(Calendar.YEAR) + "-" + monthStr
                            + "-" + dayStr + "T00:00:00.000";
                    int emid = Integer.parseInt(myApp.getComInfo().getEmid());
                    if (!MyApplication.isConnection) {
                        SbjcCommitInfo info = new SbjcCommitInfo();
                        info.setCPID(SCPID);
                        info.setCpName(sbmc.getText().toString());
                        info.setCDate(nowDate);
                        info.setCEmid(emid);
                        info.setREmid(emid);
                        info.setCheckDate(new Date());
                        info.setCRemark(edt.getText().toString());
                        info.setCState(sbztSp.getSelectedItem().toString());
                        SbjcCommitInfoDao sbjcCommitInfoDao = SqliteOperator.INSTANCE.getSbCommitDao(AqssDetailActivity.this);
                        sbjcCommitInfoDao.insert(info);
                    } else {
                        String[] keys = {"CPID", "cDate", "cEmid", "rEmid",
                                "cRemark", "cState"};
                        Object[] values = {SCPID, nowDate, emid, emid,
                                edt.getText().toString(),
                                sbztSp.getSelectedItem().toString()};
                        dataList = WebServiceUtil.getWebServiceMsg(keys,
                                values, "AddSafetySetCheck", new String[]{},
                                WebServiceUtil.SAFE_URL);
                    }
                    if (MyApplication.isConnection) {
                        handler.sendEmptyMessage(6);
                    } else {
                        handler.sendEmptyMessage(7);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(5);
                }
            }
        }).start();
    }

    private void getData() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    String[] keys = {"SCPID", "CPID", "cStart", "cEnd",
                            "cState"};
                    Object[] values = {0, SCPID, "2000-01-01T00:00:00.000",
                            "2035-12-31T00:00:00.000", "正常"};// Integer.parseInt(myApp.getComInfo().getComId())
                    dataList = WebServiceUtil.getWebServiceMsg(keys, values,
                            "getSafetySetCheck", new String[]{"prodname","scnumber",
                                    "sccheckname", "sccheckdate",
                                    "sccheckstat", "sccheckdetail"},
                            WebServiceUtil.SAFE_URL);
                    if (dataList != null)
                        handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
