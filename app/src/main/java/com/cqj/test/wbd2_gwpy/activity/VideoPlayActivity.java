package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.util.SharedPreferenceUtil;
import com.cqj.test.wbd2_gwpy.view.SweetAlertDialog;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

public class VideoPlayActivity extends Activity {

    public static final String RESULT_PROGRESS = "result_progress";

    private static final String EXTRA_KEY_URL = "key_url";
    private static final String EXTRA_KEY_PROGRESS = "key_progress";
    public static final String EXTRA_KEY_TOTAL = "total";
    private String mUrl;
    private int mProgress;
    private int mTotal;
    private TXCloudVideoView mVideoView;
    private TXLivePlayer mLivePlayer;
    private ImageView mIvVideoPlay;
    private ImageView mIvVideoToBig;
    private ProgressBar mSeekBar;
    private LinearLayout mLlSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        init();
    }

    private void init() {
        mUrl = getIntent().getStringExtra(EXTRA_KEY_URL);
        mProgress = getIntent().getIntExtra(EXTRA_KEY_PROGRESS, 0);
        mTotal = getIntent().getIntExtra(EXTRA_KEY_TOTAL,0);
        mVideoView = (TXCloudVideoView) findViewById(R.id.big_videoview);
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
        mLlSeekbar = (LinearLayout) findViewById(R.id.seekbar_ll);
        initPlayEvent();
        mLivePlayer.startPlay(mUrl, TXLivePlayer.PLAY_TYPE_VOD_FLV);
        mLivePlayer.seek(mProgress);
    }

    private void initPlayEvent() {
        mIvVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLivePlayer!=null) {
                    if (mLivePlayer.isPlaying()) {
                        mLivePlayer.pause();
                        mIvVideoPlay.setImageResource(R.drawable.social_view_video_start_normal);
                    } else {
                        play();
                    }
                }else{
                    Toast.makeText(VideoPlayActivity.this, "播放器未准备就绪", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mIvVideoToBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(RESULT_PROGRESS,mProgress);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        mVideoView.setClickable(true);
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLlSeekbar.getVisibility()==View.VISIBLE){
                    mLlSeekbar.setVisibility(View.GONE);
                }else{
                    mLlSeekbar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void play() {
        if (mProgress == 0 || mProgress >= mTotal) {
            mLivePlayer.startPlay(mUrl, TXLivePlayer.PLAY_TYPE_VOD_FLV);
            mProgress = SharedPreferenceUtil.getInt(VideoPlayActivity.this, mUrl);
            mLivePlayer.seek(mProgress);
        } else {
            mLivePlayer.resume();
        }
    }

    private ITXLivePlayListener mPlayListener = new ITXLivePlayListener() {
        @Override
        public void onPlayEvent(int event, Bundle param) {
            if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                mIvVideoPlay.setImageResource(R.drawable.social_view_video_stop_normal);
            }else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
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
            }else if(event == TXLiveConstants.PLAY_EVT_PLAY_END){
                SharedPreferenceUtil.putInt(VideoPlayActivity.this, mUrl, 0);
                mProgress=0;
            }
        }

        @Override
        public void onNetStatus(Bundle bundle) {

        }
    };

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
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra(RESULT_PROGRESS,mProgress);
        setResult(RESULT_OK,intent);
    }

    private void showPreventAwaitDialog() {
        SweetAlertDialog alertDialog = new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
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

    public static void startForResult(Activity activity, String url, int progress,int total, int requestCode) {
        Intent starter = new Intent(activity, VideoPlayActivity.class);
        starter.putExtra(EXTRA_KEY_URL, url);
        starter.putExtra(EXTRA_KEY_PROGRESS, progress);
        starter.putExtra(EXTRA_KEY_TOTAL,total);
        activity.startActivityForResult(starter, requestCode);
    }
}
