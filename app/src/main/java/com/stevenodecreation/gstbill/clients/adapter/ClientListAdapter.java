package com.stevenodecreation.gstbill.clients.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stevenodecreation.gstbill.OnSubmitClickListener;
import com.stevenodecreation.gstbill.R;
import com.stevenodecreation.gstbill.model.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 21-11-2017.
 */

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ClientViewHolder> {

    private List<Client> mClientList = new ArrayList<>();
    private OnSubmitClickListener<Client> mOnSubmitClickListener;
    private boolean mIsFooterVisible;

    public void setmOnSubmitClickListener(OnSubmitClickListener<Client> listener) {
        mOnSubmitClickListener = listener;
    }

    public void setData(List<Client> clients) {
        mClientList = clients;
        notifyDataSetChanged();
    }

    public void isFooterVisible(boolean footerVisible) {
        mIsFooterVisible = footerVisible;
        notifyItemChanged(getItemCount() - 1);
    }

    public void clearData() {
        mClientList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ClientListAdapter.ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ClientViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client, parent, false));
    }

    @Override
    public void onBindViewHolder(ClientListAdapter.ClientViewHolder holder, int position) {
        Client item = mClientList.get(position);
        holder.textViewName.setText(item.name);
        holder.textViewEmail.setText(item.emailId);
        holder.textViewMobileNo.setText(TextUtils.join(", ", item.phoneNoList));
        holder.textViewGstin.setText(item.gstinNo);

        if (position == getItemCount() - 1)
            holder.progressBar.setVisibility(mIsFooterVisible ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return mClientList != null ? mClientList.size() : 0;
    }

    class ClientViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewEmail;
        private TextView textViewMobileNo;
        private TextView textViewGstin;
        private ImageView imageViewEdit;
        private ProgressBar progressBar;

        public ClientViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textview_name);
            textViewEmail = itemView.findViewById(R.id.textview_email);
            textViewMobileNo = itemView.findViewById(R.id.textview_mobile_no);
            textViewGstin = itemView.findViewById(R.id.textview_gstin);
            imageViewEdit = itemView.findViewById(R.id.imageview_edit);
            progressBar = itemView.findViewById(R.id.prgress_bar);

            imageViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnSubmitClickListener != null)
                        mOnSubmitClickListener.onSubmitClicked(mClientList.get(getAdapterPosition()));
                }
            });
        }
    }
}
