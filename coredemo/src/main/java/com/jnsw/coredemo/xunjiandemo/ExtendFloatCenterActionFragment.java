package com.jnsw.coredemo.xunjiandemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jnsw.android.ui.widget.CircularFloatActionFragment;
import com.jnsw.core.util.L;
import com.jnsw.core.util.Tip;
import com.jnsw.coredemo.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by fox on 2015/9/17.
 */

@EFragment(R.layout.fragment_float_action)
public class ExtendFloatCenterActionFragment extends CircularFloatActionFragment {
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


    @Override
    protected void onInterceptCreateFloatingActionMenu(FloatingActionMenu.Builder floatingActionMenuBuilder) {

    }

    @Override
    protected List<View> onAddSubMenuView(List<View> subMenuList) {
        subMenuList.add(imageView4);
        subMenuList.add(imageView5);
        subMenuList.add(imageView6);
        subMenuList.add(imageView7);
        subMenuList.add(imageView8);
        return subMenuList;
    }

    @Override
    protected void onSetMainActionMenu() {
        setMainActionMenuView(imageView_center_main);
    }

    int click = 0;

    @AfterViews  void init(){
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tip.shortTip("imageView4");
                L.d(getClass(), "click = " + click++);
            }
        });
    }

    @Click
    void imageView6() {
        Tip.shortTip("imageView6");
        L.d(getClass(), "click = " + click++);
    }
}
