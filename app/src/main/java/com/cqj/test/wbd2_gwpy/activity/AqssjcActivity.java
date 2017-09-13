package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.SbjcCommitInfo;
import com.cqj.test.wbd2_gwpy.SbjcCommitInfoDao;
import com.cqj.test.wbd2_gwpy.SbjcListInfo;
import com.cqj.test.wbd2_gwpy.SbjcListInfoDao;
import com.cqj.test.wbd2_gwpy.adapter.AqssAdapter;
import com.cqj.test.wbd2_gwpy.dao.SqliteOperator;
import com.cqj.test.wbd2_gwpy.util.StringUtil;
import com.cqj.test.wbd2_gwpy.util.WebServiceUtil;
import com.zxing.activity.MipcaActivityCapture;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * 安全设施检查
 *
 * @author Administrator
 */
public class AqssjcActivity extends Activity {

    private ListView listView;
    protected MyApplication myApp;
    private EditText edText;
    private Spinner cssp, zrrsp, sbzt;
    private ArrayList<HashMap<String, Object>> dataList;
    protected ArrayList<HashMap<String, Object>> data2;
    private AqssAdapter mAdapter;
    private Button aqssjc_commit, aqssjc_sxbtn;
    protected Calendar canl;
    protected ArrayList<HashMap<String, Object>> gyData;
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 1:
                    SbjcListInfoDao sbjcCommitInfoDao = SqliteOperator.INSTANCE.getSbjcListInfoDao(AqssjcActivity.this);
                    sbjcCommitInfoDao.deleteAll();
                    List<SbjcListInfo> infoList =parseSbjcInfo();
                    sbjcCommitInfoDao.insertOrReplaceInTx(infoList);
                    aqssjc_sxbtn.setEnabled(true);
                    mAdapter = new AqssAdapter(AqssjcActivity.this, infoList);
                    listView.setAdapter(mAdapter);
                    break;
                case 2:
                    SimpleAdapter mSpAdapter = new SimpleAdapter(
                            AqssjcActivity.this, data2,
                            android.R.layout.simple_spinner_item,
                            new String[]{"mplname"},
                            new int[]{android.R.id.text1});
                    cssp.setAdapter(mSpAdapter);
                    mSpAdapter
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    break;
                case 3:
                    SimpleAdapter ada = new SimpleAdapter(AqssjcActivity.this,
                            gyData, android.R.layout.simple_spinner_item,
                            new String[]{"emName"},
                            new int[]{android.R.id.text1});
                    zrrsp.setAdapter(ada);
                    ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    break;
                case 4:
                    Toast.makeText(AqssjcActivity.this, "网络连接超时，请稍后再试...",
                            Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    aqssjc_commit.setEnabled(true);
                    Toast.makeText(AqssjcActivity.this, "提交失败。", Toast.LENGTH_LONG)
                            .show();
                    break;
                case 6:
                    aqssjc_commit.setEnabled(true);
                    Toast.makeText(AqssjcActivity.this, "提交成功。", Toast.LENGTH_LONG)
                            .show();
                    edText.setText("");
                    break;
                case 7:
                    aqssjc_commit.setEnabled(true);
                    Toast.makeText(AqssjcActivity.this, "未联网，已保存！等待下次联网后再提示", Toast.LENGTH_LONG)
                            .show();
                    edText.setText("");
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private List<SbjcListInfo> parseSbjcInfo() {
        List<SbjcListInfo> list =new ArrayList<SbjcListInfo>();
        for (HashMap<String,Object> map:dataList){
            SbjcListInfo info =new SbjcListInfo();
            String sbNum =StringUtil.noNull(map.get("cpnumber"));
            if(!TextUtils.isEmpty(sbNum)){
                sbNum = String.format("(%s)", sbNum);
            }
            info.setProdname(StringUtil.noNull(map.get("cpname"))+sbNum);
            info.setSccheckdate(StringUtil.noNull(map.get("lastcheck")));
            info.setSccheckname(StringUtil.noNull(map.get("cpmaintain")));
            info.setSccheckstat("正常");
            String cpidStr =StringUtil.noNull(map.get("cpid"));
            try {
                info.setCpid(Integer.parseInt(cpidStr));
            } catch (NumberFormatException pE) {
                pE.printStackTrace();
                info.setCpid(0);
            }
            list.add(info);
        }

        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aqssjc_view);
        myApp = (MyApplication) getApplication();
        initComplement();
        if(MyApplication.isConnection) {
            getData();
        }else{
            SbjcListInfoDao sbjcListInfoDao =SqliteOperator.INSTANCE.getSbjcListInfoDao(this);
            mAdapter = new AqssAdapter(AqssjcActivity.this, sbjcListInfoDao.loadAll());
            listView.setAdapter(mAdapter);
        }
    }

