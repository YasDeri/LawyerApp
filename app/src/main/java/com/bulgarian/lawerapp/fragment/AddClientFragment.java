package com.bulgarian.lawerapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.other.URLHelper;
import com.bulgarian.lawerapp.R;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


public class AddClientFragment extends Fragment {
    EditText name, mobile, email, notes;
    ImageView save;
    SpinKitView spinKitView;
    String userid = "";
public static String comming_status = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_client, container, false);


        name = (EditText) view.findViewById(R.id.name);
        mobile = (EditText) view.findViewById(R.id.mobile);
        email = (EditText) view.findViewById(R.id.email);
        notes = (EditText) view.findViewById(R.id.notes);
        save = (ImageView) view.findViewById(R.id.save);
        spinKitView = (SpinKitView) view.findViewById(R.id.spin_kit);
        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);

        userid = prefs.getString(AppsContants.USERID, "");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().trim().equals("")) {

                    showSnackBar(view, "Моля, попълнете полетата обозначени с \"*\".");
                } else {

                    addClient(userid, mobile.getText().toString(), email.getText().toString(), notes.getText().toString(), name.getText().toString(), view);


                }


            }
        });


        return view;

    }

    public void showSnackBar(View view, String message) {


        Snackbar snackbar = Snackbar.make(view, message,
                Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
        snackbar.show();


    }


    public void addClient(String user_id, String mobile, String email, String notes, String clientName, final View view) {

        spinKitView.setVisibility(View.VISIBLE);

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


                spinKitView.setVisibility(View.GONE);
                if (response.has("id")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Съобщение!")
                            .setMessage("Успешно добавен клиент!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    NavigationActivity.mToolbar.setTitle("Client List");
                                    NavigationActivity.mToolbar.setLogo(R.drawable.client_list);

                                    Fragment fragment = new ClientListFragment();
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
                        spinKitView.setVisibility(View.GONE);
                        showSnackBar(view, response.getString("result"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    spinKitView.setVisibility(View.GONE);
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
                spinKitView.setVisibility(View.GONE);
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
