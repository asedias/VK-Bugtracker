package com.asedias.bugtracker.ui;

import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.asedias.bugtracker.R;
import com.asedias.bugtracker.fragments.ProfileFragment;
import com.asedias.bugtracker.fragments.ReportListFragment;

import me.grishka.appkit.fragments.AppKitFragment;

/**
 * Created by Рома on 23.08.2017.
 */

public class ButtomNavigationInstance {

    private AppCompatActivity mActivity;
    private static AppKitFragment mReports = ReportListFragment.AllReports();
    private static AppKitFragment mProfile = new ProfileFragment();
    private static int checkedItem = R.id.navigation_dashboard;

    public ButtomNavigationInstance(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void Setup(final boolean savedState) {
        BottomNavigationViewEx bottom = this.mActivity.findViewById(R.id.navigation);
        bottom.setTextSize(13);
        bottom.setTextTypeface(Fonts.Medium);
        bottom.enableAnimation(false);
        final FragmentManager fm = mActivity.getFragmentManager();
        if(!savedState) {
            fm.beginTransaction().add(R.id.content, mReports, "reports").commit();
            fm.beginTransaction().add(R.id.content, mProfile, "profile").commit();
            fm.beginTransaction().hide(mProfile).commit();
        } else {
            mReports = (AppKitFragment) fm.findFragmentByTag("reports");
            mProfile = (AppKitFragment) fm.findFragmentByTag("profile");
            fm.beginTransaction().hide(checkedItem == R.id.navigation_dashboard ? mProfile : mReports).commit();
        }
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() != checkedItem) {
                    checkedItem = item.getItemId();
                    switch (checkedItem) {
                        case R.id.navigation_dashboard: {
                            fm.beginTransaction().hide(mProfile).show(mReports).commit();
                            break;
                        }
                        case R.id.navigation_home: {
                            fm.beginTransaction().hide(mReports).show(mProfile).commit();
                            break;
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
