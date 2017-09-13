package com.cqj.test.wbd2_gwpy.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.myinterface.ITakePhotoListener;
import com.cqj.test.wbd2_gwpy.myinterface.IYhdjImageCallBack;
import com.cqj.test.wbd2_gwpy.util.BitmapUtil;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/4.
 */
public class MyCamera extends LinearLayout implements IYhdjImageCallBack {

    private Context mContext;
    private SurfaceView mSurfaceView;
    private SurfaceCallback mSurfaceCallback;
    private Camera mCamera;
    private ArrayList<String> mImageData;
    private Camera.Parameters mParameters;
//    private ImageView mTopImageView;
    private ITakePhotoListener mTakePhotoListener;

    public void setTakePhotoListener(ITakePhotoListener pTakePhotoListener) {
        mTakePhotoListener = pTakePhotoListener;
    }

    public MyCamera(Context context) {
        super(context);
        init(context);
    }

    public MyCamera(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyCamera(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.my_camera, this, true);
//        mTopImageView = (ImageView) findViewById(R.id.take_photo_iv);
        mSurfaceView = (SurfaceView) findViewById(R.id.take_photo_surface);
        mSurfaceView.getHolder()
                .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceView.getHolder().setFixedSize(176, 144); //设置Surface分辨率
        mSurfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
        mImageData = new ArrayList<String>();
    }

    public void setCameraCallback() {
        if (mSurfaceView != null && mSurfaceView.getHolder() != null) {
            mSurfaceCallback = new SurfaceCallback();
            mSurfaceView.getHolder().addCallback(mSurfaceCallback);
        }
    }

    @Override
    public ArrayList<String> getImageDatas() {
        return mImageData;
    }

    @Override
    public void takePhoto() {
        if (mImageData.size() == 3) {
            Toast.makeText(mContext, R.string.take_photo_max_toast, Toast.LENGTH_SHORT).show();
            return;
        }
//        if (mSurfaceView.getVisibility() == View.GONE) {
//            mSurfaceView.setVisibility(View.VISIBLE);
//            mTopImageView.setVisibility(View.GONE);
//        } else {
            mCamera.takePicture(null, null, new MyPictureCallback());
//        }
    }

    @Override
    public void success() {
        mSurfaceView.setVisibility(View.VISIBLE);
//        mTopImageView.setVisibility(View.GONE);
        mImageData.clear();
    }

    @Override
    public void releaseCamera() {
        mSurfaceView.getHolder().removeCallback(mSurfaceCallback);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private final class MyPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                Bundle bundle = new Bundle();
                bundle.putByteArray("bytes", data); //将图片字节数据保存在bundle当中，实现数据交换
                zipBitmap(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void zipBitmap(final byte[] data) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> pSubscriber) {
                try {
                    Bitmap bitmap = BitmapUtil.imageZoom(data);
                    String path = BitmapUtil.saveToSDCard(bitmap);
//                    String newPath =BitmapUtil.reviewPicRotate(bitmap,path);
                    pSubscriber.onNext(path);
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

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(String pBitmap) {
                        getBitmap(pBitmap);
                    }
                });
    }


    private void getBitmap(String imageData) {
//        FinalBitmap.create(mContext).display(mTopImageView, imageData);
        mImageData.add(imageData);
//        mImagePath = imagePath;
//        mTopImageView.setVisibility(View.VISIBLE);
        mSurfaceView.setVisibility(View.GONE);
        mSurfaceView.setVisibility(View.VISIBLE);
        Toast.makeText(mContext, R.string.take_photo_success, Toast.LENGTH_SHORT).show();
        if(mTakePhotoListener!=null){
            mTakePhotoListener.getPhotoCount(mImageData.size());
        }
    }

    private final class SurfaceCallback implements SurfaceHolder.Callback {

        // 拍照状态变化时调用该方法
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            if (mCamera == null) {
                return;
            }
            mParameters = mCamera.getParameters(); // 获取各项参数
            mParameters.setPictureFormat(PixelFormat.RGB_565); // 设置图片格式
            mParameters.setPreviewSize(width, height); // 设置预览大小
            mParameters.setPreviewFrameRate(5);  //设置每秒显示4帧
            mParameters.setPictureSize(width, height); // 设置保存的图片尺寸
            mParameters.setJpegQuality(80); // 设置照片质量
        }

        // 开始拍照时调用该方法
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCamera = Camera.open(); // 打开摄像头
                mCamera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
                mCamera.setDisplayOrientation(getPreviewDegree((Activity) mContext));
                mCamera.startPreview(); // 开始预览
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 停止拍照时调用该方法
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mCamera != null) {
                mCamera.release(); // 释放照相机
                mCamera = null;
            }
        }
    }

    public static int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }
}
