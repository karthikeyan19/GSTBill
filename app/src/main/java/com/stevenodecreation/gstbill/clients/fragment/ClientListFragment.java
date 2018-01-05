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
import android.support.v7.widget.SearchView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stevenodecreation.gstbill.BaseFragment;
import com.stevenodecreation.gstbill.OnSubmitClickListener;
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
    private ErrorView mErrorView;
    private ProgressBar mProgressBar;

    private boolean isLoading;
    private String mQueryText;

    public static ClientListFragment newInstance() {
        return new ClientListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_list, container, false);
        setHasOptionsMenu(true);

        mErrorView = view.findViewById(R.id.errorview_msg);
        mProgressBar = view.findViewById(R.id.progressbar);

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

        mClientListAdapter.setmOnSubmitClickListener(new OnSubmitClickListener<Client>() {
            @Override
            public void onSubmitClicked(Client object) {
                replace(R.id.fragment_host, ClientFragment.newInstance(object));
            }
        });

        getClientList();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search_view).getActionView();
        searchView.setIconified(false);
        searchView.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mQueryText = s;
                mClientListAdapter.clearData();
                getClientList();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0) {
                    mQueryText = s;
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
        if (!TextUtils.isEmpty(mQueryText)) {
            if (TextUtils.isDigitsOnly(mQueryText)) {
                request.mobileNo = mQueryText;
                request.searchType = ClientSearchRequest.SEARCH_BY_MOBILE;
            } else {
                request.name = mQueryText;
                request.searchType = ClientSearchRequest.SEARCH_BY_NAME;
            }
        }
        request.from = mClientListAdapter.getItemCount();

        if (request.from > 0) {
            mClientListAdapter.isFooterVisible(true);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            mErrorView.setVisibility(View.GONE);
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
        configErrorMsg("");
        configErrorMsg(getString(R.string.msg_no_results_found));

    }

    @Override
    public void OnGetClientListError(GstBillException exception) {
        configErrorMsg("");
        mErrorView.setSubtitle(R.string.error_network_response);
        mErrorView.setRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                getClientList();
            }
        });
    }

    @Override
    public void onGetClientListEmpty() {
        recyclerViewClientList.removeOnScrollListener(mScrollListener);
        configErrorMsg("");
        if (mClientListAdapter.getItemCount() > 0)  // for pagination empty response, we should not show any msg.
            return;
        if (TextUtils.isEmpty(mQueryText)) {
            configErrorMsg(getString(R.string.msg_no_client));
        } else {
            mErrorView.setRetryVisible(false);
            configErrorMsg(getString(R.string.msg_no_results_found));
        }
        mErrorView.setRetryText(R.string.menu_add_client);
        mErrorView.setRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                replace(R.id.fragment_host, ClientFragment.newInstance());
            }
        });
    }

    private void configErrorMsg(String errorMsg) {
        mClientListAdapter.isFooterVisible(false);
        mProgressBar.setVisibility(View.GONE);
        isLoading = false;
        mErrorView.setSubtitle(errorMsg);
    }
}
