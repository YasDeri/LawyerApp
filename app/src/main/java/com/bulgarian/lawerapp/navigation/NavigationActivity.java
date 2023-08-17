package com.bulgarian.lawerapp.navigation;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bulgarian.lawerapp.fragment.RemoveAdsFragment;
import com.bulgarian.lawerapp.other.AppsContants;
import com.bulgarian.lawerapp.fragment.AddClientFragment;
import com.bulgarian.lawerapp.fragment.AddMeetingFragment;
import com.bulgarian.lawerapp.fragment.ArchiveListFragment;
import com.bulgarian.lawerapp.fragment.AssociateListFragment;
import com.bulgarian.lawerapp.fragment.CalendarFragment;
import com.bulgarian.lawerapp.fragment.CaseListFragment;
import com.bulgarian.lawerapp.fragment.ChatFragment;
import com.bulgarian.lawerapp.fragment.ClientListFragment;
import com.bulgarian.lawerapp.fragment.ContactUs;
import com.bulgarian.lawerapp.fragment.MeetinglistFragment;
import com.bulgarian.lawerapp.fragment.ProfileFragment;
import com.bulgarian.lawerapp.fragment.RemoveFragmentOrder;
import com.bulgarian.lawerapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Stack;


public class NavigationActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {


    public static Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    TextView name, email;
    ImageView iv_profile_photo;
    String title = null;
    int drawable;
    Stack<String> titleStack;
    Stack<Integer> drawableStack;

    private AdView mAdView;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        MobileAds.initialize(this, getString(R.string.admob_app_id));
        setBannerAd();

        titleStack = new Stack<>();
        drawableStack = new Stack<>();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);


        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        iv_profile_photo = (ImageView) findViewById(R.id.iv_profile_photo);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //  getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void setBannerAd(){
        mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("0DD2969C125F73204F51BBBBDB55625A")
                .addTestDevice("F5DD0B29347CC8624D032EFBA8AFB0E1")
                .build();

        mAdView.loadAd(adRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void displayView(int position) {
        Fragment fragment = null;
        if(title != null){
            titleStack.push(title);
            drawableStack.push(drawable);
        }
        switch (position) {

            case 0:
                fragment = new CalendarFragment();
                title = "Календар";
                drawable = android.R.color.transparent;
                getSupportActionBar().setIcon(
                        new ColorDrawable(getResources().getColor(android.R.color.transparent)));


                break;

            case 1:
                fragment = new ChatFragment();
                title = "Добави дело";
                drawable = android.R.color.transparent;
                getSupportActionBar().setIcon(
                        new ColorDrawable(getResources().getColor(android.R.color.transparent)));


                break;

            case 2:
                fragment = new CaseListFragment();
                title = "Списък дела";
                drawable = R.drawable.caselist;
                getSupportActionBar().setIcon(R.drawable.caselist);
                break;
            case 3:

                SharedPreferences.Editor editor = getSharedPreferences(AppsContants.MyPREFERENCES, MODE_PRIVATE).edit();
                editor.putString(AppsContants.CLIENT_ID, "");
                editor.putString(AppsContants.CLIENT_NAME, "");
                editor.putString(AppsContants.CLIENT_EMAIL, "");
                editor.putString(AppsContants.CLIENT_MOBILE, "");
                editor.putString(AppsContants.CLIENT_NOTES, "");
                editor.apply();

                fragment = new AddMeetingFragment();
                title = "Добави среща";
                drawable = R.drawable.meeting;
                getSupportActionBar().setIcon(R.drawable.meeting_nav);
                break;

            case 4:

                fragment = new MeetinglistFragment();
                title = "Списък срещи";
                drawable = R.drawable.add_meeting_nav;
                getSupportActionBar().setIcon(R.drawable.add_meeting_nav);
                break;
            case 5:
                AddClientFragment.comming_status = "0";
                fragment = new AddClientFragment();
                title = "Добави клиент";
                drawable = R.drawable.addclient_nav;
                getSupportActionBar().setIcon(R.drawable.addclient_nav);
                break;
            case 6:
                fragment = new ClientListFragment();
                title = "Списък клиенти";
                drawable = R.drawable.client_list;
                getSupportActionBar().setIcon(R.drawable.client_list);
                break;
            case 7:
                fragment = new ArchiveListFragment();
                title = "Архив";
                drawable = R.drawable.archive_small;
                getSupportActionBar().setIcon(R.drawable.archive_small);
                break;
            case 8:
                fragment = new AssociateListFragment();
                title = "Сътрудници";
                drawable = R.drawable.associate_nav;
                getSupportActionBar().setIcon(R.drawable.associate_nav);
                break;
            case 9:
                fragment = new ProfileFragment();
                title = "Профил";
                drawable = android.R.color.transparent;
                getSupportActionBar().setIcon(
                        new ColorDrawable(getResources().getColor(android.R.color.transparent)));

                break;
            case 10:
                //fragment=new RemoveFragmentOrder();
                fragment = new RemoveAdsFragment();
                title = "Remove Ads";
                drawable = android.R.color.transparent;
                //Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                getSupportActionBar().setIcon(
                        new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                break;
            case 11:
                fragment = new ContactUs();
                title = "Контакт с нас";
                drawable = R.drawable.e_mail;
                getSupportActionBar().setIcon(R.drawable.e_mail);

                break;

            default:
                break;


        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(title);
        }
    }


    @Override
    public void onBackPressed(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            if(!titleStack.isEmpty()){
                getSupportActionBar().setTitle(title = titleStack.pop());
                drawable = drawableStack.pop();
                if(drawable == android.R.color.transparent){
                    getSupportActionBar().setIcon(
                            new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                }
                else {
                    getSupportActionBar().setIcon(drawable);
                }
            }
        } else {
            super.onBackPressed();
        }
    }
}