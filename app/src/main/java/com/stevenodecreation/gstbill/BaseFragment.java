package com.stevenodecreation.gstbill;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

/**
 * Created by bhuvanesh on 01-07-2017.
 */

public class BaseFragment extends Fragment {

    private Dialog mProgressDialog;

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

    public void popAllFragmentUpTo(int upTo) {

        if (getActivity() != null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            int count = fm.getBackStackEntryCount();
            for (int i = count; i > upTo; i--)
                fm.popBackStack();
        }

    }

    protected void onBackPress() {
        pop();
    }

    public void showProgressDialog(String msg) {
        mProgressDialog = new Dialog(getActivity());
        mProgressDialog.setContentView(R.layout.layout_custom_dialog);
        mProgressDialog.setCancelable(false);
        TextView textViewMsg = mProgressDialog.findViewById(R.id.textview_status_msg);
        textViewMsg.setText(msg);
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
        mProgressDialog = null;
    }
}
