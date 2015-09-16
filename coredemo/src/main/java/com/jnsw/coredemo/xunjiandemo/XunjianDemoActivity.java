package com.jnsw.coredemo.xunjiandemo;

import android.app.Fragment;
import android.graphics.Color;
import android.support.annotation.UiThread;
import android.support.v4.widget.StickDrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.google.common.eventbus.Subscribe;
import com.jnsw.android.ui.widget.event.CloseableMenuGroupLayoutCloseEvent;
import com.jnsw.core.CustomApplication;
import com.jnsw.coredemo.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;


@Fullscreen
@EActivity(R.layout.activity_xunjian_demo)
@WindowFeature({Window.FEATURE_NO_TITLE})
public class XunjianDemoActivity extends AppCompatActivity {
    @FragmentById(R.id.left_fragment)
    LeftFragment leftFragment;
    @FragmentById(R.id.right_fragment)
    RightFragment rightFragment;
    @FragmentById(R.id.center_float_container)
    Fragment currentCenterFragment;

    @ViewById(R.id.xunjian_demo_root)
    StickDrawerLayout stickDrawerLayout;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_xunjian_demo, menu);
        return true;
    }

    @AfterViews
    void init(){
        stickDrawerLayout.setScrimColor(Color.TRANSPARENT);
        stickDrawerLayout.setDrawerLockMode(StickDrawerLayout.LOCK_MODE_UNLOCKED);
        CustomApplication.getInstance().eventBus.register(this);
        stickDrawerLayout.openDrawer(Gravity.LEFT);
        stickDrawerLayout.closeDrawer(Gravity.START);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Subscribe
    public void onFloatCenterClose(CloseableMenuGroupLayoutCloseEvent closeableMenuGroupLayoutCloseEvent) {
//        getFragmentManager().beginTransaction().hide(currentCenterFragment).commit();
         Fragment  center =FloatCenterFragment_.builder().build();
        getFragmentManager().beginTransaction().replace(R.id.center_float_container, center).commit();
    }

    @UiThread
    @Subscribe
    public void leftRightManager(LeftRightManageMessage message) {
        if (message.open) {
            stickDrawerLayout.openDrawer(message.witch);
        }else {
            stickDrawerLayout.closeDrawer(message.witch);
        }
    }
    @Background
   public void longTime(){
        opUI("DF");

    }

    @UiThread
    public  void  opUI(String sdsd) {

    }
}
