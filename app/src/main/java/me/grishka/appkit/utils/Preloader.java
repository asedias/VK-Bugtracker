package me.grishka.appkit.utils;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Preloader {

   private Callback callback;
   protected ArrayList data = new ArrayList();
   private View footerError;
   private View footerProgress;
   private boolean footerVisible = true;
   private int itemsPerPage;
   protected boolean moreAvailable;
   protected boolean preloadOnReady;
   protected ArrayList preloadedData = new ArrayList();
   protected boolean preloading;


   public Preloader(Callback var1, int var2) {
      this.callback = var1;
      this.itemsPerPage = var2;
   }

   public ArrayList getData() {
      return this.data;
   }

   public ArrayList getPreloadedData() {
      return this.preloadedData;
   }

   public boolean isFooterVisible() {
      return this.footerVisible;
   }

   public boolean isMoreAvailable() {
      return this.moreAvailable;
   }

   public void onDataLoaded(List var1, boolean var2) {
      byte var3 = 0;
      if(this.callback.isRefreshing()) {
         this.data.clear();
         this.preloadedData.clear();
         this.callback.onClearItems();
      }

      if(this.preloading) {
         this.preloadedData.addAll(var1);
      } else if(var1.size() > this.itemsPerPage && var2) {
         this.data.addAll(var1.subList(0, this.itemsPerPage));
         this.callback.onAppendItems(var1.subList(0, this.itemsPerPage));
         this.preloadedData.addAll(var1.subList(this.itemsPerPage, var1.size()));
      } else {
         this.data.addAll(var1);
         this.callback.onAppendItems(var1);
      }

      this.preloading = false;
      if(this.preloadOnReady) {
         this.preloading = true;
         this.preloadOnReady = false;
         this.callback.loadData(this.data.size(), this.itemsPerPage * 2);
      }

      this.callback.updateList();
      this.moreAvailable = var2;
      if(this.footerProgress != null) {
         View var4 = this.footerProgress;
         if(!this.moreAvailable) {
            var3 = 8;
         }

         var4.setVisibility(var3);
         if(this.footerVisible != this.moreAvailable) {
            this.footerVisible = this.moreAvailable;
         }
      }

   }

   public void onScrolledToLastItem() {
      if(!this.callback.isDataLoading() || this.preloading) {
         if(this.preloading) {
            this.preloading = false;
            this.preloadOnReady = true;
         } else if(this.preloadedData.size() > 0) {
            this.data.addAll(this.preloadedData);
            this.callback.onAppendItems(this.preloadedData);
            this.callback.updateList();
            this.preloadedData.clear();
            if(this.moreAvailable) {
               this.preloading = true;
               this.callback.loadData(this.data.size(), this.itemsPerPage);
               return;
            }
         } else if(this.moreAvailable) {
            this.callback.loadData(this.data.size(), this.itemsPerPage * 2);
            return;
         }
      }

   }

   public void setFooterViews(View var1, View var2) {
      this.footerProgress = var1;
      this.footerError = var2;
      if(this.footerProgress != null && this.footerError != null) {
         var1 = this.footerProgress;
         byte var3;
         if(this.moreAvailable) {
            var3 = 0;
         } else {
            var3 = 8;
         }

         var1.setVisibility(var3);
         if(this.footerVisible != this.moreAvailable) {
            this.footerVisible = this.moreAvailable;
         }
      }

   }

   public void setMoreAvailable(boolean var1) {
      this.moreAvailable = var1;
   }

   public interface Callback {

      boolean isDataLoading();

      boolean isRefreshing();

      void loadData(int var1, int var2);

      void onAppendItems(List var1);

      void onClearItems();

      void updateList();
   }
}
