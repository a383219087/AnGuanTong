package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.view.ZoomImageView;

import net.tsz.afinal.FinalBitmap;

public class ShowImageActivity extends Activity implements ZoomImageView.ZoomClick {

	private ZoomImageView ziv;
	private WindowManager mWindowManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.showimage_view);
		mWindowManager =getWindowManager();
		DisplayMetrics metrics =new DisplayMetrics();
		mWindowManager.getDefaultDisplay().getMetrics(metrics);
		ziv =(ZoomImageView) findViewById(R.id.ziv);
		ziv.setZoomClick(this);
		String url = getIntent().getStringExtra("data");
		FinalBitmap.create(this).display(ziv,url);
		ziv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void click() {
		finish();
	}
}
