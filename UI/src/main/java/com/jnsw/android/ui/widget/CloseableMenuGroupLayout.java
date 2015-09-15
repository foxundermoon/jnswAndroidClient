package com.jnsw.android.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jnsw.android.ui.R;
import com.jnsw.android.ui.widget.event.CloseableMenuGroupLayoutCloseEvent;

/**
 * Created by fox on 2015/8/28.
 */
public class CloseableMenuGroupLayout extends FrameLayout {
    public ImageButton closebtn;
    static String rootViewTag = "closeable_layout_root_view";

    private LinearLayout linearLayoutContainer;
    private LinearLayout linearLayoutInScrollView;
    private HorizontalScrollView horizontalScrollView;
    private ViewGroup currentContainer;

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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int realWeidthMeasureSpec = widthMeasureSpec;
        int myWidthSize = MeasureSpec.getSize(widthMeasureSpec);

//        LinearLayout containerLayout = (LinearLayout) findViewWithTag(getResources().getString(R.string.closeable_menu_group_layout_linelayout_container));
        int totalWidth = 0;
        if (currentContainer == null) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != GONE && child != closebtn) {
//                    LayoutParams params = (LayoutParams) child.getLayoutParams();
                    final int childWidth = child.getMeasuredWidth();
                    totalWidth += childWidth;
                }
            }
            if (totalWidth == 0) {
                measureChildren(widthMeasureSpec, heightMeasureSpec);
                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    if (child.getVisibility() != GONE && child != closebtn) {
//                    LayoutParams params = (LayoutParams) child.getLayoutParams();
                        final int childWidth = child.getMeasuredWidth();
                        totalWidth += childWidth;
                    }
                }
            }
            if (totalWidth != 0) {
                if (totalWidth > myWidthSize) {
                    currentContainer = linearLayoutInScrollView;
                    if (horizontalScrollView.getVisibility() == GONE) {
                        horizontalScrollView.setVisibility(VISIBLE);
                    }
                } else {
                    currentContainer = linearLayoutContainer;
                    if (linearLayoutContainer.getVisibility() == GONE) {
                        linearLayoutContainer.setVisibility(VISIBLE);
                    }
                }
                childCount = getChildCount();
                View[] childs = new View[childCount];
                for (int i = 0; i < childCount; i++) {
                    childs[i] = getChildAt(i);
                }
                for (View child : childs) {
                    if (child.getVisibility() != GONE && child != closebtn && child != horizontalScrollView && child != linearLayoutContainer) {
//                    LayoutParams params = (LayoutParams) child.getLayoutParams();
                        final int childWidth = child.getMeasuredWidth();
//                        totalWidth += childWidth;
                        removeView(child);
                        currentContainer.addView(child);
                    }
                }
            }
        } else {
            int viewCount = currentContainer.getChildCount();
            for (int i = 0; i < viewCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
//                    LayoutParams params = (LayoutParams) child.getLayoutParams();
                    final int childWidth = child.getMeasuredWidth();
                    totalWidth += childWidth;
                }
            }
        }
        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
        int realWidthSize = Math.min(totalWidth, myWidthSize);
        realWeidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthModel, realWidthSize);
//        }

        super.onMeasure(realWeidthMeasureSpec, heightMeasureSpec);
    }

    protected void inflaterLayout(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.closeable_button, this, true);
        LayoutInflater.from(context).inflate(R.layout.linear_layout_container_layout, this, true);
        LayoutInflater.from(context).inflate(R.layout.horizontal_scroll_container_layout, this, true);

        linearLayoutContainer = (LinearLayout) findViewById(R.id.linear_layout_container_layout_linear_layout);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontal_scroll_container_horizontal_scroll_view);
        closebtn = (ImageButton) findViewById(R.id.imageButton_close);
        closebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new CloseableMenuGroupLayoutCloseEvent(CloseableMenuGroupLayout.this).post();
            }
        });
    }
}
