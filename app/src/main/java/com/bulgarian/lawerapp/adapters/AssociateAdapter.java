package com.bulgarian.lawerapp.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bulgarian.lawerapp.fragment.AssociateDetailFragment;
import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.model.AssociateData;
import com.bulgarian.lawerapp.other.URLHelper;
import com.bulgarian.lawerapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AssociateAdapter extends RecyclerView.Adapter<AssociateAdapter.MyViewHolder> {

    private List<AssociateData> associateDataList;

    public AssociateAdapter(ArrayList<AssociateData> allAsssociates) {

        this.associateDataList = allAsssociates;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView client_name;
        public ImageView associate_info, delete;

        public MyViewHolder(View view) {
            super(view);
            client_name = (TextView) view.findViewById(R.id.client_name);
            associate_info = (ImageView) view.findViewById(R.id.associate_info);
            delete = (ImageView) view.findViewById(R.id.delete);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.associate_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AssociateData associateData = associateDataList.get(position);

        holder.client_name.setText(associateData.getName());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(view.getContext())
                        .setTitle("Съобщение!")
                        .setMessage("Сигурни ли сте, че искате да изтриете този сътрудник?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAssociate(view, associateData.getId(),
                                        associateData, holder, associateDataList, position);

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

        holder.associate_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences.Editor editor = view.getContext().getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                editor.putString(AppsContants.CLIENT_ID, associateData.getId());
                editor.putString(AppsContants.CLIENT_NAME, associateData.getName());
                editor.putString(AppsContants.CLIENT_EMAIL, associateData.getEmail());
                editor.putString(AppsContants.CLIENT_MOBILE, associateData.getPhone());
                editor.putString(AppsContants.ASSOC_USERNAME, associateData.getAssoc_username());
                editor.putString(AppsContants.ASSOC_PASSWORD, associateData.getAssoc_password());
                editor.apply();
                NavigationActivity.mToolbar.setTitle("Профил на сътрудник");
                //  NavigationActivity.mToolbar.setLogo(R.drawable.client_nav);
                Fragment fragment = new AssociateDetailFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });
    }

    @Override
    public int getItemCount() {
        return associateDataList.size();
    }


    public void deleteAssociate(final View view, String AssocId, AssociateData associateData, final MyViewHolder holder, final List<AssociateData> associateDataList, final int position) {
        AndroidNetworking.post(URLHelper.deleteAssociate)
                .addBodyParameter("assoc_id", AssocId)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(final JSONObject response) {
                if (response.has("return_arr")) {

                    try {
                        if (response.getString("return_arr").equals("successful")) {
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Съобщение!")
                                    .setMessage("Успешно изтриване!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            associateDataList.remove(position);
                                            notifyDataSetChanged();
                                        }
                                    })
                                    .show();
                        } else {
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

                } else {

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

}