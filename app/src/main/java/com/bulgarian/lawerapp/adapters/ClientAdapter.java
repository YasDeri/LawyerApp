package com.bulgarian.lawerapp.adapters;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bulgarian.lawerapp.fragment.ChatFragment;
import com.bulgarian.lawerapp.fragment.ClientInfoFragment;
import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.fragment.AddMeetingFragment;
import com.bulgarian.lawerapp.model.ClientData;
import com.bulgarian.lawerapp.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.MyViewHolder> {

    private List<ClientData> clientDataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView client_name;
        public ImageView client_info, caseinfo;
        RelativeLayout add_relative;

        public MyViewHolder(View view) {
            super(view);
            client_name = (TextView) view.findViewById(R.id.client_name);
            client_info = (ImageView) view.findViewById(R.id.client_info);
            caseinfo = view.findViewById(R.id.caseinfo);
            add_relative = (RelativeLayout) view.findViewById(R.id.add_relative);

        }
    }


    public ClientAdapter(List<ClientData> clientDataList) {
        this.clientDataList = clientDataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ClientData clientData = clientDataList.get(position);

        holder.client_name.setText(clientData.getClient_name());

        holder.client_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = v.getContext().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                    editor.putString(AppsContants.CLIENT_ID, clientData.getId());
                    editor.putString(AppsContants.CLIENT_NAME, clientData.getClient_name());
                    editor.putString(AppsContants.CLIENT_EMAIL, clientData.getEmail());
                    editor.putString(AppsContants.CLIENT_MOBILE, clientData.getMobile());
                    editor.putString(AppsContants.CLIENT_NOTES, clientData.getNotes());
                    editor.apply();
                ClientInfoFragment.comming_status = "1";
                NavigationActivity.mToolbar.setTitle("Клиент");
                NavigationActivity.mToolbar.setLogo(R.drawable.client_nav);
                Fragment fragment = new ClientInfoFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }


        });

        holder.add_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = view.getContext().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                editor.putString(AppsContants.CLIENT_ID, clientData.getId());
                editor.putString(AppsContants.CLIENT_NAME, clientData.getClient_name());
                editor.putString(AppsContants.CLIENT_EMAIL, clientData.getEmail());
                editor.putString(AppsContants.CLIENT_MOBILE, clientData.getMobile());
                editor.putString(AppsContants.CLIENT_NOTES, clientData.getNotes());
                editor.apply();
                AddMeetingFragment.comming_status="3";
                NavigationActivity.mToolbar.setTitle("Добави среща");
                NavigationActivity.mToolbar.setLogo(R.drawable.add_meeting_nav);
                Fragment fragment = new AddMeetingFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        holder.caseinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = view.getContext().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                editor.putString(AppsContants.CLIENT_ID, clientData.getId());
                editor.putString(AppsContants.CLIENT_NAME, clientData.getClient_name());
                editor.putString(AppsContants.CLIENT_EMAIL, clientData.getEmail());
                editor.putString(AppsContants.CLIENT_MOBILE, clientData.getMobile());
                editor.putString(AppsContants.CLIENT_NOTES, clientData.getNotes());
                editor.apply();

                AddMeetingFragment.comming_status="1";
                NavigationActivity.mToolbar.setTitle("Добави дело");
                NavigationActivity.mToolbar.setLogo(R.drawable.add_meeting_nav);
                Fragment fragment = new ChatFragment();
                FragmentManager fragmentManager =
                        ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientDataList.size();
    }














}