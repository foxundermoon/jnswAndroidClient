package com.jnsw.core.util;

import android.content.Context;
import android.widget.Toast;

import com.jnsw.core.CustomApplication;

/**
 * Created by foxundermoon on 2015/7/22.
 */
public class Tip {
    public static void shortTip(final String tip) {
        CustomApplication.getInstance().appHandle.post(new Runnable() {
            @Override
            public void run() {
                shortTip(CustomApplication.getInstance(), tip);
            }
        });
    }
    public static void longTip(final String tip) {
        CustomApplication.getInstance().appHandle.post(new Runnable() {
            @Override
            public void run() {
                longTip(CustomApplication.getInstance(), tip);
            }
        });
    }
    public static void shortTip(Context context,String tip) {
        Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
    }
    public static void longTip(Context context,String tip) {
        Toast.makeText(context,tip,Toast.LENGTH_LONG).show();
    }
}
