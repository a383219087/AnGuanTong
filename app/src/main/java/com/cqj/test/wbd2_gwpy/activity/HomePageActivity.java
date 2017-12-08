package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.AqjcCommitInfo;
import com.cqj.test.wbd2_gwpy.CompanyInfo;
import com.cqj.test.wbd2_gwpy.CompanyInfoDao;
import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.SbjcCommitInfo;
import com.cqj.test.wbd2_gwpy.UserInfoDao;
import com.cqj.test.wbd2_gwpy.dao.SqliteOperator;
import com.cqj.test.wbd2_gwpy.util.BitmapUtil;
import com.cqj.test.wbd2_gwpy.util.PermisionUtils;
import com.cqj.test.wbd2_gwpy.util.StringUtil;
import com.cqj.test.wbd2_gwpy.util.WebServiceUtil;
import com.cqj.test.wbd2_gwpy.view.SweetAlertDialog;
import com.cqj.test.wbd2_gwpy.view.SweetAlertDialog.OnSweetClickListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.greenrobot.dao.query.QueryBuilder;
import rx.functions.Action1;


public class HomePageActivity extends Activity implements OnClickListener {

    private TextView arcProgress;
    private ArrayList<HashMap<String, Object>> safeData;
    private Calendar canl;
    private MyApplication myApp;
    private Button mExitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_view);
        PermisionUtils.verifyStoragePermissions(this);
        initComplement();
        geneItems();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 1:
                    if (safeData.size() == 0) {
                        Toast.makeText(HomePageActivity.this, "安全指数查询无数据",
                                Toast.LENGTH_LONG).show();
                    } else {
                        String indexStr = StringUtil.noNull(safeData.get(0).get(
                                "comindex"));
                        double index = 0;
                        if (StringUtil.isNotEmpty(indexStr)) {
                            try {
                                index = Double.parseDouble(indexStr);
                            } catch (NumberFormatException ignored) {
                            }
                        }
                        //0~10算0
                        arcProgress.setText(index + "分");
                        arcProgress.setBackgroundResource(getSafeIndexDrawable(index));
                    }
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    private int getSafeIndexDrawable(double safeIndex) {
        if (safeIndex > 0 && safeIndex < 10) {
            return R.drawable.safetyindex00;
        } else if (safeIndex < 20) {
            return R.drawable.safetyindex10;
        } else if (safeIndex < 30) {
            return R.drawable.safetyindex20;
        } else if (safeIndex < 40) {
            return R.drawable.safetyindex30;
        } else if (safeIndex < 50) {
            return R.drawable.safetyindex40;
        } else if (safeIndex < 60) {
            return R.drawable.safetyindex50;
        } else if (safeIndex < 70) {
            return R.drawable.safetyindex60;
        } else if (safeIndex < 80) {
            return R.drawable.safetyindex70;
        } else if (safeIndex < 90) {
            return R.drawable.safetyindex80;
        } else if (safeIndex < 100) {
            return R.drawable.safetyindex90;
        } else if (safeIndex >= 100) {
            return R.drawable.safetyindex100;
        }
        return R.drawable.safetyindex00;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    private void initComplement() {
        myApp = (MyApplication) getApplication();
        getActionBar().setTitle("主页");
        if (!myApp.getToActivity().equals("GwList")) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        safeData = new ArrayList<HashMap<String, Object>>();
        arcProgress = (TextView) findViewById(R.id.gwlist_arc_progress);
        TextView listTitleDate = (TextView) findViewById(R.id.gwlist_title_date);
        mExitLogin = (Button) findViewById(R.id.homepage_exit);
        mExitLogin.setOnClickListener(this);
        TextView userName = (TextView) findViewById(R.id.gwlist_name);
        userName.setText(myApp.getUser().getName());
        canl = Calendar.getInstance();
        listTitleDate.setText(android.text.format.DateFormat.format("yyyy年MM月dd日", new Date()));
        TextView gwlistZz = (TextView) findViewById(R.id.gwlist_zzclick);
        gwlistZz.setText(Html.fromHtml("<u>[职责]</u>"));
        gwlistZz.setOnClickListener(this);
        HashMap<String, Object> orgItem = (HashMap<String, Object>) getIntent().getSerializableExtra(
                "orgItem");
        if (orgItem != null) {
            CompanyInfo comInfo = new CompanyInfo();
            comInfo.setCom_fullname(StringUtil.noNull(orgItem.get("orgname")));
            comInfo.setCom_name(StringUtil.noNull(orgItem.get("comname")));
            comInfo.setOrg_idstr(StringUtil.noNull(orgItem.get("orgidstr")));
            comInfo.setOrg_id(StringUtil.noNull(orgItem.get("orgid")));
            comInfo.setEmid(StringUtil.noNull(orgItem.get("Emid")));
            comInfo.setCom_id(StringUtil.noNull(orgItem.get("comid")));
            comInfo.setId(Long.parseLong(StringUtil.integerNoNull(orgItem.get("id"))));
            comInfo.setIs_choose(true);
            SqliteOperator.INSTANCE.getCompanyInfoDao(this).update(comInfo);
            myApp.setComInfo(comInfo);
        }
    }

    private void geneItems() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    int month = canl.get(Calendar.MONTH) + 1;
                    String monthStr;
                    if (month < 10) {
                        monthStr = "0" + month;
                    } else {
                        monthStr = month + "";
                    }
                    String beforeDate = canl.get(Calendar.YEAR) + "-" + monthStr
                            + "-01" + "T00:00:01.000";
                    String nowDate = canl.get(Calendar.YEAR) + "-" + monthStr
                            + "-" + String.valueOf(getDayOfMonth()) + "T23:59:59.000";
                    String keys2[] = {"ComID", "nStart", "nEnd", "staIndexID"};
                    Object values2[] = {
                            Integer.parseInt(myApp.getComInfo().getCom_id()),
                            beforeDate, nowDate, 7};
                    safeData = WebServiceUtil.getWebServiceMsg(keys2, values2,
                            "getSafetyIndexFromCom",
                            new String[]{"comindex"},
                            WebServiceUtil.HUIWEI_SAFE_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                    mHandler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static int getDayOfMonth() {
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        return aCalendar.getActualMaximum(Calendar.DATE);
    }

    public void hpClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.hp_aqjc:// 安全检查
                if (MyApplication.isConnection) {
                    QueryBuilder<AqjcCommitInfo> qb = SqliteOperator.INSTANCE.getCommitInfo(this).queryBuilder();
                    long yhfcCount = SqliteOperator.INSTANCE.getYhfcCommitInfo(this).queryBuilder().buildCount().count();
                    long yhzgCount = SqliteOperator.INSTANCE.getYhzgCommitInfo(this).queryBuilder().buildCount().count();
                    if (qb.buildCount().count() > 0 || yhzgCount > 0 || yhfcCount > 0) {
                        intent = new Intent(this, CommitTypeActivity.class);
                        startActivity(intent);
                        return;
                    }
                }
                RxPermissions.getInstance(this)
                        .request(android.Manifest.permission.CAMERA)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean) {
                                    Intent intent = new Intent(HomePageActivity.this, Yhdj_Activity_V2.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(HomePageActivity.this, "无相机权限", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;
            case R.id.hp_gwxx:// 公文信息
                if (!MyApplication.isConnection) {
                    Toast.makeText(HomePageActivity.this, "离线模式无法查看公文", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(HomePageActivity.this, GwList_Activity.class);
                startActivity(intent);
                break;
            case R.id.hp_pxks:// 培训考试
                if (!MyApplication.isConnection) {
                    Toast.makeText(HomePageActivity.this, "离线模式无法使用培训考试", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(HomePageActivity.this, PxksActivity.class);
                startActivity(intent);
                break;
            case R.id.hp_yjyl:// 应急演练

                break;
            case R.id.hp_zygl:// 作业管理

                break;
            case R.id.hp_sbxc:// 设备巡查
                if (MyApplication.isConnection) {
                    QueryBuilder<SbjcCommitInfo> qb = SqliteOperator.INSTANCE.getSbCommitDao(this).queryBuilder();
                    if (qb.buildCount().count() > 0) {
                        intent = new Intent(this, SbjcCommitActivity.class);
                        startActivity(intent);
                        return;
                    }
                }
                intent = new Intent(HomePageActivity.this, AqssjcActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.gwlist_zzclick) {
            Intent intent = new Intent(HomePageActivity.this,
                    DutyDetailActivity.class);
            startActivity(intent);

        } else if (id == mExitLogin.getId()) {
            UserInfoDao mUserInfoDao = SqliteOperator.INSTANCE.getUserInfoDao(HomePageActivity.this);
            mUserInfoDao.deleteAll();
            CompanyInfoDao companyInfoDao = SqliteOperator.INSTANCE.getCompanyInfoDao(HomePageActivity.this);
            companyInfoDao.deleteAll();
            Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }


    @Override
    public void onBackPressed() {
        if (myApp.getToActivity().equals("GwList")) {
            new SweetAlertDialog(HomePageActivity.this,
                    SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(
                            getResources().getString(
                                    R.string.dialog_default_title))
                    .setContentText("你确定退出吗？").setCancelText("点错了")
                    .setConfirmText("是的！").showCancelButton(true)
                    .setCancelClickListener(new OnSweetClickListener() {

                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.cancel();
                        }
                    }).setConfirmClickListener(new OnSweetClickListener() {

                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.cancel();
                    BitmapUtil.deleteFile(new File(BitmapUtil
                            .getPath(HomePageActivity.this)));
                    Intent intent = new Intent(HomePageActivity.this, NetworkStateService.class);
                    stopService(intent);
                    finish();
                }
            }).show();
        } else {
            Intent intent = new Intent(HomePageActivity.this, ComList_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
