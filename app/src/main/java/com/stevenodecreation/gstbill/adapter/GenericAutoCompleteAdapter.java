package com.stevenodecreation.gstbill.adapter;

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

public abstract class GenericAutoCompleteAdapter<T> extends ArrayAdapter<T> implements Filterable {

    public interface OnItemClickListener<T> {
        void onItemClicked(T item);
    }

    protected Context context;
    protected List<T> mContactsList, mOriginalList, mFilteredList;
    protected  OnItemClickListener mOnItemClickListener;


    public GenericAutoCompleteAdapter(@NonNull Context context, @NonNull List<T> mContactsList) {
        super(context, R.layout.item_dropdown, R.id.lbl_name, mContactsList);

        this.context = context;
        this.mContactsList = mContactsList;
        mOriginalList = new ArrayList<>(mContactsList); // this makes the difference.
        mFilteredList = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
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

        bindData(mContactsList.get(position), holder);


        return view;
    }

    protected abstract void bindData(T item, ViewHolder holder);

    public class ViewHolder {
        public TextView textViewName;

        ViewHolder(View itemView) {
            textViewName = itemView.findViewById(R.id.lbl_name);
        }
    }
}



