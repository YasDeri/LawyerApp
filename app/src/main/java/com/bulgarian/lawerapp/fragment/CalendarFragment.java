package com.bulgarian.lawerapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.other.URLHelper;
import com.bulgarian.lawerapp.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.marcohc.robotocalendar.RobotoCalendarView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;


public class CalendarFragment extends Fragment implements View.OnClickListener, RobotoCalendarView.RobotoCalendarListener {
    ImageView case_list, addCase;
    ImageView clientList, addClient;
    ImageView meetingList, addMeeting;
    ImageView archieve;
    RobotoCalendarView robotoCalendarView;
    SpinKitView spin_kit;
    String selectedMonth = "";
    String userid = "";
    Calendar calendar;
    int section = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Locale locale = new Locale("bg","BG");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);

        calendar = Calendar.getInstance();

        section = calendar.get(Calendar.DAY_OF_MONTH);

        userid = prefs.getString(AppsContants.USERID, "");
        //Initialize CustomCalendarView from layout
        // Gets the calendar from the view
        case_list = (ImageView) view.findViewById(R.id.case_list);
        addCase = (ImageView) view.findViewById(R.id.addCase);

        clientList = (ImageView) view.findViewById(R.id.clientList);
        addClient = (ImageView) view.findViewById(R.id.addClient);

        archieve = (ImageView) view.findViewById(R.id.archieve);
        meetingList = (ImageView) view.findViewById(R.id.meetingList);
        addMeeting = (ImageView) view.findViewById(R.id.addMeeting);

        spin_kit = (SpinKitView) view.findViewById(R.id.spin_kit);

        robotoCalendarView = (view).findViewById(R.id.robotoCalendarPicker);
        Button markDayButton = (view).findViewById(R.id.markDayButton);
        Button clearSelectedDayButton = (view).findViewById(R.id.clearSelectedDayButton);


        markDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Random random = new Random(System.currentTimeMillis());
                int style = random.nextInt(2);
                int daySelected = random.nextInt(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.DAY_OF_MONTH, daySelected);

                switch (style) {
                    case 0:
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        String inActiveDate = null;
                        inActiveDate = format1.format(calendar.getTime());


                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date d = dateFormat.parse(inActiveDate);
                            robotoCalendarView.markCircleImage1(d);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        break;
                    case 1:
                        robotoCalendarView.markCircleImage2(calendar.getTime());
                        break;
                    default:
                        break;
                }
            }
        });


        // Set listener, in this case, the same activity
        robotoCalendarView.setRobotoCalendarListener(this);

        robotoCalendarView.setShortWeekDays(false);

        robotoCalendarView.showDateTitle(true);

        robotoCalendarView.setDate(new Date());


        case_list.setOnClickListener(this);
        addCase.setOnClickListener(this);

        if (section >= 12) {
            prefs.edit().putBoolean(AppsContants.Meeting_Last_Visit, false).apply();
        }

        addMeeting.setOnClickListener(this);
        meetingList.setOnClickListener(this);

        addClient.setOnClickListener(this);
        clientList.setOnClickListener(this);

        archieve.setOnClickListener(this);

        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MM");
        System.out.println(dateFormatGmt.format(new Date()) + "");
        Log.e("gfdjgfd", dateFormatGmt.format(new Date()));
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.case_list:
                CaseListFragment.comming_status = "1";
                Fragment fragmentsss = new CaseListFragment();
                switchCaseFragment("Списък дела", fragmentsss);
                // Toast.makeText(getActivity(), "Case List", Toast.LENGTH_SHORT).show();
                break;

            case R.id.addCase:
                ChatFragment.comming_status = "1";
                Fragment fragments = new ChatFragment();
                switchCaseFragment("Добави дело", fragments);
                // Toast.makeText(getActivity(), "Add Case", Toast.LENGTH_SHORT).show();
                break;


            case R.id.meetingList:
                MeetinglistFragment.comming_status = "1";
                Fragment fragment = new MeetinglistFragment();
                switchFragment(R.drawable.meeting_nav, "Списък срещи", fragment);
                break;

            case R.id.addMeeting:
                AddMeetingFragment.comming_status = "1";
                Fragment fragmnt = new AddMeetingFragment();
                switchFragment(R.drawable.meeting_nav, "Добави среща", fragmnt);
                break;

            case R.id.clientList:
                ClientListFragment.comming_status = "1";
                Fragment fragmentss = new ClientListFragment();
                switchFragment(R.drawable.client_list, "Списък клиенти", fragmentss);
                break;


            case R.id.addClient:
                AddClientFragment.comming_status = "1";
                Fragment clientListFragment = new AddClientFragment();
                switchFragment(R.drawable.client_list, "Добави клиент", clientListFragment);
                break;

            case R.id.archieve:

                CaseListFragment.comming_status = "1";
                Fragment fragmentssss = new CaseListFragment();
                switchCaseFragment("Архив", fragmentssss);

              /*  AddClientFragment.comming_status = "1";
                Fragment clientListFragment = new AddClientFragment();
                switchFragment(R.drawable.client_list, "Client List", clientListFragment);*/
                break;
            default:
                break;
        }
    }

    public void switchFragment(int drawable, String title, Fragment fragment) {
        NavigationActivity.mToolbar.setTitle(title);
        NavigationActivity.mToolbar.setLogo(drawable);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void switchCaseFragment(String title, Fragment fragment) {
        NavigationActivity.mToolbar.setTitle(title);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onDayClick(Date date) {
        int month = (date.getMonth() + 1);
        int year = date.getYear();
        int day = date.getDate();
        String s_month = "";
        String s_day = "";
        String s_year = "";
        if (month < 10) {
            s_month = "0" + month + "";
        } else {
            s_month = month + "";
        }
        if (day < 10) {
            s_day = "0" + day + "";
        } else {
            s_day = day + "";
        }
        s_year = year + "";

        String formatDate = s_day + "." + s_month + "." + "20" + s_year.substring(1);
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
        editor.putString(AppsContants.SELECTED_DATE, formatDate);
        editor.commit();
        EventListFragment.comming_status = "1";

        NavigationActivity.mToolbar.setTitle("Списък задачи");
        NavigationActivity.mToolbar.setLogo(R.drawable.checklist_nav);
        Fragment fragment = new EventListFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        //   Log.e("fdjfdnf", formatDate);
        // Log.e("Fdkfjdksf",date.toString().replaceAll("GMT+05:30","")+"");


/*
        String getDate = date.toString().replace(" GMT+05:30", "");


        Log.e("Fdsjfd", getDate.substring(4));

        DateFormat srcDf = new SimpleDateFormat("MMM dd HH:mm:ss yyyy");
        Date newDate = null;
        // parse the date string into Date object
        try {
            newDate = srcDf.parse(getDate.substring(4));
            Log.e("Fdsjfd", newDate + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat destDf = new SimpleDateFormat("dd.MM.yyyy");


        System.out.println("Converted date is : " + destDf.format(newDate));

*/

    }

    @Override
    public void onDayLongClick(Date date) {
        Toast.makeText(getActivity(), "onDayLongClick: " + date, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onRightButtonClick() {
        Toast.makeText(getActivity(), "onRightButtonClick!", Toast.LENGTH_SHORT).show();
        getAllEvents(userid);
    }

    @Override
    public void onLeftButtonClick() {
        Toast.makeText(getActivity(), "onLeftButtonClick!", Toast.LENGTH_SHORT).show();
        getAllEvents(userid);

    }

    public void getAllEvents(String user_id) {
        int month = (robotoCalendarView.getDate().getMonth()) + 1;
        String s_month = "";
        if (month < 10) {
            s_month = "0" + month + "";
        } else {
            s_month = month + "";
        }
        spin_kit.setVisibility(View.VISIBLE);
        String finalS_month = s_month;
        AndroidNetworking.post(URLHelper.eventList)
                .addBodyParameter("user_id", user_id)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {


                for (int i = 0; i < response.length(); i++) {

                    try {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

                        Date d = dateFormat.parse(response.getJSONObject(i).getString("event_date"));
                        if (finalS_month.equals(response.getJSONObject(i).getString("event_date").split("\\.")[1])) {
                            if (response.getJSONObject(i).getString("type").equals("COURT_DEDLINE")) {
                                //   if (!response.getJSONObject(i).getString("event_date").equals("")) {
                                //  Calendar calendar = Calendar.getInstance();

                                //SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


                                //   Date d = dateFormat.parse(response.getJSONObject(i).getString("event_date"));
                                // if (finalS_month.equals(response.getJSONObject(i).getString("event_date").split("\\.")[1])) {
                                robotoCalendarView.markDeadline(d);
                               // break;

                            }
                            if (response.getJSONObject(i).getString("type").equals("MEETING")) {

                                robotoCalendarView.markMeeting(d);
                                //break;
                            }
                            if (response.getJSONObject(i).getString("type").equals("HEARING")) {

                                robotoCalendarView.markHearing(d);
                              //  break;
                            }


                        }

                    } catch (Exception e) {

                        spin_kit.setVisibility(View.GONE);
                    }
                }


                    spin_kit.setVisibility(View.GONE);


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

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);

        userid = prefs.getString(AppsContants.USERID, "");

        getAllEvents(userid);
    }

    public void checkSelectedDate() {


    }

}
