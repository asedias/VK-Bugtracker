package com.vkontakte.android.ui.holder.commons;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;

import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

public class BackgroundHolder extends RecyclerHolder {

   public BackgroundHolder(@NonNull ViewGroup var1) {
      super(createView(var1));
   }

   private static View createView(ViewGroup var0) {
      LinearLayout var1 = new LinearLayout(var0.getContext());
      var1.setLayoutParams(new LayoutParams(-1, -2));
      var1.setClickable(true);
      return var1;
   }

   public void bind(Integer var1) {
      this.itemView.setBackgroundResource(var1.intValue());
   }

   public BackgroundHolder setRes(@DrawableRes int var1) {
      this.itemView.setBackgroundResource(var1);
      return this;
   }

   @Override
   public void bind(Object var1) {
      if(var1 instanceof Integer) {
         bind((Integer)var1);
      } else if(var1 instanceof RecyclerSectionAdapter.Data) {
         bind(((RecyclerSectionAdapter.Data) var1).object);
      }
   }
}
