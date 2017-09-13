package com.cqj.test.wbd2_gwpy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.myinterface.IChooseItem;
import com.cqj.test.wbd2_gwpy.myinterface.INoCommitItem;

import java.util.HashMap;
import java.util.List;

public class NoCommitAdapter<T extends INoCommitItem> extends BaseAdapter{

	private Context mContext;
	private HashMap<Integer,Boolean> mIsCheckList;
	private List<T> mData;
	public NoCommitAdapter(Context context, List<T> data){
		this.mContext =context;
		this.mData = data;
		mIsCheckList =new HashMap<Integer, Boolean>();
	}

	public HashMap<Integer, Boolean> getIsCheckList() {
		return mIsCheckList;
	}

	public void setCommitFail(int position){
		if(mIsCheckList.get(position)!=null){
			mIsCheckList.remove(position);
		}
		notifyDataSetChanged();
	}

	public List<T> getData(){
		return mData;
	}

	public void setAllCheck(){
		if(mIsCheckList.size()== mData.size()){
			mIsCheckList.clear();
		}else {
			for (int i = 0; i < mData.size(); i++) {
				if (!mIsCheckList.containsKey(i)) {
					mIsCheckList.put(i, true);
				}
			}
		}
		notifyDataSetChanged();
	}

	public boolean isAllCommit(){
		for (T t:mData){
			if(!t.isCommitSuccess()){
				return false;
			}
		}
		return true;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public T getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		ViewHolder mVh =null;
		if(convertView ==null){
			mVh =new ViewHolder();
			convertView =LayoutInflater.from(mContext).inflate(R.layout.no_commit_item, null);
			mVh.mCsTv =(TextView) convertView.findViewById(R.id.no_commit_cs);
			mVh.mRwTv =(TextView) convertView.findViewById(R.id.no_commit_rw);
			mVh.mDate =(TextView) convertView.findViewById(R.id.no_commit_date);
			mVh.mSuccessState = (TextView) convertView.findViewById(R.id.commit_success);
			mVh.mCheckBox = (CheckBox) convertView.findViewById(R.id.no_commit_checkbox);
			convertView.setTag(mVh);
		}else{
			mVh =(ViewHolder) convertView.getTag();
		}
		T item = mData.get(arg0);
		if(item!=null){
			mVh.mCsTv.setText(item.getDetail());
			mVh.mRwTv.setText(item.getName());
			mVh.mDate.setText(item.getDate());
			if(item.isCommitSuccess()){
				mVh.mCheckBox.setVisibility(View.GONE);
				mVh.mSuccessState.setVisibility(View.VISIBLE);
			}else{
				mVh.mCheckBox.setVisibility(View.VISIBLE);
				mVh.mSuccessState.setVisibility(View.GONE);
			}
			final ViewHolder finalMVh = mVh;
			mVh.mCheckBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View pView) {
					if(mIsCheckList.get(arg0)!=null){
						mIsCheckList.remove(arg0);
					}else{
						mIsCheckList.put(arg0,true);
					}
					finalMVh.mCheckBox.setChecked(mIsCheckList.get(arg0)!=null);
				}
			});
			mVh.mCheckBox.setChecked(mIsCheckList.get(arg0)!=null);
		}
		return convertView;
	}

	static final class ViewHolder{
		TextView mCsTv;
		TextView mRwTv;
		TextView mDate;
		TextView mSuccessState;
		CheckBox mCheckBox;
	}
}
