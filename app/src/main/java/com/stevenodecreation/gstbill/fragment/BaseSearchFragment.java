package com.stevenodecreation.gstbill.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.stevenodecreation.gstbill.BaseFragment;
import com.stevenodecreation.gstbill.R;

/**
 * Created by Lenovo on 11/01/2018.
 */

public class BaseSearchFragment extends BaseFragment {

    protected SearchView searchView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search_view).getActionView();
        searchView.setIconified(false);
        searchView.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
    }
}
