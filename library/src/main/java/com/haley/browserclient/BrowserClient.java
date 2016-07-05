package com.haley.browserclient;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by huanglei on 7/4/16.
 */
public class BrowserClient extends AppCompatActivity implements TabHost.OnTabChangeListener {

    public static int KEY_WEB_INDEX = 1;

    private TabHost tabs;

    public ArrayList<TabElement> tabElements = new ArrayList<TabElement>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser_client_view);

        tabs = (TabHost) findViewById(R.id.tabhost);
        tabs.setOnTabChangedListener(this);
        tabs.setup();

        newPageTab("百度", "http://www.baidu.com");
    }

    @Override
    public void onTabChanged(String tabId) {
        getTab(tabId);
    }

    public TabElement addTab(String name) {
        boolean isExist = false;
        for (TabElement element : tabElements) {
            if (name.equals(element.getTag())) {
                isExist = true;
                break;
            }
        }

        if (isExist) {
            if (tabElements.size() == 1) {
                return tabElements.get(0);
            }
            removeTab(name);
        }

        TabHost.TabSpec tab1 = tabs.newTabSpec(name);

        LayoutInflater inflater = LayoutInflater.from(this);

        View view = inflater.inflate(R.layout.tab, null);
        final TextView text = (TextView) view.findViewById(R.id.tab_label);
        text.setText(name);
        ImageButton btn = (ImageButton) view.findViewById(R.id.tab_btn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                removeTab(text.getText().toString());
            }
        });
        tab1.setIndicator(view);

        tab1.setContent(R.id.content1);

        tabs.addTab(tab1);

        TabElement element = new TabElement(name);
        element.tabspec = tab1;
        tabElements.add(element);

        tabs.setCurrentTabByTag(name);

        return element;
    }

    private TabElement getTab(String tabId) {
        TabElement dst = null;

        for (TabElement element : tabElements) {
            if (tabId.equals(element.getTag())) {
                dst = element;
                break;
            }
        }

        if (dst != null && dst.fragment != null) {
            loginFragment(dst.fragment);
        }

        return dst;
    }

    public void removeTab(String tabId) {
        if (tabElements.size() == 1)
            return;

        int index = -1;
        for (int i = 0; i < tabElements.size(); i++) {
            TabElement element = tabElements.get(i);
            if (tabId.equals(element.getTag())) {
                tabElements.remove(i);
                index = i;
                break;
            }
        }

        refreshTab();

        if (index > 1) {
            tabs.setCurrentTab(index - 1);
        }
    }

    private void refreshTab() {
        try {
            tabs.clearAllTabs();
            for (TabElement element : tabElements) {
                tabs.addTab(element.tabspec);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loginFragment(BroswerBaseFragment fragment) {
        if (!this.isFinishing()) {
            try {
                FragmentTransaction ft = getFragmentManager()
                        .beginTransaction();
                ft.replace(R.id.mainFrameLayout, fragment);
                ft.addToBackStack(fragment.tabElement.tabspec.getTag());
                ft.commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void popupFragment(BroswerBaseFragment fragment) {

        getFragmentManager().popBackStack();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(fragment.tabElement.tabspec.getTag());
        ft.commitAllowingStateLoss();
    }

    public void newPage(View view) {
        newPageTab("", "");
    }

    private void newPageTab(String tabName, String url) {
        if (tabName == null || tabName.length() == 0) {
            tabName = "WEB_" + KEY_WEB_INDEX++;
        }

        TabElement tab = addTab(tabName);
        BroswerWebFragment fragment = new BroswerWebFragment(tab);
        fragment.setUrl(url);
        loginFragment(fragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
