package com.jnsw.coredemo.customview;

import android.graphics.Color;
import android.support.v4.widget.StickDrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jnsw.coredemo.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

@Fullscreen
@WindowFeature({Window.FEATURE_NO_TITLE})
@EActivity(R.layout.activity_stick_drawer_layout_demo)
public class StickDrawerLayoutDemo extends AppCompatActivity {

    @ViewById(R.id.stick_drawer_layout_demo)
    StickDrawerLayout stickDrawerLayout;
    @ViewById(R.id.imageView3)
    ImageView  imageView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stick_drawer_layout_demo, menu);
        return true;
    }

    @AfterViews
    void init(){
        stickDrawerLayout.setScrimColor(Color.TRANSPARENT);
        stickDrawerLayout.setDrawerLockMode(StickDrawerLayout.LOCK_MODE_UNLOCKED);
        initButton();

        initMenu();

    }

    private void initMenu() {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageResource(R.drawable.abc_btn_default_mtrl_shape);

        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .attachTo(actionButton)
                .build();
    }

    @ViewById(R.id.left_container)
    LinearLayout leftContainer;
    private void initButton() {
        View leftView = stickDrawerLayout.findDrawerWithGravity(Gravity.LEFT);
        View rightView = stickDrawerLayout.findDrawerWithGravity(Gravity.RIGHT);
        for (int i = 0; i < 20; i++) {
//            StickDrawerLayout.LayoutParams params = new StickDrawerLayout.LayoutParams(this,null);
//            params.setMargins(0,10,00,10);
            Button btn = new Button(this);

            createSubMenuTo(btn);
            btn.setText("button "+i);
            btn.setGravity(Gravity.CENTER);
            leftContainer.addView(btn);
        }
    }

    private void createSubMenuTo(View btn) {
        ImageView rlIcon1 = new ImageView(this);
        ImageView rlIcon2 = new ImageView(this);
        ImageView rlIcon3 = new ImageView(this);
        ImageView rlIcon4 = new ImageView(this);

        rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_chat_light));
        rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_camera_light));
        rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_video_light));
        rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_place_light));

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
                .setStartAngle(-70)
                .setEndAngle(70)
                .attachTo(btn)
                .build();

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
}
