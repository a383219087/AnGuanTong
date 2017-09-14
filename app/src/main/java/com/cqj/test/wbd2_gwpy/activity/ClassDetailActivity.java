package com.cqj.test.wbd2_gwpy.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.util.SharedPreferenceUtil;
import com.cqj.test.wbd2_gwpy.util.StringUtil;
import com.cqj.test.wbd2_gwpy.util.WebServiceUtil;
import com.cqj.test.wbd2_gwpy.view.SweetAlertDialog;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ClassDetailActivity extends Activity {

    public static final int REQUEST_CODE = 12;
    private TextView startDate, title, yqks, contentDetail, contentTitle;
    private TXCloudVideoView mVideoView;
    private EditText xxjl;
    private ScrollView scrollView;
    private TextView spckBtn, wbckBtn, commitBtn;
    private TXLivePlayer mLivePlayer;
    private LinearLayout mRlVideo;
    private ProgressBar mSeekBar;
    private ImageView mIvVideoToBig;
    private ImageView mIvVideoPlay;

    private int currID;
    protected ArrayList<HashMap<String, Object>> mData;
    private String mUrl;
    private Chronometer mChron;
    private int mProgress;

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message arg0) {
            // TODO Auto-generated method stub
            commitBtn.setEnabled(true);
            switch (arg0.what) {
                case 1:
                    title.setText(StringUtil.noNull(mData.get(0).get("currTitle")));
                    yqks.setText(String.format("要求课时：%s", StringUtil.noNull(mData.get(0).get("currhour"))));
//                    mUrl = StringUtil.noNull(mData.get(0).get("vlink"));
                    mUrl = "http://www.chinasafety.org/Common/vcastr22/安全生产管理培训案例视频库之叉车事故.flv";
                    if (TextUtils.isEmpty(mUrl)) {
                        spckBtn.setVisibility(View.GONE);
                        changeBtnState(false);
                    } else {
                        spckBtn.setVisibility(View.VISIBLE);
                        changeBtnState(true);
                    }
                    int obliid = -1;
                    try {
                        obliid = Integer.parseInt(StringUtil.noNull(mData.get(0)
                                .get("obliid")));
                    } catch (Exception ignored) {
                    }
                    if (obliid != -1) {
                        new myGetDetailTask(obliid, contentDetail).execute("");
                    }
                    mChron.start();
                    break;
                case 2:
                    Toast.makeText(ClassDetailActivity.this, "连接服务器超时，请稍后再试...",
                            Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(ClassDetailActivity.this, "提交成功。",
                            Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classdetail_view);
        getActionBar().setTitle("课程学习");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        String currIDStr = getIntent().getStringExtra("currID");
        try {
            currID = Integer.parseInt(currIDStr);
        } catch (Exception e) {
            Toast.makeText(ClassDetailActivity.this, "不是正确的课程。",
                    Toast.LENGTH_LONG).show();
            return;
        }
        initComplement();
        registListener();
        getData();
    }

    @SuppressLint("SimpleDateFormat")
    private void registListener() {
        spckBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                changeBtnState(true);
            }
        });
        wbckBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                changeBtnState(false);
            }
        });
        commitBtn.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View arg0) {
                if (TextUtils.isEmpty(xxjl.getText().toString())) {
                    Toast.makeText(ClassDetailActivity.this, "请输入学习记录。",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            float time = 0f;
                            String timeStr = mChron.getText().toString();
                            if (timeStr.length() > 5) {
                                Date mDate = new SimpleDateFormat("H:mm:ss")
                                        .parse(timeStr);
                                if (mDate.getHours() > 0) {
                                    time = mDate.getHours() * 60
                                            + mDate.getMinutes();
                                } else {
                                    time = mDate.getMinutes();
                                }
                            } else {
                                Date mDate = new SimpleDateFormat("mm:ss")
                                        .parse(timeStr);
                                int minutes = mDate.getMinutes();
                                time = minutes;
                            }
                            String[] keys = {"StuID", "durMinute", "CRemark"};
                            Object[] values = {currID, time,
                                    xxjl.getText().toString()};
                            WebServiceUtil.getWebServiceMsg(keys, values,
                                    "setCourseReCord",
                                    WebServiceUtil.PART_DUTY_URL);
                            handler.sendEmptyMessage(3);
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(2);
                        }
                    }
                }).start();

            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void initComplement() {
        mChron = (Chronometer) findViewById(R.id.classdetail_chornometer);
        startDate = (TextView) findViewById(R.id.classdetail_startdate);
        Calendar cal = Calendar.getInstance();
        String startDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(cal.getTime());
        startDate.setText(startDateStr);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        contentDetail = (TextView) findViewById(R.id.classdetail_textview);
        contentTitle = (TextView) findViewById(R.id.content_title);
        title = (TextView) findViewById(R.id.classdetail_title);
        yqks = (TextView) findViewById(R.id.classdetail_teacher);
        spckBtn = (TextView) findViewById(R.id.classdetail_spbf);
        wbckBtn = (TextView) findViewById(R.id.classdetail_wbck);
        commitBtn = (TextView) findViewById(R.id.commit_btn);
        xxjl = (EditText) findViewById(R.id.classdetail_xxjl);
        mRlVideo = (LinearLayout) findViewById(R.id.sp_rl);
        initTXVideoView();
    }

    private void initTXVideoView() {
        mVideoView = (TXCloudVideoView) findViewById(R.id.classdetail_webview);
        //创建player对象
        mLivePlayer = new TXLivePlayer(this);
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        TXLivePlayConfig mPlayConfig = new TXLivePlayConfig();
        //卡顿&延迟中的自动模式
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(5);
        mLivePlayer.setConfig(mPlayConfig);
        mLivePlayer.setPlayListener(mPlayListener);
        //关键player对象与界面view
        mLivePlayer.setPlayerView(mVideoView);
        mIvVideoPlay = (ImageView) findViewById(R.id.video_play);
        mIvVideoToBig = (ImageView) findViewById(R.id.video_tobig);
        mSeekBar = (ProgressBar) findViewById(R.id.progressbar);
        initPlayEvent();
    }

    private void initPlayEvent() {
        mIvVideoPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLivePlayer != null) {
                    if (mLivePlayer.isPlaying()) {
                        mLivePlayer.pause();
                        mIvVideoPlay.setImageResource(R.drawable.social_view_video_start_normal);
                    } else {
                        if (mProgress == 0) {
                            mLivePlayer.startPlay(mUrl, TXLivePlayer.PLAY_TYPE_VOD_FLV);
                            mProgress = SharedPreferenceUtil.getInt(ClassDetailActivity.this, mUrl);
                            mLivePlayer.seek(mProgress);
                        } else {
                            mLivePlayer.resume();
                        }
                    }
                } else {
                    Toast.makeText(ClassDetailActivity.this, "播放器未准备就绪", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mIvVideoToBig.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoPlayActivity.startForResult(ClassDetailActivity.this, mUrl, mProgress, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLivePlayer.stopPlay(true); // true代表清除最后一帧画面
        mVideoView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLivePlayer != null) {
            mLivePlayer.pause();
            mIvVideoPlay.setImageResource(R.drawable.social_view_video_start_normal);
            SharedPreferenceUtil.putInt(this, mUrl, mProgress);
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

    public void toEwm(View view) {
        Intent intent = new Intent(ClassDetailActivity.this,
                CameraTestActivity.class);
        startActivityForResult(intent, 110);
    }

    private void getData() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    String[] keys = {"Curid", "TitleKeyWord", "ComID", "CType"};
                    Object[] values = {currID, "", 0, ""};// currID
                    mData = WebServiceUtil.getWebServiceMsg(keys, values,
                            "getCourse", new String[]{"currTitle",
                                    "currhour", "currType", "currid", "vlink",
                                    "obliid"}, WebServiceUtil.PART_DUTY_URL);
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                }
            }
        }).start();

    }

    private void changeBtnState(boolean isSpbf) {
        if (isSpbf) {
            spckBtn.setBackgroundColor(getResources().getColor(R.color.gray));
            wbckBtn.setBackgroundColor(getResources().getColor(
                    R.color.background));
            mRlVideo.setVisibility(View.VISIBLE);
            contentDetail.setVisibility(View.GONE);
            contentTitle.setText("视频播放");
        } else {
            spckBtn.setBackgroundColor(getResources().getColor(
                    R.color.background));
            wbckBtn.setBackgroundColor(getResources().getColor(R.color.gray));
            mRlVideo.setVisibility(View.GONE);
            contentDetail.setVisibility(View.VISIBLE);
            contentTitle.setText("文本内容");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 110:
                    String ewmStr = data.getStringExtra("result");
                    Toast.makeText(getApplicationContext(), ewmStr,
                            Toast.LENGTH_LONG).show();
                    break;
                case REQUEST_CODE:
                    mProgress = data.getIntExtra(VideoPlayActivity.RESULT_PROGRESS, 0);
                    mLivePlayer.seek(mProgress);
                    mLivePlayer.resume();
                    break;

                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class myGetDetailTask extends
            AsyncTask<String, String, ArrayList<HashMap<String, Object>>> {

        private int obliid;
        private TextView detail;

        public myGetDetailTask(int obliid, TextView detail) {
            this.obliid = obliid;
            this.detail = detail;
        }

        @Override
        protected ArrayList<HashMap<String, Object>> doInBackground(
                String... params) {
            ArrayList<HashMap<String, Object>> getDatas = new ArrayList<HashMap<String, Object>>();
            String keys2[] = {"orgIDstr", "cDocID", "titleKeyWord",
                    "detailKeyWord", "carryPartID", "carryDutyID", "docType",
                    "parentCDocID", "cDocDetailID", "retstr"};
            Object values2[] = {null, obliid, "", "", 0, 0, "", 0, 0, ""};
            try {
                getDatas = WebServiceUtil.getWebServiceMsg(keys2, values2,
                        "getCapacityDocumentDetail", new String[]{
                                "carryPartName", "dLevel", "cDocDetailID",
                                "dSequence", "cDocTitle", "inTable", "inImage",
                                "createcom", "cDocDetail", "info_additional",
                                "info_additiondoc"});
            } catch (Exception e) {
                e.printStackTrace();
            }
            return getDatas;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
            super.onPostExecute(result);
            if (result.size() != 0) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < result.size(); i++) {
                    String title = StringUtil.noNull(
                            result.get(i).get("cDocTitle")).trim();
                    sb.append("·");
                    sb.append(title);
                    sb.append(StringUtil.ENTER);
                    String detail = StringUtil.noNull(
                            result.get(i).get("cDocDetail")).trim();
                    String sQe = StringUtil.noNull(result.get(i).get(
                            "dSequence"));
                    // int code = Integer.parseInt(sQe);
                    detail = detail.replace("anyType{}", "");
                    if (StringUtil.isNotEmpty(detail)) {
                        String dLevel = StringUtil.noNull(result.get(i).get(
                                "dLevel"));
                        // int level = 2;
                        // if (StringUtil.isNotEmpty(dLevel)) {
                        // level = Integer.parseInt(dLevel) + 1;
                        // sQe = StringUtil.parseNumberByLevel(level, code);
                        // }
                        sb.append(StringUtil.SPACE);
                        if (!dLevel.equals("0")) {
                            sb.append(dLevel + "." + sQe + ".");
                        } else {
                            sb.append(sQe + ".");
                        }
                        sb.append(detail);
                        sb.append(StringUtil.ENTER);
                    }
                }
                detail.setText(sb.toString());
            }
        }
    }

    private ITXLivePlayListener mPlayListener = new ITXLivePlayListener() {
        @Override
        public void onPlayEvent(int event, Bundle param) {
            if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                mIvVideoPlay.setImageResource(R.drawable.social_view_video_stop_normal);
            } else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
                //进度（秒数）
                mProgress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
                if (mProgress != 0 && mProgress % 60 == 0) {
                    mLivePlayer.pause();
                    showPreventAwaitDialog();
                }
                // UI进度进行相应的调整
                int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION); //时间（秒数）
                mSeekBar.setProgress(mProgress);
                mSeekBar.setMax(duration);
//				mTextStart.setText(String.format("%2d:%2d", mProgress /60, mProgress %60));
//				mTextDuration.setText(String.format("%2d:%2d",duration/60,duration%60));
            }
        }

        @Override
        public void onNetStatus(Bundle bundle) {

        }
    };

    private void showPreventAwaitDialog() {
        SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setContentText("验证是否挂机，请点击确定");
        alertDialog.setTitleText("提示");
        alertDialog.setConfirmText("确定");
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                mLivePlayer.resume();
            }
        });
        alertDialog.show();
    }

}
