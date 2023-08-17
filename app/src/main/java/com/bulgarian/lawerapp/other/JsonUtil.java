package com.bulgarian.lawerapp.other;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bulgarian.lawerapp.adapters.DialogDateAdapter;
import com.bulgarian.lawerapp.model.DialogData;
import com.bulgarian.lawerapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JsonUtil {

    public static boolean hasValue(String str){
        boolean has_value = false;
        if (str != null) {
            if (!str.contains("[]") && !str.isEmpty()){
                has_value = true;
            }
        }
        return has_value;
    }

    public static boolean hasString(String str){
        boolean has_string = false;
        if (str != null)
            if (!str.contains("null") && !str.isEmpty()){
                has_string = true;
            }

        return has_string;
    }

    public static void makeCall(Context context, String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        context.startActivity(intent);
    }

    public static void openEmail(Context context, String email){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("vnd.android.cursor.item/email");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
        context.startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
    }

    public static long dateToMiliSconds(String date){
        String givenDateString = date;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            Date mDate = sdf.parse(givenDateString);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void setDialog(
            Context context,
            String title,
            String sub_title_1,
            String sub_title_2,
            String sub_title_3,
            int image_id,
            ArrayList<DialogData> dialogDataList){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        TextView tv_title = dialog.findViewById(R.id.tv_title);
        TextView tv_sub_title_1 = dialog.findViewById(R.id.tv_sub_title_1);
        TextView tv_sub_title_2 = dialog.findViewById(R.id.tv_sub_title_2);
        TextView tv_sub_title_3 = dialog.findViewById(R.id.tv_sub_title_3);

        ImageView iv_close =  dialog.findViewById(R.id.iv_close);
        ImageView iv_icon =  dialog.findViewById(R.id.iv_icon);

        iv_icon.setImageResource(image_id);
        tv_title.setText(title);
        tv_sub_title_1.setText(sub_title_1);
        tv_sub_title_2.setText(sub_title_2);
        tv_sub_title_3.setText(sub_title_3);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        RecyclerView.LayoutManager layoutManagerOfrecyclerView;
        RecyclerView.Adapter recyclerViewadapter;
        RecyclerView recyclerView = dialog.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        layoutManagerOfrecyclerView = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

        recyclerViewadapter = new DialogDateAdapter(dialogDataList, context);
        recyclerView.setAdapter(recyclerViewadapter);

        dialog.show();
    }
}
