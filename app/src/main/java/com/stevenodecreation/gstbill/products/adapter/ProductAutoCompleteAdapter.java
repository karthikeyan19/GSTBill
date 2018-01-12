package com.stevenodecreation.gstbill.products.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.adapter.GenericAutoCompleteAdapter;
import com.stevenodecreation.gstbill.model.MyContacts;
import com.stevenodecreation.gstbill.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lenovo on 08-01-2018.
 */

public class ProductAutoCompleteAdapter extends GenericAutoCompleteAdapter<Product> implements Filterable {


    public ProductAutoCompleteAdapter(@NonNull Context context, @NonNull List<Product> objects) {
        super(context, objects);
    }

    @Override
    protected void bindData(Product item, ViewHolder holder) {
        holder.textViewName.setText(item.productName);
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClicked(mContactsList.get((Integer) v.getTag()));
            }
        });
    }

    @Override
    public Filter getFilter() {
        return mFilterList;
    }

    private Filter mFilterList = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (!TextUtils.isEmpty(constraint)) {
                mFilteredList.clear();
                for (Product product : mOriginalList) {
                    // some contacts save with no name only no will display in contacts list
                    if (TextUtils.isEmpty(product.productName))
                        continue;
                    if (product.productName.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        mFilteredList.add(product);
                    }
                }
//                Collections.sort(mFilteredList);
                filterResults.values = mFilteredList;
                filterResults.count = mFilteredList.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Product> filterList = (ArrayList<Product>) results.values;
            if (results != null && results.count > 0) {
                mContactsList.clear();
                for (int i = 0; i < filterList.size(); i++) {
                    mContactsList.add(filterList.get(i));
                }
                notifyDataSetChanged();
            } else
                notifyDataSetInvalidated();
        }
    };
}
