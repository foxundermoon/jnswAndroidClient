package com.jnsw.coredemo.xunjiandemo;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnsw.coredemo.R;

import org.androidannotations.annotations.EFragment;

/**
 * A simple {@link Fragment} subclass.
 */
//@EFragment(R.layout.fragment_float_center)
    @EFragment(R.layout.new_float_center_layout)
public class BottomCenterFragment extends Fragment {
    private static  BottomCenterFragment instance;
    public BottomCenterFragment() {
        // Required empty public constructor
    }

    public synchronized static BottomCenterFragment getInstance() {
        if (instance == null) {
            instance = BottomCenterFragment_.builder().build();
        }
        return instance;
    }


}
