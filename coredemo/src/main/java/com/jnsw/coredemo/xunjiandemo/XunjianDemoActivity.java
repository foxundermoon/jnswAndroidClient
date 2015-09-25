package com.jnsw.coredemo.xunjiandemo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.annotation.UiThread;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.StickDrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.util.ArrayList;
import java.util.List;


@Fullscreen
@EActivity(R.layout.activity_xunjian_demo)
@WindowFeature({Window.FEATURE_NO_TITLE})
public class XunjianDemoActivity extends AppCompatActivity {
    @ViewById(R.id.right_container)
    ViewGroup rightContainer;
    @ViewById(R.id.xunjian_center)
    ViewGroup centerContainer;
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
    @FragmentById(R.id.right_second_fragment)
    Fragment rightSecondFragment;
    private Fragment currentCenterFragment;
    private Fragment currentRightFragment;
    List<Fragment>  rightFragments;
    List<Fragment>  bottomFragments;

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
        rightSecondFragment.getView().setVisibility(View.VISIBLE);
        CustomApplication.getInstance().eventBus.register(this);
        stickDrawerLayout.setScrimColor(Color.TRANSPARENT);
        stickDrawerLayout.setDrawerLockMode(StickDrawerLayout.LOCK_MODE_UNLOCKED);
        rightFragments = new ArrayList<>();
        rightFragments.add(rightFragment);
        rightFragments.add(rightSecondFragment);
        bottomFragments= new ArrayList<>();
        bottomFragments.add(bottomFragment);
        bottomFragments.add(anotherBottomFragment);

        showRightFragment(rightSecondFragment);
        stickDrawerLayout.setDrawerListener(new StickDrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (drawerView == rightContainer) {
                    rightContainer.getLayoutParams().width++;
                    rightContainer.requestLayout();
                    rightContainer.invalidate();
                    rightContainer.getLayoutParams().width--;
                }


//                if (drawerView == rightContainer) {
//                    drawerView.requestLayout();
//                    drawerView.invalidate();
//                }
            }


            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(@StickDrawerLayout.State int newState) {

            }
        });
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @AfterViews
    void initDefault() {
//        showRightFragment(currentRightFragment!=null?currentRightFragment: rightFragment);
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
                showBottomFragment(bottomFragment);
                break;
            case R.id.left_fragment_btn_5:
                showBottomFragment(anotherBottomFragment);
                break;
            case R.id.open_circular_menu:
                new OpenCircularActionButtonEvent(circularMenuFragment.imageView_center_main).post();
                break;
            case R.id.close_circular_menu:
                new CloseCircularActionMenuEvent(circularMenuFragment.imageView_center_main).post();
                break;
            case R.id.display_right_first_fragment:
                showRightFragment(rightFragment);
                break;
            case R.id.display_right_second_fragment:
                showRightFragment(rightSecondFragment);
                break;
            default:
                otherBtnClick();
        }
    }

    @UiThread
    private void showRightFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        for (Fragment right : rightFragments) {
            if (right == fragment) {
                transaction.show(fragment);
                right.getView().setVisibility(View.VISIBLE);
                rightContainer.getLayoutParams().width = right.getView().getLayoutParams().width;

            } else {
                transaction.hide(right);
                right.getView().setVisibility(View.GONE);

            }
        }
        transaction.commit();
        stickDrawerLayout.openDrawer(Gravity.END);
        currentCenterFragment = fragment;
    }

    @UiThread
    private void showBottomFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        for (Fragment bottom : bottomFragments) {
            if (bottom == fragment) {
                bottom.getView().setVisibility(View.VISIBLE);
                transaction.show(bottom);
            } else {
                transaction.hide(bottom);
            }
        }
        transaction.commit();
        currentCenterFragment = fragment;
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().show(fragment).commit();
    }

    private void otherBtnClick() {
        Tip.shortTip("you clicked me");
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
                    Tip.shortTip("clicked me " + id);
                    break;
                case R.id.imageView5:
                    Tip.shortTip("clicked me " + id);
                    break;
            }
        }
    }
}
