package com.haley.browserclient;

import android.widget.TabHost;

/**
 * Created by huanglei on 7/5/16.
 */
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
