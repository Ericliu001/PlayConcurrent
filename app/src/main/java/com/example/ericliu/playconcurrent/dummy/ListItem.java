package com.example.ericliu.playconcurrent.dummy;

import android.app.Activity;

/**
 * A list item representing a element in the list.
 */
public class ListItem {
    public final String content;
    public final Class<? extends Activity> activity;

    public ListItem(String content, Class<? extends Activity> activity) {
        this.content = content;
        this.activity = activity;
    }

    @Override
    public String toString() {
        return content;
    }
}
