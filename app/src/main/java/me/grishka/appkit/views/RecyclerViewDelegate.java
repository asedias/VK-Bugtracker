package me.grishka.appkit.views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class RecyclerViewDelegate {

   private RecyclerView view;


   public RecyclerViewDelegate(RecyclerView var1) {
      this.view = var1;
   }

   private int getCount() {
      return this.view.getAdapter() == null?0:this.view.getAdapter().getItemCount();
   }

   public int getFirstVisiblePosition() {
      byte var2 = 0;
      LayoutManager var3 = this.view.getLayoutManager();
      int var1;
      if(var3 instanceof LinearLayoutManager) {
         var1 = ((LinearLayoutManager)var3).findFirstVisibleItemPosition();
      } else {
         var1 = var2;
         if(this.view.getAdapter() != null) {
            var1 = var2;
            if(this.view.getChildCount() != 0) {
               return this.view.getChildAdapterPosition(this.view.getChildAt(0));
            }
         }
      }

      return var1;
   }

   public int getLastVisiblePosition() {
      byte var2 = 0;
      LayoutManager var3 = this.view.getLayoutManager();
      int var1;
      if(var3 instanceof LinearLayoutManager) {
         var1 = ((LinearLayoutManager)var3).findLastVisibleItemPosition();
      } else {
         var1 = var2;
         if(this.view.getAdapter() != null) {
            var1 = var2;
            if(this.view.getChildCount() != 0) {
               return this.view.getChildAdapterPosition(this.view.getChildAt(this.view.getChildCount() - 1));
            }
         }
      }

      return var1;
   }

   public int getVisibleItemCount() {
      return this.getLastVisiblePosition() - this.getFirstVisiblePosition();
   }

   public void setOnScrollListener(final OnScrollListener var1) {
      this.view.addOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1x, int var2) {
            byte var3 = -1;
            byte var4;
            switch(var2) {
            case 0:
               var4 = 0;
               break;
            case 1:
               var4 = 1;
               break;
            case 2:
               var4 = 2;
               break;
            default:
               var4 = var3;
            }

            if(var4 != -1) {
               var1.onScrollStateChanged((AbsListView)null, var4);
            }

         }
         public void onScrolled(RecyclerView var1x, int var2, int var3) {
            var1.onScroll((AbsListView)null, RecyclerViewDelegate.this.getFirstVisiblePosition(), RecyclerViewDelegate.this.getVisibleItemCount(), RecyclerViewDelegate.this.getCount());
         }
      });
   }
}
