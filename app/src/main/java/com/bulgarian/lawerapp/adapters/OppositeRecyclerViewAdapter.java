package com.bulgarian.lawerapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulgarian.lawerapp.other.JsonUtil;
import com.bulgarian.lawerapp.model.caseinfo.RespondList;
import com.bulgarian.lawerapp.R;

import java.util.List;

public class OppositeRecyclerViewAdapter extends RecyclerView.Adapter<OppositeRecyclerViewAdapter.ViewHolder> {

    private List<RespondList> mData;
    private LayoutInflater mInflater;
    private Context context;

    public OppositeRecyclerViewAdapter(Context context, List<RespondList> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_respond, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RespondList respondList = mData.get(position);
        holder.tvName.setText(respondList.getName());
        holder.tvPhone.setText(respondList.getMobile());
        holder.tvLawyerPhone.setText(respondList.getLawyer_mobile());
        holder.tvEmail.setText(respondList.getLawyer_email());
        holder.tvLawyer.setText(respondList.getLawyer());

        holder.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonUtil.makeCall(context, respondList.getMobile());
            }
        });

        holder.ivLawyerCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonUtil.makeCall(context, respondList.getLawyer_mobile());
            }
        });

        holder.ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonUtil.openEmail(context, respondList.getLawyer_email());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvEmail, tvLawyer, tvLawyerPhone;
        ImageView ivCall, ivEmail, ivLawyerCall;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvLawyer = itemView.findViewById(R.id.tvLawyer);
            tvLawyerPhone = itemView.findViewById(R.id.tvLawyerPhone);

            ivCall = itemView.findViewById(R.id.ivCall);
            ivEmail = itemView.findViewById(R.id.ivEmail);
            ivLawyerCall = itemView.findViewById(R.id.ivLawyerCall);

        }
    }
}