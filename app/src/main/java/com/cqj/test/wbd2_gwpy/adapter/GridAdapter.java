package com.cqj.test.wbd2_gwpy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cqj.test.wbd2_gwpy.R;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    private LayoutInflater listContainer;
    private List<String> urlList;
    private Context context;

    public class ViewHolder {
        public ImageView image;
    }

    public GridAdapter(Context context, List<String> urlList) {
        listContainer = LayoutInflater.from(context);
        this.urlList = urlList;
        this.context = context;
    }

    public int getCount() {
        return urlList.size();
    }

    public Object getItem(int arg0) {

        return null;
    }

    public long getItemId(int arg0) {

        return 0;
    }


    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // 自定义视图
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = listContainer.inflate(
                    R.layout.item_published_grida, parent, false);
            // 获取控件对象
            holder.image = (ImageView) convertView
                    .findViewById(R.id.item_grida_image);
            // 设置控件集到convertView
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (holder.image.getTag() == null || !holder.image.getTag().toString().equals(urlList.get(position))) {
            FinalBitmap.create(context).display(holder.image, urlList.get(position));
            holder.image.setTag(urlList.get(position));
        }

        return convertView;
    }

    public void onDestory(){

    }
}
