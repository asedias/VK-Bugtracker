package me.grishka.appkit.views;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;

public class RecursiveSwipeRefreshLayout extends SwipeRefreshLayout implements SwipeRefreshLayoutI {

   public RecursiveSwipeRefreshLayout(Context var1) {
      super(var1);
   }

   public RecursiveSwipeRefreshLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private boolean findScrollableChild(ViewGroup var1) {
      for(int var2 = 0; var2 < var1.getChildCount(); ++var2) {
         View var3 = var1.getChildAt(var2);
         if(var3 instanceof AdapterView || var3 instanceof ScrollView || var3 instanceof RecyclerView) {
            return ViewCompat.canScrollVertically(var3, -1);
         }

         if(var3 instanceof ViewGroup) {
            return this.findScrollableChild((ViewGroup)var3);
         }
      }

      return false;
   }

   public boolean canChildScrollUp() {
      return this.findScrollableChild(this);
   }

   public boolean isReversed() {
      return false;
   }

   public void setReversed(boolean var1) {}
}
