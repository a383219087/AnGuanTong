package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.YhfcCommitInfo;
import com.cqj.test.wbd2_gwpy.dao.SqliteOperator;
import com.cqj.test.wbd2_gwpy.myinterface.ITakePhotoListener;
import com.cqj.test.wbd2_gwpy.util.StringUtil;
import com.cqj.test.wbd2_gwpy.util.UploadDataHelper;
import com.cqj.test.wbd2_gwpy.view.AudioRecordUtil;
import com.cqj.test.wbd2_gwpy.view.MyCamera;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class YhfcUploadActivity extends Activity implements View.OnClickListener,ITakePhotoListener {

    private final static int AUDIO = 25;
    public static final String YH_ID = "yhId";
    public static final String YH_NAME = "yhName";

    private ImageButton mCameraBtn, mCallBtn;
    private Button mCommitBtn, mAudioBtn, mReturnBtn, mDetailBtn;
    private MyCamera mMyCamera;
    private ProgressDialog mProgressDialog;
    private EditText mZgfyEdt, mFcqkEdt;

    private int mYhId;
    private String mYhName;
    private TextView mPhotoCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yhfc_upload);
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
        getActionBar().setTitle("隐患复查");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mYhId = getIntent().getIntExtra(YH_ID, -1);
        mYhName = getIntent().getStringExtra(YH_NAME);
        if (mYhId == -1) {
            toast("获取隐患ID失败，请重试");
            finish();
            return;
        }
        mMyCamera = (MyCamera) findViewById(R.id.yhfc_camera);
        mMyCamera.setTakePhotoListener(this);
        mCameraBtn = (ImageButton) findViewById(R.id.xcpz_btn);
        mAudioBtn = (Button) findViewById(R.id.xcly_btn);
        mCallBtn = (ImageButton) findViewById(R.id.bddh_btn);
        mCommitBtn = (Button) findViewById(R.id.commit_btn);
        mPhotoCount = (TextView) findViewById(R.id.photo_count);
        mReturnBtn = (Button) findViewById(R.id.fhlb_btn);
        mDetailBtn = (Button) findViewById(R.id.ckxq_btn);
        mFcqkEdt = (EditText) findViewById(R.id.fcqk_edt);
        mZgfyEdt = (EditText) findViewById(R.id.zgfy_edt);

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
            Intent intent = new Intent(YhfcUploadActivity.this, AudioRecordUtil.class);
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
            String fcqkStr = mFcqkEdt.getText().toString();
            if (TextUtils.isEmpty(fcqkStr)) {
                toast("请输入复查情况");
                return;
            }
            YhfcCommitInfo yhfcInfo = new YhfcCommitInfo();
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
            yhfcInfo.setDightedImgPath(imagePath + audioPath);
            yhfcInfo.setReviewRemark(fcqkStr);
            MyApplication mApplication = (MyApplication) getApplication();
            yhfcInfo.setReviewEmids(mApplication.getComInfo().getEmid());
            yhfcInfo.setReviewDate(UploadDataHelper.getNowDate());
            commit(yhfcInfo);
        } else if (id == mReturnBtn.getId()) {
            finish();
        } else if (id == mDetailBtn.getId()) {
            YhDetailActivity.start(YhfcUploadActivity.this, String.valueOf(mYhId));
        }
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

    private void commit(final YhfcCommitInfo info) {
        if (!MyApplication.isConnection) {
            toast("未联网，已保存！等待下次联网后再提示");
            SqliteOperator.INSTANCE.getYhfcCommitInfo(YhfcUploadActivity.this).insertOrReplace(info);
            return;

        }
        pendingDialog(R.string.yhdj_commit_hint);
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> pSubscriber) {
                try {
                    ArrayList<HashMap<String, Object>> data = UploadDataHelper.uploadYhfc(info);
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
        Toast.makeText(YhfcUploadActivity.this, toast, Toast.LENGTH_SHORT).show();
    }

    public void commitStatus(boolean isSuccess) {
        toast("提交" + (isSuccess ? "成功" : "失败,请重试"));
        if (isSuccess) {
            mMyCamera.success();
            mPhotoCount.setText("0");
            mAudioBtn.setBackgroundResource(R.drawable.yjbj_xcly_btn);
            mAudioBtn.setText("");
            mAudioBtn.setTag(null);
            mFcqkEdt.setText("");
        }
    }

    public void pendingDialog(int resId) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(YhfcUploadActivity.this);
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
                        toast("SD卡不存在");
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
        Intent intent = new Intent(context, YhfcUploadActivity.class);
        intent.putExtra(YH_ID, yhId);
        intent.putExtra(YH_NAME, yhName);
        context.startActivity(intent);
    }

    public static void start(Fragment context, int yhId, String yhName) {
        Intent intent = new Intent(context.getActivity(), YhfcUploadActivity.class);
        intent.putExtra(YH_ID, yhId);
        intent.putExtra(YH_NAME, yhName);
        context.startActivity(intent);
    }

    @Override
    public void getPhotoCount(int count) {
        mPhotoCount.setText(count+"");
    }
}
