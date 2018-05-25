package com.asedias.bugtracker.fragments;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.FragmentWrapperActivity;
import com.asedias.bugtracker.LoginActivity;
import com.asedias.bugtracker.MainActivity;
import com.asedias.bugtracker.R;
import com.asedias.bugtracker.UserData;
import com.asedias.bugtracker.ui.CropCircleTransformation;
import com.asedias.bugtracker.ui.ThemeManager;
import com.asedias.bugtracker.ui.holder.ButtonHolder;
import com.squareup.picasso.Picasso;
import com.vkontakte.android.fragments.CardRecyclerFragment;
import com.vkontakte.android.fragments.SettingsListFragment;
import com.vkontakte.android.fragments.VKRecyclerFragment;
import com.vkontakte.android.functions.VoidF1;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;
import com.vkontakte.android.ui.holder.commons.BackgroundHolder;
import com.vkontakte.android.ui.holder.commons.PreferenceIconItemHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rorom on 22.04.2018.
 */

public class ProfileFragment extends VKRecyclerFragment implements View.OnClickListener, VoidF1<SettingsListFragment.Item>, RecyclerSectionAdapter.DataDelegate {

    private final SettingsAdapter adapter = new SettingsAdapter(this);
    private View headerView;
    public ProfileFragment() {
        super(10);
        this.setRefreshEnabled(false);
    }

    @Override
    protected void doLoadData(int var1, int var2) {
        this.onDataLoaded(this.adapter.createData(), false);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return this.adapter;
    }

    @Override
    public void onClick(View v) {

    }

    public void onAttach(Activity var1) {
        super.onAttach(var1);
        //this.adapter.data.clear();
        //this.data.clear();
        if(!loaded) this.loadData();
    }

    public void onConfigurationChanged(Configuration var1) {
        super.onConfigurationChanged(var1);
        if(!this.data.isEmpty() && ((RecyclerSectionAdapter.Data)this.data.get(0)).type == 3) {
            this.data.set(0, RecyclerSectionAdapter.Data.middle(3, R.drawable.card_margin_fix_item));
        }
    }

