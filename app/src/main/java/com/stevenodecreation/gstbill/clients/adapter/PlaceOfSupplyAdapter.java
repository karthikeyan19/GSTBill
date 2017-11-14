package com.stevenodecreation.gstbill.clients.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.model.PlaceOfSupply;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 12-10-2017.
 */

public class PlaceOfSupplyAdapter extends BaseAdapter {

    private List<PlaceOfSupply> mPosList = new ArrayList<>();

    public void setData(List<PlaceOfSupply> posList) {
        mPosList = posList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPosList != null ? mPosList.size() : 0;
    }

    @Override
    public PlaceOfSupply getItem(int i) {
        return mPosList != null ? mPosList.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        PosViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_dropdown, parent, false);
            holder = new PosViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (PosViewHolder) view.getTag();
        }
        holder.textViewName.setTag(position);

        PlaceOfSupply item = mPosList.get(position);
        holder.textViewName.setText(item.stateName);

        return view;
    }

    private class PosViewHolder {
        TextView textViewName;
        PosViewHolder(View itemView) {
            textViewName = itemView.findViewById(R.id.lbl_name);
        }
    }
}
