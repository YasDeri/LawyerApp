package com.bulgarian.lawerapp.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bulgarian.lawerapp.R;

public class RemoveFragmentOrder extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_case_list, container, false);
        //meetingAdapter = new MeetingAdapter(allMeetings);




        return view;
    }




    public void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message,Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
        snackbar.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
