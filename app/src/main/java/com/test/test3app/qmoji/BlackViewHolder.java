package com.test.test3app.qmoji;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.R;

public class BlackViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView;

    public BlackViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.icon);
        textView = itemView.findViewById(R.id.text);
    }
}
