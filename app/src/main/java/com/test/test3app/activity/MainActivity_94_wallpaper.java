package com.test.test3app.activity;

import static androidx.recyclerview.selection.ItemKeyProvider.SCOPE_MAPPED;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.test.test3app.BaseActivity;
import com.test.test3app.CommonAdapter;
import com.test.test3app.CommonBean;
import com.test.test3app.CommonHolder;
import com.test.test3app.R;
import com.test.test3app.wallpaper.AdapterImageView;
import com.zhaoyuntao.androidutils.tools.S;

import java.util.HashSet;
import java.util.Set;

public class MainActivity_94_wallpaper extends BaseActivity {

    private AdapterImageView adapterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_94_wallpaper);

        adapterImageView = findViewById(R.id.wall);

        RecyclerView recyclerView2 = findViewById(R.id.recycler_view2);
        recyclerView2.setAdapter(new CommonAdapter(400));
        ImageView imageView = findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            boolean a = false;

            @Override
            public void onClick(View v) {
                a = !a;
                imageView.setImageResource(a ? R.drawable.a1 : R.drawable.a2);
            }
        });

        View actionbar=findViewById(R.id.actionbar);
        S.s("actionbar.getTop:"+actionbar.getTop());
        S.s("adapterImageView.getTop:"+adapterImageView.getTop());
        S.s("imageView.getTop:"+imageView.getTop());

        boolean reverseLayout = true;
        boolean stackFromEnd = false;

        RecyclerView recyclerView1 = findViewById(R.id.recycler_view1);
        CommonAdapter commonAdapter = new CommonAdapter(100);
        recyclerView1.setAdapter(commonAdapter);

        SelectionTracker<String> selectionTracker
                = new SelectionTracker.Builder<>(
                "Hello",
                recyclerView1,
                new ItemKeyProvider<String>(SCOPE_MAPPED) {
                    @Nullable
                    @Override
                    public String getKey(int position) {
                        return commonAdapter.getCurrentList().get(position).id;
                    }

                    @Override
                    public int getPosition(@NonNull String key) {
                        CommonBean commonBean = new CommonBean();
                        commonBean.id = key;
                        return commonAdapter.getCurrentList().indexOf(commonBean);
                    }
                },
                new ItemDetailsLookup<String>() {
                    @Nullable
                    @Override
                    public ItemDetails<String> getItemDetails(@NonNull MotionEvent e) {
                        View view = recyclerView1.findChildViewUnder(e.getX(), e.getY());
                        if (view != null) {
                            RecyclerView.ViewHolder viewHolder = recyclerView1.getChildViewHolder(view);
                            if (viewHolder instanceof CommonHolder) {
                                return new ItemDetails<String>() {
                                    @Override
                                    public int getPosition() {
                                        return viewHolder.getBindingAdapterPosition();
                                    }

                                    @Nullable
                                    @Override
                                    public String getSelectionKey() {
                                        return commonAdapter.getCurrentList().get(viewHolder.getBindingAdapterPosition()).id;
                                    }
                                };
                            }
                        }
                        return null;
                    }
                },
                StorageStrategy.createStringStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything()).build();
        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<String>() {
            @Override
            public void onItemStateChanged(@NonNull String key, boolean selected) {
                S.s("onItemStateChanged:key:" + key + " selected:" + selected);
            }

            @Override
            public void onSelectionRefresh() {
                S.s("onSelectionRefresh");
            }

            @Override
            public void onSelectionChanged() {
                S.s("onSelectionChanged");
            }

            @Override
            public void onSelectionRestored() {
                S.s("onSelectionRestored");
            }
        });

        commonAdapter.setSelectionTracker(selectionTracker);
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, CommonBean commonBean) {
                if (selectionTracker.isSelected(commonBean.id)) {
                    S.s("isSelected,be deselected" + commonBean.id);
                    selectionTracker.deselect(commonBean.id);
                } else {
                    S.e("!isSelected,be select");
                    selectionTracker.select(commonBean.id);
                }
            }
        });


        TextView button = findViewById(R.id.show);
        button.setOnClickListener(new View.OnClickListener() {
            boolean more;

            @Override
            public void onClick(View v) {
                if (more) {
                    commonAdapter.increase();
                } else {
                    commonAdapter.reduce();
                }
                more = !more;
            }
        });
        selectionTracker.getSelection();


//        Set<CommonBean> set = new HashSet<>();
//        set.add(new CommonBean("1",1));
//        set.add(new CommonBean("2",2));
//        set.add(new CommonBean("3",3));
//        S.lll();
//        for (CommonBean a : set) {
//            S.s("" + a.id+" "+a.resId);
//        }
//        S.lll();
//        set.add(new CommonBean("1",2));
//        for (CommonBean a : set) {
//            S.s("" + a.id+" "+a.resId);
//        }
//        S.lll();

        EditText editText=findViewById(R.id.testtest2);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TextView t=findViewById(R.id.testtest);
                t.setText(s);
            }
        });

    }
}
