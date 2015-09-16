package com.jnsw.coredemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jnsw.coredemo.customview.CloseableMenuGroupDemoActivity_;
import com.jnsw.coredemo.customview.DragActivity_;
import com.jnsw.coredemo.customview.FloatActionInFragmentActivity;
import com.jnsw.coredemo.customview.FloatActionInFragmentActivity_;
import com.jnsw.coredemo.customview.FloatCenterFragmentActivity_;
import com.jnsw.coredemo.customview.StickDrawerLayoutDemo_;
import com.jnsw.coredemo.xunjiandemo.XunjianDemoActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_nav)
public class NavActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        return true;
    }
    @Click(R.id.LeftRightDrag)
    void LeftRightDrag(){
        DragActivity_.intent(this).start();
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
    @Click(R.id.stick_drawer_layout_demo)
    void  stickDrawerLayoutDemo(){
        StickDrawerLayoutDemo_.intent(this).start();
    }

    @Click
    void xunjian_demo(){
        XunjianDemoActivity_.intent(this).start();
    }
    @Click
    void closeable_group(){
        CloseableMenuGroupDemoActivity_.intent(this).start();
    }
    @Click void center_fragment_demo(){
        FloatCenterFragmentActivity_.intent(this).start();
    }

    @Click void float_action_in_fragment(){
        FloatActionInFragmentActivity_.intent(this).start();
    }

}
