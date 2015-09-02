package com.jnsw.core.util;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import com.jnsw.core.CustomApplication;

/**
 * Created by foxundermoon on 2015/7/23.
 */
public class ScreenKit {
    public static void getScreenSize(Point outSize) {
        ((WindowManager) CustomApplication.getInstance().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getSize(outSize);
    }

    public static Point getScreenSize() {
        Point point = new Point();
        getScreenSize(point);
        return point;
    }

    public static int getScreenSizeX() {
        return getScreenSize().x;
    }

    public static int getScreenSizeY() {
        return getScreenSize().y;
    }

    public static int dip2px(float dpValue) {
        final  float scale = CustomApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int)(dpValue *scale +0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = CustomApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
