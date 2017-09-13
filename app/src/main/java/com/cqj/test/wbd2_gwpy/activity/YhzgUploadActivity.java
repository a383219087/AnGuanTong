package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.YhfcCommitInfo;
import com.cqj.test.wbd2_gwpy.YhzgCommitInfo;
import com.cqj.test.wbd2_gwpy.dao.SqliteOperator;
import com.cqj.test.wbd2_gwpy.myinterface.ITakePhotoListener;
import com.cqj.test.wbd2_gwpy.util.StringUtil;
import com.cqj.test.wbd2_gwpy.util.UploadDataHelper;
import com.cqj.test.wbd2_gwpy.util.WebServiceUtil;
import com.cqj.test.wbd2_gwpy.view.AudioRecordUtil;
import com.cqj.test.wbd2_gwpy.view.MyCamera;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class YhzgUploadActivity extends Activity implements View.OnClickListener,ITakePhotoListener {

    private final static int AUDIO = 25;
    public static final String YH_ID = "yhId";
    public static final String YH_NAME = "yhName";

    private ImageButton mCameraBtn, mCallBtn;
    private Button mCommitBtn, mAudioBtn, mReturnBtn, mDetailBtn;
    private MyCamera mMyCamera;
    private EditText mZgfyEdt, mZgqkEdt, mYhpgEdt, mZgwcDateEdt;
    private ProgressDialog mProgressDialog;

    private int mYhId;
    private String mYhName;
    private TextView mPhotoCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yhzg_upload);
        initComplement();
        registListener();
    }

    private void registListener() {
        mCameraBtn.setOnClickListener(this);
        mAudioBtn.setOnClickListener(this);
        mCallBtn.setOnClickListener(this);
        mCommitBtn.setOnClickListener(this);
        mReturnBtn.setOnClickListener(this);
        mDetailBtn.setOnClickListener(this);
    }

    private void initComplement() {
        getActionBar().setTitle("隐患整改");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mYhId = getIntent().getIntExtra(YH_ID, -1);
        mYhName = getIntent().getStringExtra(YH_NAME);
        if (mYhId == -1) {
            toast("获取隐患ID失败，请重试");
            finish();
            return;
        }
        mMyCamera = (MyCamera) findViewById(R.id.yhgz_camera);
        mMyCamera.setTakePhotoListener(this);
        mCameraBtn = (ImageButton) findViewById(R.id.xcpz_btn);
        mAudioBtn = (Button) findViewById(R.id.xcly_btn);
        mCallBtn = (ImageButton) findViewById(R.id.bddh_btn);
        mCommitBtn = (Button) findViewById(R.id.commit_btn);
        mReturnBtn = (Button) findViewById(R.id.fhlb_btn);
        mDetailBtn = (Button) findViewById(R.id.ckxq_btn);
        mPhotoCount = (TextView) findViewById(R.id.photo_count);
        mZgqkEdt = (EditText) findViewById(R.id.zgqk_edt);
        mZgfyEdt = (EditText) findViewById(R.id.zgfy_edt);
        mYhpgEdt = (EditText) findViewById(R.id.zgpg_edt);
        mZgwcDateEdt = (EditText) findViewById(R.id.zgwcrq_edt);
        mZgwcDateEdt.setText(DateFormat.format("yyyy-MM-dd",new Date()));
        setDate(mZgwcDateEdt);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMyCamera.releaseCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyCamera.setCameraCallback();
    }

    private void setDate(final EditText edt) {
        edt.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
        edt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DateDialog(edt);
            }
        });
        edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean focus) {
                if (focus) {
                    DateDialog(edt);
                }
            }
        });
    }

    private void DateDialog(final EditText edt) {
        new DatePickerDialog(YhzgUploadActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                String smon = String.valueOf(monthOfYear + 1);
                String sday = String.valueOf(dayOfMonth);
                if (smon.length() == 1) {
                    smon = "0" + smon;
                }
                if (sday.length() == 1) {
                    sday = "0" + sday;
                }
                edt.setText(String.format("%s-%s-%s", String.valueOf(year), smon, sday));
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View pView) {
        int id = pView.getId();
        if (id == mCameraBtn.getId()) {
            mMyCamera.takePhoto();
        } else if (id == mAudioBtn.getId()) {
            Intent intent = new Intent(YhzgUploadActivity.this, AudioRecordUtil.class);
            File mCache = new File(Environment.getExternalStorageDirectory(),
                    "hwagtCache");
            if (!mCache.exists())
                mCache.mkdirs();
            String mAudioPath = mCache.getAbsolutePath() + File.separator
                    + DateFormat.format("yyyyMMddHHmmss", new Date())
                    + ".mp3";
            intent.putExtra("mAudioPath", mAudioPath);

            startActivityForResult(intent, AUDIO);
        } else if (id == mCallBtn.getId()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            startActivity(intent);
        } else if (id == mCommitBtn.getId()) {
            String zgqkStr = mZgqkEdt.getText().toString();
            if (TextUtils.isEmpty(zgqkStr)) {
                toast("请输入整改情况");
                return;
            }
            String zgpgStr = mYhpgEdt.getText().toString();
            if (TextUtils.isEmpty(zgpgStr)) {
                toast("请输入整改评估");
                return;
            }
            String zgfyStr = mZgfyEdt.getText().toString();
            if (TextUtils.isEmpty(zgfyStr)) {
                toast("请输入整改费用");
                return;
            }
            String wcsjStr =mZgwcDateEdt.getText().toString();
            if (TextUtils.isEmpty(wcsjStr)) {
                toast("请输入完成时间");
                return;
            }
            YhzgCommitInfo yhfcInfo = new YhzgCommitInfo();
            yhfcInfo.setTroubleid(mYhId);
            yhfcInfo.setTroubleName(mYhName);
            ArrayList<String> imagePaths = mMyCamera.getImageDatas();
            StringBuilder stringBuilder = new StringBuilder();
            for (String imagePath : imagePaths) {
                stringBuilder.append(imagePath);
                stringBuilder.append(",");
            }
            String imagePath ="";
            if(stringBuilder.length()>0) {
                imagePath = stringBuilder.substring(0, stringBuilder.length() - 1);
            }
            String audioPath = mAudioBtn.getTag() == null ? "" : "," + StringUtil.noNull(mAudioBtn.getTag());
            yhfcInfo.setImgpath(imagePath + audioPath);
            yhfcInfo.setResults(zgqkStr);
            yhfcInfo.setEvalstr(zgpgStr);
            yhfcInfo.setFactCost(zgfyStr);
            yhfcInfo.setFinishedDate(String.format("%sT00:00:00.000", wcsjStr));
            commit(yhfcInfo);
        } else if (id == mReturnBtn.getId()) {
            finish();
        } else if (id == mDetailBtn.getId()) {
            YhDetailActivity.start(YhzgUploadActivity.this, String.valueOf(mYhId));
        }
    }

    private void commit(final YhzgCommitInfo info) {
        if (!MyApplication.isConnection) {
            toast("未联网，已保存！等待下次联网后再提示");
            SqliteOperator.INSTANCE.getYhzgCommitInfo(YhzgUploadActivity.this).insertOrReplace(info);
            return;

        }
        pendingDialog(R.string.yhdj_commit_hint);
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> pSubscriber) {
                try {
                    ArrayList<HashMap<String, Object>> data = UploadDataHelper.uploadYhzg(info);
                    if (data == null || data.isEmpty()) {
                        pSubscriber.onNext("");
                    } else {
                        pSubscriber.onNext(StringUtil.noNull(data.get(0).get("retstr")));
                    }
                } catch (Exception pE) {
                    pE.printStackTrace();
                    pSubscriber.onError(pE);
                }
                pSubscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        cancelDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        cancelDialog();
                        commitStatus(false);
                    }

                    @Override
                    public void onNext(String pS) {
                        commitStatus(TextUtils.isEmpty(pS));
                        if (!TextUtils.isEmpty(pS)) {
                            toast(pS);
                        }
                    }
                });
    }

    public void toast(String toast) {
        Toast.makeText(YhzgUploadActivity.this, toast, Toast.LENGTH_SHORT).show();
    }

    public void commitStatus(boolean isSuccess) {
        toast("提交" + (isSuccess ? "成功" : "失败,请重试"));
        if (isSuccess) {
            mMyCamera.success();
            mPhotoCount.setText("0");
            mAudioBtn.setBackgroundResource(R.drawable.yjbj_xcly_btn);
            mAudioBtn.setText("");
            mAudioBtn.setTag(null);
            mZgfyEdt.setText("");
            mZgwcDateEdt.setText("");
            mYhpgEdt.setText("");
            mZgqkEdt.setText("");
        }
    }

    public void pendingDialog(int resId) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(YhzgUploadActivity.this);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.setMessage(getString(resId));
        mProgressDialog.show();
    }


    public void cancelDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mMyCamera.setCameraCallback();
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == AUDIO) // 录音
                {
                    // 显示多媒体View
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        // 检测 sdcard 是否可用
                        Toast.makeText(YhzgUploadActivity.this, "SD卡不存在", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String strResult = data.getStringExtra("result");
                    if (strResult != null) {
                        mAudioBtn.setBackgroundResource(R.drawable.blue_button_background);
                        mAudioBtn.setText("重录");
                        mAudioBtn.setTag(strResult);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start(Context context, int yhId, String yhName) {
        Intent intent = new Intent(context, YhzgUploadActivity.class);
        intent.putExtra(YH_ID, yhId);
        intent.putExtra(YH_NAME, yhName);
        context.startActivity(intent);
    }

    public static void start(Fragment context, int yhId, String yhName) {
        Intent intent = new Intent(context.getActivity(), YhzgUploadActivity.class);
        intent.putExtra(YH_ID, yhId);
        intent.putExtra(YH_NAME, yhName);
        context.startActivity(intent);
    }

    @Override
    public void getPhotoCount(int count) {
        mPhotoCount.setText(count+"");
    }
}
