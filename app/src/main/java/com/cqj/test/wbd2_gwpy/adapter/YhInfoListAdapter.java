package com.cqj.test.wbd2_gwpy.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.YhfcInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class YhInfoListAdapter extends BaseAdapter {

    private Context mContext;
    private List<YhfcInfo> mInfoList;

    public YhInfoListAdapter(Context context, List<YhfcInfo> pInfoList) {
        mContext = context;
        mInfoList = pInfoList;
    }

    @Override
    public int getCount() {
        return mInfoList == null ? 0 : mInfoList.size();
    }

    @Override
    public YhfcInfo getItem(int pI) {
        return mInfoList == null ? null : mInfoList.get(pI);
    }

    @Override
    public long getItemId(int pI) {
        return pI;
    }

    @Override
    public View getView(int pI, View convertView, ViewGroup pViewGroup) {
        TextView info =null;
        if(convertView ==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_yhinfo,pViewGroup,false);
            info = (TextView) convertView.findViewById(R.id.item_yhinfo_text);
            convertView.setTag(info);
        }else{
            info = (TextView) convertView.getTag();
        }

        YhfcInfo yhfcInfo =getItem(pI);
        if(yhfcInfo!=null){
            String text =String.format("%s>%s>%s",yhfcInfo.getSafetyTrouble(),yhfcInfo.getTroubleGrade(),yhfcInfo.getCheckObject());
            info.setText(text);
            try {
                Calendar otherCalendar = Calendar.getInstance();
                SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sformat.parse(yhfcInfo.getCheckDate());
                otherCalendar.setTime(date);
                Calendar nowCalendar = Calendar.getInstance();
                int ret = otherCalendar.compareTo(nowCalendar);
                if(ret==1){
                    info.setBackgroundColor(mContext.getResources().getColor(R.color.ored));
                }else{
                    info.setBackgroundColor(mContext.getResources().getColor(R.color.blue_btn_bg_pressed_color));
                }
            } catch (ParseException pE) {
                pE.printStackTrace();
            }
        }

        return convertView;
    }
}
