package com.asedias.bugtracker.async.methods;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.LoginActivity;
import com.asedias.bugtracker.async.DocumentRequest;
import com.asedias.bugtracker.async.base.PostRequestParser;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.internal.http2.Header;

/**
 * Created by rorom on 23.05.2018.
 */

public class UpdateCookie extends DocumentRequest {
    public UpdateCookie() {
        super(LoginActivity.getLoginURL());
        this.Header("Origin", "vk.com");
        this.Header("User-Agent", new WebView(BugTrackerApp.context).getSettings().getUserAgentString());
    }

    @Override
    protected Object parse(PostRequestParser doc) {
        Log.d("COOKIE", this.mTask.request.header("Cookie") + "\n" +
                   "->" + this.mTask.request.url().toString() + "\n" +
                   "->" + this.mTask.response.request().url()
                   );
        for(int i = 0; i < this.mTask.response.headers().size(); i++) {
            Log.d("COOKIE", this.mTask.response.headers().name(i) + "=" + this.mTask.response.headers().value(i));
        }
        return "";
    }
}
