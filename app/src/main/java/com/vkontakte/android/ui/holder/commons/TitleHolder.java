package com.vkontakte.android.ui.holder.commons;

import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asedias.bugtracker.R;
import com.vkontakte.android.fragments.SettingsListFragment;
import com.vkontakte.android.ui.holder.RecyclerHolder;

import me.grishka.appkit.views.UsableRecyclerView;

public class TitleHolder extends RecyclerHolder {

   protected final TextView textView = (TextView)this.$(R.id.title_holder);
   @Nullable protected SettingsListFragment.Item value;

   public TitleHolder(@NonNull ViewGroup var1) {
      super(R.layout.title_holder, var1);
   }

   /*public static TitleHolder blackTitle(@NonNull ViewGroup var0) {
      return (new TitleHolder(var0)).withTextColorRes(2131689480);
   }

   public static TitleHolder darkGrayTitle(@NonNull ViewGroup var0) {
      return (new TitleHolder(var0)).withTextColorRes(2131689564);
   }

   public static TitleHolder grayTitle(@NonNull ViewGroup var0) {
      return (new TitleHolder(var0)).withTextColorRes(2131689563);
   }*/

   public static TitleHolder redTitle(@NonNull ViewGroup var0) {
      return (new TitleHolder(var0)).withTextColor(Color.RED).withTextSize(16);
   }

   public static TitleHolder blueTitle(@NonNull ViewGroup var0) {
      return (new TitleHolder(var0)).withTextColorRes(R.color.colorPrimary);
   }

   protected TitleHolder withTextColor(int var1) {
      this.textView.setTextColor(var1);
      return this;
   }

    public TitleHolder withTextSize(int var1) {
      this.textView.setTextSize(var1);
      return this;
   }

    protected  TitleHolder withTextColorRes(@ColorRes int var1) {
      this.withTextColor(this.getResources().getColor(var1));
      return this;
   }

   @Override
   public void bind(Object var1) {
      if(var1 instanceof SettingsListFragment.Item) {
         withText(((SettingsListFragment.Item) var1).text);
         this.value = (SettingsListFragment.Item) var1;
      } else {
         withText(Integer.parseInt(var1.toString()));
      }
      //ViewUtils.setText(this.textView, var1.toString());
   }

   public TitleHolder withText(@StringRes int var1) {
      this.textView.setText(var1);
      return this;
   }

   public TitleHolder withText(String var1) {
      this.textView.setText(var1);
      return this;
   }
}
