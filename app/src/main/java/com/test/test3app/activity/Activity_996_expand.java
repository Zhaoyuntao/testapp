package com.test.test3app.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.test.test3app.R;
import com.test.test3app.expandablelayoutdemo.AccordionFragment;
import com.test.test3app.expandablelayoutdemo.HorizontalFragment;
import com.test.test3app.expandablelayoutdemo.ManualFragment;
import com.test.test3app.expandablelayoutdemo.RecyclerViewFragment;
import com.test.test3app.expandablelayoutdemo.SimpleFragment;

public class Activity_996_expand extends AppCompatActivity {
    private static final String[] TAB_TITLES = {
            "Simple",
            "Accordion",
            "Recycler",
            "Horizontal",
            "Manual"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new simpleAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class simpleAdapter extends FragmentPagerAdapter {
        public simpleAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TAB_TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SimpleFragment();
                case 1:
                    return new AccordionFragment();
                case 2:
                    return new RecyclerViewFragment();
                case 3:
                    return new HorizontalFragment();
                case 4:
                    return new ManualFragment();
            }

            throw new IllegalStateException("There's no fragment for position " + position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }
    }
}
