package com.jnsw.core.util;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jnsw.core.CustomApplication;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by fox on 2015/10/9.
 */
public class StickTip {
    private View contentView;
    private Toast toast;
    private boolean isShowed = false;
    private Object obj;
    private String tipMessage;

    public StickTip(String tipMessage) {
        this.tipMessage = tipMessage;
    }

    public StickTip(int layout) {
        setContentView(layout);
    }

    public StickTip(View view) {
        setContentView(view);
    }

    public String getTipMessage() {
        return tipMessage;
    }

    public void setTipMessage(String tipMessage) {
        this.tipMessage = tipMessage;
    }

    public boolean isShowed() {
        return isShowed;
    }

    public void setIsShowed(boolean isShowed) {
        this.isShowed = isShowed;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getxOffset() {
        return xOffset;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    private int gravity = Gravity.CENTER;
    private int xOffset = 0;
    private int yOffset = 0;

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public void setContentView(int layout) {
        contentView = LayoutInflater.from(CustomApplication.getInstance()).inflate(layout, null, false);
    }

    public void show() {
        if (contentView == null) {
            if (tipMessage == null) {
                Tip.shortTipCenter("此处为你的代码bug 调用show方法之前请先设置 contentView 或者 tipMessage");
                return;
            } else {
                toast = Toast.makeText(CustomApplication.getInstance(), tipMessage, Toast.LENGTH_SHORT);
            }
        }
        if (tipMessage == null && contentView!=null) {
            toast = new Toast(CustomApplication.getInstance());
            toast.setView(contentView);
        }
        toast.setGravity(gravity, xOffset, yOffset);
        try {
            Field field = toast.getClass().getDeclaredField("mTN");
            field.setAccessible(true);
            obj = field.get(toast);
            //TN对象中获得了show方法
            Method method = obj.getClass().getDeclaredMethod("show", null);
//调用show方法来显示Toast信息提示框

            Field mNextView = obj.getClass().getDeclaredField("mNextView");
            mNextView.setAccessible(true);
            mNextView.set(obj,toast.getView());
            method.invoke(obj, null);
            isShowed = true;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Tip.shortTip(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Tip.shortTip(e.getMessage());

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Tip.shortTip(e.getMessage());

        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Tip.shortTip(e.getMessage());
        }

    }

    public void hide() {
        if (isShowed) {
            Method method = null;
            try {
                method = obj.getClass().getDeclaredMethod("hide", null);
                method.invoke(obj, null);
                isShowed = false;
            } catch (NoSuchMethodException e) {
                Tip.shortTip(e.getMessage());
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                Tip.shortTip(e.getMessage());
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                Tip.shortTip(e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
