package com.vkontakte.android.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.asedias.bugtracker.R;
import com.vkontakte.android.functions.VoidF1;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;
import com.vkontakte.android.ui.holder.commons.BackgroundHolder;
import com.vkontakte.android.ui.holder.commons.PreferenceIconItemHolder;
import com.vkontakte.android.ui.holder.commons.TitleHolder;

import java.util.ArrayList;
import java.util.List;

public class SettingsListFragment extends CardRecyclerFragment implements OnClickListener, VoidF1, RecyclerSectionAdapter.DataDelegate {

   private final SettingsAdapter adapter = new SettingsAdapter(this);
   private View headerView;

   public SettingsListFragment() {
      super(10);
      this.setRefreshEnabled(false);
   }

   protected void doLoadData(int var1, int var2) {
      this.onDataLoaded(this.adapter.createData(), false);
   }

   protected Adapter getAdapter() {
      return this.adapter;
   }

   public List getData() {
      return this.data;
   }

   public void onAttach(Activity var1) {
      super.onAttach(var1);
      this.setTitle("!@Settings");
      this.loadData();
   }

   public void onClick(View var1) {
      switch(var1.getId()) {
      case 2131755451:
         /*String var3 = VKAccountManager.getCurrent().getPhoto();
         boolean var2;
         if(var3 != null && !var3.endsWith(".gif")) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.showUpdatePhotoDlg(var2);*/
         return;
      case 2131756143:
         //Navigate.to(ProfileEditFragment.class, new Bundle(), this.getActivity());
         return;
      default:
      }
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      if(!this.data.isEmpty() && ((RecyclerSectionAdapter.Data)this.data.get(0)).type == 3) {
         this.data.set(0, RecyclerSectionAdapter.Data.middle(3, Integer.valueOf(R.drawable.card_top_fix_item)));
      }
      this.updateDecorator();
   }

   public View onCreateContentView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      this.headerView = View.inflate(this.getActivity(), R.layout.settings_list_header, (ViewGroup)null);
      return super.onCreateContentView(var1, var2, var3);
   }

   public void onDestroyView() {
      super.onDestroyView();
      this.headerView = null;
   }

   public void f(SettingsListFragment.Item var1) {
      /*if(var1.fragment != null) {
         Navigate.to(var1.fragment, new Bundle(), this.getActivity());
      } else {
         var1.onClick.run();
      }*/
   }

   @Override
   public void f(Object var1) {
      f(((SettingsListFragment.Item)var1));
   }

   public static class Item extends PreferenceIconItemHolder.IconPrefInfo {

      public Class fragment;
      public Runnable onClick;

      public Item(@DrawableRes int var1, String var2) {
         super(var1, var2);
         this.onClick = new Runnable() {
            @Override
            public void run() {

            }
         };
      }
      
      public Item(@DrawableRes int var1, String var2, Class var3) {
         super(var1, var2);
         this.fragment = var3;
      }

      public Item(@DrawableRes int var1, String var2, Runnable var3) {
         super(var1, var2);
         this.onClick = var3;
      }
      
   }

   private class SettingsAdapter extends RecyclerSectionAdapter<RecyclerHolder> {

      private static final int TYPE_BOTTOM = 3;
      private static final int TYPE_HEADER = 5;
      private static final int TYPE_ICON_PREF = 2;
      private static final int TYPE_TITLE = 1;


      public SettingsAdapter(RecyclerSectionAdapter.DataDelegate var2) {
         super(var2);
      }

      ArrayList createData() {
         ArrayList var1 = new ArrayList();
         var1.add(RecyclerSectionAdapter.Data.middle(TYPE_BOTTOM, R.drawable.card_top_fix_item));
         var1.add(RecyclerSectionAdapter.Data.bottom(TYPE_HEADER, (Object)null));
         var1.add(RecyclerSectionAdapter.Data.top(TYPE_TITLE, R.string.app_name));
         var1.add(RecyclerSectionAdapter.Data.middle(TYPE_ICON_PREF, new Item(R.drawable.ic_launcher_background, var1.size() + "")));
         var1.add(RecyclerSectionAdapter.Data.middle(TYPE_ICON_PREF, new Item(R.drawable.ic_launcher_background, var1.size() + "")));
         var1.add(RecyclerSectionAdapter.Data.middle(TYPE_ICON_PREF, new Item(R.drawable.ic_launcher_background, var1.size() + "")));
         var1.add(RecyclerSectionAdapter.Data.middle(TYPE_ICON_PREF, new Item(R.drawable.ic_launcher_background, var1.size() + "")));
         var1.add(RecyclerSectionAdapter.Data.middle(TYPE_ICON_PREF, new Item(R.drawable.ic_launcher_background, var1.size() + "")));
         var1.add(RecyclerSectionAdapter.Data.bottom(TYPE_BOTTOM, R.drawable.card_top_fix_item));
         var1.add(RecyclerSectionAdapter.Data.top(TYPE_TITLE, R.string.app_name));
         var1.add(RecyclerSectionAdapter.Data.middle(TYPE_ICON_PREF, new Item(R.drawable.ic_launcher_background, var1.size() + "")));
         var1.add(RecyclerSectionAdapter.Data.middle(TYPE_ICON_PREF, new Item(R.drawable.ic_launcher_background, var1.size() + "")));
         var1.add(RecyclerSectionAdapter.Data.bottom(TYPE_BOTTOM, R.drawable.card_top_fix_item));
         return var1;
      }

      public RecyclerHolder onCreateViewHolder(final ViewGroup var1, int var2) {
         switch(var2) {
         case 1:
            return TitleHolder.blueTitle(var1);
         case 2:
         default:
            return new PreferenceIconItemHolder(var1, SettingsListFragment.this);
         case 3:
            return new BackgroundHolder(var1);
         case 5:
            return new RecyclerHolder(SettingsListFragment.this.headerView) {
               public void bind(Object var1) {}
            };
         }
      }

      @Override
      public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
         super.onBindViewHolder(holder, position);
      }
   }
}
