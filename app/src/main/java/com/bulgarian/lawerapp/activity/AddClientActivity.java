package com.bulgarian.lawerapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.other.URLHelper;
import com.bulgarian.lawerapp.R;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

public class AddClientActivity extends AppCompatActivity {
    String userid = "";
    EditText name, mobile, email, notes;
    ImageView save;
    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);


        name = (EditText) findViewById(R.id.name);
        mobile = (EditText) findViewById(R.id.mobile);
        email = (EditText) findViewById(R.id.email);
        notes = (EditText) findViewById(R.id.notes);
        save = (ImageView) findViewById(R.id.save);
        spinKitView = (SpinKitView) findViewById(R.id.spin_kit);
        SharedPreferences prefs = getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);

        userid = prefs.getString(AppsContants.USERID, "");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().trim().equals("")) {

                    showSnackBar(view, "Please Enter Client Name");
                } else if (mobile.getText().toString().trim().equals("")) {

                    showSnackBar(view, "Please Enter Mobile");

                } else if (email.getText().toString().trim().equals("")) {

                    showSnackBar(view, "Please Enter Email");

                } else {

                    addClient(userid, mobile.getText().toString(), email.getText().toString(), notes.getText().toString(), name.getText().toString(), view);


                }


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
                    new AlertDialog.Builder(AddClientActivity.this)
                            .setTitle("Message !")
                            .setMessage("Client Added Successfully !")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
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
                    new AlertDialog.Builder(AddClientActivity.this)
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
                new AlertDialog.Builder(AddClientActivity.this)
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
//
//    @Override
//    public void onBackPressed() {
//        moveTaskToBack(true);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            moveTaskToBack(true);
        }
        return true;
    }
}
