package com.bulgarian.lawerapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.activity.SplashScreen;
import com.bulgarian.lawerapp.other.URLHelper;
import com.bulgarian.lawerapp.R;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment {
    EditText name, mail;
    Button change_password_button;
    String password;
    SpinKitView spinKitView;
    String userid="";
    Button logout_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = (EditText) view.findViewById(R.id.name);
        mail = (EditText) view.findViewById(R.id.mail);
        logout_button = (Button) view.findViewById(R.id.logout_button);
        spinKitView = (SpinKitView) view.findViewById(R.id.spin_kit);
        change_password_button = (Button) view.findViewById(R.id.change_password_button);

        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);
        String username = prefs.getString(AppsContants.USERNAME, null);
        String email = prefs.getString(AppsContants.EMAIL, null);
        userid = prefs.getString(AppsContants.USERID, "");
        password = prefs.getString(AppsContants.USERPASSWORD, null);

        name.setText(username);
        mail.setText(email);

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("съобщение")
                        .setMessage("Сигурни ли сте, че искате да излезете от приложението")
                        .setPositiveButton("да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();

                                    editor.putString(AppsContants.USERID, "");
                                    editor.putString(AppsContants.USERNAME, "");
                                    editor.putString(AppsContants.EMAIL, "");
                                    editor.putString(AppsContants.USERPASSWORD, "");

                                editor.apply();

                                startActivity(new Intent(getActivity(), SplashScreen.class));
                            }
                        })
                        .setNegativeButton("Не", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        change_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                password = prefs.getString(AppsContants.USERPASSWORD, null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Смени парола");
                final EditText oldPass = new EditText(getActivity());
                final EditText newPass = new EditText(getActivity());
                final EditText confirmPass = new EditText(getActivity());


                oldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                oldPass.setHint("Стара парола");
                newPass.setHint("Нова парола");
                confirmPass.setHint("Повтори нова парола");
                LinearLayout ll = new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

                ll.addView(oldPass);

                ll.addView(newPass);
                ll.addView(confirmPass);
                alertDialog.setView(ll);
                alertDialog.setPositiveButton("Запази",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // dialog.cancel();

                                if (oldPass.getText().toString().equals(password)) {
                                    if ((newPass.getText().toString().trim()).equals(confirmPass.getText().toString().trim())) {
                                        updateProfile(userid, newPass.getText().toString().trim(), dialog);
                                    } else {
                                        showSnackBar(v, "Парола и парола за потвърждение не съвпаднаха");

                                    }

                                } else {

                                    showSnackBar(v, "Старата парола не съвпадна");
                                }


                            }
                        });
                alertDialog.setNegativeButton("Отмени",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = alertDialog.create();
                alert11.show();
            }
        });

        return view;

    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void showSnackBar(View view, String message) {


        Snackbar snackbar = Snackbar.make(view, message,
                Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
        snackbar.show();


    }


    public void updateProfile(String id, final String passowrd, final DialogInterface view) {

        AndroidNetworking.post(URLHelper.changePassword)
                .addBodyParameter("user_id", id)
                .addBodyParameter("password", passowrd)
                .setTag("test22")
                .setPriority(Priority.HIGH)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject response) {

                spinKitView.setVisibility(View.GONE);
                if (response.has("id")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("съобщение")
                            .setMessage("Паролата е променена успешно")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    view.cancel();
                                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                                    editor.putString(AppsContants.USERPASSWORD, passowrd);
                                    editor.apply();
                                }
                            })
                            .show();


                } else if (response.has("result")) {
                    spinKitView.setVisibility(View.GONE);

                } else {
                    spinKitView.setVisibility(View.GONE);
                    new AlertDialog.Builder(getActivity())
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
                new AlertDialog.Builder(getActivity())
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
}
