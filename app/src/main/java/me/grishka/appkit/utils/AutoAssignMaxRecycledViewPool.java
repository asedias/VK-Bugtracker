package me.grishka.appkit.utils;

import android.support.v7.widget.RecyclerView.RecycledViewPool;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.SparseIntArray;

public class AutoAssignMaxRecycledViewPool extends RecycledViewPool {

   private final SparseIntArray mMaxScrap = new SparseIntArray();
   private final int mMaxSize;


   public AutoAssignMaxRecycledViewPool(int var1) {
      this.mMaxSize = var1;
   }

   public void clear() {
      this.mMaxScrap.clear();
      super.clear();
   }

   public void putRecycledView(ViewHolder var1) {
      int var2 = var1.getItemViewType();
      if(this.mMaxScrap.get(var2, -1) == -1) {
         this.setMaxRecycledViews(var2, this.mMaxSize);
      }

      super.putRecycledView(var1);
   }

   public void setMaxRecycledViews(int var1, int var2) {
      this.mMaxScrap.put(var1, var2);
      super.setMaxRecycledViews(var1, var2);
   }
}
