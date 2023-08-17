package com.bulgarian.lawerapp.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bulgarian.lawerapp.adapters.ClientRecyclerViewAdapter;
import com.bulgarian.lawerapp.adapters.OppositeRecyclerViewAdapter;
import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.other.JsonUtil;
import com.bulgarian.lawerapp.other.URLHelper;
import com.bulgarian.lawerapp.adapters.CaseListGetSet;
import com.bulgarian.lawerapp.adapters.DateAdapter;
import com.bulgarian.lawerapp.model.DialogData;
import com.bulgarian.lawerapp.model.caseinfo.BeHalfOf;
import com.bulgarian.lawerapp.model.caseinfo.CaseType;
import com.bulgarian.lawerapp.model.caseinfo.ClientList;
import com.bulgarian.lawerapp.model.caseinfo.CourtId;
import com.bulgarian.lawerapp.model.caseinfo.DeadlineList;
import com.bulgarian.lawerapp.model.caseinfo.HearingList;
import com.bulgarian.lawerapp.model.caseinfo.Instance;
import com.bulgarian.lawerapp.model.caseinfo.RespondList;
import com.bulgarian.lawerapp.model.caseinfo.TypeJudgment;
import com.bulgarian.lawerapp.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class CaseListDetail extends Fragment {

    public static String comming_status = "";

    CaseListGetSet case_;
    TextView txtCaseNo, txtCaseType, tvInstances, tvJudge, tvCourtCount, tvBehalf, tvDispute_for, tvAmount;
    TextView tvRest, tvPrice, tvPaid;

    TextView tvNote, tvNumber, tvTookEffect, tvAppeal, tvExamCase, tv_scheduling, tv_deadline;

    LinearLayout llInstances;
    ImageView ivInstancesTel, ivInstancesEmail;
    TextView tvInstancesName, tvInstancesTel, tvInstancesEmail;
    ClientRecyclerViewAdapter adapterClient;
    OppositeRecyclerViewAdapter adapterOpposite;
    RecyclerView recyclerViewClient;
    RecyclerView recyclerViewOpposite;
    ImageView ivExamCase;
    RelativeLayout rlExamCase;
    CaseListGetSet ref_case;
    LinearLayout ll_add_scheduling, ll_add_client, ll_add_price, ll_add_opposite, ll_edit,
            ll_add_legal_notice, ll_mark_as_done, ll_success, ll_failed, ll_add_to_archive;

    RelativeLayout rlClient;
    LinearLayout llClient;
    AutoCompleteTextView spinClient;
    ArrayList<String> clientNameList;
    ArrayList<ClientList> clientsList;
    ArrayList<String> selectedClientIdList = new ArrayList<>();
    ArrayList<DialogData> listHearing;
    ArrayList<DialogData> listDeadline;
    ClientList clientList = null;


    //
    RecyclerView listMarkDone, listScheduling;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_case_list_detail, container, false);

        String str_object = getArguments().getString("CASE");
        case_ = new Gson().fromJson(str_object, CaseListGetSet.class);

        findViews(view);
        setCaseValues();
        setClientList();
        setPrices();
        setOppositeList();
        setothers();
        setContractor();

        listeners();
        return view;
    }

    private void listeners() {
        ivExamCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = v.getContext().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                editor.putString(AppsContants.CaseId, ref_case.getId());
                editor.putString(AppsContants.CaseNum, ref_case.getCaseNo());
                editor.putString(AppsContants.CaseYear, ref_case.getYear());
                editor.apply();

                Bundle bundle = new Bundle();
                bundle.putString("CASE", new Gson().toJson(ref_case));

                CaseListDetail.comming_status = "1";
                NavigationActivity.mToolbar.setTitle("Информация за дело");
                Fragment fragment = new CaseListDetail();
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        setHearingList();



        tv_scheduling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonUtil.setDialog(getActivity(), "Насрочване", "Насрочване",
                        "Напомняне", "Статус", R.drawable.add_court_hearing_icon,
                        listHearing);
            }
        });

        setCortMessageList();

        tv_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonUtil.setDialog(getActivity(), "Съдебно съобщение", "Краен срок",
                        "Напомняне", "съобщение", R.drawable.table_add_icon,
                        listDeadline);
            }
        });


        ll_add_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "ll_add_price", Toast.LENGTH_SHORT).show();
            }
        });

        ll_add_opposite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "ll_add_opposite", Toast.LENGTH_SHORT).show();
            }

        });

        ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "ll_edit", Toast.LENGTH_SHORT).show();
            }
        });


        ll_mark_as_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "ll_mark_as_done", Toast.LENGTH_SHORT).show();
            }
        });

        ll_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "ll_success", Toast.LENGTH_SHORT).show();
            }
        });

        ll_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "ll_failed", Toast.LENGTH_SHORT).show();
            }
        });

        ll_add_to_archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "ll_add_to_archive", Toast.LENGTH_SHORT).show();
            }
        });

        addClientListener();
        addHearingAndMessage();
    }

    private void setCortMessageList(){
        listDeadline = new ArrayList<>();
        ArrayList<DialogData> listMark = new ArrayList<>();
        long current_time = System.currentTimeMillis();

        if (case_.getDeadlineList() != null) {
            if (JsonUtil.hasString(case_.getDeadlineList().toString())) {
                for (int i = 0; i < case_.getDeadlineList().size(); i++) {
                    DialogData dialogData = new DialogData(
                            case_.getDeadlineList().get(i).getEvent_date().replace("-", ".") + " " +
                                    case_.getDeadlineList().get(i).getEvent_time() + " ч",

                            case_.getDeadlineList().get(i).getRemind_date().replace("-", ".") + " " +
                                    case_.getDeadlineList().get(i).getRemind_time() + " ч",
                            case_.getDeadlineList().get(i).getMsg_court()
                    );

                    String str_date = case_.getDeadlineList().get(i).getEvent_date() + " " +
                            case_.getDeadlineList().get(i).getEvent_time();

                    long date = JsonUtil.dateToMiliSconds(str_date);

                    if (date >= current_time) {
                        DialogData date1 = new DialogData(
                                "Краен срок:",
                                case_.getDeadlineList().get(i).getEvent_date().replace("-", "."),
                                case_.getDeadlineList().get(i).getEvent_time() + " ч"
                        );

                        DialogData date2 = new DialogData(
                                "Напомняне:",
                                case_.getDeadlineList().get(i).getRemind_date().replace("-", "."),
                                case_.getDeadlineList().get(i).getRemind_time() + " ч"
                        );

                        listMark.add(date1);
                        listMark.add(date2);
                    }

                    listDeadline.add(dialogData);

                }
            }
        }
        RecyclerView.LayoutManager layoutManagerOfrecyclerView1;
        RecyclerView.Adapter recyclerViewadapter1;
        listMarkDone.setHasFixedSize(true);
        layoutManagerOfrecyclerView1 = new LinearLayoutManager(getActivity());
        listMarkDone.setLayoutManager(layoutManagerOfrecyclerView1);
        recyclerViewadapter1 = new DateAdapter(listMark, getActivity());
        listMarkDone.setAdapter(recyclerViewadapter1);
    }

    private void setHearingList(){
        listHearing = new ArrayList<>();
        ArrayList<DialogData> listSchedul = new ArrayList<>();
        long current_time = System.currentTimeMillis();

        if (case_.getHearingList() != null) {
            if (JsonUtil.hasString(case_.getHearingList().toString())) {
                for (int i = 0; i < case_.getHearingList().size(); i++) {
                    DialogData dialogData = new DialogData(
                            case_.getHearingList().get(i).getEvent_date().replace("-", ".") + " " +
                                    case_.getHearingList().get(i).getEvent_time() + " ч",

                            case_.getHearingList().get(i).getRemind_date().replace("-", ".") + " " +
                                    case_.getHearingList().get(i).getRemind_time() + " ч",
                            "Изпълнено"
                    );

                    String str_date = case_.getHearingList().get(i).getEvent_date() + " " +
                            case_.getHearingList().get(i).getEvent_time();

                    long date = JsonUtil.dateToMiliSconds(str_date);

                    if (date >= current_time) {
                        DialogData date1 = new DialogData(
                                "Насрочване:",
                                case_.getHearingList().get(i).getEvent_date().replace("-", "."),
                                case_.getHearingList().get(i).getEvent_time() + " ч"
                        );

                        DialogData date2 = new DialogData(
                                "Напомняне:",
                                case_.getHearingList().get(i).getRemind_date().replace("-", "."),
                                case_.getHearingList().get(i).getRemind_time() + " ч"
                        );
                        listSchedul.add(date1);
                        listSchedul.add(date2);
                    }

                    listHearing.add(dialogData);


                }
            }
        }

        RecyclerView.LayoutManager layoutManagerOfrecyclerView;
        RecyclerView.Adapter recyclerViewadapter;
        listScheduling.setHasFixedSize(true);
        layoutManagerOfrecyclerView = new LinearLayoutManager(getActivity());
        listScheduling.setLayoutManager(layoutManagerOfrecyclerView);
        recyclerViewadapter = new DateAdapter(listSchedul, getActivity());
        listScheduling.setAdapter(recyclerViewadapter);
    }

    private void findViews(View view) {
        //casetxtJudge
        txtCaseNo = view.findViewById(R.id.txtCaseNo);
        txtCaseType = view.findViewById(R.id.txtCaseType);
        tvInstances = view.findViewById(R.id.tvInstances);
        tvJudge = view.findViewById(R.id.tvJudge);
        tvCourtCount = view.findViewById(R.id.tvCourtCount);
        tvBehalf = view.findViewById(R.id.tvBehalf);
        tvDispute_for = view.findViewById(R.id.tvDispute_for);
        tvAmount = view.findViewById(R.id.tvAmount);

        //client list
        recyclerViewClient = view.findViewById(R.id.rvClient);
        listMarkDone = view.findViewById(R.id.listMarkDone);
        listScheduling = view.findViewById(R.id.listScheduling);

        //price
        tvPaid = view.findViewById(R.id.tvPaid);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvRest = view.findViewById(R.id.tvRest);

        //opposite_side
        recyclerViewOpposite = view.findViewById(R.id.rvOpposite);

        //others
        tvNote = view.findViewById(R.id.tvNote);
        tvNumber = view.findViewById(R.id.tvNumber);
        tvNumber.setInputType(InputType.TYPE_CLASS_PHONE);
        tvTookEffect = view.findViewById(R.id.tvTookEffect);
        tvAppeal = view.findViewById(R.id.tvAppeal);

        //exam case
        ivExamCase = view.findViewById(R.id.ivExamCase);
        tvExamCase = view.findViewById(R.id.tvExamCase);
        rlExamCase = view.findViewById(R.id.rlExamCase);


        //clickable views
        tv_scheduling = view.findViewById(R.id.tv_scheduling);
        tv_deadline = view.findViewById(R.id.tv_deadline);

        ll_add_scheduling = view.findViewById(R.id.ll_add_scheduling);
        ll_add_client = view.findViewById(R.id.ll_add_client);
        ll_add_price = view.findViewById(R.id.ll_add_price);
        ll_add_opposite = view.findViewById(R.id.ll_add_opposite);
        ll_edit = view.findViewById(R.id.ll_edit);
        ll_add_legal_notice = view.findViewById(R.id.ll_add_legal_notice);
        ll_mark_as_done = view.findViewById(R.id.ll_mark_as_done);
        ll_success = view.findViewById(R.id.ll_success);
        ll_failed = view.findViewById(R.id.ll_failed);
        ll_add_to_archive = view.findViewById(R.id.ll_add_to_archive);

        //add client views
        rlClient = view.findViewById(R.id.rlClient);
        llClient = view.findViewById(R.id.llClient);
        spinClient = view.findViewById(R.id.spinClient);

        //Contractor
        llInstances = view.findViewById(R.id.llInstances);
        ivInstancesTel = view.findViewById(R.id.ivInstancesTel);
        ivInstancesEmail = view.findViewById(R.id.ivInstancesEmail);
        tvInstancesName = view.findViewById(R.id.tvInstancesName);
        tvInstancesTel = view.findViewById(R.id.tvInstancesTel);
        tvInstancesTel.setInputType(InputType.TYPE_CLASS_PHONE);
        tvInstancesEmail = view.findViewById(R.id.tvInstancesEmail);

    }

    private void setContractor() {
        if (JsonUtil.hasString(case_.getContractor_judge()) || JsonUtil.hasString(case_.getContr_judge_email())
                || JsonUtil.hasString(case_.getContr_judge_contect()))
            llInstances.setVisibility(View.VISIBLE);

        if (JsonUtil.hasString(case_.getContractor_judge()))
            tvInstancesName.setText(case_.getContractor_judge());
        if (JsonUtil.hasString(case_.getContr_judge_contect()))
            tvInstancesTel.setText(case_.getContr_judge_contect());
        if (JsonUtil.hasString(case_.getContr_judge_email()))
            tvInstancesEmail.setText(case_.getContr_judge_email());

        ivInstancesEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonUtil.openEmail(getActivity(), case_.getContr_judge_email());
            }
        });

        ivInstancesTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonUtil.makeCall(getActivity(), case_.getContr_judge_contect());
            }
        });
    }

    private void addHearingAndMessage() {
        ll_add_legal_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddDialog("Добави съдебно съобщение", "Краен срок:",
                        "Напомняне:", R.drawable.table_add_icon, true);
            }
        });

        ll_add_scheduling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddDialog("Добави насрочване", "Насрочване:",
                        "Напомняне:", R.drawable.add_court_hearing_icon, false);
            }
        });
    }

    private void addClientListener() {

        markClients();
        getClients();

        ll_add_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlClient.setVisibility(View.VISIBLE);
            }
        });

        spinClient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i,
                                    long arg3) {
                for (int j = 0; j < clientNameList.size(); j++) {
                    if (clientNameList.get(j).equals(adapterView.getItemAtPosition(i).toString())) {
                        clientList = clientsList.get(j);
                        break;
                    }
                }
                makeClientNameButton(llClient, adapterView.getItemAtPosition(i).toString(),
                        clientList);
                spinClient.setText("");
                SharedPreferences prefs = getActivity().getSharedPreferences(
                        AppsContants.MyPREFERENCES, MODE_PRIVATE);

                addClient(prefs.getString(AppsContants.USERID, ""),
                        clientList.getMobile(),
                        clientList.getEmail(),
                        clientList.getNotes(),
                        clientList.getClient_name());
            }
        });
    }

    private void setothers() {
        if (JsonUtil.hasString(case_.getPrice())) tvPrice.setText(case_.getPrice());
        if (JsonUtil.hasString(case_.getNotes())) tvNote.setText(case_.getNotes());

        String number = "";
        if (case_.getTypeJudgment() != null)
            if (JsonUtil.hasValue(case_.getTypeJudgment().toString())) {
                if (JsonUtil.hasString(case_.getTypeJudgment().getType()))
                    number = case_.getTypeJudgment().getType();
            }
        if (JsonUtil.hasString(case_.getType_judgment_no()))
            number += "/" + case_.getType_judgment_no();
        tvNumber.setText(number);

        if (JsonUtil.hasString(case_.getTook_effect()))
            tvTookEffect.setText(case_.getTook_effect().replaceAll("-", "."));

        if (JsonUtil.hasString(case_.getAppeal_caase_n()))
            tvAppeal.setText(case_.getAppeal_caase_n());


        if (JsonUtil.hasString(case_.getRefer_case_id())) {
            rlExamCase.setVisibility(View.VISIBLE);
            getReferCase(case_.getRefer_case_id());
        }
    }

    private void setClientList() {
        recyclerViewClient.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (case_.getClientList() != null)
            if (JsonUtil.hasValue(case_.getClientList().toString())) {
                adapterClient = new ClientRecyclerViewAdapter(getActivity(), case_.getClientList());
                recyclerViewClient.setAdapter(adapterClient);
            }
    }

    private void setOppositeList() {
        recyclerViewOpposite.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (case_.getRespondList() != null)
            if (JsonUtil.hasValue(case_.getRespondList().toString())) {
                adapterOpposite = new OppositeRecyclerViewAdapter(getActivity(), case_.getRespondList());
                recyclerViewOpposite.setAdapter(adapterOpposite);
            }
    }

    private void setPrices() {
        String currency = getString(R.string.лв);
        if (JsonUtil.hasString(case_.getPrice())) tvPrice.setText(case_.getPrice() + currency);
        if (JsonUtil.hasString(case_.getPrice())) tvPaid.setText(case_.getPrice() + currency);
        tvRest.setText("0" + currency);
    }

    @SuppressLint("SetTextI18n")
    private void setCaseValues() {
        String str_id_years = "";
        if (JsonUtil.hasString(case_.getCaseNo())) str_id_years = case_.getCaseNo();
        if (JsonUtil.hasString(case_.getYear())) str_id_years += "/" + case_.getYear();
        txtCaseNo.setText(str_id_years);

        if (case_.getCaseType() != null)
            if (JsonUtil.hasValue(case_.getCaseType().toString())) {
                if (JsonUtil.hasString(case_.getCaseType().getKind()))
                    txtCaseType.setText(case_.getCaseType().getKind());
            }

        if (case_.getInstance() != null)
            if (JsonUtil.hasValue(case_.getInstance().toString())) {
                if (JsonUtil.hasString(case_.getInstance().getName()))
                    tvInstances.setText(case_.getInstance().getName());
            }

        if (case_.getCourtId() != null)
            if (JsonUtil.hasValue(case_.getCourtId().toString())) {
                String court_info = "", court_url = "";
                if (JsonUtil.hasString(case_.getCourtId().getName()))
                    court_info = case_.getCourtId().getName();
                if (JsonUtil.hasString(case_.getCourtId().getUrl()))
                    court_url = case_.getCourtId().getUrl();

                Log.i("getans",String.format("%s%s", court_info, court_url));
                tvJudge.setText(String.format("%s%s", court_info, court_url));
               // tvJudge.setText(Html.fromHtml(court_info + "<br>" + court_url));
            }

        if (JsonUtil.hasString(case_.getJudge())) tvCourtCount.setText(case_.getJudge());

        if (case_.getBeHalfOf() != null)
            if (JsonUtil.hasValue(case_.getBeHalfOf().toString())) {
                if (JsonUtil.hasString(case_.getBeHalfOf().getBehalf()))
                    tvBehalf.setText(case_.getBeHalfOf().getBehalf());
            }

        if (JsonUtil.hasString(case_.getDispute_for()))
            tvDispute_for.setText(case_.getDispute_for());
        if (JsonUtil.hasString(case_.getAmount()))
            tvAmount.setText(case_.getAmount() + getString(R.string.лв));
    }

    public void getReferCase(String refer_case_id) {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                AppsContants.MyPREFERENCES, MODE_PRIVATE);
        String userid = prefs.getString(AppsContants.USERID, "");

        AndroidNetworking.post(URLHelper.ListCases)
                .addBodyParameter("user_id", userid)
                .setTag("Case List")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                if (refer_case_id.equals(jsonObject.getString("id"))) {

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
                                    ref_case = getSet;
                                    tvExamCase.setText(ref_case.CaseNo + " / " + ref_case.getYear());
                                }


                            }

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

    public void markClients() {
        if (case_.getClientList() != null)
            if (!case_.getClientList().toString().isEmpty() ||
                    !case_.getClientList().toString().equals("null")) {
                for (int j = 0; j < case_.getClientList().size(); j++) {
                    makeClientNameButton(llClient, case_.getClientList().get(j).getClient_name(),
                            case_.getClientList().get(j));
                }
            }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void removeFromClienList(String strId) {
        if (case_.getClientList() == null) return;
        if (case_.getClientList().size() == 0) return;

        ArrayList<ClientList> lists = case_.getClientList();
        for (int i = 0; i < case_.getClientList().size(); i++) {
            if (case_.getClientList().get(i).getId().equals(strId)) {
                lists.remove(i);
                deleteClient(strId);
            }
        }

        case_.setClientList(lists);
        setClientList();

    }

    private void makeClientNameButton(LinearLayout linearLayout, String text, ClientList client) {
        selectedClientIdList.add(client.getId());

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

        btnTag.setId(Integer.parseInt(client.getId()));
        btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < selectedClientIdList.size(); i++) {
                    if (selectedClientIdList.get(i).equals(String.valueOf(btnTag.getId()))) {
                        rlClient.setVisibility(View.GONE);
                        removeFromClienList(selectedClientIdList.get(i));
                        selectedClientIdList.remove(i);
                        break;
                    }
                }
                linearLayout.removeView(btnTag);
            }
        });
        linearLayout.addView(btnTag);
    }


    public void setAddDialog(String title, String title_1,
                             String title_2, int image_id, boolean is_add_message) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_add_dialog);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        TextView tv_title = dialog.findViewById(R.id.tv_title);
        TextView tvRow_1 = dialog.findViewById(R.id.tvRow_1);
        TextView tvRow_2 = dialog.findViewById(R.id.tvRow_2);

        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        ImageView iv_icon = dialog.findViewById(R.id.iv_icon);
        ImageView ivAdd = dialog.findViewById(R.id.ivAdd);

        iv_icon.setImageResource(image_id);
        tv_title.setText(title);
        tvRow_1.setText(title_1);
        tvRow_2.setText(title_2);

        EditText etNote = dialog.findViewById(R.id.etNote);
        EditText etDateRow_1 = dialog.findViewById(R.id.etDateRow_1);
        EditText etTimeRow_1 = dialog.findViewById(R.id.etTimeRow_1);
        EditText etDateRow_2 = dialog.findViewById(R.id.etDateRow_2);
        EditText etTimeRow_2 = dialog.findViewById(R.id.etTimeRow_2);

        etDateRow_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] months = {""};
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
                                etDateRow_1.setText(dayOfMonth + "." + months[0] + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        etDateRow_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] months = {""};
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
                                etDateRow_2.setText(dayOfMonth + "." + months[0] + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        etTimeRow_1.setOnClickListener(new View.OnClickListener() {
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
                                etTimeRow_1.setText(hour[0] + ":" + minutes[0]);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });
        etTimeRow_2.setOnClickListener(new View.OnClickListener() {
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
                                etTimeRow_2.setText(hour[0] + ":" + minutes[0]);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        if (is_add_message) {
            etNote.setVisibility(View.VISIBLE);
        } else {
            etNote.setVisibility(View.GONE);
        }

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String case_id = case_.getId();
                String user_id = case_.getUserId();
                String msg_court = etNote.getText().toString();

                String msg_dedline_date = etDateRow_1.getText().toString();
                String msg_dedline_time = etTimeRow_1.getText().toString();
                String dedline_remind_date = etDateRow_2.getText().toString();
                String dedline_remind_time = etTimeRow_2.getText().toString();

                if (msg_dedline_date.contains("."))
                    msg_dedline_date = msg_dedline_date.replaceAll("\\.", "-");

                if (dedline_remind_date.contains("."))
                    dedline_remind_date = dedline_remind_date.replaceAll("\\.", "-");


                if (is_add_message) {
                    addCortMessage(case_id, user_id, msg_court, msg_dedline_date,
                            msg_dedline_time, dedline_remind_date, dedline_remind_time);
                } else {
                    addCortHearing(case_id, user_id, msg_dedline_date,
                            msg_dedline_time, dedline_remind_date, dedline_remind_time);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void getClients() {
        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);
        String strUserId = prefs.getString(AppsContants.USERID, "");
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
                            clientsList = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String strID = jsonObject.getString("id");
                                String strClient = jsonObject.getString("client_name");
                                clientNameList.add(strClient);

                                ClientList clientList = new ClientList(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("user_id"),
                                        jsonObject.getString("client_name"),
                                        jsonObject.getString("mobile"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("notes"),
                                        jsonObject.getString("strtotime")
                                );
                                clientsList.add(clientList);
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_item, clientNameList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinClient.setThreshold(1);
                            spinClient.setAdapter(dataAdapter);


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


    public void deleteClient(String client_id) {

        AndroidNetworking.post(URLHelper.delete_client)
                .addBodyParameter("client_id", client_id)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject response) {

                if (response.has("id")) {

                }
            }

            @Override
            public void onError(ANError anError) {
            }
        });


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

                }
            }

            @Override
            public void onError(ANError anError) {
            }
        });


    }

    public void addCortHearing(String case_id, String user_id,
                               String hearing_date, String hearing_time,
                               String hear_remind_date, String hear_remind_time) {

        AndroidNetworking.post(URLHelper.AddCortHearing)
                .addBodyParameter("case_id", case_id)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("hearing_date", hearing_date)
                .addBodyParameter("hearing_time", hearing_time)
                .addBodyParameter("hear_remind_date", hear_remind_date)
                .addBodyParameter("hear_remind_time", hear_remind_time)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject response) {
                ArrayList<HearingList> lists = case_.getHearingList();
                HearingList hearingList = new HearingList(case_id, "name");
                hearingList.setEvent_date(hearing_date);
                hearingList.setEvent_time(hearing_time);
                hearingList.setRemind_date(hear_remind_date);
                hearingList.setRemind_time(hear_remind_time);
                hearingList.setClient_id(user_id);
                hearingList.setCase_id(case_id);

                lists.add(hearingList);
                case_.setHearingList(lists);
                setHearingList();
            }

            @Override
            public void onError(ANError anError) {
                Log.d("EROR", anError.getErrorDetail());
            }
        });


    }

    public void addCortMessage(String case_id, String user_id, String msg_court,
                               String msg_dedline_date, String msg_dedline_time,
                               String dedline_remind_date, String dedline_remind_time) {

        AndroidNetworking.post(URLHelper.AddCortMessage)
                .addBodyParameter("case_id", case_id)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("msg_court", msg_court)
                .addBodyParameter("msg_dedline_date", msg_dedline_date)
                .addBodyParameter("msg_dedline_time", msg_dedline_time)
                .addBodyParameter("dedline_remind_date", dedline_remind_date)
                .addBodyParameter("dedline_remind_time", dedline_remind_time)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject response) {

                ArrayList<DeadlineList> lists = case_.getDeadlineList();
                DeadlineList deadlineList = new DeadlineList(case_id, "name");
                deadlineList.setEvent_date(msg_dedline_date);
                deadlineList.setEvent_time(msg_dedline_time);
                deadlineList.setRemind_date(dedline_remind_date);
                deadlineList.setRemind_time(dedline_remind_time);
                deadlineList.setClient_id(user_id);
                deadlineList.setCase_id(case_id);

                lists.add(deadlineList);

                case_.setDeadlineList(lists);
                setCortMessageList();
            }

            @Override
            public void onError(ANError anError) {
                Log.d("EROR", anError.getErrorDetail());
            }
        });


    }
}
