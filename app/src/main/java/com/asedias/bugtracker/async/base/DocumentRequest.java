package com.asedias.bugtracker.async.base;

import org.jsoup.nodes.Document;

/**
 * Created by rorom on 11.05.2018.
 */

public class DocumentRequest<I extends Object> {
    protected GetDocument mTask;
    protected String url = "https://vk.com/al_bugtracker.php?";
    protected RequestCallback mCallback;

    public DocumentRequest(boolean post) {
        this.mTask = new GetDocument(url).setCallback(new Callback<String>() {
            @Override
            public void onBackground(String document) {
                mCallback.onSuccess(parse(document));
            }

            @Override
            public void onError(Exception e) {
                mCallback.onError(e);
            }

            @Override
            public void onUIThread() {
                mCallback.onUIThread();
            }
        });
        this.param("al", post ? "1" : "0");
    }

    public DocumentRequest param(String key, String value) {
        this.mTask.param(key, value);
        return this;
    }
    public DocumentRequest param(String key, int value) {
        this.mTask.param(key, value);
        return this;
    }

    public DocumentRequest setCallback(RequestCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public void cancel(boolean b) {
        this.mTask.cancel(b);
    }

    public void run() {
        this.mTask.execute();
    }

    protected I parse(String doc) {
        return null;
    }

    public static class RequestCallback<T> {
        public void onSuccess(T obj){}
        public void onError(Exception e){}
        public void onUIThread(){}
    }

}
