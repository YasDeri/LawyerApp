package com.bulgarian.lawerapp.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.URLHelper;
import com.bulgarian.lawerapp.R;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class MeetingInfoFragment extends Fragment {
    public static String comming_status = "";
    String client_id, meetingId, clientEmail, clientName, clientMobile, meeting_place = "", clientNotes, event_date = "", event_time = "", rem_event_Date = "", rem_event_time = "";
    SpinKitView spin_kit;
    ImageView update, save;
    private int mYear, mMonth, mDay, mHour, mMinute;
    RelativeLayout delete_meeting;
    EditText name, mobile, notes, email, places, date_event, time_event, reminder_date_event, reminder_time_event;
    ImageView call, emailImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meeting_info, container, false);
        name =  view.findViewById(R.id.name);
        mobile =  view.findViewById(R.id.mobile);
        notes =  view.findViewById(R.id.notes);
        email =  view.findViewById(R.id.email);
        places =  view.findViewById(R.id.places);
        date_event = view.findViewById(R.id.date_event);
        time_event =  view.findViewById(R.id.time_event);
        delete_meeting = (RelativeLayout) view.findViewById(R.id.delete_meeting);
        reminder_date_event =  view.findViewById(R.id.reminder_date_event);
        reminder_time_event = view.findViewById(R.id.reminder_time_event);
        spin_kit = (SpinKitView) view.findViewById(R.id.spin_kit);
        update = (ImageView) view.findViewById(R.id.update);
        save = (ImageView) view.findViewById(R.id.save);
        call = (ImageView) view.findViewById(R.id.call);
        emailImage = (ImageView) view.findViewById(R.id.emailImage);


        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);

        meetingId = prefs.getString(AppsContants.MEETING_ID, "");
        client_id = prefs.getString(AppsContants.CLIENT_ID, "");
        clientEmail = prefs.getString(AppsContants.CLIENT_EMAIL, "");
        clientMobile = prefs.getString(AppsContants.CLIENT_MOBILE, "");
        clientName = prefs.getString(AppsContants.CLIENT_NAME, "");
        clientNotes = prefs.getString(AppsContants.CLIENT_NOTES, "");
        event_date = prefs.getString(AppsContants.EVENT_DATE, "");
        event_time = prefs.getString(AppsContants.EVENT_TIME, "");
        rem_event_time = prefs.getString(AppsContants.REM_EVENT_TIME, "");
        rem_event_Date = prefs.getString(AppsContants.REM_EVENT_DATE, "");
        meeting_place = prefs.getString(AppsContants.Meeting_place, "");


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mobile.getText().toString().equals("")) {

                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 5);
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);

                        callIntent.setData(Uri.parse("tel:" + mobile.getText().toString()));//change the number

                        startActivity(callIntent);
                    }


                } else {
                    showSnackBar(view, "Mobile no not found !");
                }
            }
        });
        emailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().equals("")) {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString()});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                    startActivity(Intent.createChooser(intent, ""));
                } else {
                    showSnackBar(view, "Email not found !");
                }
            }
        });


        name.setText(clientName);
        mobile.setText(clientMobile);
        notes.setText(clientNotes);
        email.setText(clientEmail);
        date_event.setText(event_date.replaceAll("-", "."));
        time_event.setText(event_time);
        reminder_date_event.setText(rem_event_Date.replaceAll("-", "."));
        reminder_time_event.setText(rem_event_time);
        places.setText(meeting_place);


        disabledOrBackground(name);
        disabledOrBackground(mobile);
        disabledOrBackground(notes);
        disabledOrBackground(email);
        disabledOrBackground(time_event);
        disabledOrBackground(date_event);
        disabledOrBackground(reminder_date_event);
        disabledOrBackground(reminder_time_event);
        disabledOrBackground(places);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                update.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                enabledOrBackground(name);
                enabledOrBackground(mobile);
                enabledOrBackground(notes);
                enabledOrBackground(email);
                enabledOrBackground(time_event);
                enabledOrBackground(date_event);
                enabledOrBackground(reminder_date_event);
                enabledOrBackground(reminder_time_event);
                enabledOrBackground(places);


            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMeeting(view, name.getText().toString().trim(), mobile.getText().toString().trim(), email.getText().toString().trim(), places.getText().toString().trim(), notes.getText().toString().trim(), date_event.getText().toString().trim(), time_event.getText().toString().trim(), reminder_date_event.getText().toString().trim(), reminder_time_event.getText().toString().trim());

            }
        });


        date_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                                date_event.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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
        reminder_time_event.setOnClickListener(new View.OnClickListener() {
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


                                reminder_time_event.setText(hour[0] + ":" + minutes[0]);

                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        reminder_date_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                                reminder_date_event.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        delete_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Съобщение!")
                        .setMessage("Сигурни ли сте, че искате да изтриете срещата?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                spin_kit.setVisibility(View.VISIBLE);
                                deleteMeeting(view, meetingId);
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();


            }
        });
        return view;


    }

    public void updateMeeting(final View view, String name, String mobile, String email, String location, String notes, String date_event, String time_event, String date_reminder, String time_reminder) {


        spin_kit.setVisibility(View.VISIBLE);

        AndroidNetworking.post(URLHelper.updateMeeting)
                .addBodyParameter("meet_id", meetingId)
                .addBodyParameter("meet_with", name)
                .addBodyParameter("client_id", client_id)
                .addBodyParameter("mobile", mobile)
                .addBodyParameter("email", email)
                .addBodyParameter("place", location)
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
                if (response.has("result")) {


                    try {

                        if (response.getString("result").equals("successful")) {

                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Съобщение!")
                                    .setMessage("Успешна актуализация за среща!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            NavigationActivity.mToolbar.setTitle("Списък срещи");
                                            NavigationActivity.mToolbar.setLogo(R.drawable.meeting_nav);

                                            Fragment fragment = new MeetinglistFragment();
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.container_body, fragment);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                        }
                                    })
                                    .show();
                        } else {


                            spin_kit.setVisibility(View.GONE);
                            showSnackBar(view, response.getString("result"));
                        }
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

    public void showSnackBar(View view, String message) {


        Snackbar snackbar = Snackbar.make(view, message,
                Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
        snackbar.show();


    }

    public void deleteMeeting(final View view, String meetingId) {
        AndroidNetworking.post(URLHelper.deleteMeeting)
                .addBodyParameter("meet_id", meetingId)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject response) {
                spin_kit.setVisibility(View.GONE);
                if (response.has("result")) {

                    try {
                        if (response.getString("result").equals("successful")) {
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Съобщение !")
                                    .setMessage("Успешно изтриване!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            NavigationActivity.mToolbar.setTitle("Списък срещи");
                                            NavigationActivity.mToolbar.setLogo(R.drawable.meeting_nav);

                                            Fragment fragment = new MeetinglistFragment();
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.container_body, fragment);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                        }
                                    })
                                    .show();
                        } else {
                            spin_kit.setVisibility(View.GONE);
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Alert !")
                                    .setMessage(response.getString("return_arr"))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .show();

                        }
                    } catch (JSONException e) {
                        spin_kit.setVisibility(View.GONE);
                        e.printStackTrace();
                    }

                } else {
                    spin_kit.setVisibility(View.GONE);
                    new AlertDialog.Builder(view.getContext())
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
                new AlertDialog.Builder(view.getContext())
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

    public void enabledOrBackground(EditText editText) {


        editText.setEnabled(true);
        editText.setBackground(getResources().getDrawable(R.drawable.background_edittext));
    }

    public void disabledOrBackground(EditText editText) {

        editText.setEnabled(false);
        editText.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_no_bg));
        editText.setTextColor(Color.BLACK);
    }

}


