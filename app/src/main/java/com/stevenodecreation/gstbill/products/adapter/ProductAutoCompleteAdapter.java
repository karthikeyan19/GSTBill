package com.stevenodecreation.gstbill.products.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filterable;

import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.adapter.GenericAutoCompleteAdapter;
import com.stevenodecreation.gstbill.model.Product;

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
}
