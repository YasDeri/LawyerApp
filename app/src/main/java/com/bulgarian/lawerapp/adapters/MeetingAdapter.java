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
import android.widget.TextView;

import com.bulgarian.lawerapp.fragment.MeetingInfoFragment;
import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.model.MeetingData;
import com.bulgarian.lawerapp.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MyViewHolder> {

    private List<MeetingData> meetingDataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView client_name, time, date;
        public ImageView client_info;

        public MyViewHolder(View view) {
            super(view);
            client_name = (TextView) view.findViewById(R.id.client_name);
            time = (TextView) view.findViewById(R.id.time);
            date = (TextView) view.findViewById(R.id.date);
            client_info = (ImageView) view.findViewById(R.id.client_info);

        }
    }


    public MeetingAdapter(List<MeetingData> meetingDataList) {
        this.meetingDataList = meetingDataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meeting_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MeetingData meetingData = meetingDataList.get(position);

        holder.client_name.setText(meetingData.getName());
        holder.time.setText(meetingData.getEvent_time());
        holder.date.setText(meetingData.getEvent_date()
                .replaceAll("-", "."));
       holder.client_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = v.getContext().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                    editor.putString(AppsContants.MEETING_ID, meetingData.getId());
                    editor.putString(AppsContants.CLIENT_ID, meetingData.getClient_id());
                    editor.putString(AppsContants.CLIENT_NAME, meetingData.getName());
                    editor.putString(AppsContants.CLIENT_EMAIL, meetingData.getEmail());
                    editor.putString(AppsContants.CLIENT_MOBILE, meetingData.getMobile());
                    editor.putString(AppsContants.CLIENT_NOTES, meetingData.getNotes());
                    editor.putString(AppsContants.EVENT_DATE, meetingData.getEvent_date());
                    editor.putString(AppsContants.EVENT_TIME, meetingData.getEvent_time());
                    editor.putString(AppsContants.REM_EVENT_DATE, meetingData.getRemind_date());
                    editor.putString(AppsContants.REM_EVENT_TIME, meetingData.getRemind_time());
                    editor.putString(AppsContants.Meeting_place, meetingData.getPlace());
                    editor.apply();
                MeetingInfoFragment.comming_status = "1";
                NavigationActivity.mToolbar.setTitle("Информация за среща");
                NavigationActivity.mToolbar.setLogo(R.drawable.openfile_nav);
                Fragment fragment = new MeetingInfoFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

    }

    @Override
    public int getItemCount() {
        return meetingDataList.size();
    }
}