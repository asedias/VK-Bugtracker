package com.vkontakte.android.ui.holder;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.grishka.appkit.views.UsableRecyclerView;

public abstract class RecyclerHolder<I extends Object> extends UsableRecyclerView.ViewHolder {

   private final Context mContext;
   protected ViewGroup mParent;


   public RecyclerHolder(@LayoutRes int var1, @NonNull Context var2) {
      this(LayoutInflater.from(var2).inflate(var1, (ViewGroup)null));
      this.mParent = null;
   }

   public RecyclerHolder(@LayoutRes int var1, @NonNull ViewGroup var2) {
      this(LayoutInflater.from(var2.getContext()).inflate(var1, var2, false));
      this.mParent = var2;
   }

   public RecyclerHolder(View var1) {
      super(var1);
      this.mContext = var1.getContext();
      this.mParent = null;
   }

   public RecyclerHolder(View var1, @NonNull ViewGroup var2) {
      super(var1);
      this.mContext = var1.getContext();
      this.mParent = var2;
   }

   public View $(@IdRes int var1) {
      return this.itemView.findViewById(var1);
   }

   public abstract void bind(I var1);

   public Context getContext() {
      return this.mContext;
   }

   public Drawable getDrawable(@DrawableRes int var1) throws NotFoundException {
      return this.getResources().getDrawable(var1);
   }

   public ViewGroup getParent() {
      return this.mParent;
   }

   public String getQuantityString(@PluralsRes int var1, int var2) throws NotFoundException {
      return this.getResources().getQuantityString(var1, var2);
   }

   public String getQuantityString(@PluralsRes int var1, int var2, Object ... var3) throws NotFoundException {
      return this.getResources().getQuantityString(var1, var2, var3);
   }

   public Resources getResources() {
      return this.getContext().getResources();
   }

   public String getString(@StringRes int var1) throws NotFoundException {
      return this.getResources().getString(var1);
   }

   public String getString(@StringRes int var1, Object ... var2) throws NotFoundException {
      return this.getResources().getString(var1, var2);
   }

   public String[] getStringArray(@ArrayRes int var1) throws NotFoundException {
      return this.getResources().getStringArray(var1);
   }
}
