package com.vkontakte.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asedias.bugtracker.R;
import com.vkontakte.android.navigation.ToolbarHelper;

import me.grishka.appkit.fragments.BaseRecyclerFragment;
import me.grishka.appkit.utils.V;

public abstract class VKRecyclerFragment extends BaseRecyclerFragment {

   public VKRecyclerFragment(int var1) {
      super(var1);
   }

   public VKRecyclerFragment(int var1, int var2) {
      super(var1, var2);
   }

   protected boolean canGoBack() {
      return ToolbarHelper.canGoBack(this);
   }

   public boolean hasNavigationDrawer() {
      return ToolbarHelper.hasNavigationDrawer(this);
   }

   public View onCreateContentView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      View var4 = super.onCreateContentView(var1, var2, var3);
      this.list.setPadding(0, V.dp(8.0F), 0, V.dp(8.0F));
      //this.items.setSelector(2130837806);
      this.refreshLayout.setColorSchemeResources(new int[]{R.color.colorPrimary});
      return var4;
   }

   public void onToolbarNavigationClick() {
      ToolbarHelper.onToolbarNavigationClick(this);
   }

   public void onViewCreated(View var1, Bundle var2) {
      super.onViewCreated(var1, var2);
      ToolbarHelper.onViewCreated(this.getToolbar());
   }

}
