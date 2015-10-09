package com.jnsw.coredemo.xunjiandemo;


import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnsw.core.CustomApplication;
import com.jnsw.core.util.Tip;
import com.jnsw.coredemo.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_left)
public class LeftFragment extends Fragment {

    public LeftFragment() {
        // Required empty public constructor
    }
    @Click void click_me() {
        Tip.shortTip("自定义点击方法");

    }
//    @Click void set_title() {
//        XunjianDemoActivity activity = (XunjianDemoActivity) getActivity();
//        activity.curr
//    }


}


