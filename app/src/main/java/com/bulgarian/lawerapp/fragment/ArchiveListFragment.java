package com.bulgarian.lawerapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bulgarian.lawerapp.adapters.ArchiveListAdapter;
import com.bulgarian.lawerapp.model.caseinfo.BeHalfOf;
import com.bulgarian.lawerapp.model.caseinfo.CaseType;
import com.bulgarian.lawerapp.model.caseinfo.Instance;
import com.bulgarian.lawerapp.model.caseinfo.TypeJudgment;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.other.JsonUtil;
import com.bulgarian.lawerapp.other.URLHelper;
import com.bulgarian.lawerapp.adapters.CaseListGetSet;
import com.bulgarian.lawerapp.model.caseinfo.ClientList;
import com.bulgarian.lawerapp.model.caseinfo.CourtId;
import com.bulgarian.lawerapp.model.caseinfo.DeadlineList;
import com.bulgarian.lawerapp.model.caseinfo.HearingList;
import com.bulgarian.lawerapp.model.caseinfo.RespondList;
import com.bulgarian.lawerapp.R;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ArchiveListFragment extends Fragment {


    List<CaseListGetSet> ListOfdataAdapter;
    RecyclerView.LayoutManager layoutManagerOfrecyclerView;
    RecyclerView.Adapter recyclerViewadapter;
    ImageView search_button;
    EditText search_edittext;
    ArchiveListAdapter archiveListAdapter;
    //ArchiveListAdapter archiveListAdapter;

    RecyclerView recyclerView;
    SpinKitView spin_kit;
    String userid = "";

    private List<CaseListGetSet> allMeetings = new ArrayList<>() ;

    public static String comming_status = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_archive_list, container, false);
      //  archiveListAdapter = new ArchiveListAdapter(allMeetings,getA);

        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);

        userid = prefs.getString(AppsContants.USERID, "");
        recyclerView = view.findViewById(R.id.recyclerview1);
        spin_kit = view.findViewById(R.id.spin_kit);
        archiveListAdapter=new ArchiveListAdapter(ListOfdataAdapter,getActivity());
        search_button = (ImageView) view.findViewById(R.id.search_button);
        search_edittext = (EditText) view.findViewById(R.id.search_edittext);
        recyclerView.setHasFixedSize(true);
        layoutManagerOfrecyclerView = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);



        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length() == 0) {


                    if (search_edittext.isFocusable()) {
                        // allcases = new ArrayList<>();
                        //getAllCases(userid);
                        ShowCaseList();
                    }

                } else {
                    CharSequence name = charSequence.toString();
                    filter(name);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }


        });


        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (search_edittext.getText().length() != 0) {

                    CharSequence name = search_edittext.getText().toString();
                    filter(name);
                    Toast.makeText(getActivity(),"Button Added",Toast.LENGTH_SHORT).show();
                } else {
                    search_edittext.requestFocus();
                    showSnackBar(view, "Field cannot be blank");
                }

            }
        });


        return view;
    }
    public void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message,Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
        snackbar.show();
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public void filter(CharSequence clientName) {
        ArrayList<CaseListGetSet> filtered = new ArrayList<>();
        int index=0;

        for (CaseListGetSet town : ListOfdataAdapter) {
            Log.d("sizzzzz",String.valueOf(ListOfdataAdapter.size()));
            if(isNumeric(clientName.toString()))
            {
                if (town.getCaseNo().contains(clientName.toString())){
                    Log.i("matchingcaseno",town.getCaseNo());
                    filtered.add(town);
                }

            }
            else if(!isNumeric(clientName.toString())) {
                //Toast.makeText(getActivity(),"alpha",Toast.LENGTH_SHORT).show();
                // filtered=new =ArrayList<>();
                Log.i("insideelse_if","mmk");
                if(!town.getClientList().isEmpty()) {
                    Log.i("insidsec_if",String.valueOf(index));
                    for(int i=0;i<town.getClientList().size();i++)
                    {
                        // if (i <= town.getClientList().size()) {
                        Log.i("inside3rfif",town.getClientList().get(i).getClient_name());
                        if (town.getClientList().get(i).getClient_name().contains(clientName.toString())) {
                            Log.i("matchingclient", town.getClientList().get(i).getClient_name());
                            filtered.add(town);
                        }
                    }
                }
            }
            index++;
        }
        ListOfdataAdapter.clear();
        ListOfdataAdapter.addAll(filtered);
        archiveListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        ShowCaseList();
        super.onResume();
    }

    public void ShowCaseList() {
        spin_kit.setVisibility(View.VISIBLE);

        AndroidNetworking.post(URLHelper.ListCases)
                .addBodyParameter("user_id", userid)
                .setTag("Case List")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ListOfdataAdapter = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                if (jsonObject.getString("archive").equals("0")) continue;

                                CaseListGetSet getSet = new CaseListGetSet();
                                getSet.setId(jsonObject.getString("id"));
                                getSet.setUserId(jsonObject.getString("user_id"));
                                getSet.setCaseNo(jsonObject.getString("case_no"));
                                getSet.setYear(jsonObject.getString("year"));

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


                                try {
                                    ArrayList<ClientList> clientList = new ArrayList<>();
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
                            }
                            spin_kit.setVisibility(View.GONE);
                            recyclerViewadapter = new ArchiveListAdapter(ListOfdataAdapter, getActivity());
                            recyclerView.setAdapter(recyclerViewadapter);
                        } catch (Exception ex) {
                            spin_kit.setVisibility(View.GONE);
                            Log.e("dfjdsghkjfsdf", ex.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        spin_kit.setVisibility(View.GONE);
                        Log.e("dfjdsghkjfsdf", anError.getMessage());
                    }
                });


    }





}
