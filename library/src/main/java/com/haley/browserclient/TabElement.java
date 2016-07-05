package com.haley.browserclient;

import android.widget.TabHost;

public class TabElement {

    private String tag;
    public BroswerBaseFragment fragment;
    public TabHost.TabSpec tabspec;

    public TabElement(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

}
