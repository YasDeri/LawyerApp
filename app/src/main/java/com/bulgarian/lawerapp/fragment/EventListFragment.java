package com.bulgarian.lawerapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.other.URLHelper;
import com.bulgarian.lawerapp.adapters.EventAdapter;
import com.bulgarian.lawerapp.model.EventData;
import com.bulgarian.lawerapp.R;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class EventListFragment extends Fragment implements View.OnClickListener {
    ArrayList<EventData> allEvents = new ArrayList<>();
    SpinKitView spin_kit;
    EventAdapter eventAdapter;
    RecyclerView recyclerView;
    public static String comming_status = "";
    public String selectedDate = "", user_id = "";
    ImageView add_client, add_case;
    RelativeLayout add_meeting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        findViews(view);
        setListeners();

        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);

        selectedDate = prefs.getString(AppsContants.SELECTED_DATE, "");
        user_id = prefs.getString(AppsContants.USERID, "");
      //  getAllEvents(selectedDate);

        return view;
    }

    private void findViews(View view) {
        spin_kit = view.findViewById(R.id.spin_kit);
        recyclerView = view.findViewById(R.id.recycler_view);

        add_client = view.findViewById(R.id.add_client);
        add_case = view.findViewById(R.id.add_case);
        add_meeting = view.findViewById(R.id.add_meeting);

    }

    private void setListeners(){
        add_case.setOnClickListener(this);
        add_client.setOnClickListener(this);
        add_meeting.setOnClickListener(this);
    }

    public void getAllEvents(String selectedDate) {
        spin_kit.setVisibility(View.VISIBLE);
        AndroidNetworking.post(URLHelper.eventList)
                .addBodyParameter("user_id",user_id)
                .addBodyParameter("event_date", selectedDate)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        allEvents.add(new EventData(response.getJSONObject(i).getString("id"), response.getJSONObject(i).getString("event_date"), response.getJSONObject(i).getString("name"), response.getJSONObject(i).getString("event_time"), response.getJSONObject(i).getString("type"),  response.getJSONObject(i).getString("case_no"), response.getJSONObject(i).getString("year")));
                        Log.i("getrssp",response.toString());
                    } catch (Exception e) {
                        Log.i("excp",e.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }

                }
                spin_kit.setVisibility(View.GONE);
                eventAdapter = new EventAdapter(allEvents,getActivity());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(eventAdapter);
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ANError anError) {
                spin_kit.setVisibility(View.GONE);

                new AlertDialog.Builder(getActivity())
                        .setTitle("Alert !")
                        .setMessage(anError.getMessage())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }


    public void showSnackBar(View view, String message) {


        Snackbar snackbar = Snackbar.make(view, message,
                Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
        snackbar.show();


    }

    @Override
    public void onResume() {
        super.onResume();
        allEvents = new ArrayList<>();
        getAllEvents(selectedDate);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        int id = R.drawable.checklist_nav;

        switch (v.getId()) {

            case R.id.add_case:
                fragment = new ChatFragment();
                title = "Добави дело";
                id = R.drawable.checklist_nav;
                break;

            case R.id.add_client:
                AddClientFragment.comming_status = "0";
                fragment = new AddClientFragment();
                title = "Добави клиент";
                id = R.drawable.addclient_nav;
                break;

            case R.id.add_meeting:
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                editor.putString(AppsContants.CLIENT_ID, "");
                editor.putString(AppsContants.CLIENT_NAME, "");
                editor.putString(AppsContants.CLIENT_EMAIL, "");
                editor.putString(AppsContants.CLIENT_MOBILE, "");
                editor.putString(AppsContants.CLIENT_NOTES, "");
                editor.apply();

                fragment = new AddMeetingFragment();
                title = "Добави среща";
                id = R.drawable.add_meeting_nav;
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            NavigationActivity.mToolbar.setTitle(title);
            NavigationActivity.mToolbar.setLogo(id);
        }
    }
}
