package com.asedias.bugtracker.async.base;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.others.AuthorizationNeededException;
import com.asedias.bugtracker.others.InternetException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rorom on 11.04.2018.
 */

public class GetDocument extends AsyncTask<Void, Void, Integer> {

    private Callback<String> mCallback;
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

    public GetDocument(String mUrl) {
        this.mUrl = mUrl;
        SharedPreferences prefs = BugTrackerApp.context.getSharedPreferences("user", 0);
        this.cookies = prefs.getString("cookies", "");
        this.builder = new Request.Builder();
        this.client = new OkHttpClient();
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
        this.request = this.builder.addHeader("Cookie", this.cookies).url(this.mUrl).build();
        try {
            this.response = this.client.newCall(this.request).execute();
            String re = this.response.body().string();
            //Document document = Jsoup.parse(re);
            //Log.d("bugtracker uri", this.mUrl);
            if(re.contains("<!>3<!>")) {
                /*Log.d("cookie", "CURRENT: " + this.request.headers());
                Log.d("cookie", "NEW: " + this.response.headers());
                String regex = "(<!--)?([0-9]*)<!>.*<!>([0-9]*)<!>([0-9]*)<!>([0-9]*)<!>(\\w*)<!>(\\w*-?)";
                String ip_h = re.replaceAll(regex, "$6");
                String to = re.replaceAll(regex, "$7");
                Log.d("Relogin", "ip_h: " + ip_h + "\n" + "to: " + to);
                new GetLoginQHash(ip_h, to).setCallback(new DocumentRequest.RequestCallback() {}).run();
                Log.d("bugtracker uri", re);
                return EXIT_NO_INTERNET;*/
                this.mCallback.onError(new AuthorizationNeededException());
                return EXIT_AUTH;
            }
            /*if(document.getElementsByClass("message_page_body").size() > 0) {
                this.mCallback.onError(new AccessDeniedException());
                return EXIT_ACCESS;
            }*/
            this.mCallback.onBackground(re);
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
            if (mUrl.contains("https://vk.com/bugtracker?act=reporter&id=")) return;
            mCallback.onUIThread();
        }
        if(out == EXIT_NO_INTERNET) {
            mCallback.onError(new InternetException());
        }
    }

    public GetDocument setCallback(Callback<String> callback) {
        this.mCallback = callback;
        return this;
    }
}
