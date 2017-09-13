package com.cqj.test.wbd2_gwpy.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.cqj.test.wbd2_gwpy.YhfcInfo;
import com.cqj.test.wbd2_gwpy.activity.YhzgUploadActivity;

/**
 *隐患整改反馈
 * Created by Administrator on 2016/4/23.
 */
public class JcfkFragment extends AqfcFragment{

    public static JcfkFragment newInstance() {
        
        Bundle args = new Bundle();
        
        JcfkFragment fragment = new JcfkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getFragmentTitle(){
        return "以下隐患未反馈";
    }


    public AdapterView.OnItemClickListener getListItemClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
                YhfcInfo info = (YhfcInfo) pAdapterView.getItemAtPosition(pI);
                try{
                    YhzgUploadActivity.start(getActivity(),Integer.parseInt(info.getHTroubleID()),info.getSafetyTrouble());
                }catch (NumberFormatException ignored){

                }
            }
        };
    }

    @Override
    public int getReview() {
        return 0;
    }

    @Override
    public int getFinished() {
        return 2;
    }

    @Override
    public boolean isYhzg() {
        return true;
    }

}
