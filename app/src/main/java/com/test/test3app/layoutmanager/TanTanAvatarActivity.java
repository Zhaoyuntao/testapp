package com.test.test3app.layoutmanager;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import base.ui.BaseActivity;
import com.test.test3app.CommonBean;
import com.test.test3app.CommonImageAdapter;
import com.test.test3app.R;

import java.util.Collections;
import java.util.List;

public class TanTanAvatarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tan_tan_avatar);
        RecyclerView mRv = (RecyclerView) findViewById(R.id.rv);
        mRv.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = mRv.getLayoutParams();
                layoutParams.height = getResources().getDisplayMetrics().widthPixels;
                mRv.setLayoutParams(layoutParams);
            }
        });
        CommonImageAdapter adapter = new CommonImageAdapter();
        mRv.setLayoutManager(new AvatarLayoutManager());
        mRv.setAdapter(adapter);

        List<CommonBean> mDatas = adapter.getCurrentList();
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(mDatas, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(mDatas, i, i - 1);
                    }
                }
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRv);
    }
}
