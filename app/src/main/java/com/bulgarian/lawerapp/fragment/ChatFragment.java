package com.bulgarian.lawerapp.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bulgarian.lawerapp.model.ExamCcaseData;
import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.other.URLHelper;
import com.bulgarian.lawerapp.adapters.CaseListGetSet;
import com.bulgarian.lawerapp.R;
import com.jaiselrahman.hintspinner.HintSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class ChatFragment extends Fragment {

    HintSpinner spinCaseType;
    Spinner spinCaseYear;
    AutoCompleteTextView spinCourtList;
    Spinner spinBehalf;
    AutoCompleteTextView spinClient;
    Spinner spinJudgementType;

    ArrayList<String> caseTypeList;
    ArrayList<String> caseTypeIdList;


    ArrayList<String> courtNameList;
    ArrayList<String> courIdList;
    ArrayList<String> courtWesiteList;

    ArrayList<String> behalfList;
    ArrayList<String> behalfIdList;

    ArrayList<String> clientNameList;
    ArrayList<String> clientIdList;
    ArrayList<ExamCcaseData> examCaseIdList;
    ArrayList<String> selectedClientIdList = new ArrayList<>();

    ArrayList<String> judgementTypeList;
    ArrayList<String> judgementTypeIdList;

    String[] caseYear = {"година", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997"
            , "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008"
            , "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019"
            , "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029"
            , "2030", "2031", "2032", "2033", "2034", "2035"};
    EditText editCaseNo;
    EditText conJugeName;
    EditText ConJudgeContact;
    EditText ConJudgeEmail;
    EditText editJudgeName;
    EditText editDespute;
    EditText editAmount;
    EditText editPrice;
    EditText editPayAmount, editJudgmentNo, editTookEffect;
    EditText editAppealCaseNo, editNotes, editAgainstName;
    EditText editAgainstMob, editAgainstLawyer, editAgainstLawyerMob;
    EditText editAgainstLawyerEmail, editHearingDate, editHearingTime;
    EditText editRemindDate, editRemindTime, editCourtMessage;
    EditText editCourtMessageDadlineDate, editCourtMessageDadlineTime,
            editCourtMsgReminderD, editCourtMsgReminderT;


    TextView txtCourtWebsite;
    ImageView addCase;
    String strCaseYear = "", strCaseTypeId = "", strCourtId = "";
    String strBehalfId = "", strClientId = "", strExamCaseId = "", strJudgementTypeId = "", strInstaceId = "";
    String strUserId = "";
    public static String comming_status = "";
    Button btnInstanceOne;
    Button btnInstanceTwo;
    Button btnInstanceThree;
    Button btnEnforcement;
    LinearLayout linearMoreRespondent;
    ImageView imgMoreRespondent;
    ImageView imgCutRespondent;


    //new fields
    AutoCompleteTextView spinCaseNumber;
    EditText editCost, etClientName, etClientMobile, etClientEmail, etClientNotes;
    Button btnPaid, btnNotPaid;
    LinearLayout ll_data;
    RelativeLayout ll_judgetext, ll_judge_phone, ll_judge_email, rlAddClient;
    LinearLayout llClient, ll_add_client, ll_against, llRespondent;
    ArrayList<String> caseIdList;
    ImageView ivClientSave;
    int respond_count = 0;
    String s_clientEmail;
    String s_clientMobile;
    String s_clientName ;
    String s_clientNotes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        Views(view);
        listeners();

        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);
        strUserId = prefs.getString(AppsContants.USERID, "");
        Log.e("UserID", strUserId);



        s_clientEmail = prefs.getString(AppsContants.CLIENT_EMAIL, "");
        s_clientMobile = prefs.getString(AppsContants.CLIENT_MOBILE, "");
        s_clientName = prefs.getString(AppsContants.CLIENT_NAME, "");
        s_clientNotes = prefs.getString(AppsContants.CLIENT_NOTES, "");
        //spinClient.setText(s_clientName);


        CaseType();
        CourtList();
        OnBehalf();
        Client();
        JudgementType();
        ShowCaseList();




        spinCaseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCaseTypeId = caseTypeIdList.get(position);
                Log.e("caseTypeLog", strCaseTypeId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        spinCourtList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i,
                                    long arg3) {
                strCourtId = courIdList.get(i);
                String strCourtWebsite = courtWesiteList.get(i);

                txtCourtWebsite.setText(strCourtWebsite);
                txtCourtWebsite.setVisibility(View.VISIBLE);
            }
        });

        spinCourtList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCourtId = courIdList.get(position);
                String strCourtWebsite = courtWesiteList.get(position);
                if (position == 0) {
                    txtCourtWebsite.setVisibility(View.GONE);
                } else {
                    txtCourtWebsite.setText(strCourtWebsite);
                    txtCourtWebsite.setVisibility(View.VISIBLE);
                }

                Log.e("courtTypeLog", strCaseTypeId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinBehalf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strBehalfId = behalfIdList.get(position);
                Log.e("behalfLog", strBehalfId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinJudgementType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strJudgementTypeId = judgementTypeIdList.get(position);
                Log.e("JudgementTypeLog", strJudgementTypeId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter year = new ArrayAdapter(getActivity(), R.layout.add_case_spin, caseYear);
        year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCaseYear.setAdapter(year);

        spinCaseYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCaseYear = caseYear[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnInstanceOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_data.setVisibility(View.GONE);
                setVisibleJudgeValues(false);

                btnInstanceOne.setBackground(getResources().getDrawable(R.drawable.instance_btn_select));

                btnInstanceTwo.setBackground(getResources().getDrawable(R.drawable.instance_btn_back));
                btnInstanceThree.setBackground(getResources().getDrawable(R.drawable.instance_btn_back));
                btnEnforcement.setBackground(getResources().getDrawable(R.drawable.instance_btn_back));

                strInstaceId = "1";
            }
        });

        btnInstanceTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_data.setVisibility(View.VISIBLE);
                setVisibleJudgeValues(false);
                btnInstanceTwo.setBackground(getResources().getDrawable(R.drawable.instance_btn_select));

                btnInstanceOne.setBackground(getResources().getDrawable(R.drawable.instance_btn_back));
                btnInstanceThree.setBackground(getResources().getDrawable(R.drawable.instance_btn_back));
                btnEnforcement.setBackground(getResources().getDrawable(R.drawable.instance_btn_back));
                strInstaceId = "2";
            }
        });

        btnInstanceThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_data.setVisibility(View.VISIBLE);
                setVisibleJudgeValues(false);
                btnInstanceThree.setBackground(getResources().getDrawable(R.drawable.instance_btn_select));

                btnInstanceOne.setBackground(getResources().getDrawable(R.drawable.instance_btn_back));
                btnInstanceTwo.setBackground(getResources().getDrawable(R.drawable.instance_btn_back));
                btnEnforcement.setBackground(getResources().getDrawable(R.drawable.instance_btn_back));

                strInstaceId = "3";
            }
        });

        btnEnforcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_data.setVisibility(View.GONE);
                setVisibleJudgeValues(true);
                btnEnforcement.setBackground(getResources().getDrawable(R.drawable.instance_btn_select));

                btnInstanceOne.setBackground(getResources().getDrawable(R.drawable.instance_btn_back));
                btnInstanceTwo.setBackground(getResources().getDrawable(R.drawable.instance_btn_back));
                btnInstanceThree.setBackground(getResources().getDrawable(R.drawable.instance_btn_back));

                strInstaceId = "4";
            }
        });


        imgMoreRespondent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearMoreRespondent.setVisibility(View.VISIBLE);
            }
        });

        imgCutRespondent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearMoreRespondent.setVisibility(View.GONE);
            }
        });

        /*court msg reminder Date Time*/
        editCourtMsgReminderD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] months = {""};
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


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


                                editCourtMsgReminderD.setText(dayOfMonth + "." + months[0] + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });


        editCourtMsgReminderT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] hour = {""};
                final String[] minutes = {""};

                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

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


                                editCourtMsgReminderT.setText(hour[0] + ":" + minutes[0]);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();


            }
        });


        editCourtMessageDadlineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] months = {""};
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


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


                                editCourtMessageDadlineDate.setText(dayOfMonth + "." + months[0] + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });


        editCourtMessageDadlineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] hour = {""};
                final String[] minutes = {""};

                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

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


                                editCourtMessageDadlineTime.setText(hour[0] + ":" + minutes[0]);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();


            }
        });


        editHearingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] months = {""};
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


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


                                editHearingDate.setText(dayOfMonth + "." + months[0] + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });


        editHearingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] hour = {""};
                final String[] minutes = {""};

                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

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


                                editHearingTime.setText(hour[0] + ":" + minutes[0]);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();



            }
        });


        editRemindDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] months = {""};
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


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


                                editRemindDate.setText(dayOfMonth + "." + months[0] + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });


        editRemindTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] hour = {""};
                final String[] minutes = {""};

                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

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


                                editRemindTime.setText(hour[0] + ":" + minutes[0]);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();


            }
        });


        editTookEffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] months = {""};
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


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


                                editTookEffect.setText(dayOfMonth + "." + months[0] + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });


        addCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String strCaseNo = editCaseNo.getText().toString().trim();
                String strConJudge = conJugeName.getText().toString().trim();
                String strConJudgeContact = ConJudgeContact.getText().toString().trim();
                String strConJudgeEmail = ConJudgeEmail.getText().toString().trim();
                String strJudgeName = editJudgeName.getText().toString().trim();
                String strDespute = editDespute.getText().toString().trim();

                String strAmount = editCost.getText().toString().trim();
                String strPrice = editAmount.getText().toString().trim();
                String strPayAmount = editPrice.getText().toString().trim();
                String strPayedAmount = editPayAmount.getText().toString().trim();

                String strJudgmentNo = editJudgmentNo.getText().toString().trim();
                String strTookEffect = editTookEffect.getText().toString().trim();
                String strAppealCaseNo = editAppealCaseNo.getText().toString().trim();
                String strNotes = editNotes.getText().toString().trim();
                String strAgainstName = editAgainstName.getText().toString().trim();
                String strAgainstMob = editAgainstMob.getText().toString().trim();
                String strAgainstLawyer = editAgainstLawyer.getText().toString().trim();
                String strAgainstLawyerMob = editAgainstLawyerMob.getText().toString().trim();
                String strAgainstLawyerEmail = editAgainstLawyerEmail.getText().toString().trim();
                String strHearingDate = editHearingDate.getText().toString().trim();
                String strHearingTime = editHearingTime.getText().toString().trim();
                String strRemindDate = editRemindDate.getText().toString().trim();
                String strRemindTime = editRemindTime.getText().toString().trim();
                String strCourtMessage = editCourtMessage.getText().toString().trim();
                String strCourtMsgDD = editCourtMessageDadlineDate.getText().toString().trim();
                String strCourtMsgDT = editCourtMessageDadlineTime.getText().toString().trim();
                String strCourtMsgRD = editCourtMsgReminderD.getText().toString().trim();
                String strCourtMsgRT = editCourtMsgReminderT.getText().toString().trim();

                AddCase(strCaseNo, strConJudge, strConJudgeContact, strConJudgeEmail, strJudgeName
                        , strDespute, strAmount, strPrice, strPayAmount, strPayedAmount, strJudgmentNo, strTookEffect, strAppealCaseNo
                        , strNotes, strAgainstName, strAgainstMob, strAgainstLawyer, strAgainstLawyerMob
                        , strAgainstLawyerEmail, strHearingDate, strHearingTime, strRemindDate, strRemindTime, strCourtMessage
                        , strCourtMsgDD, strCourtMsgDT, strCourtMsgRD, strCourtMsgRT);


            }
        });

        return view;
    }

    public void ShowCaseList() {
        AndroidNetworking.post(URLHelper.ListCases)
                .addBodyParameter("user_id", strUserId)
                .setTag("Case List")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        caseIdList = new ArrayList<>();
                        examCaseIdList = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                CaseListGetSet getSet = new CaseListGetSet();
                                getSet.setId(jsonObject.getString("id"));
                                getSet.setUserId(jsonObject.getString("user_id"));
                                getSet.setCaseNo(jsonObject.getString("case_no"));
                                getSet.setYear(jsonObject.getString("year"));

                                String name = jsonObject.getString("case_no") + " / " +
                                        jsonObject.getString("year");
                                caseIdList.add(name);
                                examCaseIdList.add(new ExamCcaseData(
                                        jsonObject.getString("id"), name));
                            }
