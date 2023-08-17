package com.bulgarian.lawerapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulgarian.lawerapp.other.JsonUtil;
import com.bulgarian.lawerapp.model.caseinfo.ClientList;
import com.bulgarian.lawerapp.R;

import java.util.List;

public class ClientRecyclerViewAdapter extends RecyclerView.Adapter<ClientRecyclerViewAdapter.ViewHolder> {

    private List<ClientList> mData;
    private LayoutInflater mInflater;
    public Context context;

    public ClientRecyclerViewAdapter(Context context, List<ClientList> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_client, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClientList clientList = mData.get(position);
        holder.tvName.setText(clientList.getClient_name());
        holder.tvPhone.setText(clientList.getMobile());
        holder.tvEmail.setText(clientList.getEmail());

        holder.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonUtil.makeCall(context, clientList.getMobile());
            }
        });

        holder.ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonUtil.openEmail(context, clientList.getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvEmail;
        ImageView ivCall, ivEmail;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);

            ivCall = itemView.findViewById(R.id.ivCall);
            ivEmail = itemView.findViewById(R.id.ivEmail);

        }
    }
}