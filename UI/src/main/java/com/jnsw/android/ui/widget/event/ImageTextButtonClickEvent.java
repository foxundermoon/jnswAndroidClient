package com.jnsw.android.ui.widget.event;


import com.jnsw.android.ui.widget.ImageTextButton;
import com.jnsw.core.event.AppEvent;

/**
 * Created by fox on 2015/9/18.
 */
public class ImageTextButtonClickEvent extends AppEvent<ImageTextButton> {
    public ImageTextButtonClickEvent(ImageTextButton imageTextButton) {
        setEventData(imageTextButton);
    }
}
