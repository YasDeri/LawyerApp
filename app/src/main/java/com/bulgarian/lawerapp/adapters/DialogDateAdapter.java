package com.bulgarian.lawerapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bulgarian.lawerapp.model.DialogData;
import com.bulgarian.lawerapp.R;

import java.util.List;

public class DialogDateAdapter extends RecyclerView.Adapter<DialogDateAdapter.MyViewHolder> {

    private List<DialogData> eventDataList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_1, tv_2, tv_3;

        public MyViewHolder(View view) {
            super(view);
            tv_1 = view.findViewById(R.id.tv_1);
            tv_2 = view.findViewById(R.id.tv_2);
            tv_3 = view.findViewById(R.id.tv_3);
        }
    }


    public DialogDateAdapter(List<DialogData> eventDataList, Context context) {
        this.eventDataList = eventDataList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_date_list_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DialogData eventData = eventDataList.get(position);

        holder.tv_1.setText(eventData.getSub_title_1());
        holder.tv_2.setText(eventData.getSub_title_2());
        holder.tv_3.setText(eventData.getSub_title_3());

    }

    @Override
    public int getItemCount() {
        if (eventDataList == null) return 0;
        return eventDataList.size();
    }
}