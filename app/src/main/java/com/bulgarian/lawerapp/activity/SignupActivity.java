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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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

public class SignupActivity extends AppCompatActivity {
    EditText username, email, password, confirm_password;
    CheckBox checkbox;
    Button signup_button;
    SpinKitView spinKitView;
    TextView terms, privacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username = (EditText) findViewById(R.id.username);
        terms = (TextView) findViewById(R.id.terms);
        privacy = (TextView) findViewById(R.id.privacy);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        signup_button = (Button) findViewById(R.id.signup_button);
        spinKitView = (SpinKitView) findViewById(R.id.spin_kit);


        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor =  getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                editor.putString(AppsContants.WEBVIEWURL,"https://navikatech.com/AABOKATACNCT/term_of_use.php");
                editor.commit();
                startActivity(new Intent(SignupActivity.this, WebViewActivity.class));

            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor =  getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                editor.putString(AppsContants.WEBVIEWURL,"https://navikatech.com/AABOKATACNCT/privacy_policy.php");
                editor.commit();
                startActivity(new Intent(SignupActivity.this, WebViewActivity.class));

            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("")) {


                    showSnackBar(v, "Името е празно");

                } else if (email.getText().toString().equals("")) {


                    showSnackBar(v, "Имейлът е празен");

                } else if (password.getText().toString().equals("")) {


                    showSnackBar(v, "Паролата е празна");

                } else if (confirm_password.getText().toString().equals("")) {

                    showSnackBar(v, "Потвърдете паролата е празна");

                } else if (!password.getText().toString().equals(confirm_password.getText().toString())) {

                    showSnackBar(v, "Паролата и Паролата за потвърждение не съвпадат");

                } else if (!checkbox.isChecked()) {

                    showSnackBar(v, "Моля, приемете Общите условия");

                } else {

                    spinKitView.setVisibility(View.VISIBLE);

                    registerUser(username.getText().toString().trim(), email.getText().toString().trim(), password.getText().toString().trim(), v);

                }

            }
        });


    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    public void registerUser(String name, String email, String passowrd, final View view) {

        AndroidNetworking.post(URLHelper.signup)
                .addBodyParameter("email", email)
                .addBodyParameter("name", name)
                .addBodyParameter("password", passowrd)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                spinKitView.setVisibility(View.GONE);

                if (response.has("id")) {
                    new AlertDialog.Builder(SignupActivity.this)
                            .setTitle("Cъобщение")
                            .setMessage("Регистрацията е успешна. Имейл с информация за потвърждаване на регистрацията е изпратен на Вашата електронна поща, моля проверете и папката spam. След като потвърдите регистрацията си профилът Ви в advokatasist.com ще бъде активен.\n" +
                                    "При възникнали проблеми или въпроси, моля свържете се с екипа на АДВОКАТАСИСТ на имейл office@advokatasist.com, като оставите Вашите данни за контакт – имена, имейл и телефон.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
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
                    new AlertDialog.Builder(SignupActivity.this)
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

            }

            @Override
            public void onError(ANError anError) {
                spinKitView.setVisibility(View.GONE);
                new AlertDialog.Builder(SignupActivity.this)
                        .setTitle("Тревога")
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
        public void showmessage()
        {
            new AlertDialog.Builder(SignupActivity.this)
                    .setTitle("Confirm Registration !")
                    .setMessage("Validate your registration by visiting your Email!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }

    public void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message,
                Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
        snackbar.show();
    }
}