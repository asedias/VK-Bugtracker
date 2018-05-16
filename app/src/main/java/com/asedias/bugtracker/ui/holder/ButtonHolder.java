package com.asedias.bugtracker.ui.holder;

import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asedias.bugtracker.R;
import com.vkontakte.android.fragments.SettingsListFragment;
import com.vkontakte.android.functions.VoidF1;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.commons.PreferenceIconItemHolder;

import me.grishka.appkit.views.UsableRecyclerView;

public class ButtonHolder extends RecyclerHolder<PreferenceIconItemHolder.IconPrefInfo> implements UsableRecyclerView.Clickable {


   @Nullable
   private final VoidF1 clickFunk;
   protected final TextView textView = (TextView)this.$(R.id.title_holder);
   private PreferenceIconItemHolder.IconPrefInfo value;

   public ButtonHolder(@NonNull ViewGroup var1, @Nullable VoidF1 f) {
      super(R.layout.title_holder, var1);
      clickFunk = f;
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

   protected ButtonHolder withTextColor(int var1) {
      this.textView.setTextColor(var1);
      return this;
   }

    public ButtonHolder withTextSize(int var1) {
      this.textView.setTextSize(var1);
      return this;
   }

    protected ButtonHolder withTextColorRes(@ColorRes int var1) {
      this.withTextColor(this.getResources().getColor(var1));
      return this;
   }

   public ButtonHolder withText(@StringRes int var1) {
      this.textView.setText(var1);
      return this;
   }

   public ButtonHolder withText(String var1) {
      this.textView.setText(var1);
      return this;
   }

   @Override
   public void bind(PreferenceIconItemHolder.IconPrefInfo var1) {
      withText(var1.text);
      withTextSize(16);
      withTextColor(Color.RED);
      value = var1;
   }

   @Override
   public void onClick() {
       Log.d("CLICK", "CLICK");
      if(clickFunk != null) {
         clickFunk.f(value);
      }
   }
}
