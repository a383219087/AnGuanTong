package com.cqj.test.wbd2_gwpy.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.util.StringUtil;
import com.cqj.test.wbd2_gwpy.util.WebServiceUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DutyListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, Object>> data;

	public DutyListAdapter(ArrayList<HashMap<String, Object>> data,
			Context context) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.duty_item, null);
			vHolder = new ViewHolder();
			vHolder.title = (TextView) convertView
					.findViewById(R.id.dutyitem_title);
			vHolder.detail = (TextView) convertView
					.findViewById(R.id.dutyitem_detail_tv);
			convertView.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, Object> map = data.get(position);
		if (map != null) {
			String text = StringUtil.noNull(map.get("sName"))
					+ StringUtil.noNull(map.get("partName"))
					+ StringUtil.noNull(map.get("duName")) + "职责一览";
			vHolder.title.setText(text);
			int partId = 0;
			int dutyId = 0;
			if (StringUtil.isNotEmpty(StringUtil.noNull(map.get("partid")))) {
				partId = Integer.parseInt(StringUtil.noNull(map.get("partid")));
			}
			if (StringUtil.isNotEmpty(StringUtil.noNull(map.get("dutyid")))) {
				dutyId = Integer.parseInt(StringUtil.noNull(map.get("dutyid")));
			}
			vHolder.detail.setText("");
			new myGetDetailTask(partId, dutyId, vHolder.detail).execute("");
		}
		return convertView;
	}

	class ViewHolder {
		TextView title;
		TextView detail;
	}

	class myGetDetailTask extends
			AsyncTask<String, String, ArrayList<HashMap<String, Object>>> {

		private int partId;
		private int dutyId;
		private TextView detail;

		public myGetDetailTask(int partId, int dutyId, TextView detail) {
			this.partId = partId;
			this.dutyId = dutyId;
			this.detail = detail;
		}

		@Override
		protected ArrayList<HashMap<String, Object>> doInBackground(
				String... params) {
			// TODO Auto-generated method stub
			ArrayList<HashMap<String, Object>> getDatas = new ArrayList<HashMap<String, Object>>();
			String keys2[] = { "orgIDstr", "cDocID", "titleKeyWord",
					"detailKeyWord", "carryPartID", "carryDutyID", "docType",
					"parentCDocID", "cDocDetailID", "retstr" };
			Object values2[] = { null, 0, "", "", partId, dutyId, "", 0, 0, "" };
			try {
				getDatas = WebServiceUtil.getWebServiceMsg(keys2, values2,
						"getCapacityDocumentDetail", new String[] {
								"carryPartName", "dLevel", "cDocDetailID",
								"dSequence", "cDocTitle", "inTable", "inImage",
								"createcom", "cDocDetail", "info_additional",
								"info_additiondoc" });
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return getDatas;
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
			// TODO Auto-generated method stub
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
}
