package com.bulgarian.lawerapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bulgarian.lawerapp.R;
import com.bulgarian.lawerapp.other.URLHelper;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassword extends AppCompatActivity {

    TextView email;
    Button btn;
    SpinKitView spinKitView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email = findViewById(R.id.email);
        btn = findViewById(R.id.login_button);
        spinKitView = (SpinKitView) findViewById(R.id.spin_kit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinKitView.setVisibility(View.VISIBLE);
                sendEmail(email.getText().toString());
            }
        });
    }
//--
    private void sendEmail(String email) {
        AndroidNetworking.post(URLHelper.forgotPassword)
                .addBodyParameter("email", email)
                .setTag("forgetPassword")
                .setPriority(Priority.HIGH)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                spinKitView.setVisibility(View.GONE);
                try {
                    if(response.getString("result").equalsIgnoreCase("successfully")){
                        new AlertDialog.Builder(ForgotPassword.this)
                                .setTitle("съобщение")
                                .setMessage("Заявка за забравена парола е изпратена на Вашата електронна поща")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
                                    }
                                })
                                .show();
                    }
                    else {
                        new AlertDialog.Builder(ForgotPassword.this)
                                .setTitle("съобщение")
                                .setMessage("Нещо се обърка")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                spinKitView.setVisibility(View.GONE);
                new AlertDialog.Builder(ForgotPassword.this)
                        .setTitle("Тревога")
                        .setMessage("Нещо се обърка")
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
