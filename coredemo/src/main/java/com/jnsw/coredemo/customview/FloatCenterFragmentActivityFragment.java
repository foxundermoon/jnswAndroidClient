package com.jnsw.coredemo.customview;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnsw.coredemo.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FloatCenterFragmentActivityFragment extends Fragment {

    public FloatCenterFragmentActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_float_center2, container, false);
    }
}
