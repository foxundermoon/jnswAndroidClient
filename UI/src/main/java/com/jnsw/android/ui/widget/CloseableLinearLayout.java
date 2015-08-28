package com.jnsw.android.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jnsw.android.ui.R;

/**
 * Created by fox on 2015/8/28.
 */
public class CloseableLinearLayout extends LinearLayout {
    public CloseableLinearLayout(Context context) {
        this(context, null);
    }

    public CloseableLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CloseableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomView(context, attrs);
    }

    public CloseableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCustomView(context, attrs);
    }

    private void initCustomView(Context context, AttributeSet attrs) {
        inflaterLayout(context);

    }
    protected void inflaterLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.closeable_lauout, this, true);
    }
}
