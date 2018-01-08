package com.stevenodecreation.gstbill.products.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stevenodecreation.gstbill.OnSubmitClickListener;
import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.clients.adapter.ClientListAdapter;
import com.stevenodecreation.gstbill.model.Client;
import com.stevenodecreation.gstbill.model.Product;
import com.stevenodecreation.gstbill.util.TimeDateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 08-01-2018.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    private List<Product> mProductList = new ArrayList<>();
    private OnSubmitClickListener<Product> mOnSubmitClickListener;
    private boolean mIsFooterVisible;


    public void setmOnSubmitClickListener(OnSubmitClickListener<Product> listener) {
        mOnSubmitClickListener = listener;
    }

    public void setData(List<Product> clients) {
        mProductList = clients;
        notifyDataSetChanged();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product item = mProductList.get(position);
        holder.textViewProductName.setText(item.productName);
        holder.textViewQuantity.setText(String.valueOf(item.quantity));
        holder.textViewTimestamp.setText(TimeDateUtil.getFormattedString(item.lastUpdateTimestamp,
                TimeDateUtil.DATE_TIME_FORMAT_dd_MMM_yyyy));
    }

    @Override
    public int getItemCount() {
        return mProductList != null ? mProductList.size() : 0;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewProductName;
        private TextView textViewQuantity;
        private TextView textViewTimestamp;
        private ImageView imageViewEdit;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewProductName = itemView.findViewById(R.id.textview_name);
            textViewQuantity = itemView.findViewById(R.id.textview_email);
            textViewTimestamp = itemView.findViewById(R.id.textview_timestamp_product);
            imageViewEdit = itemView.findViewById(R.id.imageview_edit);

            imageViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnSubmitClickListener != null)
                        mOnSubmitClickListener.onSubmitClicked(mProductList.get(getAdapterPosition()));
                }
            });
        }
    }
}
