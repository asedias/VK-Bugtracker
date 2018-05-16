package com.asedias.bugtracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.asedias.bugtracker.async.GetUserInfo;
import com.asedias.bugtracker.async.base.Callback;
import com.asedias.bugtracker.async.base.DocumentRequest;
import com.asedias.bugtracker.async.base.GetDocument;
import com.asedias.bugtracker.model.ProfileItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSharedPreferences("user", 0).getString("user_id", "").isEmpty()) clearCookies();
        //clearAndFillCookies();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("blank.html")) {
                    SharedPreferences.Editor editor = getSharedPreferences("user", 0).edit();
                    editor.putString("url", url);
                    editor.putString("time", (System.currentTimeMillis() / 1000) + "");
                    url = url.substring("https://oauth.vk.com/blank.html#".length());
                    String[] result = url.split("&");
                    for (int i = 0; i < result.length; i++) {
                        editor.putString(result[i].split("=")[0], result[i].split("=")[1]);
                    }
                    String cookies = CookieManager.getInstance().getCookie("https://vk.com");
                    editor.putString("cookies", cookies);
                    editor.apply();
                    editor.commit();
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    //finish();
                    getInfoAndStart();
                }
            }

        });

        //webView.loadDataWithBaseURL(getLoginURL(false), null, "text/html", "UTF-8", null);
        webView.loadUrl(getLoginURL(false));
    }

    private void getInfoAndStart() {
        final ProgressDialog dialog = ProgressDialog.show(this, BugTrackerApp.String(R.string.title_activity_login), BugTrackerApp.String(R.string.loading));
        new GetUserInfo(UserData.getUID()).setCallback(new DocumentRequest.RequestCallback<GetUserInfo.Result>() {
            @Override
            public void onSuccess(GetUserInfo.Result obj) {
                SharedPreferences.Editor editor = BugTrackerApp.context.getSharedPreferences("user", 0).edit();
                editor.putString("user_photo", obj.photo);
                editor.putString("user_name", obj.name);
                editor.putString("user_subtitle", obj.subtitle);
                editor.apply();
            }
        }).run();
    }

    private String getLoginURL(boolean revoke) {
        String client_id = "4967124";
        return "https://oauth.vk.com/authorize?" +
                "client_id=" + client_id +
                "&scope=65536" +
                "&display=mobile" +
                "&v=5.47" +
                "&response_type=token";
    }

    public static boolean isLoggedOn(Context context) {
        SharedPreferences pref = context.getSharedPreferences("user", 0);
        return (pref.contains("access_token"));
    }

    public static void clearCookies()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d("Login", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else
        {
            Log.d("Login", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(BugTrackerApp.context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    public static void clearPrefs() {
        BugTrackerApp.context.getSharedPreferences("user", 0).edit().clear().apply();
    }


}
