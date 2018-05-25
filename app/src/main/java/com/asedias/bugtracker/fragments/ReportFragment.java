package com.asedias.bugtracker.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.LoginActivity;
import com.asedias.bugtracker.R;
import com.asedias.bugtracker.async.methods.SendComment;
import com.asedias.bugtracker.async.DocumentRequest;
import com.asedias.bugtracker.async.methods.GetReport;
import com.asedias.bugtracker.model.TitleItem;
import com.asedias.bugtracker.ui.adapter.ReportAdapter;
import com.asedias.bugtracker.others.AccessDeniedException;
import com.asedias.bugtracker.others.AuthorizationNeededException;
import com.asedias.bugtracker.others.InternetException;
import com.vkontakte.android.fragments.VKRecyclerFragment;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

import java.util.ArrayList;
import java.util.List;

import me.grishka.appkit.api.ErrorResponse;
import me.grishka.appkit.utils.V;
import me.grishka.appkit.views.DividerItemDecoration;

/**
 * Created by rorom on 22.04.2018.
 */

public class ReportFragment extends VKRecyclerFragment {

    private ReportAdapter mAdapter;
    private String mSubtitle;
    private View mWriteBar;
    private String hash;
    private String id;

    public ReportFragment() {
        super(10);
    }

    @Override
    public void onAttach(Activity var1) {
        super.onAttach(var1);
        this.setHasOptionsMenu(true);
        this.setTitleMarqueeEnabled(true);
        this.setSubtitleMarqueeEnabled(true);
        this.id = getArguments().getString("id", "0");
        if(!this.loaded) loadData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //menu.add(0, R.id.add, 0, R.string.report_delete);
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
        this.mRequest = new GetReport(id);
        this.mRequest.setCallback(new DocumentRequest.RequestCallback<GetReport.Result>() {
            @Override
            public void onSuccess(GetReport.Result result) {
                mSubtitle = result.name;
                hash = result.comment_hash;
                mAdapter.createData(result);
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
                int mWriteBarHeight = (int) BugTrackerApp.dp(48);
                list.setPadding(0, 0, 0, mWriteBarHeight);
                CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mWriteBarHeight);
                lp.gravity = Gravity.BOTTOM;
                ((ViewGroup)getAppbar().getParent()).addView(mWriteBar, lp);
                onDataLoaded(new ArrayList(), false);
            }
        });
        this.mRequest.run();
    }

    private void sendComment() {
        String message = ((EditText)mWriteBar.findViewById(R.id.editText)).getText().toString();
        if(!message.isEmpty()) new SendComment(getActivity(), id, hash, message).setCallback(new DocumentRequest.RequestCallback<List<RecyclerSectionAdapter.Data>>() {
            @Override
            public void onSuccess(List<RecyclerSectionAdapter.Data> obj) {
                for(int i = mAdapter.data.size() - 1; i > 0; i--) {
                    if(mAdapter.getItemViewType(i) == 0) {
                        String comments = BugTrackerApp.QuantityString(R.plurals.good_comments, obj.size(), obj.size());
                        mAdapter.data.set(i, RecyclerSectionAdapter.Data.top(0, new TitleItem(comments)));
                        break;
                    }
                    mAdapter.data.remove(i);
                }
                mAdapter.data.addAll(obj);
            }

            @Override
            public void onUIThread() {
                mAdapter.notifyDataSetChanged();
                ((EditText)mWriteBar.findViewById(R.id.editText)).setText("");
                list.scrollToPosition(mAdapter.data.size());
                if (getActivity().getCurrentFocus() != null) {
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    getActivity().getCurrentFocus().clearFocus();
                }
            }
        }).run();
    }

    @Override
    public void onViewCreated(View var1, Bundle var2) {
        super.onViewCreated(var1, var2);
        mWriteBar = getActivity().getLayoutInflater().inflate(R.layout.writebar, null);
        mWriteBar.findViewById(R.id.send).setOnClickListener(v -> sendComment());
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
