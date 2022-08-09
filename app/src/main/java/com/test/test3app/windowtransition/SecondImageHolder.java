package com.test.test3app.windowtransition;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.R;

import im.thebot.chat.ui.view.ChatImageView;

/**
 * created by zhaoyuntao
 * on 25/11/2021
 * description:
 */
public class SecondImageHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;

    public SecondImageHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview_item_second);
    }
}
