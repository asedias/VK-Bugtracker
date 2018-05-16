package me.grishka.appkit.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.view.ViewGroup;

import com.vkontakte.android.ui.holder.RecyclerHolder;

public class RecyclerViewAdapterWrapper extends UsableRecyclerView.Adapter {

   @NonNull
   final UsableRecyclerView.Adapter wrapped;


   public RecyclerViewAdapterWrapper(@NonNull UsableRecyclerView.Adapter<RecyclerHolder> var1) {
      this.wrapped = var1;
      super.setHasStableIds(var1.hasStableIds());
   }

   public int getImageCountForItem(int var1) {
      return this.wrapped.getImageCountForItem(var1);
   }

   public String getImageURL(int var1, int var2) {
      return this.wrapped.getImageURL(var1, var2);
   }

   public int getItemCount() {
      return this.wrapped.getItemCount();
   }

   public long getItemId(int var1) {
      return this.wrapped.getItemId(var1);
   }

   public int getItemViewType(int var1) {
      return this.wrapped.getItemViewType(var1);
   }

   public void onAttachedToRecyclerView(RecyclerView var1) {
      this.wrapped.onAttachedToRecyclerView(var1);
   }

   public void onBindViewHolder(UsableRecyclerView.ViewHolder var1, int var2) {
      this.wrapped.onBindViewHolder(var1, var2);
   }

   public UsableRecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      return (UsableRecyclerView.ViewHolder)this.wrapped.onCreateViewHolder(var1, var2);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      onBindViewHolder((UsableRecyclerView.ViewHolder)holder, position);
   }

   public void onDetachedFromRecyclerView(RecyclerView var1) {
      this.wrapped.onDetachedFromRecyclerView(var1);
   }

   public boolean onFailedToRecycleView(UsableRecyclerView.ViewHolder var1) {
      return this.wrapped.onFailedToRecycleView(var1);
   }

   public void onViewAttachedToWindow(UsableRecyclerView.ViewHolder var1) {
      this.wrapped.onViewAttachedToWindow(var1);
   }

   public void onViewDetachedFromWindow(UsableRecyclerView.ViewHolder var1) {
      this.wrapped.onViewDetachedFromWindow(var1);
   }

   public void onViewRecycled(UsableRecyclerView.ViewHolder var1) {
      this.wrapped.onViewRecycled(var1);
   }

   public void registerAdapterDataObserver(AdapterDataObserver var1) {
      super.registerAdapterDataObserver(var1);
      this.wrapped.registerAdapterDataObserver(var1);
   }

   public void setHasStableIds(boolean var1) {
      this.wrapped.setHasStableIds(var1);
   }

   public void unregisterAdapterDataObserver(AdapterDataObserver var1) {
      super.unregisterAdapterDataObserver(var1);
      this.wrapped.unregisterAdapterDataObserver(var1);
   }
}
