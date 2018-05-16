package com.asedias.bugtracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.vkontakte.android.ui.FitSystemWindowsFragmentWrapperFrameLayout;

import static com.asedias.bugtracker.ui.ThemeManager.currentTheme;

/**
 * Created by rorom on 18.04.2018.
 */

public class FragmentWrapperActivity extends AppCompatActivity {

    public static Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(currentTheme);
        super.onCreate(savedInstanceState);
        ViewGroup tlView = (ViewGroup) getWindow().getDecorView();
        tlView.setFocusable(true);
        tlView.setFocusableInTouchMode(true);
        tlView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN + View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if(Build.VERSION.SDK_INT >= 23 && currentTheme == R.style.AppTheme) {
                visibility += View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            if(Build.VERSION.SDK_INT >= 26 && currentTheme == R.style.AppTheme) {
                visibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                getWindow().setNavigationBarColor(0);
            }
            getWindow().getDecorView().setSystemUiVisibility(visibility);
        }
        FitSystemWindowsFragmentWrapperFrameLayout content = new FitSystemWindowsFragmentWrapperFrameLayout(this);
        content.setId(R.id.content);
        content.setFitsSystemWindows(true);
        setContentView(content);
        initFragment();
    }

    public FragmentWrapperActivity start(Activity activity) {
        Intent intent = new Intent(activity, this.getClass());
        activity.startActivity(intent);
        return this;
    }

    public static void startWithFragment(Activity activity, Fragment fragment, Bundle args) {
        Intent intent = new Intent(activity, FragmentWrapperActivity.class);
        intent.putExtra("args", args);
        intent.putExtra("class", fragment.getClass().getSimpleName());
        activity.startActivity(intent);
    }

    public void replaceFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    public void initFragment() {
        String fragName = getIntent().getStringExtra("class");
        Bundle args = getIntent().getBundleExtra("args");
        try
        {
            Fragment fr = (Fragment) Class.forName("com.asedias.bugtracker.fragments." + fragName).newInstance();
            fr.setArguments(args);
            replaceFragment(fr);
            mFragment = fr;

        }
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
    }
}
