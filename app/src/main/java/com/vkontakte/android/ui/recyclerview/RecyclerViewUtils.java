package com.vkontakte.android.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;

class RecyclerViewUtils {

   static int getRealFirstPosition(@NonNull RecyclerView var0) {
      int var2 = Integer.MAX_VALUE;
      int var3 = var0.getChildCount();

      for(int var1 = 0; var1 < var3; ++var1) {
         var2 = Math.min(var0.getChildAdapterPosition(var0.getChildAt(var1)), var2);
      }

      return var2;
   }

   static int mapAdapterToChildPositions(@NonNull RecyclerView var0, @NonNull SparseIntArray var1, int var2) {
      int var3 = 0;
      var1.clear();
      int var4 = var0.getChildCount();

      for(var2 = 0; var2 < var4; ++var2) {
         int var5 = var0.getChildAdapterPosition(var0.getChildAt(var2));
         var3 = Math.max(var3, var5);
         var1.put(var5, var2);
      }

      return var3;
   }
}
