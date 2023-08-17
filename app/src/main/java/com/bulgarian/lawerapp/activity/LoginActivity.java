package com.bulgarian.lawerapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login_button;
    TextView forgot_password;
    SpinKitView spinKitView;
    TextView error_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        error_text = (TextView) findViewById(R.id.error_text);
        password = (EditText) findViewById(R.id.password);
        login_button = (Button) findViewById(R.id.login_button);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        spinKitView = (SpinKitView) findViewById(R.id.spin_kit);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("")) {


                    showSnackBar(v, "Имейлът е празен");

                } else if (password.getText().toString().equals("")) {


                    showSnackBar(v, "Паролата е празна");

                } else {

                    spinKitView.setVisibility(View.VISIBLE);

                    loginUser(email.getText().toString().trim(), password.getText().toString().trim(), v);

                }

            }
        });


    }


    public void loginUser(String email, final String passowrd, final View view) {

        AndroidNetworking.post(URLHelper.login)
                .addBodyParameter("email", email)
                .addBodyParameter("password", passowrd)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject response) {

                spinKitView.setVisibility(View.GONE);
                if (response.has("id")) {
                    try {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Съобщение")
                                .setMessage("Добре дошли " + response.getString("name"))
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        SharedPreferences.Editor editor = getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                                        try {
                                            editor.putString(AppsContants.USERID, response.getString("id"));
                                            editor.putString(AppsContants.USERNAME, response.getString("name"));
                                            editor.putString(AppsContants.EMAIL, response.getString("email"));
                                            editor.putString(AppsContants.USERPASSWORD, response.getString("passwordDcpt"));
                                            editor.putString(AppsContants.USER_TYPE,
                                                    response.getString("type"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        editor.apply();

                                        startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                                    }
                                })
                                .show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.has("result")) {
                    spinKitView.setVisibility(View.GONE);
                    //  showSnackBar(view, response.getString("result"));
                    error_text.setVisibility(View.VISIBLE);

                } else {
                    spinKitView.setVisibility(View.GONE);
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Тревога!")
                            .setMessage("Нещо се обърка!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Тревога!")
                        .setMessage(anError.getMessage())
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
