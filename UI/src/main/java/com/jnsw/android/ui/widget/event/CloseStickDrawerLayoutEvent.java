package com.jnsw.android.ui.widget.event;

import com.jnsw.core.event.AppEvent;

/**
 * Created by fox on 2015/9/18.
 */
public class CloseStickDrawerLayoutEvent extends AppEvent<Integer> {
    public CloseStickDrawerLayoutEvent(int which) {
        setEventData(which);
    }
}
