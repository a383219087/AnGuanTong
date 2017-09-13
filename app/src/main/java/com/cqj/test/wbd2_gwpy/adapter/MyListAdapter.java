package com.cqj.test.wbd2_gwpy.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.util.StringUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, Object>> data;
	private String[] keys;

	public MyListAdapter(ArrayList<HashMap<String, Object>> data,
			Context context, String[] keys) {
		this.context = context;
		this.data = data;
		this.keys = keys;
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
					R.layout.gwlist_item, null);
			vHolder = new ViewHolder();
			vHolder.iv = (ImageView) convertView.findViewById(R.id.gwitem_iv);
			vHolder.content = (TextView) convertView
					.findViewById(R.id.gwitem_content);
			vHolder.comname = (TextView) convertView
					.findViewById(R.id.gwitem_comname);
			vHolder.date = (TextView) convertView
					.findViewById(R.id.gwitem_time);
			convertView.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}
		HashMap<String, Object> map = data.get(position);
		if (map != null) {
			if ("".equals(StringUtil.noNull(map.get(keys[0])))) {
				vHolder.iv
						.setImageResource(R.drawable.gwlb_icon3);
			} else if ("0".equals(StringUtil.noNull(map.get(keys[0])))) {
				vHolder.iv.setImageResource(R.drawable.gwlb_icon2);
			} else {
				vHolder.iv.setImageResource(R.drawable.gwlb_icon1);
			}
			vHolder.content.setText(StringUtil.noNull(map.get(keys[1])));
			vHolder.comname.setText(StringUtil.noNull(map.get(keys[2])));
			String date =StringUtil.noNull(map.get(keys[3]));
			if(date.length()>10){
				date =date.substring(0,10);
			}
			vHolder.date.setText(date);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView iv;
		TextView content;
		TextView comname;
		TextView date;
	}

}
