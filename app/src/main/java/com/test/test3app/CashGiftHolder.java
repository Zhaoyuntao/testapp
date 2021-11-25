package com.test.test3app;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.cashgift.CashGiftThemeBean;

/**
 * created by zhaoyuntao
 * on 25/11/2021
 * description:
 */
class CashGiftHolder extends RecyclerView.ViewHolder {

    public CashGiftHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void set(CashGiftThemeBean cashGiftThemeBean){
        ImageView background = itemView.findViewById(R.id.skin_image_view_view_pager_cash_gift_theme);
        ImageView button = itemView.findViewById(R.id.button_image_view_view_pager_cash_gift_theme);
        TextView buttonText = itemView.findViewById(R.id.button_text_view_view_pager_cash_gift_theme);
        if (TextUtils.isEmpty(cashGiftThemeBean.getSkinCellUrl())) {
            background.setImageResource(cashGiftThemeBean.getSkinNotOpenRes());
        } else {
//            Glide.with(background).load(cashGiftThemeBean.getSkinNotOpenUrl()).into(background);
        }
        if (TextUtils.isEmpty(cashGiftThemeBean.getButtonUrl())) {
            button.setImageResource(cashGiftThemeBean.getButtonRes());
        } else {
//            Glide.with(button).load(cashGiftThemeBean.getButtonUrl()).into(button);
        }
        buttonText.setTextColor(cashGiftThemeBean.getTextBackgroundColor());
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onItemClickListener != null) {
//                    onItemClickListener.onClickTheme(position, cashGiftThemeBean);
//                }
//            }
//        });
    }
}
