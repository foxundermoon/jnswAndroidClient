package com.jnsw.android.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jnsw.android.ui.R;
import com.jnsw.core.util.ScreenKit;

/**
 * Created by fox on 2015/8/28.
 */
public class CloseableMenuGroupLayout extends ViewGroup {
    ImageButton closebtn;

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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int mViewGroupWidth = getMeasuredWidth() - ScreenKit.dip2px(40);  //当前ViewGroup的总宽度
        int mPainterPosX = l;  //当前绘图光标横坐标位置
        int mPainterPosY = t+ScreenKit.dip2px(20);  //当前绘图光标纵坐标位置

        setBackgroundResource(R.color.background_material_dark);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {

            View childView = getChildAt(i);
            if (childView.getId() == closebtn.getId()) {
                int  left = getWidth() -childView.getMeasuredWidth()-ScreenKit.dip2px(1);
                int top = ScreenKit.dip2px(1);
                int right = left +childView.getMeasuredWidth();
                int botom = top +childView.getMeasuredHeight();
                childView.layout(left,top,right,botom);
            } else {

                int width = childView.getMeasuredWidth();
                int height = childView.getMeasuredHeight();

                //如果剩余的空间不够，则移到下一行开始位置
                if (mPainterPosX + width > mViewGroupWidth) {
                    mPainterPosX = l;
                    mPainterPosY += height;
                }

                //执行ChildView的绘制
                childView.layout(mPainterPosX, mPainterPosY, mPainterPosX + width, mPainterPosY + height);

                //记录当前已经绘制到的横坐标位置
                mPainterPosX += width;
            }
        }
    }

    private void initCustomView(Context context, AttributeSet attrs) {
        inflaterLayout(context, attrs);

    }

    protected void inflaterLayout(Context context, AttributeSet attrs) {
        closebtn = (ImageButton) LayoutInflater.from(context).inflate(R.layout.closeable_button, null, false);
//        MarginLayoutParams layoutParams = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
////        Point screenSize = new Point();
////        ScreenKit.getScreenSize(screenSize);
//        int marginRight = ScreenKit.dip2px(10);  //getWidth();
//        int marginTop = ScreenKit.dip2px(10);
//        layoutParams.rightMargin = marginRight;
//        layoutParams.topMargin = marginTop;
//        addView(closebtn, layoutParams);
        closebtn.setBackgroundColor(Color.TRANSPARENT);
        addView(closebtn);
//        closebtn = (ImageButton) findViewById(R.id.imageButton_close);
    }

//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        int bw = closebtn.getMeasuredWidth();
//        int bh = closebtn.getMeasuredHeight();
//        int bx = getLeft() + getWidth() - 50;
//        int by = getTop() +5;
//
//        closebtn.layout(bx, by, bw, bh);
//}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        for (int index = 0; index < getChildCount(); index++) {
//            final View child = getChildAt(index);
//            child.measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);
//        }
        int measuredHeight = measureHeight(heightMeasureSpec);
//        int measuredWidth = mea
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureHeight(int heightMeasureSpec) {
        return 0;
    }
}
