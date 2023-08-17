package com.bulgarian.lawerapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.bulgarian.lawerapp.R;

public class RemoveAdsFragment extends Fragment implements BillingProcessor.IBillingHandler {

    Button oneMonthSubBtn;
    BillingProcessor bp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.remove_ads_fragment, container, false);
        oneMonthSubBtn = view.findViewById(R.id.oneMonthSubBtn);
        bp = new BillingProcessor(getActivity(), "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0M/UbEcIwUxKyArHIlbHfK9mgmdFYB+VMzcTDSilUHcsmULAvI0eNfGUdWiZlFvG/m3/iMtYRL2HW/y7YOthYQrO2DYToFkAhdKBAcxHGyApaY+GGYUIeC+JudUNcHUFLTQ4kH/YAiCPQctiTiGCPyPu4oYSyRInQtkJyubaHra6UrUefKza3RVbJgnEJMzJzWWlR6Awum/j9y3hyQjPoF5ZlMM32sV9UtHHAvk8fv1Q2CU/5wvioFBlCijSdSIxJO7ubb8mra605BbLQ2zoyzdknhpHclhvoyPL8LiDbirl5832bD+bML+EMGVefrxcUPhvr6RUi+S3qBMxoqwZqQIDAQAB", this);
        bp.initialize();
        oneMonthSubListener();
        return view;
    }

    void oneMonthSubListener(){

        oneMonthSubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bp.subscribe(getActivity(), "remove_ads_one_month");
                //bp.subscribe(getActivity(), "android.test.canceled");
            }
        });

    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(getActivity(), "You have subscribed already", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

}
