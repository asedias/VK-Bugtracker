package com.asedias.bugtracker.ui;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.MainActivity;
import com.asedias.bugtracker.R;

import org.w3c.dom.Text;

/**
 * Created by rorom on 13.05.2018.
 */

public class ThemeManager {
    public static int currentTheme;
    private static int currentPrimary;
    public static int currentTextColor;
    private static SharedPreferences preferences;
    private static String KEY_THEME = "currentTheme";

    public ThemeManager() {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(BugTrackerApp.context);
        currentTheme = this.preferences.getInt(KEY_THEME, R.style.AppTheme);
        currentPrimary = currentTheme == R.style.AppTheme ? LightPrimary() : DarkPrimary();
        currentTextColor = currentTheme == R.style.AppTheme ? Color.BLACK : Color.WHITE;
    }

    private static void updateTheme() {
        currentTheme = (currentTheme == R.style.AppTheme) ? R.style.AppTheme_Dark : R.style.AppTheme;
        currentPrimary = currentTheme == R.style.AppTheme ? LightPrimary() : DarkPrimary();
        currentTextColor = currentTheme == R.style.AppTheme ? Color.BLACK : Color.WHITE;
        saveTheme();
    }

    private static void saveTheme() {
        preferences.edit().putInt(KEY_THEME, currentTheme).apply();
    }

    private static int LightPrimary() {
        return BugTrackerApp.Color(R.color.colorPrimaryLight);
    }

    private static int DarkPrimary() {
        return BugTrackerApp.Color(R.color.colorPrimaryDark);
    }

    private static void getAllViews(ViewGroup viewGroup) {
        int newPrimary = currentTheme == R.style.AppTheme ? DarkPrimary() : LightPrimary();
        int newTextColor = currentTheme == R.style.AppTheme ? Color.WHITE : Color.BLACK;
        for(int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            boolean staticColorView = view.getId() == android.support.design.R.id.design_menu_item_text ||
                            view.getId() == R.id.title_holder ||
                            view.getId() == R.id.profile_subtitle ||
                            view.getId() == R.id.tag ||
                            view.getId() == R.id.time ||
                            view.getId() == R.id.state;
            if(view instanceof TextView && !staticColorView) {
                ObjectAnimator anim = ObjectAnimator.ofInt((TextView)view, "textColor", currentTextColor, newTextColor);
                anim.setEvaluator(new ArgbEvaluator());
                anim.setDuration(400).start();
            } else if(view instanceof ViewGroup) {
                boolean navigation = view.getId() == R.id.navigation;
                if(view.getId() == R.id.toolbar || navigation) {
                    ObjectAnimator anim = ObjectAnimator.ofInt(view, "backgroundColor", currentPrimary, newPrimary);
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.setDuration(400).start();
                }
                if(!navigation) getAllViews((ViewGroup) view);
            }
        }
    }

    public static void changeTheme(final MainActivity activity) {
        int newPrimary = currentTheme == R.style.AppTheme ? DarkPrimary() : LightPrimary();
        ObjectAnimator anim = ObjectAnimator.ofInt(activity.getWindow().getDecorView(), "backgroundColor", currentPrimary, newPrimary);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setDuration(400).start();
        anim.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) { }
            @Override public void onAnimationEnd(Animator animation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN + View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    if(Build.VERSION.SDK_INT >= 23 && currentTheme == R.style.AppTheme_Dark) {
                        visibility += View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    }
                    if(Build.VERSION.SDK_INT >= 26 && currentTheme == R.style.AppTheme_Dark) {
                        visibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                    }
                    activity.getWindow().getDecorView().setSystemUiVisibility(visibility);
                }
                updateTheme();
            }

            @Override public void onAnimationCancel(Animator animation) { }
            @Override public void onAnimationRepeat(Animator animation) { }
        });
        getAllViews((ViewGroup) activity.getWindow().getDecorView());
    }

}
