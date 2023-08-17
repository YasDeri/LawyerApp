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
import android.widget.Button;
import android.widget.EditText;

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


public class AddAssociateFragment extends Fragment {

    EditText name, mobile, email, notes;
    Button save;
    SpinKitView spinKitView;
    String userid = "";
    public static String comming_status = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_associate, container, false);
        name = (EditText) view.findViewById(R.id.name);
        mobile = (EditText) view.findViewById(R.id.mobile);
        email = (EditText) view.findViewById(R.id.email);
        save = (Button) view.findViewById(R.id.save);
        spinKitView = (SpinKitView) view.findViewById(R.id.spin_kit);
        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);

        userid = prefs.getString(AppsContants.USERID, "");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().trim().equals("")) {

                    showSnackBar(view, "Please Enter Client Name");
                }
                else if (email.getText().toString().trim().equals("")) {

                    showSnackBar(view, "Please Enter Email");

                }else if (!email.getText().toString().trim().contains("@")) {

                    showSnackBar(view, "Please include a '@' in the email address. '" +
                            email.getText().toString() + "' is missing '@'.");

                } else if (mobile.getText().toString().trim().equals("")) {

                    showSnackBar(view, "Please Enter Mobile");

                } else {

                    addClient(userid, mobile.getText().toString(), email.getText().toString(),"", name.getText().toString(), view);


                }


            }
        });

        return view;
    }


    public void addClient(String user_id, String mobile, String email, String notes,
                          String clientName, final View view) {

        spinKitView.setVisibility(View.VISIBLE);




                AndroidNetworking.post(URLHelper.addAssociate)
                .addBodyParameter("email", email)
                .addBodyParameter("phone", mobile)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("name", clientName)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject response) {


                spinKitView.setVisibility(View.GONE);
                if (response.has("id")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Съобщение!")
                            .setMessage("Успешно добавен сътрудник!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    NavigationActivity.mToolbar.setTitle("Assoicates");
                                    NavigationActivity.mToolbar.setLogo(R.drawable.client_list);

                                    Fragment fragment = new AssociateListFragment();
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

    public void showSnackBar(View view, String message) {


        Snackbar snackbar = Snackbar.make(view, message,
                Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
        snackbar.show();


    }



}
