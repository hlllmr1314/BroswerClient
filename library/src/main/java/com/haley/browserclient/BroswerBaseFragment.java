package com.haley.browserclient;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huanglei on 7/5/16.
 */
@SuppressLint("ValidFragment")
public class BroswerBaseFragment extends Fragment {

    public TabElement tabElement;
    public BroswerBaseFragment lastFragment = null;
    public boolean isLoaded = false;

    public void login(BroswerBaseFragment parent) {
        setTab(parent.tabElement);

        FragmentTransaction ft = parent.getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrameLayout, this);
        ft.commitAllowingStateLoss();
    }

    public BroswerBaseFragment() {
        super();
    }


    public BroswerBaseFragment(TabElement tab) {
        super();
        this.tabElement = tab;
        this.tabElement.fragment = this;
    }

    public void setLastFragment(BroswerBaseFragment fragment) {
        this.lastFragment = fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setTab(TabElement tab) {
        this.tabElement = tab;
        this.tabElement.fragment = this;
    }

    public void back() {
        if (lastFragment == null)
            return;
        lastFragment.login(this);
    }

}
