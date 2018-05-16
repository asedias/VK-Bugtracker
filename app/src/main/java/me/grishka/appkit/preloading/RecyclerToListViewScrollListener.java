package me.grishka.appkit.preloading;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RecyclerToListViewScrollListener extends OnScrollListener {

   public static final int SCROLL_STATE_SLOWDOWN = 3;
   public static final int UNKNOWN_SCROLL_STATE = Integer.MIN_VALUE;
   private int lastDY;
   private int lastFirstVisible = -1;
   private int lastItemCount = -1;
   private int lastScrollState = Integer.MIN_VALUE;
   private int lastVisibleCount = -1;
   private final List listeners = new ArrayList();


   public RecyclerToListViewScrollListener(AbsListView.OnScrollListener var1) {
      this.listeners.add(var1);
   }

   public void addScrollListener(AbsListView.OnScrollListener var1) {
      this.listeners.add(var1);
   }

   public void onScrollStateChanged(RecyclerView var1, int var2) {
      switch(var2) {
      case 0:
         this.lastScrollState = 0;
         break;
      case 1:
         this.lastScrollState = 1;
         break;
      case 2:
         this.lastScrollState = 2;
         break;
      default:
         this.lastScrollState = Integer.MIN_VALUE;
      }

      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AbsListView.OnScrollListener)var3.next()).onScrollStateChanged((AbsListView)null, this.lastScrollState);
      }

   }

   public void onScrolled(RecyclerView var1, int var2, int var3) {
      if(this.lastScrollState == 2 && var3 < 35 && this.lastDY > 0 && this.lastDY - var3 < 100) {
         this.lastScrollState = 3;
         Iterator var5 = this.listeners.iterator();

         while(var5.hasNext()) {
            ((AbsListView.OnScrollListener)var5.next()).onScrollStateChanged((AbsListView)null, this.lastScrollState);
         }
      }

      this.lastDY = var3;
      LayoutManager var7 = var1.getLayoutManager();
      int var4;
      Iterator var6;
      if(var7 instanceof LinearLayoutManager) {
         LinearLayoutManager var8 = (LinearLayoutManager)var7;
         var2 = var8.findFirstVisibleItemPosition();
         var3 = Math.abs(var2 - var8.findLastVisibleItemPosition());
         var4 = var1.getAdapter().getItemCount();
         if(var2 != this.lastFirstVisible || var3 != this.lastVisibleCount || var4 != this.lastItemCount) {
            var6 = this.listeners.iterator();

            while(var6.hasNext()) {
               ((AbsListView.OnScrollListener)var6.next()).onScroll((AbsListView)null, var2, var3, var4);
            }

            this.lastFirstVisible = var2;
            this.lastVisibleCount = var3;
            this.lastItemCount = var4;
         }
      } else if(var7 instanceof GridLayoutManager) {
         GridLayoutManager var9 = (GridLayoutManager)var7;
         var2 = var9.findFirstVisibleItemPosition();
         var3 = Math.abs(var2 - var9.findLastVisibleItemPosition());
         var4 = var1.getAdapter().getItemCount();
         if(var2 != this.lastFirstVisible || var3 != this.lastVisibleCount || var4 != this.lastItemCount) {
            var6 = this.listeners.iterator();

            while(var6.hasNext()) {
               ((AbsListView.OnScrollListener)var6.next()).onScroll((AbsListView)null, var2, var3, var4);
            }

            this.lastFirstVisible = var2;
            this.lastVisibleCount = var3;
            this.lastItemCount = var4;
            return;
         }
      }

   }

   public void removeScrollListener(AbsListView.OnScrollListener var1) {
      this.listeners.remove(var1);
   }
}
