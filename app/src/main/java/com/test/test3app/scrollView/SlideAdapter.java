package com.test.test3app.scrollView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.R;
import com.test.test3app.recyclerview.ChatCellContainer;
import com.test.test3app.textview.BubbleView;
import com.zhaoyuntao.androidutils.tools.S;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhaoyuntao
 * on 2019-12-20
 * description:
 */
public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.ViewHolder> {
    private List<String> mList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public SlideAdapter(List<String> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_cell_container, parent, false);
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
        if(position%2==0){
            holder.slideLayout.setTag(" ");
        }else{
            holder.slideLayout.setTag(null);
        }
        holder.bubbleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, text);
                }
            }
        });
        holder.bubbleView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                S.s("long click item");
                return true;
            }
        });
        holder.chatCellContainer.setText(text);
        S.s("onBindViewHolder[" + position + "]:" + text);
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        SlideLayout slideLayout;
        ChatCellContainer chatCellContainer;
        ViewGroup rootView;
        BubbleView bubbleView;


        ViewHolder(View itemView) {
            super(itemView);
            slideLayout = itemView.findViewById(R.id.slide_view);
            bubbleView = itemView.findViewById(R.id.bubble_view);
            slideLayout.setSlideListener(new SlideLayout.SlideListener() {
                @Override
                public void onFingerUp(boolean slideToRight) {
//                    S.s("onFingerUp: slideToRight: " + slideToRight);
                }

                @Override
                public void onSlideDistanceChange(float percent) {

                }

                @Override
                public boolean canSlide() {
                    return slideLayout.getTag() != null;
                }

                @Override
                public void onSlideToRight() {
                    S.s("onSlideToRight");
                }
            });

            chatCellContainer = itemView.findViewById(R.id.text_conversation_cell_simple_text);
            rootView = itemView.findViewById(R.id.message_root_view);
            SlideLayout.SlideLayoutParams slideLayoutParams = (SlideLayout.SlideLayoutParams) rootView.getLayoutParams();
            slideLayoutParams.gravity = Gravity.END;
            slideLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    S.s("click");
                }
            });
            slideLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    S.s("long click");
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String String);
    }
}
