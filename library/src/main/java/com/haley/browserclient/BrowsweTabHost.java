package com.haley.browserclient;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;
import android.widget.TabWidget;

/**
 * Created by huanglei on 7/5/16.
 */
public class BrowsweTabHost extends FragmentTabHost {

    private int lastIndex = -1;

    public BrowsweTabHost(Context context) {
        super(context);
    }

    public BrowsweTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onTabChanged(String tabId) {
        try {
            super.onTabChanged(tabId);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                reSetTab();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        lastIndex = getCurrentTab();
    }

    private void reSetTab() {
        TabWidget tabWidget = getTabWidget();
        if (tabWidget != null) {
            tabWidget.focusCurrentTab(lastIndex);
        }
    }

    @Override
    public void setCurrentTab(int index) {
        super.setCurrentTab(index);
    }

}
