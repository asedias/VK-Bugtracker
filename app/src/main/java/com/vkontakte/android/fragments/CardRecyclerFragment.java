package com.vkontakte.android.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.vkontakte.android.ui.CardItemDecorator;

import me.grishka.appkit.utils.V;
import me.grishka.appkit.views.UsableRecyclerView;

public abstract class CardRecyclerFragment extends VKRecyclerFragment {

   public static final int TABLET_MIN_WIDTH = 924;
   public static final int TABLET_PADDING = 924;
   private CardItemDecorator decorator;


   public CardRecyclerFragment(int var1) {
      super(var1);
   }

   public CardRecyclerFragment(int var1, int var2) {
      super(var1, var2);
   }

   protected boolean isTabletDecorator() {
      return this.scrW >= 924;
   }

   public void onAttach(Activity var1) {
      super.onAttach(var1);
      this.updateConfiguration();
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.updateConfiguration();
      this.updateDecorator();
   }

   protected CardItemDecorator onCreateCardDecorator() {
      boolean var3 = this.isTabletDecorator();
      UsableRecyclerView var4 = this.list;
      boolean var2;
      if(!var3) {
         var2 = true;
      } else {
         var2 = false;
      }

      CardItemDecorator var5 = new CardItemDecorator(var4, var2);
      var5.setPadding(V.dp(2.0F), V.dp(3.0F), V.dp(8.0F), 0);
      int var1;
      if(var3) {
         var1 = V.dp((float)Math.max(16, (this.scrW - 924) / 2));
      } else {
         var1 = 0;
      }

      this.list.setPadding(var1, 0, var1, 0);
      return var5;
   }

   public void onDestroyView() {
      super.onDestroyView();
      this.decorator = null;
   }

   public void onViewCreated(View var1, Bundle var2) {
      super.onViewCreated(var1, var2);
      //this.items.setScrollBarStyle(33554432);
      this.updateDecorator();
   }

   protected void updateDecorator() {
      this.list.removeItemDecoration(this.decorator);
      UsableRecyclerView var1 = this.list;
      CardItemDecorator var2 = this.onCreateCardDecorator();
      this.decorator = var2;
      var1.addItemDecoration(var2);
   }
}
