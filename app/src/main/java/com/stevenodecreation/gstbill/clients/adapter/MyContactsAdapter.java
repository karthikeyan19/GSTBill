package com.stevenodecreation.gstbill.clients.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.model.MyContacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lenovo on 08-10-2017.
 */

public class MyContactsAdapter extends ArrayAdapter<MyContacts> implements Filterable {

    public interface OnItemClickListener {
        void onItemClicked(MyContacts item);
    }

    private Context context;
    private List<MyContacts> mContactsList, mOriginalList, mFilteredList;
    private  OnItemClickListener mOnItemClickListener;

    public MyContactsAdapter(@NonNull Context context, @NonNull List<MyContacts> mContactsList) {
        super(context, R.layout.item_dropdown, R.id.lbl_name, mContactsList);

        this.context = context;
        this.mContactsList = mContactsList;
        mOriginalList = new ArrayList<>(mContactsList); // this makes the difference.
        mFilteredList = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /*@Nullable
    @Override
    public MyContacts getItem(int position) {
        return (mContactsList != null && position < mContactsList.size()) ? mContactsList.get(position) : null;
    }*/

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_dropdown, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textViewName.setTag(position);

        MyContacts item = mContactsList.get(position);
        holder.textViewName.setText(item.displayName);
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClicked(mContactsList.get((Integer) v.getTag()));
            }
        });

        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    public Filter nameFilter = new Filter() {

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

    class ViewHolder {
        TextView textViewName;

        ViewHolder(View itemView) {
            textViewName = itemView.findViewById(R.id.lbl_name);
        }
    }
}



