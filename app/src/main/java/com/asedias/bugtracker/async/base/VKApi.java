package com.asedias.bugtracker.async.base;

import android.os.AsyncTask;
import android.util.Log;

import com.asedias.bugtracker.UserData;
import com.asedias.bugtracker.others.AuthorizationNeededException;
import com.asedias.bugtracker.others.InternetException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rorom on 19.05.2018.
 */

public class VKApi extends AsyncTask<Void, Void, Integer> {

    private Callback<JSONObject> mCallback;
    private String mUrlBase = "https://api.vk.com/method/";
    private String mUrl;
    private String mMethod;
    public OkHttpClient client;
    public Request request;
    public Response response;
    public Request.Builder builder;
    private final int EXIT_AUTH = 1;
    private final int EXIT_SUCCESS = 0;
    private final int EXIT_NO_INTERNET = 4;

    public VKApi(String mMethod) {
        this.mMethod = mMethod;
        this.mUrl = this.mUrlBase + this.mMethod;
        this.mUrl += "?access_token="+UserData.getAccessToken();
        this.param("v", "5.75");
        this.builder = new Request.Builder();
        this.client = new OkHttpClient();
    }

    public VKApi param(String key, String value) {
        this.mUrl += "&"+key+"="+value;
        return this;
    }

    public VKApi param(String key, int value) {
        this.mUrl += "&"+key+"="+value;
        return this;
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
    protected Integer doInBackground(Void... voids) {
        this.request = this.builder.url(this.mUrl).build();
        try {
            this.response = this.client.newCall(this.request).execute();
            String re = this.response.body().string();
            this.mCallback.onBackground(new JSONObject(re));
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            return EXIT_NO_INTERNET;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return EXIT_SUCCESS;
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

    public VKApi setCallback(Callback<JSONObject> callback) {
        this.mCallback = callback;
        return this;
    }

    public static String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2) h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
