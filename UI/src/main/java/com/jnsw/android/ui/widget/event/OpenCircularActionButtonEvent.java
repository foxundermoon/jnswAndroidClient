package com.jnsw.android.ui.widget.event;

import android.view.View;

import com.jnsw.core.event.AppEvent;

/**
 * Created by fox on 2015/9/20.
 */
public class OpenCircularActionButtonEvent extends AppEvent<View> {
    public OpenCircularActionButtonEvent(View mainView) {
        setEventData(mainView);
    }
}
