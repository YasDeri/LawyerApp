package com.bulgarian.lawerapp.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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


public class ClientInfoFragment extends Fragment {
    EditText name, mobile, email, notes;
    String clientID, clientEmail, clientName, clientMobile, clientNotes;
    RelativeLayout add_meeting;
    public static String comming_status = "";
    SpinKitView spinKitView;
    ImageView update, save, call, emailImage, delete_client, add_case;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_info, container, false);

        name = (EditText) view.findViewById(R.id.name);
        mobile = (EditText) view.findViewById(R.id.mobile);
        notes = (EditText) view.findViewById(R.id.notes);
        email = (EditText) view.findViewById(R.id.email);
        add_meeting = (RelativeLayout) view.findViewById(R.id.add_meeting);
        spinKitView = (SpinKitView) view.findViewById(R.id.spin_kit);
        update = (ImageView) view.findViewById(R.id.update);
        save = (ImageView) view.findViewById(R.id.save);
        call = (ImageView) view.findViewById(R.id.call);
        emailImage = (ImageView) view.findViewById(R.id.emailImage);
        delete_client = (ImageView) view.findViewById(R.id.delete_client);
        add_case = view.findViewById(R.id.add_case);
        add_case.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = view.getContext().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                editor.putString(AppsContants.CLIENT_ID, clientID);
                editor.putString(AppsContants.CLIENT_NAME, clientName);
                editor.putString(AppsContants.CLIENT_EMAIL, clientEmail);
                editor.putString(AppsContants.CLIENT_MOBILE, clientMobile);
                editor.putString(AppsContants.CLIENT_NOTES, clientNotes);
                editor.apply();

                AddMeetingFragment.comming_status="1";
                NavigationActivity.mToolbar.setTitle("Add Case");
                NavigationActivity.mToolbar.setLogo(R.drawable.add_meeting_nav);
                Fragment fragment = new ChatFragment();
                FragmentManager fragmentManager =
                        ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);

        clientID = prefs.getString(AppsContants.CLIENT_ID, "");
        clientEmail = prefs.getString(AppsContants.CLIENT_EMAIL, "");
        clientMobile = prefs.getString(AppsContants.CLIENT_MOBILE, "");
        clientName = prefs.getString(AppsContants.CLIENT_NAME, "");
        clientNotes = prefs.getString(AppsContants.CLIENT_NOTES, "");

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mobile.getText().toString().equals("")) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);

                    callIntent.setData(Uri.parse("tel:" + mobile.getText().toString()));//change the number

                    startActivity(callIntent);
                } else {
                    showSnackBar(view, "Mobile no not found !");
                }
            }
        });
        emailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().equals("")) {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString()});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                    startActivity(Intent.createChooser(intent, ""));
                } else {
                    showSnackBar(view, "Email not found !");
                }
            }
        });

        delete_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Съобщение!")
                        .setMessage("Сигурни ли сте, че искате да изтриете този клиент?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deleteClient(clientID, view);
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
        name.setText(clientName);
        mobile.setText(clientMobile);
        notes.setText(clientNotes);
        email.setText(clientEmail);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enabledOrBackground(name);
                enabledOrBackground(email);
                enabledOrBackground(notes);
                enabledOrBackground(mobile);
                update.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateClient(clientID, mobile.getText().toString().trim(), email.getText().toString().trim(), notes.getText().toString().trim(), name.getText().toString().trim(), view);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mobile.getText().toString().equals("")) {

                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 5);
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);

                        callIntent.setData(Uri.parse("tel:" + mobile.getText().toString()));//change the number

                        startActivity(callIntent);
                    }


                } else {
                    showSnackBar(view, "Mobile no not found !");
                }
            }
        });
        emailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().equals("")) {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString()});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                    startActivity(Intent.createChooser(intent, ""));
                } else {
                    showSnackBar(view, "Email not found !");
                }
            }
        });


        disabledOrBackground(name);
        disabledOrBackground(email);
        disabledOrBackground(notes);
        disabledOrBackground(mobile);

        add_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMeetingFragment.comming_status = "2";
                NavigationActivity.mToolbar.setTitle("Add Meeting");
                NavigationActivity.mToolbar.setLogo(R.drawable.meeting_nav);
                Fragment fragment = new AddMeetingFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return view;
    }


    public void updateClient(String client_id, final String mobileupdate, final String emailUpdate, final String notesupdate, final String clientName, final View view) {

        spinKitView.setVisibility(View.VISIBLE);

        AndroidNetworking.post(URLHelper.updateClient)
                .addBodyParameter("email", emailUpdate)
                .addBodyParameter("mobile", mobileupdate)
                .addBodyParameter("client_id", client_id)
                .addBodyParameter("client_name", clientName)
                .addBodyParameter("notes", notesupdate)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject response) {


                spinKitView.setVisibility(View.GONE);
                if (response.has("id")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Съобщение!")
                            .setMessage("Успешна актуализация на клиент!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    name.setText(clientName);
                                    mobile.setText(mobileupdate);
                                    notes.setText(notesupdate);
                                    email.setText(emailUpdate);

                                    NavigationActivity.mToolbar.setTitle("Клиент");
                                    NavigationActivity.mToolbar.setLogo(R.drawable.client_list);

                                    Fragment fragment = new ClientListFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_body, fragment);
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



    public void deleteClient(String client_id, final View view) {

        spinKitView.setVisibility(View.VISIBLE);

        AndroidNetworking.post(URLHelper.deleteClient)
                .addBodyParameter("client_id", client_id)
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject response) {


                spinKitView.setVisibility(View.GONE);


                if(response.has("return_arr")){

                    try {
                        if (response.getString("return_arr").equals("successful")) {
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Съобщение!")
                                    .setMessage("Успешно изтриване!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            NavigationActivity.mToolbar.setTitle("Client List");
                                            NavigationActivity.mToolbar.setLogo(R.drawable.client_list);

                                            Fragment fragment = new ClientListFragment();
                                            FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.container_body, fragment);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                        }
                                    })
                                    .show();
                        } else {
                            spinKitView.setVisibility(View.GONE);
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Alert !")
                                    .setMessage(response.getString("return_arr"))
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


    public void enabledOrBackground(EditText editText) {


        editText.setEnabled(true);
        editText.setBackground(getResources().getDrawable(R.drawable.background_edittext));
    }

    public void disabledOrBackground(EditText editText) {

        editText.setEnabled(false);
        editText.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_no_bg));
        editText.setTextColor(Color.BLACK);
    }


}
