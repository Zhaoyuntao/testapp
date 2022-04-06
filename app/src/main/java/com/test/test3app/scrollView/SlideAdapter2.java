package com.test.test3app.scrollView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.R;
import com.zhaoyuntao.androidutils.tools.S;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 2019-12-20
 * description:
 */
public class SlideAdapter2 extends RecyclerView.Adapter<SlideAdapter2.ViewHolder> {
    private List<String> mList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private TextView textView;

    public SlideAdapter2(List<String> list) {
        setData(list);
    }

    public void setData(List<String> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(String String) {
        this.mList.add(String);
        notifyItemInserted(0);
    }

    int i = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide, parent, false);
//        S.s("onCreateViewHolder:"+i++);
        return new ViewHolder(view);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        final String String = mList.get(holder.getAdapterPosition());
//        S.e("[" + holder.getAdapterPosition() + "]-- onViewRecycled:" + String.getCountryName() + "               " + holder.itemView.findViewById(R.id.country_code_name));
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
//        final String String = mList.get(holder.getAdapterPosition());
//        S.e("======== onDetachedFromRecyclerView============================:");
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        final String String = mList.get(holder.getAdapterPosition());
//        S.e("["+holder.getAdapterPosition()+"]-------- onViewDetachedFromWindow:" + String.getCountryName()+"               "+holder.itemView.findViewById(R.id.country_code_name));
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
//        S.s("onViewAttachedToWindow:"+mList.get(holder.getAdapterPosition()).getCountryName());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String text = mList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, text);
                }
            }
        });
        holder.textView1.setText(text);
        holder.parentView.setTag(text);
        if (position == 30) {
            textView = holder.textView1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List payloads) {
        onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setItem(String toString) {

        if (textView != null) {
            textView.setText(toString);
            textView.requestLayout();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SlideLayout slideLayout;
        TextView textView1;
        View parentView;

        ViewHolder(View itemView) {
            super(itemView);
            slideLayout = itemView.findViewById(R.id.slide_view2);
            slideLayout.setListener(new SlideLayout.SlideListener() {
                @Override
                public void onFingerUp(boolean slideToRight) {
                    S.s("onFingerUp: slideToRight: " + slideToRight);
                }

                @Override
                public void onSlideToRight() {
                    S.s("onSlideToRight");
                }
            });
            parentView = itemView.findViewById(R.id.parentView);
            textView1 = itemView.findViewById(R.id.slideText);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String String);
    }
}
