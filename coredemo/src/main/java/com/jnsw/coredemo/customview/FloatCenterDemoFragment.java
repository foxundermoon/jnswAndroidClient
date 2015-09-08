package com.jnsw.coredemo.customview;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnsw.android.ui.widget.FloatCenterFragment;
import com.jnsw.coredemo.R;

import org.androidannotations.annotations.EFragment;

@EFragment
public class FloatCenterDemoFragment extends FloatCenterFragment {

    public FloatCenterDemoFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setContentLayout(R.layout.fragment_float_center_demo);
    }
}
