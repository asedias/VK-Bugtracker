package com.asedias.bugtracker.ui;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.R;

/**
 * Created by Рома on 18.04.2017.
 */

public class ToolbarBehavior extends AppBarLayout.ScrollingViewBehavior {

    public ToolbarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        Toolbar toolbar = (Toolbar) parent.findViewById(R.id.toolbar);
        float alpha = (dependency.getY() + dependency.getHeight()/2)/(dependency.getHeight() - dependency.getHeight()/2);
        if(alpha >= 0) {
            toolbar.setAlpha(alpha);
        } else {
            toolbar.setAlpha(0);
        }
        FloatingActionButton mFab = parent.findViewById(R.id.chains);
        if(mFab != null) {
            float full = BugTrackerApp.dp(64);
            mFab.setTranslationY(-full-dependency.getY()*3);
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
