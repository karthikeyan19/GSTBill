package com.stevenodecreation.gstbill.clients.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Filter;

import com.stevenodecreation.gstbill.adapter.GenericAutoCompleteAdapter;
import com.stevenodecreation.gstbill.model.MyContacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lenovo on 08-01-2018.
 */

public class MyContactsAdapter extends GenericAutoCompleteAdapter<MyContacts> {


    public MyContactsAdapter(@NonNull Context context, @NonNull List<MyContacts> mContactsList) {
        super(context, mContactsList);
    }

    @Override
    protected void bindData(MyContacts item, ViewHolder holder) {
        holder.textViewName.setText(item.displayName);
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
                for (MyContacts contact : mOriginalList) {
                    // some contacts save with no name only no will display in contacts list
                    if (TextUtils.isEmpty(contact.displayName))
                        continue;
                    if (contact.displayName.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        mFilteredList.add(contact);
                    }
                }
                Collections.sort(mFilteredList);
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
            List<MyContacts> filterList = (ArrayList<MyContacts>) results.values;
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
