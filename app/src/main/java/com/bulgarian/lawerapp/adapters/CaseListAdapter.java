package com.bulgarian.lawerapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulgarian.lawerapp.fragment.CaseListDetail;
import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.R;
import com.google.gson.Gson;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CaseListAdapter extends RecyclerView.Adapter<CaseListAdapter.ViewHolder> {

    Context context;

    List<CaseListGetSet> dataAdapters;

    public CaseListAdapter(List<CaseListGetSet> getDataAdapter, Context context) {

        //super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.case_list_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {
        CaseListGetSet dataAdapterOBJ = dataAdapters.get(position);
        Viewholder.case_no.setText(dataAdapterOBJ.getCaseNo() + "/" + dataAdapterOBJ.getYear());
        Viewholder.case_deadline.setText(dataAdapterOBJ.getDeadline()
                .replaceAll("-", "."));
        Viewholder.court_hearing.setText(dataAdapterOBJ.getHearing()
                .replaceAll("-", "."));
        Viewholder.client.setText(dataAdapterOBJ.getClientName());

        Viewholder.case_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = v.getContext().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                editor.putString(AppsContants.CaseId, dataAdapterOBJ.getId());
                editor.putString(AppsContants.CaseNum, dataAdapterOBJ.getCaseNo());
                editor.putString(AppsContants.CaseYear, dataAdapterOBJ.getYear());
                editor.apply();
                Bundle bundle = new Bundle();
                bundle.putString("CASE", new Gson().toJson(dataAdapterOBJ));
                CaseListDetail.comming_status = "1";
                NavigationActivity.mToolbar.setTitle("Информация за дело");
                Fragment fragment = new CaseListDetail();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView case_no;
        public TextView case_deadline;
        public TextView court_hearing;
        public TextView client;
        public ImageView case_info;

        public ViewHolder(View itemView) {
            super(itemView);
            case_no = (TextView) itemView.findViewById(R.id.case_no);
            case_deadline = (TextView) itemView.findViewById(R.id.case_deadline);
            court_hearing = (TextView) itemView.findViewById(R.id.court_hearing);
            client = (TextView) itemView.findViewById(R.id.client);
            case_info = (ImageView) itemView.findViewById(R.id.case_info);
        }
    }
}
