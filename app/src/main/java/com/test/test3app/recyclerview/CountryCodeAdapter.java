package com.test.test3app.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.R;
import im.turbo.utils.log.S;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 2019-12-20
 * description:
 */
public class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.ViewHolder> {
    private List<CountryCodeBean> mList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public CountryCodeAdapter(List<CountryCodeBean> list) {
        setData(list);
    }

    public void setData(List<CountryCodeBean> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(CountryCodeBean countryCodeBean) {
        this.mList.add(countryCodeBean);
        notifyItemInserted(0);
    }

    int i = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.g42pay_sdk_layout_item_country_code, parent, false);
//        S.s("onCreateViewHolder:"+i++);
        return new ViewHolder(view);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        final CountryCodeBean countryCodeBean = mList.get(holder.getAdapterPosition());
//        S.e("[" + holder.getAdapterPosition() + "]-- onViewRecycled:" + countryCodeBean.getCountryName() + "               " + holder.itemView.findViewById(R.id.country_code_name));
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
//        final CountryCodeBean countryCodeBean = mList.get(holder.getAdapterPosition());
//        S.e("======== onDetachedFromRecyclerView============================:");
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        final CountryCodeBean countryCodeBean = mList.get(holder.getAdapterPosition());
//        S.e("["+holder.getAdapterPosition()+"]-------- onViewDetachedFromWindow:" + countryCodeBean.getCountryName()+"               "+holder.itemView.findViewById(R.id.country_code_name));
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
//        S.s("onViewAttachedToWindow:"+mList.get(holder.getAdapterPosition()).getCountryName());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final CountryCodeBean countryCodeBean = mList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, countryCodeBean);
                }
            }
        });
        holder.countryCodeIcon.setImageResource(countryCodeBean.getDrawableId());
        holder.countryCodeName.setText(countryCodeBean.getCountryName());
        S.s("onBindViewHolder 1:" + countryCodeBean.getCountryName());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List payloads) {
        S.s("onBindViewHolder 2:" + payloads);
        onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        S.s("getItemViewType:" + position);
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CountryCodeText countryCodeName;
        private final ImageView countryCodeIcon;

        ViewHolder(View itemView) {
            super(itemView);
            countryCodeName = itemView.findViewById(R.id.country_code_name);
            countryCodeIcon = itemView.findViewById(R.id.country_code_icon);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, CountryCodeBean countryCodeBean);
    }
}