//                            ArrayAdapter type = new ArrayAdapter(getActivity(), R.layout.add_case_spin,
//                                    caseIdList);
//                            type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            spinCaseNumber.setAdapter(type);

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_item, caseIdList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinCaseNumber.setThreshold(1);
                            spinCaseNumber.setAdapter(dataAdapter);

                        } catch (Exception ex) {
                            Log.e("dfjdsghkjfsdf", ex.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dfjdsghkjfsdf", anError.getMessage());
                    }
                });


    }

    private void setVisibleJudgeValues(boolean visibleJudgeValues) {
        if (visibleJudgeValues) {
            ll_judge_email.setVisibility(View.VISIBLE);
            ll_judge_phone.setVisibility(View.VISIBLE);
            ll_judgetext.setVisibility(View.VISIBLE);
        } else {
            ll_judge_email.setVisibility(View.GONE);
            ll_judge_phone.setVisibility(View.GONE);
            ll_judgetext.setVisibility(View.GONE);
        }
    }

    private void listeners() {

        ll_against.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRespondView(llRespondent);
            }
        });

        btnNotPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCost.setEnabled(false);
                btnNotPaid.setBackground(getResources().getDrawable(R.drawable.instance_btn_select));
                btnPaid.setBackground(getResources().getDrawable(R.drawable.instance_btn_back));
            }
        });

        btnPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCost.setEnabled(true);
                btnPaid.setBackground(getResources().getDrawable(R.drawable.instance_btn_select));
                btnNotPaid.setBackground(getResources().getDrawable(R.drawable.instance_btn_back));
            }
        });

        spinClient.setOnItemClickListener((adapterView, view, i, arg3) -> {
            for (int j = 0; j < clientNameList.size(); j++) {
                if (clientNameList.get(j).equals(adapterView.getItemAtPosition(i).toString())) {
                    strClientId = clientIdList.get(j);
                    break;
                }
            }
            makeClientNameButton(llClient, adapterView.getItemAtPosition(i).toString(),
                    Integer.parseInt(strClientId));
            spinClient.setText("");
        });

        spinCaseNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i,
                                    long arg3) {
                for (int j = 0; j < examCaseIdList.size(); j++){
                    if (examCaseIdList.get(j).getName().equals(
                            adapterView.getItemAtPosition(i).toString())) {
                        strExamCaseId = examCaseIdList.get(j).getId();
                        Log.d("AAAA", strExamCaseId + " D");
                    }
                }
            }
        });

        ll_add_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlAddClient.setVisibility(View.VISIBLE);
            }
        });

        ivClientSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etClientName.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Моля, попълнете полетата обозначени с \"*\".",
                            Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences prefs = getActivity().getSharedPreferences(
                            AppsContants.MyPREFERENCES, MODE_PRIVATE);

                    addClient(prefs.getString(AppsContants.USERID, ""),
                            etClientMobile.getText().toString(),
                            etClientEmail.getText().toString(),
                            etClientNotes.getText().toString(),
                            etClientName.getText().toString());


                }
            }
        });

        editPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editAmount.getText().toString().isEmpty()) {
                    int amount = Integer.valueOf(editAmount.getText().toString());
                    int price = 0;
                    if (!editPrice.getText().toString().isEmpty())
                        price = Integer.valueOf(editPrice.getText().toString());

                    int payed = amount - price;
                    editPayAmount.setText(payed + "");
                } else {
                    Toast.makeText(getActivity(), "You select no cost value",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int convertDpToPixel(int dp) {
        return dp * getActivity().getResources().getDisplayMetrics().densityDpi
                / DisplayMetrics.DENSITY_DEFAULT;
    }

    public void addClient(String user_id, String mobile, String email, String notes,
                          String clientName) {

        AndroidNetworking.post(URLHelper.add_client)
                .addBodyParameter("email", email)
                .addBodyParameter("mobile", mobile)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("client_name", clientName)
                .addBodyParameter("notes", notes)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject response) {

                if (response.has("id")) {
                    try {
                        makeClientNameButton(llClient, etClientName.getText().toString(),
                                response.getInt("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    etClientEmail.setText("");
                    etClientMobile.setText("");
                    etClientName.setText("");
                    etClientNotes.setText("");
                    rlAddClient.setVisibility(View.GONE);

                }
            }

            @Override
            public void onError(ANError anError) {
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

    private void setRespondView(LinearLayout ll) {
        respond_count++;
        ImageView iv_close = makeCloseImageView(respond_count);
        ll.addView(iv_close);
        ll.addView(makeParentLinearLayout(respond_count * 100));

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.removeView(getActivity().findViewById(iv_close.getId() * 100));
                ll.removeView(iv_close);
            }
        });
    }

    private ImageView makeCloseImageView(int id) {
        ImageView imageView = new ImageView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                dpToPx(30),
                dpToPx(30));

        params.gravity = Gravity.RIGHT;
        params.setMargins(dpToPx(10), dpToPx(10), dpToPx(5), 0);
        imageView.setLayoutParams(params);
        imageView.setId(id);
        imageView.setImageResource(R.drawable.cut);

        return imageView;
    }

    private ImageView makeLinImageView() {
        ImageView imageView = new ImageView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                dpToPx(1));
        params.setMargins(dpToPx(5), dpToPx(10), dpToPx(5), 0);
        imageView.setLayoutParams(params);
        imageView.setBackgroundColor(Color.parseColor("#D0CFCF"));

        return imageView;
    }

    private LinearLayout makeParentLinearLayout(int id) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setId(id);
        linearLayout.addView(makeLinearLayout(false, "Имена:", id + 1));
        linearLayout.addView(makeLinearLayout(false, "Тел.:", id + 2));
        linearLayout.addView(makeLinearLayout(false, "Адвокат:", id + 3));
        linearLayout.addView(makeLinearLayout(false, "Адвокат тел.:", id + 4));
        linearLayout.addView(makeLinearLayout(true, "Адвокат имейл:", id + 5));
        linearLayout.addView(makeLinImageView());
        return linearLayout;
    }

    private LinearLayout makeLinearLayout(boolean is_long_text, String text, int edit_id) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(dpToPx(3), dpToPx(10), 0, 0);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(makeTextView(text, is_long_text));
        linearLayout.addView(makeEditText(edit_id));
        return linearLayout;
    }

    private TextView makeTextView(String text, boolean is_long_text) {
        TextView textView = new TextView(getActivity());
        int text_width = (int) getResources().getDimension(R.dimen.add_case_text_width);
        if (is_long_text)
            text_width = (int) getResources().getDimension(R.dimen.add_case_text_width_long);

        textView.setLayoutParams(new RelativeLayout.LayoutParams(
                text_width,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        textView.setText(text);
        textView.setTextColor(Color.parseColor("#333333"));
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.btnTextSize));

        return textView;
    }

    private EditText makeEditText(int id) {
        EditText editText = new EditText(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(dpToPx(20), 0, dpToPx(5), 0);
        editText.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
        editText.setLayoutParams(params);

        editText.setId(id);
        editText.setBackgroundResource(R.drawable.background_edittext);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.btnTextSize));

        return editText;
    }

    private void makeClientNameButton(LinearLayout linearLayout, String text, int id) {
        selectedClientIdList.add(String.valueOf(id));

        Button btnTag = new Button(getActivity());
        btnTag.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        btnTag.setText(text);
        btnTag.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.btnTextSize));
        Drawable img = getContext().getResources().getDrawable(R.drawable.ic_close_grey_600_24dp);
        img.setBounds(0, 0, dpToPx(15), dpToPx(15));
        btnTag.setCompoundDrawables(img, null, null, null);

        btnTag.setId(id);
        btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < selectedClientIdList.size(); i++) {
                    if (selectedClientIdList.get(i).equals(String.valueOf(btnTag.getId()))) {
                        selectedClientIdList.remove(i);
                        break;
                    }
                }
                linearLayout.removeView(btnTag);
            }
        });
        linearLayout.addView(btnTag);
    }

    public void Views(View view) {

        editCaseNo = view.findViewById(R.id.editCaseNo);
        conJugeName = view.findViewById(R.id.conJugeName);
        ConJudgeContact = view.findViewById(R.id.ConJudgeContact);
        ConJudgeEmail = view.findViewById(R.id.ConJudgeEmail);
        editJudgeName = view.findViewById(R.id.editJudgeName);
        editDespute = view.findViewById(R.id.editDespute);
        editAmount = view.findViewById(R.id.editAmount);
        editPrice = view.findViewById(R.id.editPrice);
        editPayAmount = view.findViewById(R.id.editPayAmount);
        editJudgmentNo = view.findViewById(R.id.editJudgmentNo);
        editTookEffect = view.findViewById(R.id.editTookEffect);
        editAppealCaseNo = view.findViewById(R.id.editAppealCaseNo);
        editAgainstName = view.findViewById(R.id.editAgainstName);
        editNotes = view.findViewById(R.id.editNotes);
        editAgainstMob = view.findViewById(R.id.editAgainstMob);
        editAgainstLawyer = view.findViewById(R.id.editAgainstLawyer);
        editAgainstLawyerMob = view.findViewById(R.id.editAgainstLawyerMob);
        editAgainstLawyerEmail = view.findViewById(R.id.editAgainstLawyerEmail);
        editHearingDate = view.findViewById(R.id.editHearingDate);
        editHearingTime = view.findViewById(R.id.editHearingTime);
        editRemindDate = view.findViewById(R.id.editRemindDate);
        editRemindTime = view.findViewById(R.id.editRemindTime);
        editCourtMessage = view.findViewById(R.id.editCourtMessage);
        editCourtMessageDadlineDate = view.findViewById(R.id.editCourtMessageDadlineDate);
        editCourtMessageDadlineTime = view.findViewById(R.id.editCourtMessageDadlineTime);
        editCourtMsgReminderD = view.findViewById(R.id.editCourtMsgReminderD);
        editCourtMsgReminderT = view.findViewById(R.id.editCourtMsgReminderT);
        btnEnforcement = view.findViewById(R.id.btnEnforcement);
        btnInstanceOne = view.findViewById(R.id.btnInstanceOne);
        btnInstanceTwo = view.findViewById(R.id.btnInstanceTwo);
        btnInstanceThree = view.findViewById(R.id.btnInstanceThree);
        linearMoreRespondent = view.findViewById(R.id.linearMoreRespondent);
        imgMoreRespondent = view.findViewById(R.id.imgMoreRespondent);
        imgCutRespondent = view.findViewById(R.id.imgCutRespondent);

        txtCourtWebsite = view.findViewById(R.id.txtCourtWebsite);
        spinCaseYear = view.findViewById(R.id.spinCaseYear);
        spinCaseType = view.findViewById(R.id.spinCaseType);
        spinCourtList = view.findViewById(R.id.spinCourtList);
        spinBehalf = view.findViewById(R.id.spinBehalf);
        spinClient = view.findViewById(R.id.spinClient);
        spinJudgementType = view.findViewById(R.id.spinJudgementType);

        addCase = view.findViewById(R.id.addCase);

        spinCaseNumber = view.findViewById(R.id.spinCaseNumber);
        editCost = view.findViewById(R.id.editCost);
        btnPaid = view.findViewById(R.id.btnPaid);
        btnNotPaid = view.findViewById(R.id.btnNotPaid);
        ll_data = view.findViewById(R.id.ll_data);
        ll_judgetext = view.findViewById(R.id.ll_judgetext);
        ll_judge_phone = view.findViewById(R.id.ll_judge_phone);
        ll_judge_email = view.findViewById(R.id.ll_judge_email);
        etClientName = view.findViewById(R.id.etClientName);
        etClientMobile = view.findViewById(R.id.etClientMobile);
        etClientEmail = view.findViewById(R.id.etClientEmail);
        llClient = view.findViewById(R.id.llClient);
        etClientNotes = view.findViewById(R.id.etClientNotes);
        rlAddClient = view.findViewById(R.id.rlAddClient);
        ll_add_client = view.findViewById(R.id.ll_add_client);
        ivClientSave = view.findViewById(R.id.ivClientSave);
        ll_against = view.findViewById(R.id.ll_against);
        llRespondent = view.findViewById(R.id.llRespondent);
    }

    public void CaseType() {
        AndroidNetworking.get(URLHelper.CaseType)
                .setTag("Type")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            caseTypeList = new ArrayList<>();
                            caseTypeIdList = new ArrayList<>();
                            caseTypeList.add("Избери вид дело");
                            caseTypeIdList.add("0");
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String strCaseTypeID = jsonObject.getString("id");
                                String strCaseType = jsonObject.getString("kind");
                                caseTypeList.add(strCaseType);
                                caseTypeIdList.add(strCaseTypeID);
                            }
                            ArrayAdapter type = new ArrayAdapter(getActivity(), R.layout.add_case_spin, caseTypeList);
                            type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinCaseType.setAdapter(type);


                        } catch (Exception ex) {
                            Log.e("dkjfsjfsd", ex.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dkjfsjfsd", anError.toString());
                    }
                });
    }

    public void CourtList() {
        AndroidNetworking.get(URLHelper.CoutList)
                .setTag("Type")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            courtNameList = new ArrayList<>();
                            courIdList = new ArrayList<>();

                            courtNameList.add("Избери");
                            courIdList.add("0");
                            courtWesiteList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String strID = jsonObject.getString("id");
                                String strCourt = jsonObject.getString("name");
                                String strCourtWebSite = jsonObject.getString("url");
                                courtNameList.add(strCourt);
                                courIdList.add(strID);
                                courtWesiteList.add(strCourtWebSite);
                            }
