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
import com.asedias.bugtracker.async.base.DocumentRequest;
import com.asedias.bugtracker.async.GetReport;
import com.asedias.bugtracker.ui.adapter.ReportAdapter;
import com.asedias.bugtracker.others.AccessDeniedException;
import com.asedias.bugtracker.others.AuthorizationNeededException;
import com.asedias.bugtracker.others.InternetException;
import com.vkontakte.android.fragments.CardRecyclerFragment;
import com.vkontakte.android.fragments.VKRecyclerFragment;

import java.util.ArrayList;

import me.grishka.appkit.api.ErrorResponse;
import me.grishka.appkit.utils.V;
import me.grishka.appkit.views.DividerItemDecoration;

/**
 * Created by rorom on 22.04.2018.
 */

public class ReportFragment extends VKRecyclerFragment {

    private ReportAdapter mAdapter;
    private String mSubtitle;

    public ReportFragment() {
        super(10);
    }

    @Override
    public void onAttach(Activity var1) {
        super.onAttach(var1);
        this.setHasOptionsMenu(true);
        this.setTitleMarqueeEnabled(true);
        this.setSubtitleMarqueeEnabled(true);
        if(!this.loaded) loadData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, R.id.add, 0, R.string.report_delete);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add: {

            }
        }
        return false;
    }

    @Override
    protected void doLoadData(int var1, int var2) {
        String id = getArguments().getString("id", "0");
        this.mRequest = new GetReport(id);
        this.mRequest.setCallback(new DocumentRequest.RequestCallback<GetReport.Result>() {
            @Override
            public void onSuccess(GetReport.Result result) {
                mSubtitle = result.name;
                mAdapter.createData(result);
            }
            @Override
            public void onError(Exception e) {
                if(e instanceof AuthorizationNeededException) {
                    startActivity(new Intent(BugTrackerApp.context, LoginActivity.class));
                    getActivity().finish();
                }
                if(e instanceof AccessDeniedException) {
                    LoginActivity.clearCookies();
                    BugTrackerApp.context.getSharedPreferences("user", 0).edit().clear().apply();
                    getActivity().finish();
                }
                if(e instanceof InternetException) {
                    ReportFragment.this.onError(new ErrorResponse() {
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
                super.onUIThread();
                if(getToolbar() != null) {
                    setTitle(R.string.report);
                    setSubtitle(mSubtitle);
                }
                onDataLoaded(new ArrayList(), false);
            }
        });
        this.mRequest.run();
    }

    @Override
    public void onViewCreated(View var1, Bundle var2) {
        super.onViewCreated(var1, var2);
        list.setPadding(0, 0, 0, 0);
        list.addItemDecoration(new DividerItemDecoration(new ColorDrawable(637534208), V.dp(0.5f)).setProvider((DividerItemDecoration.Provider) getAdapter()));
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        if(mAdapter == null) {
            mAdapter = new ReportAdapter();
        }
        return mAdapter;
    }
}
