package com.bulgarian.lawerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.bulgarian.lawerapp.navigation.NavigationActivity;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class SplashScreen extends AppCompatActivity {
    String userid = "";
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences prefs = getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE);

        userid = prefs.getString(AppsContants.USERID, "");

//      AutoLogin Fixed..
//        boolean metting_visit = prefs.getBoolean(AppsContants.Meeting_Last_Visit, true);
//        if (!metting_visit) return;

        if (isNetworkAvailable(this)) {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad));
            final AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("0DD2969C125F73204F51BBBBDB55625A")
                    .addTestDevice("F5DD0B29347CC8624D032EFBA8AFB0E1")
                    .build();

            new CountDownTimer(1000, 500) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    mInterstitialAd.loadAd(adRequest);
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startNextActivity();
                        }

                        public void onAdLoaded() {
                            showInterstitial();
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            startNextActivity();
                        }
                    });
                }

            }.start();
        } else {
            nextActivity();
        }


    }

    private void startNextActivity(){
        if (userid.equals("")) {
            Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
            SplashScreen.this.startActivity(mainIntent);
            SplashScreen.this.finish();
        } else {
            Intent mainIntent = new Intent(SplashScreen.this, NavigationActivity.class);
            SplashScreen.this.startActivity(mainIntent);
            SplashScreen.this.finish();
        }
    }

    private void nextActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startNextActivity();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
}








