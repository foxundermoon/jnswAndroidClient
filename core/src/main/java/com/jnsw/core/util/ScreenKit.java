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
}
