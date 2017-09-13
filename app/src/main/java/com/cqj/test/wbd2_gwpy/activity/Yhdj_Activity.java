package com.cqj.test.wbd2_gwpy.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.kobjects.base64.Base64;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cqj.test.wbd2_gwpy.util.StringUtil;
import com.cqj.test.wbd2_gwpy.util.WebServiceUtil;
import com.cqj.test.wbd2_gwpy.view.AudioRecordUtil;import com.cqj.test.wbd2_gwpy.R;


/**
 * 隐患登记
 * 
 * @author Administrator
 * 
 */
public class Yhdj_Activity extends Activity {

	File cache;
	private String videoPath;
	private String audioPath;
	private final static int VIDEO = 24;
	private final static int AUDIO = 25;
	protected final static int TAKEPHOTO = 22;
	protected final static int IMAGE_SELECT = 23;
	private ImageView xcsp, xcpz, xcly;
	private CheckBox isCCb, isYh;
	private LinearLayout yhdj_ccb, yyh_ln, jcbxz_ln;
	private TextView yhdj_mstv, yhdj_yhtv, yhdj_rwtv, yhdj_cstv, yhdj_bmtv,
			yhdj_jcbtv, jcbxz_tv;
	private Spinner yhdj_zgzrr, yhdj_yhjb;
	public static final int RW_CHOOSE = 1;
	public static final int BM_CHOOSE = 2;
	public static final int CS_CHOOSE = 3;
	public static final int JCB_LIST = 4;
	private ArrayList<HashMap<String, Object>> data;
	private ArrayList<HashMap<String, Object>> gyData;// 雇员数据集
	private int currint = 0;
	private Button commit;
	private EditText yhdj_zgqx, yhdj_fy, yhdj_zgcs, yhdj_jcqk, yhdj_sbedt;

