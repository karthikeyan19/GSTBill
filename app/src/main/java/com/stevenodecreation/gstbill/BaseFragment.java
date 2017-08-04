package com.stevenodecreation.gstbill;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by bhuvanesh on 01-07-2017.
 */

public class BaseFragment extends Fragment {
    public void replace(int containerId, BaseFragment fragment) {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).replace(containerId, fragment);
        }
    }

    public void replace(int containerId, BaseFragment fragment, boolean addToBackStack) {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).replace(containerId, fragment);
        }
    }

    public void pop() {
        if (getActivity() != null) {

            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();

        }
    }


    protected void onBackPress() {
        pop();
    }


}
