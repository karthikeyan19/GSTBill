package com.stevenodecreation.gstbill.clients.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.stevenodecreation.gstbill.BaseFragment;
import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.clients.adapter.ClientListAdapter;
import com.stevenodecreation.gstbill.clients.manager.ClientManager;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.fragment.SearchHistoryFragment;
import com.stevenodecreation.gstbill.model.Client;

import java.util.List;

/**
 * Created by lenovo on 15-11-2017.
 */

public class ClientListFragment extends BaseFragment {

    private ClientListAdapter mClientListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_list, container, false);
        setHasOptionsMenu(true);

        RecyclerView recyclerViewClientList = view.findViewById(R.id.recyclerview_client_list);
        recyclerViewClientList.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mClientListAdapter == null)
            mClientListAdapter = new ClientListAdapter();
        recyclerViewClientList.setAdapter(mClientListAdapter);


        getClientList();
        return view;
    }

    private void getClientList() {
        ClientManager manager = new ClientManager();
        manager.getClientList(0, new ClientManager.OnGetClientListListener() {
            @Override
            public void OnGetClientListSuccess(List<Client> response) {
                mClientListAdapter.setData(response);
            }

            @Override
            public void OnGetClientListError(GstBillException exception) {

            }

            @Override
            public void onGetClientListEmpty() {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search)
            replace(R.id.fragment_host, SearchHistoryFragment.newInstance());
        return true;
    }
}
