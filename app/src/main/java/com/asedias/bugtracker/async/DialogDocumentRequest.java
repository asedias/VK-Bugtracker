package com.asedias.bugtracker.async;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.R;
import com.asedias.bugtracker.async.DocumentRequest;
import com.asedias.bugtracker.async.base.Callback;
import com.asedias.bugtracker.async.base.GetDocument;
import com.asedias.bugtracker.async.base.PostRequestParser;

/**
 * Created by rorom on 12.05.2018.
 */

public class DialogDocumentRequest<I extends Object> extends DocumentRequest<I> {

    private Activity mContext;
    private AlertDialog mDialog;

    public DialogDocumentRequest(Activity activity) {
        super(true);
        this.mContext = activity;
        this.mDialog = ProgressDialog.show(this.mContext, BugTrackerApp.String(R.string.loading), BugTrackerApp.String(R.string.loading));
        this.mTask = new GetDocument(url).setCallback(new Callback<PostRequestParser>() {
            @Override
            public void onBackground(PostRequestParser document) {
                mCallback.onSuccess(parse(document));
            }

            @Override
            public void onError(Exception e) {
                mDialog.cancel();
                mCallback.onError(e);
            }

            @Override
            public void onUIThread() {
                mCallback.onUIThread();
                mDialog.cancel();
            }
        });
    }
}
