package com.jnsw.coredemo.xunjiandemo;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jnsw.core.util.L;
import com.jnsw.core.util.ScreenKit;
import com.jnsw.coredemo.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fox on 2015/9/16.
 */
@EFragment(R.layout.fragment_float_action)
public class FloatActionFragment extends Fragment {
    @ViewById
    ImageView imageView4;
    @ViewById
    ImageView imageView5;
    @ViewById
    ImageView imageView6;
    @ViewById
    ImageView imageView7;
    @ViewById
    ImageView imageView8;
    @ViewById
    ImageView imageView_center_main;

    List<View> subViews = new ArrayList<>();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewGroup root = (ViewGroup) getView();
        initAction();
    }

    public void setActionBackground(int resId) {
        imageView_center_main.setBackgroundResource(resId);
    }

    public void setActionBackground(Drawable drawable) {
        imageView_center_main.setBackground(drawable);
    }
    public void initAction() {
        ViewGroup root = (ViewGroup) getView();
        root.removeView(imageView_center_main);
        int redActionButtonSize = ScreenKit.dip2px(56); //getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        int redActionButtonMargin = ScreenKit.dip2px(0); //getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
        int redActionButtonContentSize = ScreenKit.dip2px(40); // getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = ScreenKit.dip2px(0);// //getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int redActionMenuRadius = ScreenKit.dip2px(70);// getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
        int blueSubActionButtonSize = ScreenKit.dip2px(48);// getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = ScreenKit.dip2px(16);//getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        starParams.setMargins(redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin);
        imageView_center_main.setLayoutParams(starParams);
        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconStarParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);

        final FloatingActionButton leftCenterButton = new FloatingActionButton.Builder(getActivity())
                .setContentView(imageView_center_main, fabIconStarParams)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .setPosition(FloatingActionButton.POSITION_TOP_CENTER)
                .setLayoutParams(starParams)
                .build();
        // Set up customized SubActionButtons for the right center menu
        SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(getActivity());
        lCSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue_selector));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);
        lCSubBuilder.setLayoutParams(blueContentParams);
        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        lCSubBuilder.setLayoutParams(blueParams);

        final FloatingActionMenu.Builder leftCenterMenuBuilder = new FloatingActionMenu.Builder(getActivity());
        for (View sub : subViews) {
            root.removeView(sub);
            leftCenterMenuBuilder.addSubActionView(lCSubBuilder.setContentView(sub, blueContentParams).build());
        }
        leftCenterMenuBuilder.setRadius(redActionMenuRadius)
                .setStartAngle(5)
                .setEndAngle(175)
                .attachTo(imageView_center_main)
                .build();
    }

    @AfterViews
    void init() {
        subViews.add(imageView4);
        subViews.add(imageView5);
        subViews.add(imageView6);
        subViews.add(imageView7);
        subViews.add(imageView8);
    }

    int click = 0;

    @Click(R.id.imageView_center_main)
    void mainAction() {
        click++;
        L.d(getClass(), "click " + click);
    }

    @Click(R.id.imageView4)
    void first() {
        click++;
        L.d(getClass(), "click " + click);
    }

    @Touch(R.id.imageView5)
    void img5() {
        click++;
        L.d(getClass(), "click " + click);
    }
}

