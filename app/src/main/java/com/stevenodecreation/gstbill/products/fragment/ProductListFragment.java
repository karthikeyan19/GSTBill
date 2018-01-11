package com.stevenodecreation.gstbill.products.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.stevenodecreation.gstbill.BaseFragment;
import com.stevenodecreation.gstbill.OnSubmitClickListener;
import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.clients.adapter.ClientListAdapter;
import com.stevenodecreation.gstbill.clients.fragment.ClientFragment;
import com.stevenodecreation.gstbill.exception.GstBillException;
import com.stevenodecreation.gstbill.fragment.BaseSearchFragment;
import com.stevenodecreation.gstbill.model.Product;
import com.stevenodecreation.gstbill.products.adapter.ProductListAdapter;
import com.stevenodecreation.gstbill.products.manager.ProductManager;
import com.stevenodecreation.gstbill.widget.ErrorView;

import java.util.List;


/**
 * Created by Lenovo on 08/01/2018.
 */
public class ProductListFragment extends BaseSearchFragment {

    private ErrorView mErrorView;
    private ProgressBar mProgressBar;
    private RecyclerView recyclerViewProductList;

    private ProductListAdapter mProductListAdapter;

    public static ProductListFragment newInstance() {
        return new ProductListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_client_list, container, false);

        mErrorView = view.findViewById(R.id.errorview_msg);
        mProgressBar = view.findViewById(R.id.progressbar);

        recyclerViewProductList = view.findViewById(R.id.recyclerview_prod_client_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewProductList.setLayoutManager(layoutManager);

        if (mProductListAdapter == null)
            mProductListAdapter = new ProductListAdapter();
        recyclerViewProductList.setAdapter(mProductListAdapter);

        mProductListAdapter.setmOnSubmitClickListener(new OnSubmitClickListener<Product>() {
            @Override
            public void onSubmitClicked(Product object) {
                replace(R.id.fragment_host, EditProductFragment.newInstance(Product.PRODUCT_UPDATE));
            }
        });

        getProductList();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void getProductList() {
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);

        ProductManager manager = new ProductManager();
        manager.getProductList("", new ProductManager.OnGetProductListListener() {
            @Override
            public void OnGetProductListSuccess(List<Product> response) {
                mProductListAdapter.setData(response);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void OnGetProductListError(GstBillException exception) {
                mProgressBar.setVisibility(View.GONE);
                mErrorView.setSubtitle(R.string.error_network_response);
                mErrorView.setRetryListener(new ErrorView.RetryListener() {
                    @Override
                    public void onRetry() {
                        getProductList();
                    }
                });
            }

            @Override
            public void onGetProductListEmpty() {
                mProgressBar.setVisibility(View.GONE);
                mErrorView.setSubtitle(R.string.msg_no_product);
                mErrorView.setRetryText(R.string.lbl_add_product);
                mErrorView.setRetryListener(new ErrorView.RetryListener() {
                    @Override
                    public void onRetry() {
                        replace(R.id.fragment_host, EditProductFragment.newInstance(Product.PRODUCT_UPDATE));
                    }
                });
            }
        });
    }
}