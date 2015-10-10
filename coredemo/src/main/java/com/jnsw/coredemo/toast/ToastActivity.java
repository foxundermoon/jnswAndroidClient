package com.jnsw.coredemo.toast;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.jnsw.core.CustomApplication;
import com.jnsw.core.util.StickTip;
import com.jnsw.core.util.Tip;
import com.jnsw.coredemo.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.zip.Inflater;

@EActivity(R.layout.activity_toast)
public class ToastActivity extends AppCompatActivity {
    StickTip stickTip;

    @Click(R.id.custom_toast)
    void customToast() {
        Toast toast = new Toast(CustomApplication.getInstance());
        View root = LayoutInflater.from(this).inflate(R.layout.sample_toast, null, false);
        toast.setView(root);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    @Click(R.id.forever_toast)
    void forerverToast() {
//        View root = LayoutInflater.from(this).inflate(R.layout.sample_toast, null, false);
        if (stickTip == null)
            stickTip = new StickTip(R.layout.sample_toast);
        stickTip.show();

    }

    @Click(R.id.close_toast)
    void closeToast() {
        if (stickTip != null) {
            stickTip.hide();
            stickTip = null;
        }
        else
            Tip.shortTipCenter("请先打开才能关闭");
    }

    @Click
    void string_toast() {
        stickTip = new StickTip("string stick toast \n" +
                "  string stick toast \n" +
                "  string stick toast \n" +
                "  string stick toast \n" +
                "  ");
        stickTip.show();
    }
}

