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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.other.URLHelper;
import com.bulgarian.lawerapp.adapters.ClientAdapter;
import com.bulgarian.lawerapp.model.ClientData;
import com.bulgarian.lawerapp.R;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class ClientListFragment extends Fragment {

    private ArrayList<ClientData> allClients = new ArrayList<>();
    String userid = "";

    SpinKitView spin_kit;
    RecyclerView recyclerView;
    ClientAdapter clientAdapter;

    ImageView search_button;
    EditText search_edittext;

    RelativeLayout add_client_relative;
    public static  String comming_status="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_list, container, false);
        search_button = (ImageView) view.findViewById(R.id.search_button);
        search_edittext = (EditText) view.findViewById(R.id.search_edittext);
        add_client_relative = (RelativeLayout) view.findViewById(R.id.add_client_relative);
        spin_kit = (SpinKitView) view.findViewById(R.id.spin_kit);
        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);

        userid = prefs.getString(AppsContants.USERID, "");

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        add_client_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddClientFragment.comming_status = "1";

                NavigationActivity.mToolbar.setTitle("Add Client");
                NavigationActivity.mToolbar.setLogo(R.drawable.addclient_nav);
                Fragment fragment = new AddClientFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (charSequence.toString().length() == 0) {


                    if (search_edittext.isFocusable()) {
                        allClients = new ArrayList<>();
                        getAllClients(userid);
                    }

                }else {

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
                } else {

                    search_edittext.requestFocus();

                    showSnackBar(view, "Field cannot be blank");
                }

            }
        });


        return view;
    }


    public void getAllClients(String user_id) {
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
                        allClients.add(new ClientData(response.getJSONObject(i).getString("id"), response.getJSONObject(i).getString("user_id"), response.getJSONObject(i).getString("client_name"), response.getJSONObject(i).getString("mobile"), response.getJSONObject(i).getString("email"), response.getJSONObject(i).getString("notes"), response.getJSONObject(i).getString("strtotime")));
                    } catch (Exception e) {

                        spin_kit.setVisibility(View.GONE);
                    }

                }
                spin_kit.setVisibility(View.GONE);

                clientAdapter = new ClientAdapter(allClients);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(clientAdapter);
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

    public void filter(CharSequence clientName) {
        ArrayList<ClientData> filtered = new ArrayList<ClientData>();

        for (ClientData town : allClients) {



            if (town.getClient_name().toUpperCase().contains(clientName.toString().toUpperCase())) {
                filtered.add(town);
            }


        }

        allClients.clear();
        allClients.addAll(filtered);
        clientAdapter.notifyDataSetChanged();
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

        allClients = new ArrayList<>();
        getAllClients(userid);


    }
}
