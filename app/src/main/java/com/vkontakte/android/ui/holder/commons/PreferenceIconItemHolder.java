package com.vkontakte.android.ui.holder.commons;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asedias.bugtracker.R;
import com.asedias.bugtracker.ui.ThemeManager;
import com.vkontakte.android.functions.VoidF1;
import com.vkontakte.android.ui.holder.RecyclerHolder;

import me.grishka.appkit.views.UsableRecyclerView;

public class PreferenceIconItemHolder extends RecyclerHolder<PreferenceIconItemHolder.IconPrefInfo> implements UsableRecyclerView.Clickable {

   @Nullable
   private final VoidF1 clickFunk;
   protected final View icon = this.$(android.R.id.icon);
   protected final TextView text = (TextView)this.$(android.R.id.text1);
   private IconPrefInfo value;


   public PreferenceIconItemHolder(ViewGroup var1, @Nullable VoidF1 var2) {
      super(R.layout.icon_pref, var1);
      this.onViewInit();
      this.clickFunk = var2;
   }

   @Override
   public void bind(IconPrefInfo var1) {
      this.value = var1;
      this.icon.setBackgroundResource(var1.iconRes);
      this.text.setText(var1.text);
      this.text.setTextColor(ThemeManager.currentTextColor);
      //ViewUtils.setText(this.text, var1.text);
   }

   public void onClick() {
      if(this.clickFunk != null) {
         this.clickFunk.f(this.value);
      }

   }

   protected void onViewInit() {}

   public static class IconPrefInfo {

      @DrawableRes
      public final int iconRes;
      public final String text;


      public IconPrefInfo(@DrawableRes int var1, String var2) {
         this.iconRes = var1;
         this.text = var2;
      }
   }
}
