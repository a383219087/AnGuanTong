package com.cqj.test.wbd2_gwpy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.util.StringUtil;
import com.cqj.test.wbd2_gwpy.util.TableParse;
import com.cqj.test.wbd2_gwpy.util.WebServiceUtil;
import com.cqj.test.wbd2_gwpy.view.SweetAlertDialog;
import com.cqj.test.wbd2_gwpy.view.SweetAlertDialog.OnSweetClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class Gwsh_Activity extends Activity {

	private TextView title, gwfbsj, cyrs, content, date, comname, remarkTv;
	private EditText pyEdt;
	private Button commit;
	private int gwid;// 公文ID
	private int emid;// 雇员ID
	private int orgid;// 公司ID
	private String orgidStr;
	private int DocId;
	private MyApplication myApp;
	private CheckBox qxfbCb;
	private ArrayList<HashMap<String, Object>> gwData;// 公文数据集
	private Spinner xygsh;//
	private ArrayList<HashMap<String, Object>> gyData;// 雇员数据集
	private Thread myGetDataThread;
	private String comnameStr;
	private HashMap<String, String> tableOrImageData;
	private LinearLayout gwpy_fjwd, gwpy_fjbg;// 附件文档 图片等

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				changeButtonStyle(false, "");
				new SweetAlertDialog(Gwsh_Activity.this,
						SweetAlertDialog.SUCCESS_TYPE).setContentText("提交成功！")
						.setConfirmText("确定")
						.setConfirmClickListener(new OnSweetClickListener() {

							@Override
							public void onClick(
									SweetAlertDialog sweetAlertDialog) {
								// TODO Auto-generated method stub
								sweetAlertDialog.cancel();
								myApp.isRefresh = true;
								finish();
							}
						}).show();
				break;
			case 2:
				changeButtonStyle(false, "");
				Toast.makeText(Gwsh_Activity.this, "提交失败。", Toast.LENGTH_LONG)
						.show();
				break;
			case 3:
				if (gwData.size() > 0) {
					title.setText(gwData.get(0).get("cDocTitle").toString());
					StringBuffer sb = new StringBuffer();
					sb.append(StringUtil.SPACE);// 缩进符
					String overView = gwData.get(0).get("overview").toString();
					overView = overView.replace("anyType#$", "");
					sb.append(overView);
					content.setText(sb.toString());
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String detail = setDetail("-1");
							Message msg = mHandler.obtainMessage();
							msg.what = 5;
							msg.obj = detail;
							mHandler.sendMessage(msg);
						}
					}).start();
					String detail2 = StringUtil.noNull(gwData.get(0).get(
							"cRemark"));
					detail2 = detail2.replace("anyType#$", "");

					if (StringUtil.isNotEmpty(detail2)) {
						remarkTv.setText(StringUtil.ENTER + StringUtil.SPACE
								+ detail2);
					}
					String dateStr = gwData.get(0).get("cdate").toString();
					if (dateStr != null) {
						String[] dateStrs = dateStr.split("T");
						if (dateStrs.length > 0) {
							date.setText(dateStrs[0]);
							gwfbsj.setText(dateStrs[0]);
						}
					}
				} else {
					Toast.makeText(Gwsh_Activity.this, "获取无数据，请重试。",
							Toast.LENGTH_LONG).show();
				}

				SimpleAdapter ada = new SimpleAdapter(Gwsh_Activity.this,
						gyData, android.R.layout.simple_spinner_item,
						new String[] { "emName" },
						new int[] { android.R.id.text1 });
				xygsh.setAdapter(ada);
				ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				break;
			case 4:
				Toast.makeText(Gwsh_Activity.this, "连接服务器失败，请重试。",
						Toast.LENGTH_LONG).show();
				break;
			case 5:
				content.append(String.valueOf(msg.obj));
				comname.setText(comnameStr);
				if (StringUtil.isNotEmpty(tableOrImageData.get("image"))) {
					String[] images = tableOrImageData.get("image").split(";");
					for (int i = 0; i < images.length; i++) {
						String url = images[i].replace("../", "");
						ImageView iv = new ImageView(Gwsh_Activity.this);
						iv.setClickable(true);
						gwpy_fjwd.addView(iv);
						String realUrl = WebServiceUtil.IMAGE_URLPATH + url;
						Glide.with(Gwsh_Activity.this)
								.load(realUrl)
								.placeholder(R.drawable.picture_load)
								.error(R.drawable.picture_load)
								.into(iv);
						iv.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String url = (String) v.getTag();
								if (StringUtil.isNotEmpty(url)) {
									File file = new File(url);
									Intent it = new Intent(Intent.ACTION_VIEW);
									it.setDataAndType(Uri.fromFile(file),
											"image/*");
									startActivity(it);
								}
							}
						});
					}
				}
				if (StringUtil.isNotEmpty(tableOrImageData.get("table"))) {
					try {
						ArrayList<ArrayList<String>> tableData = TableParse
								.getTableData(tableOrImageData.get("table"));
						TableLayout t = new TableLayout(Gwsh_Activity.this);
						t.setBackgroundColor(getResources().getColor(
								R.color.balck));
						for (int x = 0; x < tableData.size(); x++) {
							TableRow row = new TableRow(Gwsh_Activity.this);
							for (int i = 0; i < tableData.get(i).size(); i++) {
								TextView t1 = new TextView(Gwsh_Activity.this);
								LayoutParams params = new LayoutParams();
								params.setMargins(1, 1, 1, 1);
								t1.setLayoutParams(params);
								t1.setBackgroundColor(getResources().getColor(
										R.color.white));
								t1.setText(tableData.get(x).get(i));
								t1.setTextColor(Color.BLACK);
								row.addView(t1);
							}
							t.addView(row);
						}
						gwpy_fjbg.addView(t);
						// GridLinearLayout gl = new GridLinearLayout(
						// Gwpy_Activity.this);
						// gl.setColumns(tableData.get(0).size());
						// gl.setRows(tableData.size());
						// gl.setHorizontalSpace(1);
						// gl.setVerticalSpace(1);
						// ArrayList<String> data = new ArrayList<String>();
						// for (int i = 0; i < tableData.size(); i++) {
						// for (int j = 0; j < tableData.get(i).size(); j++) {
						// data.add(tableData.get(i).get(j));
						// }
						// }
						// ArrayAdapter<String> ada = new ArrayAdapter<String>(
						// Gwpy_Activity.this,
						// android.R.layout.simple_list_item_1, data);
						// gl.setAdapter(ada);
						// gl.bindLinearLayout();
						// gwpy_fjbg.addView(gl);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
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
		setContentView(R.layout.gwsh_view);
		initComplement();
		getGwData();
	}

	/**
	 * 获取数据
	 */
	private void getGwData() {
		myGetDataThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					// getCapacityDocument
					String keys[] = { "orgIDstr", "cDocID", "keyWord",
							"cStart", "cEnd", "onlyInTitle", "cState",
							"userId", "cType", "docTempId", "retstr" };
					// System.out.println("orgidstr" +
					// orgItem.get("Emid").toString());
					Object values[] = { orgidStr, DocId, "",
							"1900-01-01T00:00:00.850",
							"2049-12-31T00:00:00.850", true, true, 0, "", 0, "" };
					ArrayList<HashMap<String, Object>> data = WebServiceUtil
							.getWebServiceMsg(keys, values,
									"getCapacityDocument");
					gwData.addAll(data);

					// getAllEmployeeFromOrgID
					String keys3[] = { "orgid" };
					Object values3[] = { orgid };
					ArrayList<HashMap<String, Object>> data3 = WebServiceUtil
							.getWebServiceMsg(keys3, values3,
									"getAllEmployeeFromOrgID", new String[] {
											"emName", "Emid" });
					gyData.addAll(data3);
					mHandler.sendEmptyMessage(3);
				} catch (InterruptedException ex) {

				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(4);
				}
			}
		});
		myGetDataThread.start();
	}

	private void initComplement() {
		if (StringUtil.isEmpty(getIntent().getStringExtra("nac"))) {
			((LinearLayout) findViewById(R.id.gone_ln))
					.setVisibility(View.GONE);
			getActionBar().setTitle("公文信息查看");
		} else {
			getActionBar().setTitle("公文信息审核");
		}
		gwData = new ArrayList<HashMap<String, Object>>();
		gyData = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("emName", "无");
		map.put("Emid", "0");
		myApp = (MyApplication) getApplication();
		gyData.add(map);
		String gwidStr = getIntent().getStringExtra("gwid");// 公文ID
		String emidStr = myApp.getComInfo().getEmid();
		String orgid_ = myApp.getComInfo().getOrg_id();
		try {
			DocId = Integer.parseInt(getIntent().getStringExtra("DocId"));
		} catch (Exception e) {
			Toast.makeText(Gwsh_Activity.this, "获取雇员ID错误，请重试。",
					Toast.LENGTH_LONG).show();
			return;
		}
		orgidStr = myApp.getComInfo().getOrg_idstr();
		orgid = Integer.parseInt(orgid_);
		if (StringUtil.isNotEmpty(emidStr)) {
			emid = Integer.parseInt(emidStr);
		} else {
			Toast.makeText(Gwsh_Activity.this, "获取雇员ID错误，请重试。",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (StringUtil.isNotEmpty(gwidStr)) {
			gwid = Integer.parseInt(gwidStr);
		} else {
			Toast.makeText(Gwsh_Activity.this, "获取公文ID错误，请重试。",
					Toast.LENGTH_LONG).show();
			return;
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
		xygsh = (Spinner) findViewById(R.id.sh_xygsh);
		qxfbCb = (CheckBox) findViewById(R.id.gwsh_qxfb);
		title = (TextView) findViewById(R.id.gwpy_title);
		gwfbsj = (TextView) findViewById(R.id.gwpy_fbsj);
		cyrs = (TextView) findViewById(R.id.gwpy_cyrs);
		content = (TextView) findViewById(R.id.gwpy_content);
		date = (TextView) findViewById(R.id.gwpy_date);
		comname = (TextView) findViewById(R.id.gwpy_comname);
		pyEdt = (EditText) findViewById(R.id.gwpy_pyedt);
		commit = (Button) findViewById(R.id.gwpy_commit);
		remarkTv = (TextView) findViewById(R.id.gwpy_remark);
		gwpy_fjwd = (LinearLayout) findViewById(R.id.gwpy_fjwd);
		gwpy_fjbg = (LinearLayout) findViewById(R.id.gwpy_fjbg);
		tableOrImageData = new HashMap<>();
	}

	/**
	 * 公文提交
	 * 
	 * @param view
	 */
	public void commit_gw(View view) {
		// if (StringUtil.isEmpty(pyEdt.getText().toString())) {
		// Toast.makeText(Gwsh_Activity.this, "请填写批阅意见。", Toast.LENGTH_LONG)
		// .show();
		// return;
		// }
		changeButtonStyle(true, "提交中...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					// SetInfoAudit
					String keys[] = { "AuditEmid", "Infoid", "CRemark",
							"AddfilePaths", "IPStr", "IsAudited",
							"NextAuditEmid" };
					Object values[] = {
							emid,
							gwid,
							pyEdt.getText().toString(),
							"",
							getPhoneNumber(),
							!qxfbCb.isChecked(),
							gyData.get(xygsh.getSelectedItemPosition()).get(
									"Emid") };
					WebServiceUtil.putWebServiceMsg(keys, values,
							"SetInfoAudit");
					mHandler.sendEmptyMessage(1);
				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(2);
				}
			}
		}).start();

	}

	/**
	 * 切换按钮状态
	 * 
	 * @param isPress
	 * @param alertMsg
	 */
	private void changeButtonStyle(boolean isPress, String alertMsg) {
		if (isPress) {
			commit.setText(alertMsg);
			commit.setBackgroundColor(Color.GRAY);
			commit.setEnabled(false);
		} else {
			commit.setEnabled(true);
			commit.setText("提　　交");
			commit.setBackgroundColor(getResources().getColor(R.color.login_bg));
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

	private String getPhoneNumber() {
		StringBuffer pm = new StringBuffer();
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		pm.append(telephonyManager.getDeviceId());
		return pm.toString();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (myGetDataThread.isAlive()) {
			myGetDataThread.interrupt();
		}
	}

	private String setDetail(final String cDocDetailID) {
		if (StringUtil.isEmpty(cDocDetailID)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		String keys2[] = { "orgIDstr", "cDocID", "titleKeyWord",
				"detailKeyWord", "carryPartID", "carryDutyID", "docType",
				"parentCDocID", "cDocDetailID", "retstr" };
		Object values2[] = { orgidStr, DocId, "", "", 0, 0, "",
				Integer.parseInt(cDocDetailID), 0, "" };
		ArrayList<HashMap<String, Object>> data2 = new ArrayList<HashMap<String, Object>>();
		try {
			data2 = WebServiceUtil.getWebServiceMsg(keys2, values2,
					"getCapacityDocumentDetail", new String[] {
							"carryPartName", "dLevel", "cDocDetailID",
							"dSequence", "cDocDetail", "inTable", "inImage",
							"createcom", "cDocDetail", "info_additional",
							"info_additiondoc" });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (data2.size() > 0) {
			// 表格和图片数据
			String inImage = StringUtil.noNull(data2.get(0).get("inImage"))
					.trim();
			inImage = inImage.replace("anyType{}", "");
			if (StringUtil.isNotEmpty(inImage)) {
				tableOrImageData.put("image", inImage);
			}
			String inTable = StringUtil.noNull(data2.get(0).get("inTable"))
					.trim();
			inTable = inTable.replace("anyType{}", "");
			if (StringUtil.isNotEmpty(inTable)) {
				tableOrImageData.put("table", inTable);
			}
			for (int i = 0; i < data2.size(); i++) {
				String detail = StringUtil.noNull(
						data2.get(i).get("cDocDetail")).trim();
				String sQe = StringUtil.noNull(data2.get(i).get("dSequence"));
				comnameStr = StringUtil.noNull(data2.get(i).get("createcom"));
				int code = Integer.parseInt(sQe);
				detail = detail.replace("anyType{}", "");
				if (StringUtil.isNotEmpty(detail)) {
					sb.append(StringUtil.ENTER);
					String dLevel = StringUtil.noNull(data2.get(i)
							.get("dLevel"));
					int level = 2;
					if (StringUtil.isNotEmpty(dLevel)) {
						level = Integer.parseInt(dLevel) + 1;
						sQe = StringUtil.parseNumberByLevel(level, code);
					}
					for (int j = 0; j < level; j++) {
						sb.append(StringUtil.SPACE);
					}
					sb.append(sQe + ".");
					sb.append(detail);
				}
				sb.append(setDetail(StringUtil.noNull(data2.get(i).get(
						"cDocDetailID"))));
			}
		}
		return sb.toString();
	}

}
