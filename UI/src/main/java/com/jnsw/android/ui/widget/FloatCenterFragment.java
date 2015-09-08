package com.jnsw.android.ui.widget;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.jnsw.android.ui.R;

import java.util.zip.Inflater;

/**
 * Created by fox on 2015/9/8.
 */
public class FloatCenterFragment extends Fragment {
    private ImageButton closeBtn;
    private LinearLayout containerLayout;
    private View contentView;
    LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        contentView = inflater.inflate(R.layout.closeable_fragment_layout, container, false);
        containerLayout = (LinearLayout) contentView.findViewById(R.id.closeable_menu_group_layout_linelayout_container);
        closeBtn = (ImageButton) contentView.findViewById(R.id.imageButton_close);
        return contentView;
    }

    protected void setContentLayout(int layout) {
        inflater.inflate(layout,containerLayout,false);
    }
}