	// private UploadVideoRunnable uploadRunnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yhdj_view);
		myApp = (MyApplication) getApplication();
		initComplement();
		registListener();
		getData();
	}

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				jcbxz_tv.setText(data.get(0).get("odetail").toString());
				break;
			case 2:
				SimpleAdapter ada = new SimpleAdapter(Yhdj_Activity.this,
						gyData, android.R.layout.simple_spinner_item,
						new String[] { "emName" },
						new int[] { android.R.id.text1 });
				yhdj_zgzrr.setAdapter(ada);
				ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				break;
			case 3:
				changeButtonStyle(false, "提交");
				Toast.makeText(Yhdj_Activity.this, "提交成功", Toast.LENGTH_LONG)
						.show();
				yhdj_fy.setText("0");
				yhdj_jcqk.setText("正常");
				yhdj_zgcs.setText("");
				break;
			case 11:
				final String imagePath = (String) msg.obj;
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int month = canl.get(Calendar.MONTH) + 1;
						String monthStr = "";
						if (month < 10) {
							monthStr = "0" + month;
						} else {
							monthStr = month + "";
						}
						int day = canl.get(Calendar.DAY_OF_MONTH);
						String dayStr = "";
						if (day < 10) {
							dayStr = "0" + day;
						} else {
							dayStr = day + "";
						}
						String nowDate = canl.get(Calendar.YEAR) + "-"
								+ monthStr + "-" + dayStr + "T00:00:00.000";
						String limit = "2015-01-01T00:00:00.000";
						int lEmid = 0;
						float dCost = 0f;
						String dScheme = "", hGrade = "";
						if (isYh.isChecked()) {
							limit = yhdj_zgqx.getText().toString()
									+ "T00:00:00.000";
							lEmid = Integer.parseInt(gyData
									.get(yhdj_zgzrr.getSelectedItemPosition())
									.get("Emid").toString());
							dScheme = yhdj_zgcs.getText().toString();
							hGrade = yhdj_yhjb.getSelectedItem().toString();
							dCost = Float.parseFloat(yhdj_fy.getText()
									.toString());
						}
						int taskId = Integer.parseInt(yhdj_rwtv.getTag()
								.toString());
						int fliedId = Integer.parseInt(yhdj_cstv.getTag()
								.toString());
						int objPartid = Integer.parseInt(yhdj_bmtv.getTag()
								.toString());
						String keys3[] = { "checkDate", "recEmid", "dLimit",
								"hDetail", "dScheme", "hGrade", "lEmid",
								"dCost", "imgPatch", "objOrganizationID",
								"Taskid", "FliedID", "objPartid", "SetStr",
								"retstr" };
						Object[] values3 = { nowDate,
								Integer.parseInt(myApp.getComInfo().getEmid()),
								limit, yhdj_jcqk.getText().toString(), dScheme,
								hGrade, lEmid, dCost, imagePath, 0, taskId,
								fliedId, objPartid,
								yhdj_sbedt.getText().toString(), "" };
						try {
							WebServiceUtil.getWebServiceMsg(keys3, values3,
									"AddHiddenTroubleFull",
									WebServiceUtil.SAFE_URL);
							handler.sendEmptyMessage(3);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
				break;
			default:
				break;
			}
			return false;
		}
	});
	protected MyApplication myApp;
	protected Calendar canl;

	private void registListener() {
		yhdj_zgqx.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);
		yhdj_zgqx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new DatePickerDialog(Yhdj_Activity.this,
						new OnDateSetListener() {
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								String monthStr = "";
								if (monthOfYear < 9) {
									monthStr = "0" + (monthOfYear + 1);
								} else {
									monthStr = (monthOfYear + 1) + "";
								}
								String dayStr = "";
								if (dayOfMonth < 10) {
									dayStr = "0" + dayOfMonth;
								} else {
									dayStr = dayOfMonth + "";
								}
								yhdj_zgqx.setText(year + "-" + monthStr + "-"
										+ dayStr);
							}
						}, canl.get(Calendar.YEAR), canl.get(Calendar.MONTH),
						canl.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		yhdj_zgqx.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					new DatePickerDialog(Yhdj_Activity.this,
							new OnDateSetListener() {
								public void onDateSet(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									String monthStr = "";
									if (monthOfYear < 9) {
										monthStr = "0" + (monthOfYear + 1);
									} else {
										monthStr = (monthOfYear + 1) + "";
									}
									String dayStr = "";
									if (dayOfMonth < 10) {
										dayStr = "0" + dayOfMonth;
									} else {
										dayStr = dayOfMonth + "";
									}
									yhdj_zgqx.setText(year + "-" + monthStr
											+ "-" + dayStr);
								}
							}, canl.get(Calendar.YEAR), canl
									.get(Calendar.MONTH), canl
									.get(Calendar.DAY_OF_MONTH)).show();

				}
			}
		});
		isCCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					yhdj_ccb.setVisibility(View.VISIBLE);
					yhdj_mstv.setText("检查表模式");
				} else {
					yhdj_ccb.setVisibility(View.GONE);
					yhdj_mstv.setText("录入模式");
				}
			}
		});
		isYh.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					yyh_ln.setVisibility(View.VISIBLE);
					yhdj_yhtv.setText("有隐患");
				} else {
					yyh_ln.setVisibility(View.GONE);
					yhdj_yhtv.setText("无隐患");
				}
			}
		});
	}

	public void commit(View view) {
		if (yhdj_rwtv.getTag() == null) {
			Toast.makeText(Yhdj_Activity.this, "任务未选择", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (yhdj_bmtv.getTag() == null) {
			Toast.makeText(Yhdj_Activity.this, "部门未选择", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (yhdj_cstv.getTag() == null) {
			Toast.makeText(Yhdj_Activity.this, "场所未选择", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (isYh.isChecked()) {
			if (yhdj_zgqx.getText().toString().equals("")) {
				Toast.makeText(Yhdj_Activity.this, "整改期限未填", Toast.LENGTH_LONG)
						.show();
				return;
			}
			if (yhdj_fy.getText().toString().equals("")) {
				Toast.makeText(Yhdj_Activity.this, "费用未填", Toast.LENGTH_LONG)
						.show();
				return;
			}
			if (StringUtil.noNull(
					gyData.get(yhdj_zgzrr.getSelectedItemPosition())
							.get("Emid").toString()).equals("")) {
				Toast.makeText(Yhdj_Activity.this, "选择的整改责任人无效",
						Toast.LENGTH_LONG).show();
				return;
			}
			try {
				Float.parseFloat(yhdj_fy.getText().toString());
			} catch (Exception e) {
				Toast.makeText(Yhdj_Activity.this, "整改费用输入有误。",
						Toast.LENGTH_LONG).show();
				return;
			}
		}
		String[] urls = new String[1];
		if (xcpz.getTag() != null) {
			urls[0] = (String) xcpz.getTag();
			System.out.println("fileName:");
		}
		// if (xcsp.getTag() != null) {
		// urls[1] = (String) xcsp.getTag();
		// }
		// if (xcly.getTag() != null) {
		// urls[2] = (String) xcly.getTag();
		// }
		changeButtonStyle(true, "提交中...");
		testUpload(urls);
	}

	private void initComplement() {
		getActionBar().setTitle("隐患登记");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		canl = Calendar.getInstance();
		xcsp = (ImageView) findViewById(R.id.yjbj_xcsp);
		xcpz = (ImageView) findViewById(R.id.yjbj_xcpz);
		xcly = (ImageView) findViewById(R.id.yjbj_xcly);
		isCCb = (CheckBox) findViewById(R.id.yhdj_isccb);
		isYh = (CheckBox) findViewById(R.id.yhdj_isyh);
		yhdj_ccb = (LinearLayout) findViewById(R.id.yhdj_ccb);
		yyh_ln = (LinearLayout) findViewById(R.id.yyh_ln);
		yhdj_yhtv = (TextView) findViewById(R.id.yhdj_yhtv);
		yhdj_mstv = (TextView) findViewById(R.id.yhdj_mstv);
		yhdj_rwtv = (TextView) findViewById(R.id.yhdj_rwtv);
		yhdj_cstv = (TextView) findViewById(R.id.yhdj_cstv);
		yhdj_jcbtv = (TextView) findViewById(R.id.yhdj_jcbtv);
		yhdj_bmtv = (TextView) findViewById(R.id.yhdj_bmtv);
		jcbxz_ln = (LinearLayout) findViewById(R.id.jcbxz_ln);
		jcbxz_tv = (TextView) findViewById(R.id.jcbxz_tv);
		yhdj_zgzrr = (Spinner) findViewById(R.id.yhdj_zgzrr);
		yhdj_yhjb = (Spinner) findViewById(R.id.yhdj_yhjb);
		commit = (Button) findViewById(R.id.yhdj_commit);
		yhdj_zgqx = (EditText) findViewById(R.id.yhdj_zgqx);
		yhdj_fy = (EditText) findViewById(R.id.yhdj_fy);
		yhdj_zgcs = (EditText) findViewById(R.id.yhdj_zgcs);
		yhdj_jcqk = (EditText) findViewById(R.id.yhdj_jcqk);
		yhdj_sbedt = (EditText) findViewById(R.id.yhdj_sbedt);
		cache = new File(Environment.getExternalStorageDirectory(),
				"hwagtCache");
		if (!cache.exists())
			cache.mkdirs();
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

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String keys3[] = { "orgid" };
				Object values3[] = { Integer.parseInt(myApp.getComInfo()
						.getOrg_id()) };
				try {
					gyData = WebServiceUtil.getWebServiceMsg(keys3, values3,
							"getAllEmployeeFromOrgID", new String[] { "emName",
									"Emid" });
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (gyData != null) {
					handler.sendEmptyMessage(2);
				}
			}
		}).start();
	}

	private void getJcbDetail(final int docID) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String keys2[] = { "SCheckID", "CDocID", "InduID", "FliedID",
						"ProName" };
				Object values2[] = { 0, docID, 0, 0, "" };
				data = new ArrayList<HashMap<String, Object>>();
				try {
					data = WebServiceUtil.getWebServiceMsg(keys2, values2,
							"getSafetyCheckList", new String[] { "oblititle",
									"odetail", "sCheckListtype", "induname",
									"inseName", "malName", "rAdvise" },
							WebServiceUtil.SAFE_URL);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (data != null) {
					handler.sendEmptyMessage(1);
				}
			}
		}).start();
	}

	public void toChoose(View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.yhdj_rw:// 任务选择
			intent = new Intent(Yhdj_Activity.this, ChooseActivity.class);
			intent.putExtra("type", RW_CHOOSE);
			startActivityForResult(intent, 301);
			break;
		case R.id.yhdj_bm:// 部门选择
			intent = new Intent(Yhdj_Activity.this, ChooseActivity.class);
			intent.putExtra("type", BM_CHOOSE);
			startActivityForResult(intent, 302);
			break;

		case R.id.yhdj_cs:// 场所选择
			intent = new Intent(Yhdj_Activity.this, ChooseActivity.class);
			intent.putExtra("type", CS_CHOOSE);
			startActivityForResult(intent, 303);
			break;
		case R.id.yhdj_ccb:
			intent = new Intent(Yhdj_Activity.this, ChooseActivity.class);
			intent.putExtra("type", JCB_LIST);
			startActivityForResult(intent, 304);
			break;
		case R.id.jcbxz_syt:// 上一条
			if (currint == 0) {
				Toast.makeText(Yhdj_Activity.this, "没有上一条了", Toast.LENGTH_LONG)
						.show();
				break;
			}
			currint--;
			jcbxz_tv.setText(data.get(currint).get("odetail").toString());
			break;
		case R.id.jcbxz_xyt:// 下一条
			if (currint == data.size() - 1) {
				Toast.makeText(Yhdj_Activity.this, "没有下一条了", Toast.LENGTH_LONG)
						.show();
				break;
			}
			currint++;
			jcbxz_tv.setText(data.get(currint).get("odetail").toString());
			break;
		case R.id.jcbxz_xq:// 详情
			StringBuffer sb = new StringBuffer();
			sb.append("标题：");
			sb.append(StringUtil.noNull(data.get(currint).get(
					"oblititle".toString())));
			sb.append("\n");
			sb.append("细则：");
			sb.append(StringUtil.noNull(data.get(currint).get(
					"odetail".toString())));
			sb.append("\n");
			sb.append("检查表项针对的人的行为、设备、环境的因素：");
			sb.append(StringUtil.noNull(data.get(currint).get(
					"sCheckListtype".toString())));
			sb.append("\n");
			sb.append("行业类别：");
			sb.append(StringUtil.noNull(data.get(currint).get(
					"induname".toString())));
			sb.append("\n");
			sb.append("伤害类别：");
			sb.append(StringUtil.noNull(data.get(currint).get(
					"inseName".toString())));
			sb.append("\n");
			sb.append("事故类别：");
			sb.append(StringUtil.noNull(data.get(currint).get(
					"malName".toString())));
			sb.append("\n");
			sb.append("整改建议：");
			sb.append(StringUtil.noNull(data.get(currint).get(
					"rAdvise".toString())));
			sb.append("\n");
			intent = new Intent(Yhdj_Activity.this, JcbDetailActivity.class);
			intent.putExtra("detail", sb.toString());
			startActivity(intent);

			break;
		default:
			break;
		}
	}

	public void takePhoto(View view) {

		String[] itemName = { "拍照", "本地照片" };
		AlertDialog.Builder ad = new AlertDialog.Builder(Yhdj_Activity.this);
		ad.setTitle("请选择");
		ad.setItems(itemName, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					if (0 == which) // 拍照
					{
//						Intent i = new Intent(Yhdj_Activity.this,
//								CameraActivity.class);
//						i.putExtra("takephoto", "takephoto1");
//						startActivityForResult(i, TAKEPHOTO);
					} else if (1 == which) // 浏览本地
					{
//						Intent intent = new Intent(Yhdj_Activity.this,
//								LocalImageActivity.class);
//						intent.putExtra("type", "takephoto1");
//						startActivityForResult(intent, IMAGE_SELECT);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		ad.show();

	}

	/**
	 * 录像
	 * 
	 * @param view
	 */
	public void takeVideo(View view) {
		Intent mIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		mIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(mIntent, VIDEO);
	}

	/**
	 * 录音
	 */
	public void takeAudio(View view) {
		Intent intent = new Intent(Yhdj_Activity.this, AudioRecordUtil.class);
		audioPath = cache.getAbsolutePath() + File.separator
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
				+ ".mp3";
		intent.putExtra("audioPath", audioPath);

		startActivityForResult(intent, AUDIO);
	}

	/**
	 * 拍摄视频
	 * 
	 * @param data
	 */
	private void handleVideo(Intent data) {
		FileOutputStream fos = null;
		File tmpFile = null; // 视频文件
		try {
			AssetFileDescriptor videoAsset = getContentResolver()
					.openAssetFileDescriptor(data.getData(), "r");
			// 存储拍摄视频
			FileInputStream fis = videoAsset.createInputStream();

			videoPath = cache.getAbsolutePath() + File.separator
					+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
					+ ".3gp";
			tmpFile = new File(videoPath);

			fos = new FileOutputStream(tmpFile);
			byte[] buf = new byte[1024];
			int len;
			while ((len = fis.read(buf)) > 0) {
				fos.write(buf, 0, len);
			}
			fis.close();
			fos.close();
			Bitmap bm = ThumbnailUtils.createVideoThumbnail(videoPath,
					Thumbnails.MICRO_KIND);
			// Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath,
			// 512 * 384);
			xcsp.setImageBitmap(bm);
			xcsp.setTag(videoPath);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}// finally
	}

	public void testUpload(final String[] filenames) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String result = "";
				// TODO Auto-generated method stub
				for (String filename : filenames) {
					if (TextUtils.isEmpty(filename)) {
						continue;
					}
					String fileName = new File(filename).getName();
					try {
						FileInputStream fis = new FileInputStream(filename);

						ByteArrayOutputStream baos = new ByteArrayOutputStream();

						byte[] buffer = new byte[4096];

						int count = 0;

						while ((count = fis.read(buffer)) >= 0) {

							baos.write(buffer, 0, count);

						}

						String uploadBuffer = new String(Base64.encode(baos
								.toByteArray())); // 进行Base64编码

						String methodName = "UploadFile";
						String[] keys = { "fileBytesstr","fileName" };
						Object[] values = {  uploadBuffer,fileName };
						System.out.println("fileName:"+fileName);
						result = WebServiceUtil.putWebServiceMsg(keys, values,
								methodName, WebServiceUtil.URL);
						fis.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				Message msg = handler.obtainMessage();
				msg.what = 11;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		}).start();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
				// 检测 sdcard 是否可用
				Toast.makeText(this, "SD卡不存在", Toast.LENGTH_LONG).show();
				return;
			}

			if (resultCode == Activity.RESULT_OK) {

				String photoFilename = data.getStringExtra("picFile");

				if (requestCode == TAKEPHOTO) {
					photoFilename = data.getStringExtra("picFile");
					Bitmap bm = ThumbnailUtils.extractThumbnail(
							BitmapFactory.decodeFile(photoFilename), 100, 100);
					xcpz.setImageBitmap(bm);
					xcpz.setTag(photoFilename);
					// handlePhoto(R.id.photo_iv, 1);
				} else if (requestCode == IMAGE_SELECT) // 图片浏览器返回
				{
					photoFilename = data.getStringExtra("picFile");
					Bitmap bm = ThumbnailUtils.extractThumbnail(
							BitmapFactory.decodeFile(photoFilename), 100, 100);
					xcpz.setImageBitmap(bm);
					xcpz.setTag(photoFilename);
				}

				if (requestCode == VIDEO) // 视频录制
				{
					handleVideo(data);

				}

				if (requestCode == AUDIO) // 录音
				{
					// 显示多媒体View

					String strResult = data.getStringExtra("result");
					System.out.println("audio:" + strResult);
					if (strResult != null) {
						xcly.setImageResource(R.drawable.homepage_wb_row_ly_bg);
						xcly.setTag(strResult);
					}

				}
				String strResult = "";
				String resultID = "";
				switch (requestCode) {
				case 301:
					strResult = StringUtil
							.noNull(data.getStringExtra("result"));
					resultID = StringUtil.noNull(data
							.getStringExtra("resultID"));
					yhdj_rwtv.setText(strResult);
					yhdj_rwtv.setTag(resultID);
					break;
				case 302:
					strResult = StringUtil
							.noNull(data.getStringExtra("result"));
					resultID = StringUtil.noNull(data
							.getStringExtra("resultID"));
					yhdj_bmtv.setText(strResult);
					yhdj_bmtv.setTag(resultID);
					break;
				case 303:
					strResult = StringUtil
							.noNull(data.getStringExtra("result"));
					resultID = StringUtil.noNull(data
							.getStringExtra("resultID"));
					yhdj_cstv.setText(strResult);
					yhdj_cstv.setTag(resultID);
					break;
				case 304:
					strResult = StringUtil
							.noNull(data.getStringExtra("result"));
					resultID = StringUtil.noNull(data
							.getStringExtra("resultID"));
					yhdj_jcbtv.setText(strResult);
					yhdj_jcbtv.setTag(resultID);
					jcbxz_ln.setVisibility(View.VISIBLE);
					int docID = Integer.parseInt(resultID);
					getJcbDetail(docID);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
}
