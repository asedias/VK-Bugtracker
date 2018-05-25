package com.asedias.bugtracker.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.LoginActivity;
import com.asedias.bugtracker.R;
import com.asedias.bugtracker.UserData;
import com.asedias.bugtracker.async.methods.GetReportList;
import com.asedias.bugtracker.async.DocumentRequest;
import com.asedias.bugtracker.ui.adapter.ReportsAdapter;
import com.asedias.bugtracker.others.AccessDeniedException;
import com.asedias.bugtracker.others.AuthorizationNeededException;
import com.asedias.bugtracker.others.InternetException;
import com.vkontakte.android.fragments.VKRecyclerFragment;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

import java.util.ArrayList;

import me.grishka.appkit.api.ErrorResponse;
import me.grishka.appkit.utils.V;
import me.grishka.appkit.views.DividerItemDecoration;

/**
 * Created by rorom on 22.04.2018.
 */

public class ReportListFragment extends VKRecyclerFragment {

    private ReportsAdapter mAdapter;
    private int udate = 0;
    String mid = "0";
    boolean bookmark = false;
    boolean mAddButton = false;

    public static ReportListFragment AllReports() {
        ReportListFragment fr = new ReportListFragment();
        Bundle args = new Bundle();
        args.putString("mid", "0");
        fr.setArguments(args);
        return fr;
    }

    public static Bundle getBookmarkBundle() {
        Bundle args = new Bundle();
        args.putBoolean("bookmarks", true);
        args.putString("mid", "0");
        return args;
    }

    public static ReportListFragment Bookmarks() {
        ReportListFragment fr = new ReportListFragment();
        Bundle args = new Bundle();
        args.putString("mid", "0");
        args.putBoolean("bookmarks", true);
        fr.setArguments(args);
        return fr;
    }

    public static Bundle getBundle(String mid) {
        Bundle args = new Bundle();
        args.putString("mid", mid);
        return args;
    }

    public ReportListFragment() {
        super(10);
    }

    @Override
    public void onAttach(Activity var1) {
        super.onAttach(var1);
        this.setHasOptionsMenu(true);
        this.bookmark = getArguments().getBoolean("bookmarks", false);
        this.mid = getArguments().getString("mid");
        if(Integer.parseInt(this.mid) == 0) mAddButton = true;
        if(!this.loaded) loadData();
    }

    @Override
    protected void doLoadData(int var1, int var2) {
        //Log.e("doLoadData", mid);
        this.mRequest = new GetReportList(mid, udate, bookmark).setCallback(new DocumentRequest.RequestCallback<GetReportList.Result>() {
            GetReportList.Result result;
            @Override
            public void onSuccess(GetReportList.Result obj) {
                udate = obj.udate;
                result = obj;
            }

            @Override
            public void onError(Exception e) {
                if(e instanceof AuthorizationNeededException) {
                    startActivity(new Intent(BugTrackerApp.context, LoginActivity.class));
                    getActivity().finish();
                }
                if(e instanceof AccessDeniedException) {
                    getActivity().finish();
                }
                if(e instanceof InternetException) {
                    ReportListFragment.this.onError(new ErrorResponse() {
                        @Override
                        public void bindErrorView(View var1) {

                        }

                        @Override
                        public void showToast(Context var1) {

                        }
                    });
                }
            }

            @Override
            public void onUIThread() {
                mAdapter.mItems.addAll(result.reports);
                onDataLoaded(mAdapter.mItems, result.more);
            }
        });
        this.mRequest.run();
        //((PostReports)this.mTask).setUdate(udate).setMid(mid).execute();
    }

    @Override
    public void onViewCreated(View var1, Bundle var2) {
        super.onViewCreated(var1, var2);
        int title = R.string.all_reports;
        if(this.mid.contains(UserData.getUID())) {
            title = R.string.prefs_reports;
            mAddButton = true;
        } else if(bookmark){
            title = R.string.prefs_bookmarks;
        }
        this.setTitle(title);
        list.setPadding(0, 0, 0, 0);
        list.addItemDecoration(new DividerItemDecoration(new ColorDrawable(637534208), V.dp(0.5f)).setProvider((DividerItemDecoration.Provider) getAdapter()));
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        if(mAdapter == null) {
            mAdapter = new ReportsAdapter(new ArrayList<RecyclerSectionAdapter.Data>());
        }
        return this.mAdapter;
    }

    @Override
    public void onRefresh() {
        showProgress();
        udate = 0;
        super.onRefresh();
        mAdapter.mItems.clear();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.report_list, menu);
        //if(!mAddButton)
            menu.removeItem(R.id.add);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        //menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add: {
                //FragmentWrapperActivity.startWithFragment(getActivity(), new AddReportFragment(), new Bundle());

            }
        }
        return false;
    }
}
