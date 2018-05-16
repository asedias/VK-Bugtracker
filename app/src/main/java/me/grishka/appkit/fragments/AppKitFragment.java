package me.grishka.appkit.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.R;
import com.asedias.bugtracker.ui.Fonts;

import java.lang.reflect.Field;

import me.grishka.appkit.utils.V;

public class AppKitFragment extends DialogFragment {

   public static final String EXTRA_IS_TAB = "__is_tab";
   private boolean hasOptionsMenu;
   protected int scrW;
   private CharSequence subtitle;
   private boolean subtitleMarquee = true;
   private CharSequence title;
   private boolean titleMarquee = true;
   private Toolbar toolbar;
   private AppBarLayout appbar;
   private boolean viewCreated;
   private int backArrow = R.drawable.ic_ab_back_arrow;


   private void invalidateToolbarMenu() {
      try {
         this.toolbar.getMenu().clear();
         if(this.hasOptionsMenu) {
            this.onCreateOptionsMenu(this.toolbar.getMenu(), new MenuInflater(this.getActivity()));
         }

      } catch (Throwable var2) {
         Log.e("AppKit", "error invalidateToolbarMenu");
      }
   }

   private void updateToolbarMarquee() {
      if (this.toolbar != null) {
         TextView title = null;
         TextView subtitle = null;
         try {
            Field fld = this.toolbar.getClass().getDeclaredField(
                    "mTitleTextView");
            fld.setAccessible(true);
            title = (TextView) fld.get(this.toolbar);
            fld = this.toolbar.getClass().getDeclaredField("mSubtitleTextView");
            fld.setAccessible(true);
            subtitle = (TextView) fld.get(this.toolbar);
         } catch (Exception e) {
            Toast.makeText(getActivity(), "ERROR: " + e.toString(), Toast.LENGTH_LONG).show();
         }
         if (title != null) {
            title.setFadingEdgeLength(V.dp(10.0f));
            title.setHorizontalFadingEdgeEnabled(true);
            title.setMarqueeRepeatLimit(2);
            title.setTextSize(18);
            title.setTypeface(Fonts.Regular);
            //title.setTextColor(BugTrackerApp.Color(R.color.colorPrimary));
            if(!canGoBack()) setCenter(title);
            if (this.titleMarquee) {
               title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
               title.setSelected(true);
            } else {
               title.setSelected(false);
               title.setEllipsize(TextUtils.TruncateAt.END);
            }
         }
         if (subtitle != null) {
            subtitle.setFadingEdgeLength(V.dp(10.0f));
            subtitle.setHorizontalFadingEdgeEnabled(true);
            subtitle.setMarqueeRepeatLimit(2);
            subtitle.setTextSize(14);
            subtitle.setTypeface(Fonts.Regular);
            if(!canGoBack()) setCenter(subtitle);
            if (this.subtitleMarquee) {
               subtitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
               subtitle.setSelected(true);
            } else {
               subtitle.setSelected(false);
               subtitle.setEllipsize(TextUtils.TruncateAt.END);
            }
         }
      }
   }

   private void setCenter(TextView title) {
      title.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
      title.setGravity(Gravity.CENTER);
      float right = BugTrackerApp.dp(8) + BugTrackerApp.dp(getToolbar().getMenu().size() * 48);
      float left = (getToolbar().getNavigationIcon() != null) ? BugTrackerApp.dp(72) : BugTrackerApp.dp(16);
      float width = title.getPaint().measureText(title.getText().toString());
      float maxwidth = BugTrackerApp.dp(this.scrW) - left - right;
      if(width < maxwidth) {
         if (left > right) {
            title.setPadding(0, 0, (int) (left - right), 0);
            //Log.d("TITLE", "left > right : " + (left - right));
         } else {
            title.setPadding((int) (right - left), 0, 0, 0);
            //Log.d("TITLE", "right > left : " + (right - left));
         }
      }
   }

   protected boolean canGoBack() {
      return this.getArguments() != null && this.getArguments().getBoolean("_can_go_back");
   }

   public Context getContext() {
      return this.getActivity();
   }

   public Toolbar getToolbar() {
      return this.toolbar;
   }

   public AppBarLayout getAppbar() {
      return appbar;
   }

   protected Context getToolbarContext() {
      TypedArray var2 = this.getActivity().getTheme().obtainStyledAttributes(new int[]{2130772063});
      int var1 = var2.getResourceId(0, 0);
      var2.recycle();
      return (Context)(var1 == 0?this.getActivity():new ContextThemeWrapper(this.getActivity(), var1));
   }

