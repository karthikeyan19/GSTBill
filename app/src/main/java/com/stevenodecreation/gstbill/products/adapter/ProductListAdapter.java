package com.stevenodecreation.gstbill.products.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lenovo on 08-01-2018.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        public ProductViewHolder(View itemView) {
            super(itemView);
        }
    }
}
