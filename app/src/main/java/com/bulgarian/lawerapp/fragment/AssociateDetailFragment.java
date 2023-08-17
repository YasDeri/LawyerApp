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


public class AssociateDetailFragment extends Fragment {
    EditText name, mobile, email, profile,password;
    String clientID, clientEmail, clientName, clientMobile, clientNotes, assoc_username, assoc_password;
    RelativeLayout add_meeting;
    public static String comming_status = "";
    SpinKitView spinKitView;
    ImageView update, delete,call,emailImage,save;
    public int REQUEST_PHONE_CALL=10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_associate_detail, container, false);


        name = (EditText) view.findViewById(R.id.name);
        mobile = (EditText) view.findViewById(R.id.mobile);
        password = (EditText) view.findViewById(R.id.password);
        profile = (EditText) view.findViewById(R.id.profile);
        email = (EditText) view.findViewById(R.id.email);
        spinKitView = (SpinKitView) view.findViewById(R.id.spin_kit);
        update = (ImageView) view.findViewById(R.id.update);
        delete = (ImageView) view.findViewById(R.id.delete);
        save = (ImageView) view.findViewById(R.id.save);
        call = (ImageView) view.findViewById(R.id.call);
        emailImage = (ImageView) view.findViewById(R.id.emailImage);

        SharedPreferences prefs = getActivity().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);

        clientID = prefs.getString(AppsContants.CLIENT_ID, "");
        clientEmail = prefs.getString(AppsContants.CLIENT_EMAIL, "");
        clientMobile = prefs.getString(AppsContants.CLIENT_MOBILE, "");
        clientName = prefs.getString(AppsContants.CLIENT_NAME, "");
        clientNotes = prefs.getString(AppsContants.CLIENT_NOTES, "");
        assoc_username = prefs.getString(AppsContants.ASSOC_USERNAME, "");
        assoc_password = prefs.getString(AppsContants.ASSOC_PASSWORD, "");


        name.setText(clientName);
        mobile.setText(clientMobile);
        profile.setText(assoc_username);
        email.setText(clientEmail);
        password.setText(assoc_password);
//        update.setVisibility(View.INVISIBLE);
        save.setVisibility(View.VISIBLE);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enabledOrBackground(name);
                enabledOrBackground(mobile);
                enabledOrBackground(email);
                enabledOrBackground(profile);
                enabledOrBackground(password);
                update.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enabledOrBackground(name);
                enabledOrBackground(mobile);
                enabledOrBackground(email);
                enabledOrBackground(profile);
                enabledOrBackground(password);
                updateClient(clientID, mobile.getText().toString().trim(), email.getText().toString().trim(), "", name.getText().toString().trim(), assoc_username, assoc_password, view);

            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mobile.getText().toString().equals("")) {

                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                    }
                    else
                    {
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

//                new AlertDialog.Builder(getActivity())
//                        .setTitle("Delete Associate !")
//                        .setMessage("Are you sure you want to delete associate ?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                spinKitView.setVisibility(View.VISIBLE);
//                                deleteAssociate(view, clientID);
//                            }
//                        })
//                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        })
//                        .show();
                Fragment fragment = new AssociateListFragment();
                NavigationActivity.mToolbar.setTitle("Associates");
                NavigationActivity.mToolbar.setLogo(R.drawable.associate_nav);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
            }
        });


        disabledOrBackground(name);
        disabledOrBackground(mobile);
        disabledOrBackground(email);
        disabledOrBackground(profile);
        disabledOrBackground(password);
        return view;
    }


    public void updateClient(String client_id, final String mobileupdate, final String emailUpdate, final String notesupdate, final String clientName, String assoc_username, String assoc_password, final View view) {

        spinKitView.setVisibility(View.VISIBLE);

        AndroidNetworking.post(URLHelper.updateAssociate)
                .addBodyParameter("email", emailUpdate)
                .addBodyParameter("phone", mobileupdate)
                .addBodyParameter("assoc_id", client_id)
                .addBodyParameter("assoc_unser_name", assoc_username)
                .addBodyParameter("assoc_pwd", assoc_password)
                .addBodyParameter("name", clientName)
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
                            .setMessage("Промените са запазени успешно!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    name.setText(clientName);
                                    mobile.setText(mobileupdate);
                                    email.setText(emailUpdate);
                                    String title = "Сътрудници";
                                    ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(R.drawable.associate_nav);
                                    Fragment fragment = new AssociateListFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_body, fragment);
                                    fragmentTransaction.commit();
                                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);


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


    public void deleteAssociate(final View view, String AssocId) {
        AndroidNetworking.post(URLHelper.deleteAssociate)
                .addBodyParameter("assoc_id", AssocId)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject response) {
                spinKitView.setVisibility(View.GONE);
                if (response.has("return_arr")) {

                    try {
                        if (response.getString("return_arr").equals("successful")) {
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Message !")
                                    .setMessage("Associate Deleted Successfully !")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            NavigationActivity.mToolbar.setTitle("Associates");
                                            NavigationActivity.mToolbar.setLogo(R.drawable.associate_nav);
                                            Fragment fragment = new AssociateListFragment();
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
                        spinKitView.setVisibility(View.GONE);
                        e.printStackTrace();
                    }

                } else {
                    spinKitView.setVisibility(View.GONE);
                    new AlertDialog.Builder(view.getContext())
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
                new AlertDialog.Builder(view.getContext())
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

    public void enabledOrBackground(EditText editText) {


        editText.setEnabled(true);
        editText.setBackground(getResources().getDrawable(R.drawable.background_edittext_oneside));
    }

    public void disabledOrBackground(EditText editText) {

        editText.setEnabled(false);
        editText.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_no_bg));
        editText.setTextColor(Color.BLACK);
    }

}
