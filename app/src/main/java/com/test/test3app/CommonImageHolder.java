package com.test.test3app;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by zhaoyuntao
 * on 25/11/2021
 * description:
 */
class CommonImageHolder extends RecyclerView.ViewHolder {
    public AppCompatImageView imageView;

    public CommonImageHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview_item);
    }
}
