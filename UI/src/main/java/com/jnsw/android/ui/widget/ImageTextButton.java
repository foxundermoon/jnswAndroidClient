package com.jnsw.android.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jnsw.android.ui.R;

/**
 * Created by fox on 2015/8/27.
 */
public class ImageTextButton extends LinearLayout {
    private ImageView mImg;
    private TextView mTextView;
    private Context mContext =null;
    public ImageTextButton(Context context) {
        this(context,null);
    }

    public ImageTextButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomView(context, attrs);
    }

    public ImageTextButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCustomView(context, attrs);
    }

    private void initCustomView(Context context, AttributeSet attrs) {
        mContext = context;
        Resources.Theme  theme =context.getTheme();
        inflaterLayout(context);
        mImg = (ImageView) findViewById(R.id.imageView);
        mTextView = (TextView) findViewById(R.id.textView);
        mImg.setClickable(false);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageTextButton);
        if (typedArray == null) {
            return;
        }
        int count = typedArray.getIndexCount();
        int resId = 0;
        Drawable imgDrawable =null ;
        int imgWidth =-1;
        int imgHeight =-1;
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.ImageTextButton_text) {
                mTextView.setText(typedArray.getText(attr));
            }else if (attr == R.styleable.ImageTextButton_text_color) {
                mTextView.setTextColor(typedArray.getColor(attr, -1));
            }else if (attr == R.styleable.ImageTextButton_text_size) {
                mTextView.setTextSize(typedArray.getDimensionPixelSize(attr,-1));
            } else if (attr == R.styleable.ImageTextButton_image) {
                imgDrawable = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.ImageTextButton_image_width) {
                imgWidth = typedArray.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.ImageTextButton_image_height) {
                imgHeight = typedArray.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.ImageTextButton_image_max_height) {
                mImg.setMaxHeight(typedArray.getDimensionPixelSize(attr, -1));
            } else if (attr == R.styleable.ImageTextButton_image_max_width) {
                mImg.setMaxWidth(typedArray.getDimensionPixelSize(attr, -1));
            } else if (attr == R.styleable.ImageTextButton_image_min_height) {
                mImg.setMinimumHeight(typedArray.getDimensionPixelSize(attr, -1));
            } else if (attr == R.styleable.ImageTextButton_image_min_width) {
                mImg.setMinimumWidth(typedArray.getDimensionPixelSize(attr, -1));
            }
        }
        typedArray.recycle();
        if (imgDrawable != null) {
            imgDrawable.setBounds(0,0,imgWidth,imgHeight);
        }
//        mImg.setBackground(imgDrawable);
        mImg.setImageDrawable(imgDrawable);
    }

    protected void inflaterLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.image_text_button_layout, this, true);
    }
    @Override
    public void refreshDrawableState(){
        super.refreshDrawableState();
        //------------ ImageView控件的联动刷新 -------------------
        Drawable drawable = mImg.getDrawable();
        if (drawable != null && drawable.isStateful()) {
            //关键中的关键，根据当前状态设置drawable的状态，本来应该是LinearLayout的getDrawableState()状态，
            //但是现在是实现联动效果，而且获取的ImageView的getDrawState()结果不对。
            drawable.setState(this.getDrawableState());
        }
        ColorStateList mTextColor = mTextView.getTextColors();

        int color = mTextColor.getColorForState(this.getDrawableState(), 0);
        if (mTextView.getCurrentTextColor() != color) {
            mTextView.getPaint().setColor(color);
            mTextView.invalidate();
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        final  OnClickListener clickListener = l;
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                clickListener.onClick(v);

            }
        });
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        final OnTouchListener touchListener = l;
        super.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return touchListener.onTouch(v,event);
            }
        });
    }

    public void setImageResource(int resId) {
        mImg.setImageResource(resId);
    }

    public void setImageResource(Bitmap bitmap) {
        mImg.setImageBitmap(bitmap);
    }

    public void setTextViewText(int resId) {
        mTextView.setText(resId);
    }

    public void setTextViewText(CharSequence charSequence) {
        mTextView.setText(charSequence);
    }
}
