package com.jnsw.android.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jnsw.android.ui.R;
import com.jnsw.android.ui.widget.event.CloseableMenuGroupLayoutCloseEvent;
import com.jnsw.core.util.ScreenKit;

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
    private TextView title;
    private int totalMargin = ScreenKit.dip2px(0);

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CloseableMenuGroupLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCustomView(context, attrs);
    }

    public void setTitleColor(int color) {
        title.setTextColor(color);
    }

    public void setTitleText(CharSequence text) {
        if (text == null) {
            text = "";
        }
        title.setText(text);
    }

    private void initCustomView(Context context, AttributeSet attrs) {
        setClickable(true);
        Resources.Theme theme = context.getTheme();
        inflaterLayout(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CloseableMenuGroupLayoutStyleable);
        if (typedArray == null) {
            return;
        }
        int conut = typedArray.getIndexCount();
        int resId = 0;
        for (int i = 0; i < conut; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.CloseableMenuGroupLayoutStyleable_title_text) {
                CharSequence text = typedArray.getText(attr);
                if (text != null && text.length() > 0) {
                    title.setText(text);
                }
            } else if (attr == R.styleable.CloseableMenuGroupLayoutStyleable_title_color) {
                int color = typedArray.getColor(attr, -1);
                if (color != -1) {
                    title.setTextColor(color);
                }
            } else if (attr == R.styleable.CloseableMenuGroupLayoutStyleable_title_textSize) {
                int textSize = typedArray.getDimensionPixelSize(attr, -1);
                float fontSize = typedArray.getDimension(attr, -1);
                if (textSize != -1) {
                    title.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
                }
            }
        }

    }

    private boolean isUserAddedView(View child) {
        if (child == null) {
            return false;
        }
        return child != title && child != closebtn && child != closebtn && child != linearLayoutContainer && child != linearLayoutInScrollView && child != horizontalScrollView;
    }

    private boolean isVisibleUserAddedView(View child) {
        if (child == null) {
            return false;
        }
        return isUserAddedView(child) && child.getVisibility() != GONE;
    }

    private int containerWidth = 0;

//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        if (isFirst) {
//            requestLayout();
//            invalidate();
//            forceLayout();
//        }
//
//        isFirst = false;
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int realWidthMeasureSpec = widthMeasureSpec;
        int myWidthSize = MeasureSpec.getSize(widthMeasureSpec);

//        LinearLayout containerLayout = (LinearLayout) findViewWithTag(getResources().getString(R.string.closeable_menu_group_layout_linelayout_container));
        int totalWidth = 0;
        if (currentContainer == null) {
            int childCount = getChildCount();
            if (containerWidth == 0) {
                measureChildren(widthMeasureSpec, heightMeasureSpec);
                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    if (isVisibleUserAddedView(child)) {
                        final int childWidth = child.getMeasuredWidth();
                        FrameLayout.LayoutParams params = (LayoutParams) child.getLayoutParams();
                        totalWidth += childWidth + params.leftMargin + params.rightMargin;
                    }
                }
                containerWidth = totalWidth;
//                measure(widthMeasureSpec, heightMeasureSpec);
            } else {
                totalWidth = containerWidth;
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
                View[] children = new View[childCount];
                for (int i = 0; i < childCount; i++) {
                    children[i] = getChildAt(i);
                }
                for (View child : children) {
                    if (isVisibleUserAddedView(child)) {
                        if (child.getParent() == this) {
                            removeView(child);
                            currentContainer.addView(child);
                        }
                    }
                }
                FrameLayout.LayoutParams ll = (FrameLayout.LayoutParams) currentContainer.getLayoutParams();
                totalWidth = Math.max(containerWidth + ll.leftMargin + ll.rightMargin + totalMargin, totalWidth);
            }
        } else {          //currentContainer !=null
            FrameLayout.LayoutParams ll = (FrameLayout.LayoutParams) currentContainer.getLayoutParams();
            totalWidth = Math.max(containerWidth + ll.leftMargin + ll.rightMargin + totalMargin, totalWidth);
        }
        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
        int realWidthSize = Math.min(totalWidth, myWidthSize);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        String m = "";
        if (widthModel == MeasureSpec.AT_MOST) {
            m = "AT_MOST";
        }
        if (widthModel == MeasureSpec.EXACTLY) {
            m = "EXACTLY";
        }
        if (widthModel == MeasureSpec.UNSPECIFIED) {
            m = "UNSPECIFIED";
        }
        String mh = "";
        if (heightModel == MeasureSpec.AT_MOST) {
            mh = "AT_MOST";
        }
        if (heightModel == MeasureSpec.EXACTLY) {
            mh = "EXACTLY";
        }
        if (heightModel == MeasureSpec.UNSPECIFIED) {
            mh = "UNSPECIFIED";
        }
        realWidthMeasureSpec = MeasureSpec.makeMeasureSpec(realWidthSize, widthModel);
        super.onMeasure(realWidthMeasureSpec, heightMeasureSpec);
    }

    protected void inflaterLayout(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.closeable_button, this, true);
        LayoutInflater.from(context).inflate(R.layout.linear_layout_container_layout, this, true);
        LayoutInflater.from(context).inflate(R.layout.horizontal_scroll_container_layout, this, true);
        LayoutInflater.from(context).inflate(R.layout.closeable_container_title_text_view_layout, this, true);
        title = (TextView) findViewById(R.id.closeable_container_title_text_view);

        linearLayoutContainer = (LinearLayout) findViewById(R.id.linear_layout_container_layout_linear_layout);
        linearLayoutInScrollView = (LinearLayout) findViewById(R.id.horizontal_scroll_container_linearlayout);
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
