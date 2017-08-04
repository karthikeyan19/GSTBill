package com.stevenodecreation.gstbill;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.stevenodecreation.gstbill.util.GBillLoggerUtil;


public class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;


    protected void setActionBar(int resId) {
        mToolbar = (Toolbar) findViewById(resId);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    public void setTitle(int resId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(resId);
        }
    }


    public void setBackEnabled(boolean enable) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
    }

    public void replace(int containerId, BaseFragment fragment) {
        replace(containerId, fragment, true);
    }


    /**
     * Method for replacing fragment
     *
     * @param containerId Container of the fragment
     * @param fragment    The fragment to place in the container
     */
    public void replace(int containerId, BaseFragment fragment, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(containerId, fragment, Integer.toString(getSupportFragmentManager().getBackStackEntryCount()));
        if (addToBackStack) ft.addToBackStack(null);
        ft.commit();
        fm.executePendingTransactions();
        GBillLoggerUtil.debug(BaseActivity.class.getSimpleName(), "BackStackEntryCount: " + fm.getBackStackEntryCount());
    }


    public void add(int containerId, BaseFragment fragment, boolean isFirstTime) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(containerId, fragment, Integer.toString(getSupportFragmentManager().getBackStackEntryCount()));
        if (isFirstTime) ft.addToBackStack(null);
        ft.commit();
    }

    public void remove(BaseFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }


    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count <= 1) {
            finish();
        } else {
            BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag((count - 1) + "");
            if (fragment != null) {
                fragment.onBackPress();
            }
        }
    }
}
