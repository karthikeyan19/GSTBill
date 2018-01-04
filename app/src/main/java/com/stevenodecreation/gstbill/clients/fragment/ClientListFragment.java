package com.stevenodecreation.gstbill.clients.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.stevenodecreation.gstbill.BaseFragment;
import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.clients.adapter.ClientListAdapter;
import com.stevenodecreation.gstbill.clients.manager.ClientManager;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.fragment.SearchHistoryFragment;
import com.stevenodecreation.gstbill.model.Client;
import com.stevenodecreation.gstbill.model.ClientSearchRequest;
import com.stevenodecreation.gstbill.widget.ErrorView;

import java.util.List;

/**
 * Created by lenovo on 15-11-2017.
 */

public class ClientListFragment extends BaseFragment implements ClientManager.OnGetClientListListener {

    private ClientListAdapter mClientListAdapter;
    private RecyclerView recyclerViewClientList;
    private RecyclerView.OnScrollListener mScrollListener;
    private SearchView mSearchView;
    private ErrorView mErrorView;

    private boolean isLoading;

    public static ClientListFragment newInstance() {
        return new ClientListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_list, container, false);
        setHasOptionsMenu(true);

        mErrorView = view.findViewById(R.id.errorview_msg);
        recyclerViewClientList = view.findViewById(R.id.recyclerview_client_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewClientList.setLayoutManager(layoutManager);

        if (mClientListAdapter == null)
            mClientListAdapter = new ClientListAdapter();
        recyclerViewClientList.setAdapter(mClientListAdapter);

        recyclerViewClientList.addOnScrollListener(mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll up
                    int visibleItemCount, totalItemCount, firstVisibleItemPos;
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    firstVisibleItemPos = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPos) >= totalItemCount) {
                        // condition true means, we reached last pos, so start do pagination
                        getClientList();
                    }
                }
            }
        });


        getClientList();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.menu_search_view).getActionView();
        mSearchView.setIconified(false);
        mSearchView.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mClientListAdapter.clearData();
                getClientList();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0) {
                    mClientListAdapter.clearData();
                    getClientList();
                }
                return true;
            }
        });
    }

    private void getClientList() {
        if (isLoading)
            return;
        isLoading = true;

        ClientSearchRequest request = new ClientSearchRequest();
        String queryText = mSearchView.getQuery().toString();   // helpful for pagination with query on search box
        if (!TextUtils.isEmpty(queryText)) {
            if (TextUtils.isDigitsOnly(queryText)) {
                request.mobileNo = queryText;
                request.searchType = ClientSearchRequest.SEARCH_BY_MOBILE;
            } else {
                request.name = queryText;
                request.searchType = ClientSearchRequest.SEARCH_BY_NAME;
            }
        }
        request.from = mClientListAdapter.getItemCount();

        if (request.from > 0) {
            mClientListAdapter.isFooterVisible(true);
        }
        ClientManager manager = new ClientManager();
        manager.getClientList(request, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search)
            replace(R.id.fragment_host, SearchHistoryFragment.newInstance());
        return true;
    }

    @Override
    public void OnGetClientListSuccess(List<Client> response) {
        mClientListAdapter.setData(response);
        mClientListAdapter.isFooterVisible(false);
        isLoading = false;
    }

    @Override
    public void OnGetClientListError(GstBillException exception) {
        isLoading = false;
        mClientListAdapter.isFooterVisible(false);
    }

    @Override
    public void onGetClientListEmpty() {
        isLoading = false;
        mClientListAdapter.isFooterVisible(false);
        recyclerViewClientList.removeOnScrollListener(mScrollListener);
    }
}
