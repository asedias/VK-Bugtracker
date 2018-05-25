package com.asedias.bugtracker.async;

import com.asedias.bugtracker.UserData;
import com.asedias.bugtracker.async.base.Callback;
import com.asedias.bugtracker.async.base.GetDocument;
import com.asedias.bugtracker.async.base.PostRequestParser;

/**
 * Created by rorom on 11.05.2018.
 */

public class DocumentRequest<I extends Object> {
    protected GetDocument mTask;
    protected String url = "https://vk.com/al_bugtracker.php?";
    protected RequestCallback mCallback;

    public DocumentRequest(boolean post) {
        this.url += "al=" + (post ? "1" : "0");
        initTask();
        this.param("al_id", UserData.getUID());
    }

    public DocumentRequest(String url) {
        this.url = url;
        initTask();
    }

    private void initTask() {
        this.mTask = new GetDocument(url).setCallback(new Callback<PostRequestParser>() {
            @Override
            public void onBackground(PostRequestParser document) {
                I doc = parse(document);
                if(mCallback != null) {
                    mCallback.onSuccess(doc);
                }
            }

            @Override
            public void onError(Exception e) {
                if(mCallback != null) {
                    mCallback.onError(e);
                }
            }

            @Override
            public void onUIThread() {
                if(mCallback != null) {
                    mCallback.onUIThread();
                }
            }
        });
    }

    public DocumentRequest<I> Header(String name, String value) {
        this.mTask.Header(name, value);
        return this;
    }

    public DocumentRequest<I> param(String key, String value) {
        this.mTask.param(key, value);
        return this;
    }
    public DocumentRequest<I> param(String key, int value) {
        this.mTask.param(key, value);
        return this;
    }

    public DocumentRequest<I> setCallback(RequestCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public void cancel(boolean b) {
        this.mTask.cancel(b);
    }

    public void run() {
        try {
            this.mTask.execute();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected I parse(PostRequestParser doc) {
        return null;
    }

    public static class RequestCallback<I> {
        public void onSuccess(I obj){}
        public void onError(Exception e){}
        public void onUIThread(){}
    }

}
