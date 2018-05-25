package com.asedias.bugtracker.async.base;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.others.AuthorizationNeededException;
import com.asedias.bugtracker.others.InternetException;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

/**
 * Created by rorom on 11.04.2018.
 */

public class GetDocument extends AsyncTask<Void, Void, Integer> {

    private Callback<PostRequestParser> mCallback;
    private String mUrl;
    private final int EXIT_AUTH = 1;
    private final int EXIT_ACCESS = 2;
    private final int EXIT_SUCCESS = 0;
    private final int EXIT_NO_INTERNET = 4;
    public OkHttpClient client;
    public Request request;
    public Response response;
    public Request.Builder builder;
    public String cookies;
    public List<Header> headers = new ArrayList<>();

    public GetDocument(String mUrl) {
        this.mUrl = mUrl;
        SharedPreferences prefs = BugTrackerApp.context.getSharedPreferences("user", 0);
        this.cookies = prefs.getString("cookies", "");
        this.builder = new Request.Builder();
        this.client = new OkHttpClient();
        this.headers.add(new Header("Cookie", this.cookies));
    }

    public void Header(String name, String value) {
        this.headers.add(new Header(name, value));
    }

    public void cookie(String key, String value) {
        this.cookies += "; " + key + "=" + value;
    }

    public GetDocument param(String key, String value) {
        this.mUrl += "&"+key+"="+value;
        return this;
    }
    public GetDocument param(String key, int value) {
        this.mUrl += "&"+key+"="+value;
        return this;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        for(int i = 0; i < headers.size(); i++) {
            this.builder.addHeader(headers.get(i).name.utf8(), headers.get(i).value.utf8());
        }
        this.request = this.builder.url(this.mUrl).build();
        try {
            this.response = this.client.newCall(this.request).execute();
            String re = this.response.body().string();
            PostRequestParser parser;
            try {
                parser = new PostRequestParser(re);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                parser = new PostRequestParser();
                parser.html = Jsoup.parse(re);
            }
            if(parser.error_code == 3) {
                this.mCallback.onError(new AuthorizationNeededException());
                return EXIT_AUTH;
            }
            this.mCallback.onBackground(parser);
            response.close();
        } catch (IOException e) {
            return EXIT_NO_INTERNET;
        }
        return EXIT_SUCCESS;
    }

    @Override
    protected void onPreExecute() {
        if(mCallback == null) {
            throw new RuntimeException("Set Callback first: setCallback(Callback callback)");
        }
        if(mUrl.isEmpty()) {
            throw new RuntimeException("mUrl is Empty");
        }
    }

    @Override
    protected void onPostExecute(Integer out) {
        if(out == EXIT_SUCCESS) {
            mCallback.onUIThread();
        }
        if(out == EXIT_NO_INTERNET) {
            mCallback.onError(new InternetException());
        }
    }

    public GetDocument setCallback(Callback<PostRequestParser> callback) {
        this.mCallback = callback;
        return this;
    }
}
