package com.jnsw.android.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jnsw.android.ui.R;
import com.jnsw.android.ui.widget.event.CloseableMenuGroupLayoutCloseEvent;
import com.jnsw.core.util.ScreenKit;

/**
 * Created by fox on 2015/8/28.
 */
public class CloseableMenuGroupLayout extends RelativeLayout  {
    public ImageButton closebtn;
    static String rootViewTag = "closeable_layout_root_view";

    public CloseableMenuGroupLayout(Context context) {
        this(context, null);
    }

    public CloseableMenuGroupLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CloseableMenuGroupLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomView(context, attrs);
    }

    public CloseableMenuGroupLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCustomView(context, attrs);
    }

    private void initCustomView(Context context, AttributeSet attrs) {
        inflaterLayout(context, attrs);

    }

    protected void inflaterLayout(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.closeable_button, this, true);
        closebtn = (ImageButton) findViewById(R.id.imageButton_close);
        closebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new  CloseableMenuGroupLayoutCloseEvent(CloseableMenuGroupLayout.this).post();
            }
        });
    }
}
