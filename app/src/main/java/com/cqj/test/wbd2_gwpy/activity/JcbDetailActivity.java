package com.cqj.test.wbd2_gwpy.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;import com.cqj.test.wbd2_gwpy.R;


public class JcbDetailActivity extends Activity {

	private TextView jcbdetail_tv;
	private String detail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jcbdetail_view);
		getActionBar().setTitle("详情");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		jcbdetail_tv = (TextView) findViewById(R.id.jcbdetail_tv);
		detail =getIntent().getStringExtra("detail");
		jcbdetail_tv.setText(detail);
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
