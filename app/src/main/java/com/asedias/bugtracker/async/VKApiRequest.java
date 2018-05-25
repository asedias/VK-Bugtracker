package com.asedias.bugtracker.async;

import android.os.AsyncTask;

import com.asedias.bugtracker.async.base.Callback;
import com.asedias.bugtracker.async.base.VKApi;

import org.json.JSONObject;

/**
 * Created by rorom on 19.05.2018.
 */

public class VKApiRequest<I> {

    private VKApi mRequest;
    private DocumentRequest.RequestCallback mCallback;

    public VKApiRequest(String method) {
        this.mRequest = new VKApi(method).setCallback(new Callback<JSONObject>() {
            @Override
            public void onBackground(JSONObject document) {
                mCallback.onSuccess(parse(document));
            }

            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }
        });
    }

    public VKApiRequest setCallback(DocumentRequest.RequestCallback mCallback) {
        this.mCallback = mCallback;
        return this;
    }


    public void cancel() {
        if(!this.mRequest.isCancelled() && this.mRequest.getStatus() != AsyncTask.Status.FINISHED) this.mRequest.cancel(false);
    }

    public void run() {
        this.mRequest.execute();
    }

    protected I parse(JSONObject response) {
        return null;
    }

}
