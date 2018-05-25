package com.asedias.bugtracker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.asedias.bugtracker.async.UploadDoc;
import com.asedias.bugtracker.async.methods.GetUploadServer;
import com.asedias.bugtracker.async.methods.GetUserInfo;
import com.asedias.bugtracker.async.DocumentRequest;
import com.asedias.bugtracker.async.methods.UpdateCookie;
import com.asedias.bugtracker.ui.ButtomNavigationInstance;
import com.vkontakte.android.ui.FitSystemWindowsFragmentWrapperFrameLayout;
import com.vkontakte.android.upload.UploadUtils;

import java.io.File;

import static com.asedias.bugtracker.ui.ThemeManager.currentTheme;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(currentTheme);
        super.onCreate(savedInstanceState);
        BugTrackerApp.setAppDisplayMetrix(this);
        if(!LoginActivity.isLoggedOnAndActual()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            Log.d("bugtracker auth", "User is invalid");
            finish();
            return;
        }
        new GetUserInfo(UserData.getUID()).setCallback(new DocumentRequest.RequestCallback<GetUserInfo.Result>() {
            @Override public void onSuccess(GetUserInfo.Result obj) {
                UserData.updateUserData(obj);
            }
        }).run();
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN + View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if(currentTheme == R.style.AppTheme) {
                if(Build.VERSION.SDK_INT >= 23) {
                    visibility += View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                if(Build.VERSION.SDK_INT >= 26) {
                    visibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                    getWindow().setNavigationBarColor(0);
                }
            } else {
                getWindow().setNavigationBarColor(0);
            }
            getWindow().getDecorView().setSystemUiVisibility(visibility);
        }
        FitSystemWindowsFragmentWrapperFrameLayout content = findViewById(R.id.content);
        content.setFitsSystemWindows(true);
        new ButtomNavigationInstance(this).Setup(savedInstanceState != null);
        if(savedInstanceState == null) {
            if(this.getIntent().getBooleanExtra("crash", false)) {
                showCrashLog();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case 1234:
                if(resultCode==RESULT_OK){
                    String PathHolder = data.getData().getPath();
                    File file = new File(PathHolder);
                    Toast.makeText(MainActivity.this, file.exists() + "\n" + file.getAbsolutePath() , Toast.LENGTH_LONG).show();
                }
        }
    }


    private void showCrashLog() {
        AlertDialog.Builder var1 = new AlertDialog.Builder(this);
        var1.setTitle("Crash Log");
        var1.setMessage(this.getIntent().getStringExtra("crash_info"));
        var1.setPositiveButton("COPY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
                ((ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("crash", getIntent().getStringExtra("crash_info")));
                var1.dismiss();
            }
        });
        var1.show();
    }
}
