package com.bulgarian.lawerapp.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.other.URLHelper;
import com.bulgarian.lawerapp.R;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;


public class AddMeetingFragment extends Fragment {

    EditText name, mobile, email, location, notes, date_event, time_event, date_reminder, time_reminder;
    ImageView save, client_list_image;
    Spinner nameSpinner;
    AutoCompleteTextView auto_complete;
    private ArrayList<String> clientId = new ArrayList<>();
    private ArrayList<String> clientName = new ArrayList<>();
    private ArrayList<String> clientMobile = new ArrayList<>();
    private ArrayList<String> clientEmail = new ArrayList<>();
    SpinKitView spin_kit;
    String userid;
    String clientid = "";
    private int mYear, mMonth, mDay, mHour, mMinute;
    public static String comming_status = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_meeting, container, false);
        name = (EditText) view.findViewById(R.id.name);
        mobile = (EditText) view.findViewById(R.id.mobile);
        email = (EditText) view.findViewById(R.id.email);
        location = (EditText) view.findViewById(R.id.location);
        notes = (EditText) view.findViewById(R.id.notes);
        date_event = (EditText) view.findViewById(R.id.date_event);
        time_event = (EditText) view.findViewById(R.id.time_event);
        date_reminder = (EditText) view.findViewById(R.id.date_reminder);
        time_reminder = (EditText) view.findViewById(R.id.time_reminder);
        save = (ImageView) view.findViewById(R.id.save);
        client_list_image = (ImageView) view.findViewById(R.id.client_list_image);
        nameSpinner = (Spinner) view.findViewById(R.id.nameSpinner);
        auto_complete = view.findViewById(R.id.auto_complete);

        spin_kit = (SpinKitView) view.findViewById(R.id.spin_kit);
        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);
        userid = prefs.getString(AppsContants.USERID, "");

        String s_clientID = prefs.getString(AppsContants.CLIENT_ID, "");
        String s_clientEmail = prefs.getString(AppsContants.CLIENT_EMAIL, "");
        String s_clientMobile = prefs.getString(AppsContants.CLIENT_MOBILE, "");
        String s_clientName = prefs.getString(AppsContants.CLIENT_NAME, "");
        String s_clientNotes = prefs.getString(AppsContants.CLIENT_NOTES, "");


        if (!s_clientID.equals("")) {
//            name.setText(s_clientName);
//            auto_complete.setText(s_clientName);
//            client_list_image.setVisibility(View.GONE);
//            email.setText(s_clientEmail);
//            mobile.setText(s_clientMobile);
//            notes.setText(s_clientNotes);

            name.setText("");
            mobile.setText("");
            email.setText("");
            notes.setText("");
        } else {

//            client_list_image.setVisibility(View.VISIBLE);

        }
        date_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String[] months = {""};
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                if ((monthOfYear + 1) < 10) {
                                    months[0] = "0" + String.valueOf((monthOfYear + 1));

                                } else {

                                    months[0] = (monthOfYear + 1) + "";
                                }


                                date_event.setText(dayOfMonth + "." + months[0] + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }


        });

        time_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String[] hour = {""};
                final String[] minutes = {""};

                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {


                                if (hourOfDay < 10) {


                                    hour[0] = "0" + String.valueOf(hourOfDay);

                                } else {
                                    hour[0] = hourOfDay + "";

                                }

                                if (minute < 10) {

                                    minutes[0] = "0" + String.valueOf(minute);
                                } else {
                                    minutes[0] = minute + "";
                                }


                                time_event.setText(hour[0] + ":" + minutes[0]);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();


            }
        });

        date_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String[] months = {""};
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                if ((monthOfYear + 1) < 10) {
                                    months[0] = "0" + String.valueOf((monthOfYear + 1));

                                } else {

                                    months[0] = (monthOfYear + 1) + "";
                                }
                                date_reminder.setText(dayOfMonth + "." + months[0] + "." + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        time_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String[] hour = {""};
                final String[] minutes = {""};
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {


                                if (hourOfDay < 10) {


                                    hour[0] = "0" + String.valueOf(hourOfDay);

                                } else {
                                    hour[0] = hourOfDay + "";

                                }

                                if (minute < 10) {

                                    minutes[0] = "0" + String.valueOf(minute);
                                } else {
                                    minutes[0] = minute + "";
                                }


                                time_reminder.setText(hour[0] + ":" + minutes[0]);

                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (auto_complete.getText().toString().trim().equals("")) {
                    showSnackBar(view, "Моля, попълнете полетата обозначени с \"*\".");
                } else if (date_event.getText().toString().equals("")) {
                    showSnackBar(view, "Моля, попълнете полетата обозначени с \"*\".");
                }
//                else if (time_event.getText().toString().equals("")) {
//                    showSnackBar(view, "Please Select Meeting time");
//                }
                /*else if (mobile.getText().toString().trim().equals("")) {
                    showSnackBar(view, "Please Enter Mobile");
                } else if (email.getText().toString().equals("")) {
                    showSnackBar(view, "Please Enter Email");
                } else if (location.getText().toString().equals("")) {
                    showSnackBar(view, "Please Enter Location");
                } else if (notes.getText().toString().equals("")) {
                    showSnackBar(view, "Please Enter Notes");
                } else if (date_event.getText().toString().equals("")) {
                    showSnackBar(view, "Please Select Meeting date");
                } else if (time_event.getText().toString().equals("")) {
                    showSnackBar(view, "Please Select Meeting time");
                } else if (date_reminder.getText().toString().equals("")) {
                    showSnackBar(view, "Please Select Reminder date");
                } else if (time_reminder.getText().toString().equals("")) {
                    showSnackBar(view, "Please Select Reminder time");
                }*/ else {

                    String str_email = email.getText().toString().trim();
                    if (str_email.isEmpty()) str_email = " ";
                    String str_phone = mobile.getText().toString().trim();
                    if (str_phone.isEmpty()) str_phone = " ";
                    String str_time_event = time_event.getText().toString().trim();
                    if (str_time_event.isEmpty()) str_time_event = " ";

                    addMeeting(view, auto_complete.getText().toString().trim(),
                            str_phone,
                            str_email,
                            location.getText().toString().trim(),
                            notes.getText().toString().trim(),
                            date_event.getText().toString().trim(),
                            str_time_event,
                            date_reminder.getText().toString().trim(),
                            time_reminder.getText().toString().trim());
                }


            }
        });

        auto_complete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i,
                                    long arg3) {
                name.setText(adapterView.getItemAtPosition(i) + "");
                mobile.setText(clientMobile.get(i) + "");
                email.setText(clientEmail.get(i) + "");
                clientid = clientId.get(i) + "";
            }
        });

        nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //nameSpinner.setVisibility(View.GONE);
