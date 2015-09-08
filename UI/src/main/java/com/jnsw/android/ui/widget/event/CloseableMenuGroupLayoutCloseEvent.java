package com.jnsw.android.ui.widget.event;

import com.jnsw.android.ui.widget.CloseableMenuGroupLayout;
import com.jnsw.core.event.AppEvent;

/**
 * Created by fox on 2015/9/8.
 */
public class CloseableMenuGroupLayoutCloseEvent extends AppEvent<CloseableMenuGroupLayout> {
    public CloseableMenuGroupLayoutCloseEvent(CloseableMenuGroupLayout layout) {
        setEventData(layout);
    }
}
