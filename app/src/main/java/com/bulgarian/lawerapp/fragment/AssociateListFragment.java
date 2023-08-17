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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.adapters.AssociateAdapter;
import com.bulgarian.lawerapp.model.AssociateData;
import com.bulgarian.lawerapp.other.URLHelper;
import com.bulgarian.lawerapp.R;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class AssociateListFragment extends Fragment {

    private ArrayList<AssociateData> allAsssociates = new ArrayList<>();
    String userid = "";

    SpinKitView spin_kit;
    RecyclerView recyclerView;
    AssociateAdapter associateAdapter;



    RelativeLayout add_Assocaite_relative;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_associate_list, container, false);
        add_Assocaite_relative = (RelativeLayout) view.findViewById(R.id.add_Assocaite_relative);
        spin_kit = (SpinKitView) view.findViewById(R.id.spin_kit);
        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);

        userid = prefs.getString(AppsContants.USERID, "");

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        add_Assocaite_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationActivity.mToolbar.setTitle("Добави сътрудник");
                NavigationActivity.mToolbar.setLogo(R.drawable.associate_nav);
                Fragment fragment = new AddAssociateFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        return view;
    }


    public void getallAssociates(String user_id) {
        spin_kit.setVisibility(View.VISIBLE);
        AndroidNetworking.post(URLHelper.showAssociate)
                .addBodyParameter("user_id", user_id)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {

                    try {
                        allAsssociates.add(new AssociateData(response.getJSONObject(i).getString("id"),  response.getJSONObject(i).getString("name"), response.getJSONObject(i).getString("email"), response.getJSONObject(i).getString("phone"),response.getJSONObject(i).getString("assoc_unser_name"),response.getJSONObject(i).getString("assoc_pwd")));
                    } catch (Exception e) {

                        spin_kit.setVisibility(View.GONE);
                    }

                }
                spin_kit.setVisibility(View.GONE);

                associateAdapter = new AssociateAdapter(allAsssociates);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(associateAdapter);
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

        allAsssociates = new ArrayList<>();
        getallAssociates(userid);


    }
}
