package com.test.test3app;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by zhaoyuntao
 * on 25/11/2021
 * description:
 */
class CommonHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public CommonHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textview_item);
    }
}
