package com.test.test3app.textview;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test3app.R;
import im.turbo.baseui.utils.UiUtils;
import com.test.test3app.scrollView.SlideLayoutOld;
import im.turbo.utils.log.S;

import java.util.ArrayList;
import java.util.List;

import im.thebot.chat.ui.view.BubbleView;
import im.thebot.common.ui.chat.TextCellContainer;

/**
 * created by zhaoyuntao
 * on 2019-12-20
 * description:
 */
public class SlideBubbleAdapter extends RecyclerView.Adapter<SlideBubbleAdapter.ViewHolder> {
    private List<String> mList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public SlideBubbleAdapter(List<String> list) {
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
        View baseMessageCell;
        boolean matchParent = false;
        switch (viewType) {
            case 1:
                baseMessageCell = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_cell_item_file, null);
                matchParent = true;
                break;
            default:
                baseMessageCell = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_text, null);
                break;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_item_slide_bubble,
                parent, false);
//        S.s("onCreateViewHolder:"+i++);
        return new ViewHolder(view, baseMessageCell, matchParent);
    }

    @Override
    public int getItemViewType(int position) {
        String data = mList.get(position);
        if ("file".equals(data)) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String text = mList.get(position);

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
        if (holder.textView != null) {
            holder.textView.setText(text);
        }
        S.s("onBindViewHolder[" + position + "]:" + text);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List payloads) {
        onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SlideLayoutOld slideLayout;
        TextCellContainer textCellContainer;
        LinearLayout rootView;
        BubbleView bubbleView;
        TextView textView;
        ViewGroup container;
        View baseMessageCell;


        ViewHolder(View itemView, View baseMessageCell, boolean matchParent) {
            super(itemView);
            this.baseMessageCell = baseMessageCell;
            slideLayout = itemView.findViewById(R.id.slide1);
            bubbleView = itemView.findViewById(R.id.bubble1);
            container = itemView.findViewById(R.id.container_message);
            slideLayout.setSlideListener(new SlideLayoutOld.SlideListener() {
                @Override
                public void onFingerUp(boolean slideToRight) {
//                    S.s("onFingerUp: slideToRight: " + slideToRight);
                }

                @Override
                public void onSlideDistanceChange(float percent) {

                }

                @Override
                public boolean canSlide() {
                    return true;//slideLayout.getTag() != null;
                }

                @Override
                public void onSlideToRight() {
                    S.s("onSlideToRight");
                }
            });

            textCellContainer = itemView.findViewById(R.id.ztextview1);
            rootView = itemView.findViewById(R.id.message_root_view);
            textView = itemView.findViewById(R.id.text_view_1);
            SlideLayoutOld.SlideLayoutParams slideLayoutParams = (SlideLayoutOld.SlideLayoutParams) rootView.getLayoutParams();
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
            changeLayoutParams(matchParent);
        }

        private void changeLayoutParams(boolean matchParent) {
            //Child container.
            ViewGroup.LayoutParams containerLayoutParams = container.getLayoutParams();

            FrameLayout.LayoutParams cellLayoutParams;
            LinearLayout.LayoutParams bubbleViewLayoutParams = (LinearLayout.LayoutParams) bubbleView.getLayoutParams();
            if (matchParent) {
                bubbleViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                containerLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                cellLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                bubbleViewLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                containerLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                cellLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            int position = getBindingAdapterPosition();
            if (position % 2 == 0) {
                rootView.setPadding(0, 0, UiUtils.dipToPx(48), 0);
                rootView.setGravity(Gravity.START);
            } else {
                rootView.setPadding(UiUtils.dipToPx(48), 0, 0, 0);
                rootView.setGravity(Gravity.END);
            }

            this.baseMessageCell.setLayoutParams(cellLayoutParams);
            this.container.addView(baseMessageCell);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String String);
    }
}
