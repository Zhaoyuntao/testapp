package com.test.test3app;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.test.test3app.cashgift.CashGiftThemeBean;
import com.test.test3app.cashgift.CashGiftThemeUtils;

import java.util.List;

/**
 * created by zhaoyuntao
 * on 25/11/2021
 * description:
 */
public class CashGiftThemeAdapter extends ListAdapter<CashGiftThemeBean, CashGiftHolder> {
    public CashGiftThemeAdapter() {
        super(new DiffUtil.ItemCallback<CashGiftThemeBean>() {
            @Override
            public boolean areItemsTheSame(@NonNull CashGiftThemeBean oldItem, @NonNull CashGiftThemeBean newItem) {
                return oldItem.getTheme() == newItem.getTheme();
            }

            @Override
            public boolean areContentsTheSame(@NonNull CashGiftThemeBean oldItem, @NonNull CashGiftThemeBean newItem) {
                return true;
            }
        });

        List<CashGiftThemeBean> list = CashGiftThemeUtils.getAllCashGiftThemes();
        submitList(list);
    }

    @NonNull
    @Override
    public CashGiftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CashGiftHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cash_gift_theme_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CashGiftHolder holder, int position) {
        holder.set(getCurrentList().get(position));
    }
}