//                            ArrayAdapter type = new ArrayAdapter(getActivity(), R.layout.add_case_spin, courtNameList);
//                            type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            spinCourtList.setAdapter(type);

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_item, courtNameList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinCourtList.setThreshold(1);
                            spinCourtList.setAdapter(dataAdapter);

                        } catch (Exception ex) {
                            Log.e("dkjfsjfsd", ex.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dkjfsjfsd", anError.toString());

                    }
                });

    }


    public void OnBehalf() {
        AndroidNetworking.get(URLHelper.Behalf)
                .setTag("Type")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            behalfList = new ArrayList<>();
                            behalfIdList = new ArrayList<>();
                            behalfList.add("Избери");
                            behalfIdList.add("0");
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String strID = jsonObject.getString("id");
                                String strBehalf = jsonObject.getString("behalf");
                                behalfList.add(strBehalf);
                                behalfIdList.add(strID);
                            }
                            ArrayAdapter type = new ArrayAdapter(getActivity(), R.layout.add_case_spin, behalfList);
                            type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinBehalf.setAdapter(type);
                        } catch (Exception ex) {
                            Log.e("dkjfsjfsd", ex.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dkjfsjfsd", anError.toString());

                    }
                });

    }

    public void Client() {
        AndroidNetworking.post(URLHelper.ShowClient)
                .addBodyParameter("user_id", strUserId)
                .setTag("Type")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            clientNameList = new ArrayList<>();
                            clientIdList = new ArrayList<>();
                            clientNameList.add("Избери");
                            clientIdList.add("0");

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String strID = jsonObject.getString("id");
                                String strClient = jsonObject.getString("client_name");
                                clientNameList.add(strClient);
                                clientIdList.add(strID);
                                Log.i("clientadded",clientNameList.get(i));
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, clientNameList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                            // dataAdapter.addAll(clientNameList);
                            spinClient.setThreshold(1);
                            spinClient.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();

                            SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);

                            String s_clientID = prefs.getString(AppsContants.CLIENT_ID, "");

                            if (!s_clientID.equals("")) {
                                for (int i = 0; i < clientIdList.size(); i++) {
                                    if (clientIdList.get(i).equals(s_clientID)) {
                                        spinClient.setSelection(i);
                                    }
                                }
                            }

                        } catch (Exception ex) {
                            Log.e("dkjfsjfsd", ex.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dkjfsjfsd", anError.toString());

                    }
                });
    }

    public void JudgementType() {
        AndroidNetworking.get(URLHelper.JudgmentType)
                .setTag("Type")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            judgementTypeList = new ArrayList<>();
                            judgementTypeIdList = new ArrayList<>();

                            judgementTypeList.add("Избери");
                            judgementTypeIdList.add("0");

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String strID = jsonObject.getString("id");
                                String strJudgment = jsonObject.getString("type");
                                judgementTypeList.add(strJudgment);
                                judgementTypeIdList.add(strID);
                            }
                            ArrayAdapter type = new ArrayAdapter(getActivity(), R.layout.add_case_spin, judgementTypeList);
                            type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinJudgementType.setAdapter(type);
                        } catch (Exception ex) {
                            Log.e("dkjfsjfsd", ex.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dkjfsjfsd", anError.toString());
                    }
                });
    }

    public void AddCase(String strCaseNo,
                        String strConJudge,
                        String strConJudgeContact,
                        String strConJudgeEmail,
                        String strJudgeName,
                        String strDespute,
                        String strAmount,
                        String strPrice,
                        String strPayAmount,
                        String strPayedAmount,
                        String strJudgmentNo,
                        String strTookEffect,
                        String strAppealCaseNo,
                        String strNotes,
                        String strAgainstName,
                        String strAgainstMob,
                        String strAgainstLawyer,
                        String strAgainstLawyerMob,
                        String strAgainstLawyerEmail,
                        String strHearingDate,
                        String strHearingTime,
                        String strRemindDate,
                        String strRemindTime,
                        String strCourtMessage,
                        String strCourtMsgDD,
                        String strCourtMsgDT,
                        String strCourtMsgRD,
                        String strCourtMsgRT) {


        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Please wait");
        dialog.setMessage("Case is adding..");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        String client_lds = "";
        for (int i = 0; i < selectedClientIdList.size(); i++)
            client_lds += selectedClientIdList.get(i) + ",";
        if (!client_lds.isEmpty())
            client_lds = client_lds.substring(0, client_lds.length() - 1);

        String respond_name = strAgainstName + ",";
        String respond_lawyer = strAgainstLawyer + ",";
        String respond_tel = strAgainstMob + ",";
        String respond_lawyer_tel = strAgainstLawyerMob + ",";
        String respond_email = strAgainstLawyerEmail + ",";

        for (int i = 1; i <= respond_count; i++) {

            int name_id = i * 100 + 1;
            if (getActivity().findViewById(name_id) != null) {
                EditText etName = getActivity().findViewById(name_id);
                if (!etName.getText().toString().isEmpty())
                    respond_name += etName.getText().toString() + ",";
            }

            int tel_id = i * 100 + 2;
            if (getActivity().findViewById(tel_id) != null) {
                EditText etTel = getActivity().findViewById(tel_id);
                if (!etTel.getText().toString().isEmpty())
                    respond_tel += etTel.getText().toString() + ",";
            }

            int lawyer_id = i * 100 + 3;
            if (getActivity().findViewById(lawyer_id) != null) {
                EditText etlawyer = getActivity().findViewById(lawyer_id);
                if (!etlawyer.getText().toString().isEmpty())
                    respond_lawyer += etlawyer.getText().toString() + ",";
            }

            int lawyer_tel_id = i * 100 + 4;
            if (getActivity().findViewById(lawyer_tel_id) != null) {
                EditText etLawyerTel = getActivity().findViewById(lawyer_tel_id);
                if (!etLawyerTel.getText().toString().isEmpty())
                    respond_lawyer_tel += etLawyerTel.getText().toString() + ",";
            }

            int lawyer_email_id = i * 100 + 5;
            if (getActivity().findViewById(lawyer_email_id) != null) {
                EditText etLawyerEmail = getActivity().findViewById(lawyer_email_id);
                if (!etLawyerEmail.getText().toString().isEmpty())
                    respond_email += etLawyerEmail.getText().toString() + ",";
            }
        }
        if (!respond_name.isEmpty())
            respond_name = respond_name.substring(0, respond_name.length() - 1);

        if (!respond_lawyer.isEmpty())
            respond_lawyer = respond_lawyer.substring(0, respond_lawyer.length() - 1);

        if (!respond_tel.isEmpty())
            respond_tel = respond_tel.substring(0, respond_tel.length() - 1);

        if (!respond_lawyer_tel.isEmpty())
            respond_lawyer_tel = respond_lawyer_tel.substring(0, respond_lawyer_tel.length() - 1);

        if (!respond_email.isEmpty())
            respond_email = respond_email.substring(0, respond_email.length() - 1);

        ANRequest aa = AndroidNetworking.post(URLHelper.AddCase)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("case_no", strCaseNo)
                .addBodyParameter("year", strCaseYear)
                .addBodyParameter("case_type", strCaseTypeId)
                .addBodyParameter("instance", strInstaceId)
                .addBodyParameter("refer_case_id", strExamCaseId)
                .addBodyParameter("contractor_judge", strConJudge)
                .addBodyParameter("contr_judge_contect", strConJudgeContact)
                .addBodyParameter("contr_judge_email", strConJudgeEmail)
                .addBodyParameter("judge", strJudgeName)
                .addBodyParameter("court_id", strCourtId)
                .addBodyParameter("behalf_of", strBehalfId)
                .addBodyParameter("dispute_for", strDespute)
                .addBodyParameter("client_id", client_lds)

                .addBodyParameter("amount", strAmount)
                .addBodyParameter("price", strPrice)
                .addBodyParameter("payamt", strPayAmount)
                .addBodyParameter("remainamount", strPayedAmount)

                .addBodyParameter("type_judgment", strJudgementTypeId)
                .addBodyParameter("type_judgment_no", strJudgmentNo)
                .addBodyParameter("took_effect", strTookEffect)
                .addBodyParameter("appeal_case_n", strAppealCaseNo)
                .addBodyParameter("notes", strNotes)

                .addBodyParameter("respo_name", respond_name)
                .addBodyParameter("respo_mob", respond_tel)
                .addBodyParameter("respo_lawy", respond_lawyer)
                .addBodyParameter("respo_lawy_mob", respond_lawyer_tel)
                .addBodyParameter("respo_lawy_email", respond_email)

                .addBodyParameter("hearing_date", strHearingDate)
                .addBodyParameter("hearing_time", strHearingTime)
                .addBodyParameter("hear_remind_date", strRemindDate)
                .addBodyParameter("hear_remind_time", strRemindTime)

                .addBodyParameter("msg_court", strCourtMessage)

                .addBodyParameter("msg_dedline_date", strCourtMsgDD)
                .addBodyParameter("msg_dedline_time", strCourtMsgDT)
                .addBodyParameter("dedline_remind_date", strCourtMsgRD)
                .addBodyParameter("dedline_remind_time", strCourtMsgRT)
                .setTag("Add Case")
                .setPriority(Priority.HIGH)
                .build();


        aa.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    Log.e("fghhjyjfg", response.toString());
                    if (response.has("result")) {

                        if (response.getString("result").equals("successful")) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Успешно добавено дело!", Toast.LENGTH_SHORT).show();

                            CaseListFragment.comming_status = "1";
                            Fragment fragmentsss = new CaseListFragment();

                            NavigationActivity.mToolbar.setTitle("Списък дела");
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragmentsss);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        } else {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "" + response.getString("result"), Toast.LENGTH_SHORT).show();
                        }

                    }


                } catch (Exception ex) {
                    dialog.dismiss();
                    Log.e("ghfkieigjv", ex.getMessage());
                }


            }

            @Override
            public void onError(ANError anError) {
                dialog.dismiss();
                Log.e("ghfkieigjv", anError.toString());
            }
        });

    }


   /* public void  Cal(){

        int high=0;
        int low=0;
        int price =20;


        int status=1;

        if(status==1){
            high=price;
            low=price;
        }

        if(price<high){
            low=price;
            status=2;
        }
        else if(price>low){
            high=price;
            status=2;
        }
    }*/
}