   protected Context getToolbarPopupContext() {
      TypedArray var2 = this.getActivity().getTheme().obtainStyledAttributes(new int[]{2130772060});
      int var1 = var2.getResourceId(0, 0);
      var2.recycle();
      return (Context)(var1 == 0?this.getActivity():new ContextThemeWrapper(this.getActivity(), var1));
   }

   public boolean hasNavigationDrawer() {
      return false;
   }

   public void invalidateOptionsMenu() {
      if(this.toolbar != null) {
         this.invalidateToolbarMenu();
      } else if(this.getActivity() != null) {
         this.getActivity().invalidateOptionsMenu();
         return;
      }

   }

   public boolean isSubitleMarqueeEnabled(boolean var1) {
      return this.subtitleMarquee;
   }

   public boolean isTitleMarqueeEnabled(boolean var1) {
      return this.titleMarquee;
   }

   public void onAttach(Activity var1) {
      super.onAttach(var1);
      V.setApplicationContext(var1);
      this.updateConfiguration();
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.updateConfiguration();
   }

   public void onDestroyView() {
      super.onDestroyView();
      this.toolbar = null;
   }

   public void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      if(VERSION.SDK_INT == 15) {
         this.setUserVisibleHint(true);
      }

   }

   public void onToolbarNavigationClick() {
      if(this.getFragmentManager().getBackStackEntryCount() > 0) {
         this.getFragmentManager().popBackStack();
      }
   }

   public void onViewCreated(View var1, Bundle var2) {
      super.onViewCreated(var1, var2);
      this.toolbar = (Toolbar)var1.findViewById(R.id.toolbar);
      this.appbar = (AppBarLayout) var1.findViewById(R.id.appBarLayout);
      ViewCompat.setElevation(getAppbar(), 0);
      if(this.toolbar != null && this.getArguments() != null && this.getArguments().getBoolean("__is_tab")) {
         ((ViewGroup)this.toolbar.getParent()).removeView(this.toolbar);
         this.toolbar = null;
      }
      this.viewCreated = true;
      if(this.toolbar != null) {
         if(this.title != null) {
            this.toolbar.setTitle(this.title);
         }

         if(this.subtitle != null) {
            this.toolbar.setSubtitle(this.subtitle);
         }

         if(this.hasOptionsMenu) {
            this.invalidateToolbarMenu();
            this.toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
               public boolean onMenuItemClick(MenuItem var1) {
                  return AppKitFragment.this.onOptionsItemSelected(var1);
               }
            });
         }

         if(this.canGoBack()) {
            this.toolbar.setNavigationIcon(backArrow);
         } else if(this.hasNavigationDrawer()) {
            this.toolbar.setNavigationIcon(null);
         } else {
            this.toolbar.setNavigationIcon(R.drawable.ic_navigation_logo);
         }

         this.toolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               AppKitFragment.this.onToolbarNavigationClick();
            }
         });
      } else {
         if(this.title != null && (this.getArguments() == null || !this.getArguments().getBoolean("_dialog"))) {
            this.getActivity().setTitle(this.title);
         }

         if(this.getActivity().getActionBar() != null && (this.getArguments() == null || !this.getArguments().getBoolean("_dialog"))) {
            if(this.subtitle != null) {
               this.getActivity().getActionBar().setSubtitle(this.subtitle);
            }
         }
      }

      this.updateToolbarMarquee();
      Activity var3 = this.getActivity();
      if(var3 instanceof OnFragmentViewCreated) {
         ((OnFragmentViewCreated)var3).onFragmentViewCreated(this);
      }

   }

   public void setHasOptionsMenu(boolean var1) {
      super.setHasOptionsMenu(var1);
      this.hasOptionsMenu = var1;
      this.invalidateOptionsMenu();
   }

   protected void setSubtitle(int var1) {
      this.setSubtitle(this.getString(var1));
   }

   protected void setSubtitle(CharSequence var1) {
      this.subtitle = var1;
      if (viewCreated) {
         this.toolbar.setSubtitle(var1);
         this.updateToolbarMarquee();
      }
   }

   public void setSubtitleMarqueeEnabled(boolean var1) {
      this.subtitleMarquee = var1;
      this.updateToolbarMarquee();
   }

   protected void setTitle(int var1) {
      this.setTitle(this.getString(var1));
   }

   protected void setTitle(CharSequence var1) {
      this.title = var1;
      if (viewCreated) {
         this.toolbar.setTitle(var1);
         this.updateToolbarMarquee();
      }
   }

   public void setTitleMarqueeEnabled(boolean var1) {
      this.titleMarquee = var1;
      this.updateToolbarMarquee();
   }

   protected void updateConfiguration() {
      this.scrW = this.getResources().getConfiguration().screenWidthDp;
   }

}
