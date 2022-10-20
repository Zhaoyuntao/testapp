package com.test.test3app.windowtransition;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.test.test3app.R;
import com.test.test3app.wallpaper.ListItemSelector;

import java.util.List;

/**
 * created by zhaoyuntao
 * on 25/11/2021
 * description:
 */
public class ListViewAdapter extends BaseAdapter {
    private OnItemClickListener onItemClickListener;
    private final ListItemSelector<ImageBean> selector = new ListItemSelector<>();

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return selector.size();
    }

    @Override
    public Object getItem(int position) {
        return selector.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_window_item, parent, false);
        ImageView imageView0 = view.findViewById(R.id.imageview_item0);
        imageView0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, 0);
                }
            }
        });
        return view;
    }

    public void setData(List<ImageBean> imageBeans) {
        selector.setData(imageBeans);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
