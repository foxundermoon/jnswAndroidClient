package com.jnsw.android.ui.widget;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.common.eventbus.Subscribe;
import com.jnsw.android.ui.R;
import com.jnsw.android.ui.widget.event.CircularFloatingMenuClickEvent;
import com.jnsw.android.ui.widget.event.CloseCircularActionMenuEvent;
import com.jnsw.android.ui.widget.event.OpenCircularActionButtonEvent;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.util.ScreenKit;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fox on 2015/9/17.
 */

public abstract class CircularFloatActionFragment extends Fragment {
    protected View mainActionMenuView;
    protected List<View> subMenuList = new ArrayList<>();
    protected int mainActionButtonSize = ScreenKit.dip2px(56);
    protected int mainActionButtonMargin = ScreenKit.dip2px(0);
    protected int mainActionButtonContentSize = ScreenKit.dip2px(40);
    protected int mainActionButtonContentMargin = ScreenKit.dip2px(0);//
    protected int actionMenuRadius = ScreenKit.dip2px(70);//
    protected int subActionButtonSize = ScreenKit.dip2px(48);//
    protected int subActionButtonContentMargin = ScreenKit.dip2px(10);//
    protected int startAngle = 0;
    protected int endAngle = 180;
    protected int subMenuBackGround = R.drawable.button_action_blue_selector;
    protected int mainMenuBackGround = R.drawable.button_action_red_selector;

    private FloatingActionMenu mainActionMenu;

    public void setMainMenuPosition(int mainMenuPosition) {
        this.mainMenuPosition = mainMenuPosition;
    }

    private int mainMenuPosition = FloatingActionButton.POSITION_TOP_CENTER;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeFloatingActionMenu();
    }

    private FloatingActionButton.LayoutParams defaultMainMenuIconLayoutParams;

    private void initializeFloatingActionMenu() {
        CustomApplication.getInstance().eventBus.register(this);
        FrameLayout.LayoutParams subMenuContentLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        subMenuContentLayoutParams.setMargins(subActionButtonContentMargin,
                subActionButtonContentMargin,
                subActionButtonContentMargin,
                subActionButtonContentMargin);
        FloatingActionButton.LayoutParams mainMenuLayoutParams = new FloatingActionButton.LayoutParams(mainActionButtonSize, mainActionButtonSize);
        mainMenuLayoutParams.setMargins(mainActionButtonMargin,
                mainActionButtonMargin,
                mainActionButtonMargin,
                mainActionButtonMargin);

        FrameLayout.LayoutParams subMenuLayoutParams = new FrameLayout.LayoutParams(subActionButtonSize, subActionButtonSize);

        FloatingActionButton.LayoutParams mainMenuIconLayoutParams = new FloatingActionButton.LayoutParams(mainActionButtonContentSize, mainActionButtonContentSize);
        mainMenuIconLayoutParams.setMargins(mainActionButtonContentMargin,
                mainActionButtonContentMargin,
                mainActionButtonContentMargin,
                mainActionButtonContentMargin);

        onSetMainActionMenu();
        if (mainActionMenuView == null) {
            throw new RuntimeException("必须设置  MainMenu View");
        }
        ViewGroup root = (ViewGroup) getView();
        root.removeView(mainActionMenuView);
        clearSubViews();
        onAddSubMenuView(subMenuList);
        if (subMenuList.size() < 1) {
            throw new RuntimeException("至少要设置一个 SubMenu View");
        }
        final FloatingActionButton leftCenterButton = new FloatingActionButton.Builder(getActivity())
                .setContentView(mainActionMenuView, mainMenuIconLayoutParams)
                .setBackgroundDrawable(mainMenuBackGround)
                .setPosition(mainMenuPosition)
                .setLayoutParams(mainMenuLayoutParams)
                .build();

        SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(getActivity());
        lCSubBuilder.setBackgroundDrawable(getResources().getDrawable(subMenuBackGround));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(subActionButtonContentMargin,
                subActionButtonContentMargin,
                subActionButtonContentMargin,
                subActionButtonContentMargin);
//        lCSubBuilder.setLayoutParams(blueContentParams);
        lCSubBuilder.setLayoutParams(subMenuLayoutParams);

        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(subActionButtonSize, subActionButtonSize);
        lCSubBuilder.setLayoutParams(blueParams);


        final FloatingActionMenu.Builder leftCenterMenuBuilder = new FloatingActionMenu.Builder(getActivity());

        for (final View subView : subMenuList) {
            root.removeView(subView);
            SubActionButton sb = lCSubBuilder.setContentView(subView, blueContentParams).build();
            sb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CircularFloatingMenuClickEvent(subView).post();
                }
            });
            leftCenterMenuBuilder.addSubActionView(sb);
        }
        leftCenterMenuBuilder
                .setRadius(actionMenuRadius)
                .setStartAngle(startAngle)
                .setEndAngle(endAngle);
        onInterceptCreateFloatingActionMenu(leftCenterMenuBuilder);
        mainActionMenu = leftCenterMenuBuilder.attachTo(mainActionMenuView).build();
    }


    public boolean isMenuOpen(){
        if(mainActionMenu !=null)
            return mainActionMenu.isOpen();
        return false;
    }
    protected abstract void onInterceptCreateFloatingActionMenu(FloatingActionMenu.Builder floatingActionMenuBuilder);

    protected abstract List<View> onAddSubMenuView(List<View> subMenuList);

    protected abstract void onSetMainActionMenu();


    protected void setMainActionMenuView(View mainActionMenuView) {
        this.mainActionMenuView = mainActionMenuView;
    }

    public void setActionMenuStateChangeListener(FloatingActionMenu.MenuStateChangeListener stateChangeListener) {
        if (mainActionMenu != null)
            mainActionMenu.setStateChangeListener(stateChangeListener);
    }

    protected void clearSubViews() {
        subMenuList.clear();
    }

    @Subscribe
    public void onOpenCircularFloatingActionMenu(OpenCircularActionButtonEvent event) {
            if( event.getEventData() == mainActionMenuView)
                openMenu();
    }

    @Subscribe
   public void onCloseCircularFloatingActionMenu(CloseCircularActionMenuEvent event) {
        if (event.getEventData() == mainActionMenuView) {
            closeMenu();
        }
    }

    private void closeMenu() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActionMenu.close(true);
            }
        });
    }

    private void openMenu() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActionMenu.open(true);
            }
        });
    }

}
