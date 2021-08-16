package com.test.test3app.stickerreply;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.R;

/**
 * created by zhaoyuntao
 * on 28/12/2020
 * description:
 */
class StickerReplyViewHolder extends RecyclerView.ViewHolder {

    private TextView stickerDescView;
    private TextView stickerCountView;

    public StickerReplyViewHolder(@NonNull View itemView) {
        super(itemView);
        stickerCountView = itemView.findViewById(R.id.sticker_reply_view_count);
        stickerDescView = itemView.findViewById(R.id.sticker_reply_view_desc);
    }

    public void setSticker(String sticker) {
        if (stickerDescView != null) {
            stickerDescView.setText(sticker);
        }
    }

    public void setStickerCount(int count) {
        if (stickerCountView != null) {
            String content;
            if (count > 99) {
                content = "99+";
            } else {
                content = String.valueOf(count);
            }
            stickerCountView.setText(content);
        }
    }

    public void setOnClickItemListener(final View.OnClickListener onClickItemListener) {
        this.itemView.setOnClickListener(onClickItemListener);
    }

    public void setStickerSelected(boolean includeMySticker) {
        if (includeMySticker) {
            this.itemView.setBackgroundResource(R.drawable.shape_background_sticker_corner_12dp_selected);
            this.stickerCountView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color_main_white_ffffff));
        } else {
            this.itemView.setBackgroundResource(R.drawable.shape_background_sticker_corner_12dp);
            this.stickerCountView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color_text_4b4b4b));
        }
    }
}