    private void getData() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    String[] keys = {"SCPID", "SCType", "FliedID", "isOver",
                            "KEmid", "Comid"};
                    Object[] values = {0, "", 0, -1, 0,
                            Integer.parseInt(myApp.getComInfo().getCom_id())};//
                    dataList = WebServiceUtil.getWebServiceMsg(keys, values,
                            "getSafetySetList", new String[]{"cpname","cpnumber",
                                    "SCPID", "lastcheck", "cpid","cpmaintain"},
                            WebServiceUtil.HUIWEI_SAFE_URL,WebServiceUtil.HUIWEI_NAMESPACE);
                    handler.sendEmptyMessage(1);
                    String keys2[] = {"Comid"};
                    Object values2[] = {Integer.parseInt(myApp.getComInfo()
                            .getCom_id())};
                    ArrayList<HashMap<String, Object>> tempdata2 = WebServiceUtil
                            .getWebServiceMsg(keys2, values2, "getAllField",
                                    new String[]{"mplid", "mplname"},
                                    WebServiceUtil.URL);
                    if (tempdata2.size() > 1) {
                        data2.addAll(tempdata2);
                    }
                    handler.sendEmptyMessage(2);
                    String keys3[] = {"orgid"};
                    Object values3[] = {Integer.parseInt(myApp.getComInfo()
                            .getOrg_id())};
                    ArrayList<HashMap<String, Object>> tempData = WebServiceUtil
                            .getWebServiceMsg(keys3, values3,
                                    "getAllEmployeeFromOrgID", new String[]{
                                            "emName", "Emid"});
                    if (tempData.size() > 1) {
                        gyData.addAll(tempData);
                    }
                    handler.sendEmptyMessage(3);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    handler.sendEmptyMessage(4);
                }
            }
        }).start();
    }

    private void initComplement() {
        canl = Calendar.getInstance();
        getActionBar().setTitle("设备巡查");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.aqssjc_list);
        cssp = (Spinner) findViewById(R.id.aqssjc_cssp);
        zrrsp = (Spinner) findViewById(R.id.aqssjc_zrrsp);
        edText = (EditText) findViewById(R.id.aqssjc_edt);
        sbzt = (Spinner) findViewById(R.id.aqssjc_sbztsp);
        aqssjc_commit = (Button) findViewById(R.id.aqssjc_commit);
        LinearLayout shaiLn = (LinearLayout) findViewById(R.id.shaixuan_item);
        aqssjc_sxbtn = (Button) findViewById(R.id.aqssjc_sxbtn);
        data2 = new ArrayList<HashMap<String, Object>>();
        gyData = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> allCs = new HashMap<String, Object>();
        allCs.put("mplid", 0);
        allCs.put("mplname", "所有");
        data2.add(allCs);
        HashMap<String, Object> allZrr = new HashMap<String, Object>();
        allZrr.put("Emid", 0);
        allZrr.put("emName", "所有");
        gyData.add(allZrr);
        if(!MyApplication.isConnection){
            shaiLn.setVisibility(View.GONE);
        }else{
            shaiLn.setVisibility(View.VISIBLE);
        }
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

    public void shaixuan(View view) {
        aqssjc_sxbtn.setEnabled(false);
        int csidStr, zrridStr;
        try {
            csidStr = Integer.parseInt(data2
                    .get(cssp.getSelectedItemPosition()).get("mplid")
                    .toString());
            zrridStr = Integer.parseInt(gyData
                    .get(zrrsp.getSelectedItemPosition()).get("Emid")
                    .toString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        final int csid = csidStr;
        final int zrrid = zrridStr;
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    String[] keys = {"SCPID", "SCType", "FliedID", "isOver",
                            "KEmid", "Comid"};
                    Object[] values = {0, "", csid, true, zrrid,
                            Integer.parseInt(myApp.getComInfo().getCom_id())};// Integer.parseInt(myApp.getComInfo().getComId())
                    dataList = WebServiceUtil.getWebServiceMsg(keys, values,
                            "getSafetySetList", new String[]{"cpname",
                                    "SCPID", "lastcheck", "cpid"},
                            WebServiceUtil.SAFE_URL);
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void commit(View view) {
        System.out.println("sbzt.getSelectedItem().toString():"
                + sbzt.getSelectedItem().toString());
        if (TextUtils.isEmpty(edText.getText().toString())) {
            return;
        }
        HashMap<Integer, Integer> map = mAdapter.getCheckMap();
        if (map == null || map.size() == 0) {
            Toast.makeText(AqssjcActivity.this, "没有选择任何设备。", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        aqssjc_commit.setEnabled(false);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    HashMap<Integer, Integer> map = mAdapter.getCheckMap();
                    Set<Integer> keySet = map.keySet();
                    int month = canl.get(Calendar.MONTH) + 1;
                    String monthStr;
                    if (month < 10) {
                        monthStr = "0" + month;
                    } else {
                        monthStr = month + "";
                    }
                    int day = canl.get(Calendar.DAY_OF_MONTH);
                    String dayStr;
                    if (day < 10) {
                        dayStr = "0" + day;
                    } else {
                        dayStr = day + "";
                    }
                    String nowDate = canl.get(Calendar.YEAR) + "-" + monthStr
                            + "-" + dayStr + "T00:00:00.000";
                    int emid = Integer.parseInt(myApp.getComInfo().getEmid());
                    for (int key : keySet) {
                        if (!MyApplication.isConnection) {
                            SbjcCommitInfo info =new SbjcCommitInfo();
                            info.setCPID(map.get(key));
                            info.setCDate(nowDate);
                            info.setCEmid(emid);
                            info.setREmid(emid);
                            info.setCpName(mAdapter.getItem(key).getProdname());
                            info.setCheckDate(new Date());
                            info.setCRemark(edText.getText().toString());
                            info.setCState(sbzt.getSelectedItem().toString());
                            SbjcCommitInfoDao sbjcCommitInfoDao =SqliteOperator.INSTANCE.getSbCommitDao(AqssjcActivity.this);
                            sbjcCommitInfoDao.insert(info);
                        }else {
                            String[] keys = {"CPID", "cDate", "cEmid", "rEmid",
                                    "cRemark", "cState"};
                            Object[] values = {map.get(key), nowDate, emid, emid,
                                    edText.getText().toString(),
                                    sbzt.getSelectedItem().toString()};
                            dataList = WebServiceUtil.getWebServiceMsg(keys,
                                    values, "AddSafetySetCheck", new String[]{},
                                    WebServiceUtil.SAFE_URL);
                        }
                    }
                    if(MyApplication.isConnection){
                        handler.sendEmptyMessage(6);
                    }else{
                        handler.sendEmptyMessage(7);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(5);
                }
            }
        }).start();
    }

    public void toEwm(View view) {
        Intent intent = new Intent(AqssjcActivity.this, CameraTestActivity.class);
        startActivityForResult(intent, 110);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 110:
                    String ewmStr = data.getStringExtra("result");
                    String[] resultArr = ewmStr.split("-");
                    if (resultArr.length == 2) {
                        try {
                            String type = resultArr[0];
                            if (type.equals("aqss")||type.equals("sssb")) {
                                int id =0;
                                try {
                                    id =Integer.parseInt(resultArr[1]);
                                } catch (NumberFormatException ignored) {
                                }
                                Intent intent = new Intent(AqssjcActivity.this,
                                        AqssDetailActivity.class);
                                intent.putExtra("SCPID",id );
                                startActivity(intent);
                            } else {
                                Toast.makeText(AqssjcActivity.this, "识别的不是设备", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception pE) {
                            pE.printStackTrace();
                            Toast.makeText(AqssjcActivity.this, "识别的不是设备", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AqssjcActivity.this, "识别的不是设备", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