//                nameSpinner.setVisibility(View.VISIBLE);
                name.setText(adapterView.getItemAtPosition(i) + "");
                mobile.setText(clientMobile.get(i) + "");
                email.setText(clientEmail.get(i) + "");
                clientid = clientId.get(i) + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getAllClients(userid);

        client_list_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                mobile.setText("");
                email.setText("");
                clientId = new ArrayList<>();
                clientName = new ArrayList<>();
                clientEmail = new ArrayList<>();
                clientMobile = new ArrayList<>();
                getAllClients(userid);
            }
        });

        return view;
    }

    public void getAllClients(String user_id) {

        Log.e("dsfshdf", user_id);

        spin_kit.setVisibility(View.VISIBLE);
        AndroidNetworking.post(URLHelper.show_client)
                .addBodyParameter("user_id", user_id)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {

                    try {
                        clientId.add(response.getJSONObject(i).getString("id"));
                        clientName.add(response.getJSONObject(i).getString("client_name"));
                        clientMobile.add(response.getJSONObject(i).getString("mobile"));
                        clientEmail.add(response.getJSONObject(i).getString("email"));

                    } catch (Exception e) {

                        spin_kit.setVisibility(View.GONE);
                    }

                }
                spin_kit.setVisibility(View.GONE);
                // Creating adapter for spinner
//                nameSpinner.setVisibility(View.VISIBLE);

                Log.e("dfsdjhfsdf", clientName.size() + "");
                Collections.sort(clientName, String.CASE_INSENSITIVE_ORDER);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, clientName);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                auto_complete.setThreshold(1);//will start working from first character
                auto_complete.setAdapter(dataAdapter);

                // attaching data adapter to spinner
                nameSpinner.setAdapter(dataAdapter);
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

    public void addMeeting(final View view, String name, String mobile, String email, String location, String notes, String date_event, String time_event, String date_reminder, String time_reminder) {


        spin_kit.setVisibility(View.VISIBLE);

        AndroidNetworking.post(URLHelper.addMeeting)
                .addBodyParameter("user_id", userid)
                .addBodyParameter("meet_with", name)
                .addBodyParameter("client_id", clientid)
                .addBodyParameter("mobile", mobile)
                .addBodyParameter("email", email)
                .addBodyParameter("place", location)
//                .addBodyParameter("latitude", "")
//                .addBodyParameter("longitude", location)
                .addBodyParameter("notes", notes)
                .addBodyParameter("event_date", date_event)
                .addBodyParameter("event_time", time_event)
                .addBodyParameter("remind_date", date_reminder)
                .addBodyParameter("remind_time", time_reminder)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject response) {


                spin_kit.setVisibility(View.GONE);
                if (response.has("id")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Съобщение!")
                            .setMessage("Успешно въведено!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    NavigationActivity.mToolbar.setTitle("Meeting list");
                                    NavigationActivity.mToolbar.setLogo(R.drawable.client_list);
                                    Fragment fragment = new MeetinglistFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_body, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            })
                            .show();

                } else if (response.has("result")) {
                    try {
                        spin_kit.setVisibility(View.GONE);
                        showSnackBar(view, response.getString("result"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    spin_kit.setVisibility(View.GONE);
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Alert !")
                            .setMessage("Something went wrong !")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();

                }

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


}
