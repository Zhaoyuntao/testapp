package com.test.test3app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class CallFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_call_normal_phone_tips_actions, null);
        return mRootView;
    }
}
