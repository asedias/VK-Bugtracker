package com.asedias.bugtracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.asedias.bugtracker.fragments.ProfileFragment;
import com.asedias.bugtracker.fragments.ReportListFragment;

import java.util.ArrayList;

/**
 * Created by rorom on 14.05.2018.
 */

public class Navigator implements BottomNavigationView.OnNavigationItemSelectedListener {

    public ReportListFragment mReports;
    public ProfileFragment mProfile;
    private ArrayList<Fragment> FTFragments;
    private ArrayList<Fragment> STFragments;
    public int mCurrentID = R.id.navigation_dashboard;
    private FragmentManager mManager;
    private Activity context;
    private String KEY = "NAVIGATOR";

    public Navigator(Activity context) {
        this.context = context;
        this.mManager = this.context.getFragmentManager();
        this.FTFragments = new ArrayList<>();
        this.STFragments = new ArrayList<>();
        //initAndReplace();
    }

    public void go(Fragment fragment, Bundle args) {
        args.putBoolean("taskRoot", true);
        fragment.setArguments(args);
        Fragment current = getCurrentFragmentList().get(getCurrentFragmentList().size() - 1);
        getCurrentFragmentList().add(fragment);
        mManager.beginTransaction().add(R.id.content, fragment).show(fragment).hide(current).commit();
        Log.d(KEY, "OLD: " + current.getClass().getSimpleName() + "\nNEW: " + fragment.getClass().getSimpleName());
    }

    private void removeAndClose() {
        Fragment current = getCurrentFragmentList().get(getCurrentFragmentList().size() - 1);
        Fragment previous = getCurrentFragmentList().get(getCurrentFragmentList().size() - 2);
        getCurrentFragmentList().remove(current);
        mManager.beginTransaction().remove(current).show(previous).commit();
        Log.d(KEY, "REMOVE: " + current.getClass().getSimpleName() + "\nSHOW: " + previous.getClass().getSimpleName());
    }

    public void onBackPressed() {
        if(getCurrentFragmentList().size() > 1) {
            removeAndClose();
        } else {
            this.context.finish();
        }
    }

    private ArrayList<Fragment> getCurrentFragmentList() {
        switch (mCurrentID) {
            default:
            case R.id.navigation_dashboard: return this.FTFragments;
            case R.id.navigation_home: return this.STFragments;
        }
    }

    public void updateReportList() {
        mReports.updateList();
    }

    public void initAndReplace() {
        FragmentTransaction transaction = mManager.beginTransaction();
        if(mReports == null) {
            mReports = ReportListFragment.AllReports();
            this.FTFragments.add(mReports);
            transaction.add(R.id.content, mReports);
        }
        if(mProfile == null) {
            mProfile = new ProfileFragment();
            this.STFragments.add(mProfile);
            transaction.add(R.id.content, mProfile);
        }
        transaction.show(mReports).hide(mProfile).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_dashboard:
                mManager.beginTransaction().hide(this.STFragments.get(this.STFragments.size() - 1)).show(this.FTFragments.get(this.FTFragments.size() - 1)).commit();
                break;
            case R.id.navigation_home:
                mManager.beginTransaction().hide(this.FTFragments.get(this.FTFragments.size() - 1)).show(this.STFragments.get(this.STFragments.size() - 1)).commit();
                break;
        }
        mCurrentID = item.getItemId();
        return true;
    }
}
