package com.test.test3app.windowtransition;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.R;

/**
 * created by zhaoyuntao
 * on 25/11/2021
 * description:
 */
public class ImageHolder extends RecyclerView.ViewHolder {
    public ImageView imageView0;
//    public ImageView imageView1;
//    public ImageView imageView2;
//    public ImageView imageView3;

    public ImageHolder(@NonNull View itemView) {
        super(itemView);
        imageView0 = itemView.findViewById(R.id.imageview_item0);
//        imageView1 = itemView.findViewById(R.id.imageview_item1);
//        imageView2 = itemView.findViewById(R.id.imageview_item2);
//        imageView3 = itemView.findViewById(R.id.imageview_item3);
    }
}
