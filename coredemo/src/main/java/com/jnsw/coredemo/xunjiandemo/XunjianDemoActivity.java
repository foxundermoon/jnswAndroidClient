package com.jnsw.coredemo.xunjiandemo;

import android.app.Fragment;
import android.graphics.Color;
import android.support.annotation.UiThread;
import android.support.v4.widget.StickDrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.google.common.eventbus.Subscribe;
import com.jnsw.android.ui.widget.ImageTextButton;
import com.jnsw.android.ui.widget.event.CircularFloatingMenuClickEvent;
import com.jnsw.android.ui.widget.event.CloseCircularActionMenuEvent;
import com.jnsw.android.ui.widget.event.CloseStickDrawerLayoutEvent;
import com.jnsw.android.ui.widget.event.CloseableMenuGroupLayoutCloseEvent;
import com.jnsw.android.ui.widget.event.ImageTextButtonClickEvent;
import com.jnsw.android.ui.widget.event.OpenCircularActionButtonEvent;
import com.jnsw.android.ui.widget.event.OpenStickDrawerLayoutEvent;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.util.Tip;
import com.jnsw.coredemo.R;

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
    @ViewById(R.id.bottom_frame_layout)
    FrameLayout bottomFragmentContainer;
    @ViewById(R.id.xunjian_demo_root)
    StickDrawerLayout stickDrawerLayout;

    @FragmentById(R.id.bottom_fragment)
    Fragment bottomFragment;
    @FragmentById(R.id.another_bottom_fragment)
    Fragment anotherBottomFragment;

    @FragmentById(R.id.float_center_action_fragment)
    ExtendFloatCenterActionFragment circularMenuFragment;
    private Fragment currentCenterFragment;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_xunjian_demo, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        hideAllBottomFragment();
        CustomApplication.getInstance().eventBus.register(this);
        stickDrawerLayout.setScrimColor(Color.TRANSPARENT);
        stickDrawerLayout.setDrawerLockMode(StickDrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    private void hideAllBottomFragment() {
        getFragmentManager().beginTransaction().hide(bottomFragment).hide(anotherBottomFragment).commit();
        currentCenterFragment = null;
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
        hideAllBottomFragment();
//         Fragment  center =FloatCenterFragment_.builder().build();
//        getFragmentManager().beginTransaction().replace(R.id.center_float_container, center).commit();
    }

    @Background
    public void longTime() {
        opUI("DF");

    }

    @UiThread
    public void opUI(String sdsd) {

    }


    @Subscribe
    public void onImageTextButtonClick(ImageTextButtonClickEvent event) {
        ImageTextButton whitch = event.getEventData();
        int buttonViewId = whitch.getId();
        switch (buttonViewId) {
            case R.id.left_fragment_btn_1:
                clickBtn1();
                break;
            case R.id.left_fragment_btn_2:
                clickBtn2();
                break;
            case R.id.left_fragment_btn_3:
                clickBtn3();
                break;
            case R.id.left_fragment_btn_4:
                openBottomFragment();
                break;
            case R.id.left_fragment_btn_5:
                openAnotherBootomFragment();
                break;
            case R.id.open_circular_menu:
                new OpenCircularActionButtonEvent(circularMenuFragment.imageView_center_main).post();
                break;
            case R.id.close_circular_menu:
                new CloseCircularActionMenuEvent(circularMenuFragment.imageView_center_main).post();
                break;
            default:
                otherBtnClick();
        }
    }


    private void showBottomFragment(Fragment fragment) {
        showFragment(fragment);
        fragment.getView().setVisibility(View.VISIBLE);
        currentCenterFragment = fragment;
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().show(fragment).commit();
    }
    @UiThread
    private void openAnotherBootomFragment() {
        hideAllBottomFragment();
        showBottomFragment(anotherBottomFragment);
    }

    private void otherBtnClick() {
        Tip.shortTip("you clicked me");
    }

    @UiThread
    private void openBottomFragment() {
        hideAllBottomFragment();
        showBottomFragment(bottomFragment);
    }


    private void clickBtn3() {
        new OpenStickDrawerLayoutEvent(Gravity.END).post();
    }

    private void clickBtn2() {
        new CloseStickDrawerLayoutEvent(Gravity.START).post();
    }

    private void clickBtn1() {
        new CloseStickDrawerLayoutEvent(Gravity.START | Gravity.END).post();
    }

    @Subscribe
    public void onSubMenuClick(CircularFloatingMenuClickEvent event) {
        View which = event.getEventData();
        if (which != null) {
            int id = which.getId();
            switch (id) {
            case R.id.imageView4:
                Tip.shortTip("clicked me "+id);
                break;
                case R.id.imageView5:
                    Tip.shortTip("clicked me "+id);
                    break;
            }
        }
    }
}
