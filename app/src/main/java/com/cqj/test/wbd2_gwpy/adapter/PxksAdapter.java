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
import android.widget.TextView;

public class PxksAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String,Object>> data;
	public PxksAdapter(Context context,ArrayList<HashMap<String,Object>> data){
		this.context =context;
		this.data= data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mVh =null;
		if(convertView ==null){
			mVh =new ViewHolder();
			convertView =LayoutInflater.from(context).inflate(R.layout.pxks_item, null);
			mVh.content =(TextView) convertView.findViewById(R.id.pxksitem_content);
			mVh.date =(TextView) convertView.findViewById(R.id.pxksitem_date);
			mVh.khtg =(TextView) convertView.findViewById(R.id.pxksitem_kstg);
			mVh.teacher =(TextView) convertView.findViewById(R.id.pxksitem_teacher);
			convertView.setTag(mVh);
		}else{
			mVh =(ViewHolder) convertView.getTag();
		}
		HashMap<String,Object> item =data.get(arg0);
		if(item!=null){
			String score =StringUtil.noNull(item.get("lesscore"));
			if(StringUtil.isNotEmpty(score)){
				if(Integer.parseInt(score)>=60){
					mVh.date.setText("学习完成");
					mVh.khtg.setText("考核通过");
				}else{
					mVh.date.setText("学习完成");
					mVh.khtg.setText("未达标");
				}
			}else{
				mVh.date.setText("课程学时："+StringUtil.noNull(item.get("leshour")));
				mVh.khtg.setText("未考核");
			}
			mVh.content.setText(StringUtil.noNull(item.get("currName")));
			mVh.teacher.setText("讲师："+StringUtil.noNull(item.get("teachemname")));
		}
		return convertView;
	}

	class ViewHolder{
		TextView content;
		TextView date;
		TextView khtg;
		TextView teacher;
	}
}
