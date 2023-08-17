package com.bulgarian.lawerapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bulgarian.lawerapp.fragment.CaseListDetail;
import com.bulgarian.lawerapp.fragment.MeetingInfoFragment;
import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.other.JsonUtil;
import com.bulgarian.lawerapp.other.URLHelper;
import com.bulgarian.lawerapp.model.EventData;
import com.bulgarian.lawerapp.R;
import com.bulgarian.lawerapp.model.MeetingData;
import com.bulgarian.lawerapp.model.caseinfo.BeHalfOf;
import com.bulgarian.lawerapp.model.caseinfo.CaseType;
import com.bulgarian.lawerapp.model.caseinfo.ClientList;
import com.bulgarian.lawerapp.model.caseinfo.CourtId;
import com.bulgarian.lawerapp.model.caseinfo.DeadlineList;
import com.bulgarian.lawerapp.model.caseinfo.HearingList;
import com.bulgarian.lawerapp.model.caseinfo.Instance;
import com.bulgarian.lawerapp.model.caseinfo.RespondList;
import com.bulgarian.lawerapp.model.caseinfo.TypeJudgment;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<EventData> eventDataList;
    int getposition=0;
    MyViewHolder getholder=null;
    private Context context;
    private EventData eventData;
    private List<MeetingData> allMeetings;
    private ArrayList<CaseListGetSet> allcases = new ArrayList<>();
    private  List<CaseListGetSet> replicadata=new ArrayList<>();
            String userid = "";
   //SpinKitView spin_kit;
    RecyclerView recyclerView;
    CaseListGetSet dataAdapterOBJ,getDataAdapterOBJ;
    MeetingAdapter meetingAdapter;
    //        Log.d("meet",allMeetings.get(0).getClient_id());
    List<CaseListGetSet> ListOfdataAdapter ;
    private CaseListGetSet getSet;


    EditText editCaseNo;
    EditText conJugeName;
    //SpinKitView spin_kit;

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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView client_name, time, date;
        public ImageView client_info;
        LinearLayout date_layout;

        public MyViewHolder(View view) {
            super(view);
            client_name = (TextView) view.findViewById(R.id.client_name);
            time = (TextView) view.findViewById(R.id.time);
            date = (TextView) view.findViewById(R.id.date);
            client_info = (ImageView) view.findViewById(R.id.client_info);

            date_layout = (LinearLayout) view.findViewById(R.id.date_layout);
            Views(view);

        }
    }
    public void Views(View view){

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
    }


    public EventAdapter(List<EventData> eventDataList, Context context) {
        this.eventDataList = eventDataList;
        this.context = context;

        eventData = null;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
    eventData = eventDataList.get(holder.getAdapterPosition());
     getposition = holder.getAdapterPosition();
     getholder=holder;

    Log.i("mycllccc",eventData.getClientName());
    if (!eventData.getClientName().equals("")) {
        holder.client_name.setText(eventData.getClientName());
    }
    else
    {
        Log.i("yeaaaa","saa");
        holder.client_name.setText(String.format("%s/%s", eventData.getCase_no(), eventData.getYear()));
    }
    Log.i("getemoty",String.format("%s/%s", eventData.getCase_no(), eventData.getYear()));
    holder.time.setText(eventData.getTime());

        holder.date.setText(eventData.getDate());

    switch (eventData.getType()) {
            case "HEARING":
                holder.date_layout.setBackground(context.getResources()
                        .getDrawable(R.drawable.background_red));
                holder.date.setTextColor(Color.parseColor("#ffffff"));
                holder.client_info.setImageResource(R.drawable.casinfo);
             //   holder.client_name.setText(eventData.getClientName());

                break;
            case "COURT_DEDLINE":
                holder.date_layout.setBackground(context.getResources()
                        .getDrawable(R.drawable.background_yellow));
                holder.date.setTextColor(Color.parseColor("#333333"));
                holder.client_info.setImageResource(R.drawable.casinfo);
               // holder.client_name.setText(eventData.getClientName());

                break;
            case "MEETING":
                holder.date_layout.setBackground(context.getResources()
                        .getDrawable(R.drawable.background_blue));
                holder.date.setTextColor(Color.parseColor("#ffffff"));
                holder.client_info.setImageResource(R.drawable.openfile);
                //holder.client_name.setText(eventData.getClientName());

                break;
        }

        holder.client_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                spin_kit.setVisibility(View.VISIBLE);
                if (checkImageResource(context,holder.client_info,R.drawable.openfile))
                {
                    SharedPreferences prefs = context.getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);
                    userid = prefs.getString(AppsContants.USERID, "");
                    allMeetings=new ArrayList<>();
                    getAllMeetings(userid,v);
//                    spin_kit.setVisibility(View.INVISIBLE);


                } else if(checkImageResource(context,holder.client_info,R.drawable.casinfo)){
                    SharedPreferences prefs = context.getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);
                    userid = prefs.getString(AppsContants.USERID, "");
                    ShowCaseList(userid,v);
                  //  spin_kit.setVisibility(View.INVISIBLE);


                }
            }
        });



    }

        @SuppressWarnings("deprecation")
        @SuppressLint("NewApi")
        private static boolean checkImageResource(Context ctx, ImageView imageView,
                                                  int imageResource) {
            boolean result = false;

            if (ctx != null && imageView != null && imageView.getDrawable() != null) {
                Drawable.ConstantState constantState;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    constantState = ctx.getResources()
                            .getDrawable(imageResource, ctx.getTheme())
                            .getConstantState();
                } else {
                    constantState = ctx.getResources().getDrawable(imageResource)
                            .getConstantState();
                }

                if (imageView.getDrawable().getConstantState() == constantState) {
                    result = true;
                }
            }

            return result;
        }

        public void getAllMeetings(String user_id,View v) {
            AndroidNetworking.post(URLHelper.showMeeting)
                    .addBodyParameter("user_id", user_id)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build().getAsJSONArray(new JSONArrayRequestListener() {
                @Override
                public void onResponse(JSONArray response) {
                    allMeetings.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Log.d("ayanader","Adsa");
                            String name = response.getJSONObject(i).getString("event_date");
                            Log.d("nam11e",eventData.getDate());
                            Log.d("orgnam",name);
                            if (name != null && !name.isEmpty() && name.equals(eventData.getDate()))
                                allMeetings.add(new MeetingData(response.getJSONObject(i)
                                        .getString("id"), response.getJSONObject(i)
                                        .getString("user_id"),
                                        response.getJSONObject(i).getString("name"),
                                        response.getJSONObject(i).getString("client_id"),
                                        response.getJSONObject(i).getString("email"),
                                        response.getJSONObject(i).getString("mobile"),
                                        response.getJSONObject(i).getString("place"),
                                        response.getJSONObject(i).getString("latitude"),
                                        response.getJSONObject(i).getString("longitude"),
                                        response.getJSONObject(i).getString("notes"),
                                        response.getJSONObject(i).getString("event_date"),
                                        response.getJSONObject(i).getString("event_time"),
                                        response.getJSONObject(i).getString("remind_date"),
                                        response.getJSONObject(i).getString("remind_time"),
                                        response.getJSONObject(i).getString("strtotime")));
                            SharedPreferences.Editor editor = v.getContext().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                            editor.putString(AppsContants.MEETING_ID, allMeetings.get(0).getId());
                            editor.putString(AppsContants.CLIENT_ID,  allMeetings.get(0).getClient_id());
                            editor.putString(AppsContants.CLIENT_NAME,  allMeetings.get(0).getName());
                            editor.putString(AppsContants.CLIENT_EMAIL,  allMeetings.get(0).getEmail());
                            editor.putString(AppsContants.CLIENT_MOBILE,  allMeetings.get(0).getMobile());
                            editor.putString(AppsContants.CLIENT_NOTES,  allMeetings.get(0).getNotes());
                            editor.putString(AppsContants.EVENT_DATE,  allMeetings.get(0).getEvent_date());
                            editor.putString(AppsContants.EVENT_TIME,  allMeetings.get(0).getEvent_time());
                            editor.putString(AppsContants.REM_EVENT_DATE,  allMeetings.get(0).getRemind_date());
                            editor.putString(AppsContants.REM_EVENT_TIME,  allMeetings.get(0).getRemind_time());
                            editor.putString(AppsContants.Meeting_place,  allMeetings.get(0).getPlace());
                            editor.apply();
                            allMeetings.clear();
                            MeetingInfoFragment.comming_status = "1";
                            NavigationActivity.mToolbar.setTitle(" Информация за среща");
                            NavigationActivity.mToolbar.setLogo(R.drawable.openfile_nav);
                            Fragment fragment = new MeetingInfoFragment();

                            FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        } catch (Exception e) {

                         //   spin_kit.setVisibility(View.GONE);
                        }

                    }
                    //spin_kit.setVisibility(View.GvONE);

    //                meetingAdapter = new MeetingAdapter(allMeetings);
    //                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
    //                recyclerView.setLayoutManager(mLayoutManager);
    //                recyclerView.setAdapter(meetingAdapter);
                }

                @Override
                public void onError(ANError anError) {
                  //
                    //  spin_kit.setVisibility(View.GONE);


                }
            });
        }
        @Override
        public int getItemCount() {
            return eventDataList.size();
        }


    private void ShowCaseList(String userid,View v) {
     //   spin_kit.setVisibility(View.VISIBLE);
        Log.e("hnnn",userid);
        ListOfdataAdapter=new ArrayList<>();

        AndroidNetworking.post(URLHelper.ListCases)
                .addBodyParameter("user_id", userid)
                .setTag("Case List")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                       // ListOfdataAdapter.clear();
                        Log.e("huuuu", "userid");

                        for (int i = 0; i < response.length(); i++) {
                            getSet = new CaseListGetSet();
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = response.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                if (jsonObject.getString("archive").equals("1")) continue;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                getSet.setId(jsonObject.getString("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                getSet.setUserId(jsonObject.getString("user_id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                getSet.setCaseNo(jsonObject.getString("case_no"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                getSet.setYear(jsonObject.getString("year"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                String st_list = jsonObject.getString("case_type");
                                if (JsonUtil.hasValue(st_list)) {
                                    JSONObject case_type = jsonObject.getJSONObject("case_type");
                                    CaseType caseType = new CaseType(
                                            case_type.getString("id"),
                                            case_type.getString("kind")
                                    );
                                    getSet.setCaseType(caseType);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            try {
                                String st_list = jsonObject.getString("instance");
                                if (JsonUtil.hasValue(st_list)) {
                                    JSONObject instance = jsonObject.getJSONObject("instance");
                                    Instance instance1 = new Instance(
                                            instance.getString("id"),
                                            instance.getString("name")
                                    );
                                    getSet.setInstance(instance1);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                getSet.setRefer_case_id(jsonObject.getString("refer_case_id"));
                                getSet.setContractor_judge(jsonObject.getString("contractor_judge"));
                                getSet.setContr_judge_contect(jsonObject.getString("contr_judge_contect"));
                                getSet.setContr_judge_email(jsonObject.getString("contr_judge_email"));
                                getSet.setJudge(jsonObject.getString("judge"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                String st_list = jsonObject.getString("court_id");
                                if (JsonUtil.hasValue(st_list)) {
                                    JSONObject court_id = jsonObject.getJSONObject("court_id");
                                    CourtId courtId = new CourtId(
                                            court_id.getString("id"),
                                            court_id.getString("name"),
                                            court_id.getString("url"),
                                            court_id.getString("status"));

                                    getSet.setCourtId(courtId);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                String st_list = jsonObject.getString("behalf_of");
                                if (JsonUtil.hasValue(st_list)) {
                                    JSONObject behalf_of = jsonObject.getJSONObject("behalf_of");
                                    getSet.setBeHalfOf(new BeHalfOf(
                                            behalf_of.getString("id"),
                                            behalf_of.getString("behalf")
                                    ));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {

                                getSet.setDispute_for(jsonObject.getString("dispute_for"));
                                getSet.setClient_id(jsonObject.getString("client_id"));
                                getSet.setAmount(jsonObject.getString("amount"));
                                getSet.setPrice(jsonObject.getString("price"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                String st_list = jsonObject.getString("type_judgment");
                                if (JsonUtil.hasValue(st_list)) {
                                    JSONObject type_judgment = jsonObject.getJSONObject("type_judgment");
                                    getSet.setTypeJudgment(new TypeJudgment(
                                            type_judgment.getString("id"),
                                            type_judgment.getString("type")
                                    ));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                getSet.setType_judgment_no(jsonObject.getString("type_judgment_no"));
                                getSet.setTook_effect(jsonObject.getString("took_effect"));
                                getSet.setAppeal_caase_n(jsonObject.getString("appeal_caase_n"));
                                getSet.setNotes(jsonObject.getString("notes"));
                                getSet.setStatus(jsonObject.getString("status"));
                                getSet.setArchive(jsonObject.getString("archive"));
                                getSet.setStrtotime(jsonObject.getString("strtotime"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            ArrayList<ClientList> clientList = new ArrayList<>();
                            try {
                                String st_list = jsonObject.getString("client_list");
                                if (JsonUtil.hasValue(st_list)) {
                                    JSONArray client_list = jsonObject.getJSONArray("client_list");
                                    for (int j = 0; j < client_list.length(); j++) {
                                        JSONObject item = client_list.getJSONObject(j);
                                        ClientList clientList1 = new ClientList(
                                                item.getString("id"),
                                                item.getString("user_id"),
                                                item.getString("client_name"),
                                                item.getString("mobile"),
                                                item.getString("email"),
                                                item.getString("notes"),
                                                item.getString("strtotime")
                                        );
                                        clientList.add(clientList1);
                                    }
                                    getSet.setClientList(clientList);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                getSet.setClientList(clientList);
                            }

                            try {
                                ArrayList<RespondList> respondLists = new ArrayList<>();
                                String st_list = jsonObject.getString("respond_list");
                                if (JsonUtil.hasValue(st_list)) {
                                    JSONArray respond_list = jsonObject.getJSONArray("respond_list");
                                    for (int j = 0; j < respond_list.length(); j++) {
                                        JSONObject item = respond_list.getJSONObject(j);
                                        RespondList respondList = new RespondList(
                                                item.getString("id"),
                                                item.getString("case_id"),
                                                item.getString("name"),
                                                item.getString("mobile"),
                                                item.getString("lawyer"),
                                                item.getString("lawyer_mobile"),
                                                item.getString("lawyer_email")
                                        );
                                        respondLists.add(respondList);
                                    }
                                    getSet.setRespondList(respondLists);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                ArrayList<HearingList> hearingLists = new ArrayList<>();
                                String st_list = jsonObject.getString("hearing_list");
                                if (JsonUtil.hasValue(st_list)) {
                                    JSONArray hearing_list = jsonObject.getJSONArray("hearing_list");
                                    for (int j = 0; j < hearing_list.length(); j++) {
                                        JSONObject item = hearing_list.getJSONObject(j);
                                        HearingList hearingList = new HearingList(
                                                item.getString("id"),
                                                item.getString("name")
                                        );
                                        hearingList.setUser_id(item.getString("user_id"));
                                        hearingList.setCase_id(item.getString("case_id"));
                                        hearingList.setClient_id(item.getString("client_id"));
                                        hearingList.setMobile(item.getString("mobile"));
                                        hearingList.setEmail(item.getString("email"));
                                        hearingList.setPlace(item.getString("place"));
                                        hearingList.setLatitude(item.getString("latitude"));
                                        hearingList.setLongitude(item.getString("longitude"));
                                        hearingList.setNotes(item.getString("notes"));
                                        hearingList.setMsg_court(item.getString("msg_court"));
                                        hearingList.setEvent_date(item.getString("event_date"));
                                        hearingList.setEvent_time(item.getString("event_time"));
                                        hearingList.setRemind_date(item.getString("remind_date"));
                                        hearingList.setRemind_time(item.getString("remind_time"));
                                        hearingList.setStrtotime(item.getString("strtotime"));
                                        hearingList.setType(item.getString("type"));
                                        hearingList.setStrtime(item.getString("status"));
                                        hearingList.setShow_on_front(item.getString("show_on_front"));
                                        hearingList.setStrtime(item.getString("strtime"));

                                        hearingLists.add(hearingList);
                                    }
                                    getSet.setHearingList(hearingLists);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                ArrayList<DeadlineList> deadlineLists = new ArrayList<>();
                                String st_list = jsonObject.getString("court_dedline_list");
                                if (JsonUtil.hasValue(st_list)) {
                                    JSONArray court_dedline_list = jsonObject.getJSONArray("court_dedline_list");
                                    for (int j = 0; j < court_dedline_list.length(); j++) {


                                        JSONObject item = court_dedline_list.getJSONObject(j);
                                        DeadlineList deadlineList = new DeadlineList(
                                                item.getString("id"),
                                                item.getString("name")
                                        );
                                        deadlineList.setUser_id(item.getString("user_id"));
                                        deadlineList.setCase_id(item.getString("case_id"));
                                        deadlineList.setClient_id(item.getString("client_id"));
                                        deadlineList.setMobile(item.getString("mobile"));
                                        deadlineList.setEmail(item.getString("email"));
                                        deadlineList.setPlace(item.getString("place"));
                                        deadlineList.setLatitude(item.getString("latitude"));
                                        deadlineList.setLongitude(item.getString("longitude"));
                                        deadlineList.setNotes(item.getString("notes"));
                                        deadlineList.setMsg_court(item.getString("msg_court"));
                                        deadlineList.setEvent_date(item.getString("event_date"));
                                        deadlineList.setEvent_time(item.getString("event_time"));
                                        deadlineList.setRemind_date(item.getString("remind_date"));
                                        deadlineList.setRemind_time(item.getString("remind_time"));
                                        deadlineList.setStrtotime(item.getString("strtotime"));
                                        deadlineList.setType(item.getString("type"));
                                        deadlineList.setStrtime(item.getString("status"));
                                        deadlineList.setShow_on_front(item.getString("show_on_front"));
                                        deadlineList.setStrtime(item.getString("strtime"));

                                        deadlineLists.add(deadlineList);
                                    }
                                    getSet.setDeadlineList(deadlineLists);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {

                                JSONArray client_list = jsonObject.getJSONArray("client_list");
                                if (client_list != null) {
                                    if (client_list.length() > 0) {
                                        JSONObject clientObject = client_list.getJSONObject(0);
                                        getSet.setClientName(clientObject.getString("client_name"));
                                    } else {
                                        getSet.setClientName("");
                                    }
                                } else {
                                    getSet.setClientName("");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {

                                JSONArray hearing_list = jsonObject.getJSONArray("hearing_list");
                                if (hearing_list != null) {
                                    if (hearing_list.length() > 0) {
                                        JSONObject clientObject = hearing_list.getJSONObject(0);
                                        getSet.setHearing(clientObject.getString("event_date"));
                                    } else {
                                        getSet.setHearing("");
                                    }
                                } else {
                                    getSet.setHearing("");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray court_dedline_list = jsonObject.getJSONArray("court_dedline_list");
                                if (court_dedline_list != null) {
                                    if (court_dedline_list.length() > 0) {
                                        JSONObject clientObject = court_dedline_list.getJSONObject(0);
                                        getSet.setDeadline(clientObject.getString("event_date"));
                                    } else {
                                        getSet.setDeadline("");
                                    }
                                } else {
                                    getSet.setDeadline("");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            ListOfdataAdapter.add(getSet);


                         //   replicadata=ListOfdataAdapter;
                           // replicadata.add(ListOfdataAdapter.get(i));
                        }
                        Log.d("usssr",String.valueOf(ListOfdataAdapter.size()));
                        if (ListOfdataAdapter.size()!=0) {
                            dataAdapterOBJ = ListOfdataAdapter.get(getposition);
                            Log.i("possitt",String.valueOf(getposition));
                            getDataAdapterOBJ=dataAdapterOBJ;

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
                        else {
                            Toast.makeText(context, "i'm empty", Toast.LENGTH_SHORT).show();
                        }


                       // replicadata=ListOfdataAdapter;
                    }
//

//                    }

                    @Override
                    public void onError(ANError anError) {
                       // spin_kit.setVisibility(View.GONE);
                        Log.e("dfjdsghkjf", anError.getMessage());
                    }
                });

               // return ListOfdataAdapter;
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
       //ShowCaseList();
        //getAllMeetings(userid);
        super.onViewRecycled(holder);
    }
}