    public View onCreateContentView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
        this.headerView = View.inflate(getActivity(), R.layout.profile_header, (ViewGroup)null);
        return super.onCreateContentView(var1, var2, var3);
    }

    @Override
    public void onViewCreated(View var1, Bundle var2) {
        super.onViewCreated(var1, var2);
        this.setTitle(R.string.title_home);
        ((TextView)this.headerView.findViewById(R.id.title)).setText(UserData.getName());
        ((TextView)this.headerView.findViewById(R.id.title)).setTextColor(ThemeManager.currentTextColor);
        ((TextView)this.headerView.findViewById(R.id.profile_subtitle)).setText(UserData.getSubtitle());
        Picasso.with(getActivity()).load(UserData.getPhoto()).placeholder(BugTrackerApp.Drawable(R.drawable.placeholder_user)).transform(new CropCircleTransformation()).into((ImageView) this.headerView.findViewById(R.id.icon));
        list.setPadding(0, 0, 0, 0);
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.headerView = null;
    }

    @Override
    public void f(SettingsListFragment.Item var1) {
      if(var1.fragment != null) {
          try {
              FragmentWrapperActivity.startWithFragment(getActivity(), (Fragment)var1.fragment.newInstance(), new Bundle());
          } catch (java.lang.InstantiationException e) {
              e.printStackTrace();
          } catch (IllegalAccessException e) {
              e.printStackTrace();
          }
      } else {
         var1.onClick.run();
      }
    }

    public Runnable goFragment(final Fragment fr, final Bundle args) {
        return new Runnable() {
            @Override
            public void run() {
                FragmentWrapperActivity.startWithFragment(getActivity(), fr, args);
            }
        };
    }

    public Runnable changeTheme() {
        return new Runnable() {
            @Override
            public void run() {
                ThemeManager.changeTheme((MainActivity) getActivity());
            }
        };
    }

    public Runnable showLogoutDialog() {
        return new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.prefs_logout);
                builder.setMessage(R.string.logout_description);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.clearPrefs();
                        LoginActivity.clearCookies();
                        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        };
    }

    @Override
    public List getData() {
        return this.data;
    }

    private class SettingsAdapter extends RecyclerSectionAdapter<RecyclerHolder> {

        private static final int TYPE_BOTTOM = 3;
        private static final int TYPE_HEADER = 5;
        private static final int TYPE_LOGOUT = 1;
        private static final int TYPE_ICON_PREF = 2;


        public SettingsAdapter(RecyclerSectionAdapter.DataDelegate var2) {
            super(var2);
        }

        ArrayList createData() {
            ArrayList var1 = new ArrayList();
            var1.add(RecyclerSectionAdapter.Data.top(TYPE_HEADER, (Object)null));
            //var1.add(RecyclerSectionAdapter.Data.top(TYPE_BOTTOM, R.drawable.card_margin_fix_item));
            var1.add(RecyclerSectionAdapter.Data.middle(TYPE_ICON_PREF, new SettingsListFragment.Item(R.drawable.ic_reports, getString(R.string.prefs_reports), goFragment(new ReportListFragment(), ReportListFragment.getBundle(UserData.getUID())))));
            var1.add(RecyclerSectionAdapter.Data.middle(TYPE_ICON_PREF, new SettingsListFragment.Item(R.drawable.ic_products, getString(R.string.prefs_products), goFragment(new ProductListFragment(), new Bundle()))));
            var1.add(RecyclerSectionAdapter.Data.middle(TYPE_ICON_PREF, new SettingsListFragment.Item(R.drawable.ic_bookmark, getString(R.string.prefs_bookmarks), goFragment(new ReportListFragment(), ReportListFragment.getBookmarkBundle()))));
            var1.add(RecyclerSectionAdapter.Data.middle(TYPE_ICON_PREF, new SettingsListFragment.Item(R.drawable.ic_members, getString(R.string.prefs_members))));
            var1.add(RecyclerSectionAdapter.Data.middle(TYPE_ICON_PREF, new SettingsListFragment.Item(R.drawable.ic_updates, getString(R.string.prefs_updates))));
            var1.add(RecyclerSectionAdapter.Data.middle(TYPE_ICON_PREF, new SettingsListFragment.Item(R.drawable.ic_bookmark, getString(R.string.change_theme), changeTheme())));
            //var1.add(RecyclerSectionAdapter.Data.bottom(TYPE_BOTTOM, R.drawable.card_margin_fix_item));
           // var1.add(RecyclerSectionAdapter.Data.top(TYPE_BOTTOM, R.drawable.card_margin_fix_item));
            //var1.add(RecyclerSectionAdapter.Data.middle(TYPE_ICON_PREF, new SettingsListFragment.Item(R.drawable.ic_about, getString(R.string.prefs_about))));
            var1.add(RecyclerSectionAdapter.Data.bottom(TYPE_BOTTOM, R.drawable.card_margin_fix_item));
            var1.add(RecyclerSectionAdapter.Data.top(TYPE_BOTTOM, R.drawable.card_top_fix_item));
            var1.add(RecyclerSectionAdapter.Data.middle(TYPE_LOGOUT, new SettingsListFragment.Item(0, getString(R.string.prefs_logout), showLogoutDialog())));
            var1.add(RecyclerSectionAdapter.Data.bottom(TYPE_BOTTOM, R.drawable.card_top_fix_item));
            return var1;
        }

        public RecyclerHolder onCreateViewHolder(final ViewGroup var1, int var2) {
            switch(var2) {
                case TYPE_ICON_PREF:
                default:
                    return new PreferenceIconItemHolder(var1, ProfileFragment.this);
                case TYPE_BOTTOM:
                    return new BackgroundHolder(var1);
                case TYPE_HEADER:
                    return new RecyclerHolder(headerView) {
                        public void bind(Object var1) {}
                    };
                case TYPE_LOGOUT: {
                    return new ButtonHolder(var1, ProfileFragment.this);
                }
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
            super.onBindViewHolder(holder, position);
        }
    }
}
