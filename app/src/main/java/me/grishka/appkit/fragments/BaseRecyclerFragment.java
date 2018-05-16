package me.grishka.appkit.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asedias.bugtracker.R;

import java.util.ArrayList;
import java.util.List;

import me.grishka.appkit.api.ErrorResponse;
import me.grishka.appkit.api.PaginatedList;
import me.grishka.appkit.utils.Preloader;
import me.grishka.appkit.utils.V;
import me.grishka.appkit.views.SwipeRefreshLayoutI;
import me.grishka.appkit.views.UsableRecyclerView;

public abstract class BaseRecyclerFragment extends LoaderFragment implements OnRefreshListener, UsableRecyclerView.Listener, Preloader.Callback {

   protected ViewGroup contentWrap;
   protected ArrayList data;
   protected Button emptyButton;
   protected CharSequence emptyButtonText;
   protected boolean emptyButtonVisible;
   protected CharSequence emptyText;
   protected View emptyView;
   protected View footerError;
   protected View footerProgress;
   protected View footerView;
   private final Handler handler = new Handler(Looper.getMainLooper());
   protected int itemsPerPage;
   protected UsableRecyclerView list;
   private int listLayoutId = R.layout.appkit_recycler_fragment;
   protected ArrayList preloadedData;
   protected Preloader preloader;
   protected boolean preloadingFailed = false;
   private boolean refreshAfterCreate = false;
   private boolean refreshEnabled = true;
   protected SwipeRefreshLayoutI refreshLayout;
   protected boolean refreshing = false;
   protected int currentScrollOffset = 0;
   private final Runnable updateListRunnable = new Runnable() {
      public void run() {
         UsableRecyclerView var1 = BaseRecyclerFragment.this.list;
         if(var1 != null && var1.getAdapter() != null) {
            if(!var1.isComputingLayout()) {
               var1.getAdapter().notifyDataSetChanged();
               return;
            }

            BaseRecyclerFragment.this.handler.removeCallbacks(this);
            BaseRecyclerFragment.this.handler.post(this);
         }

      }
   };


   public BaseRecyclerFragment(int var1) {
      this.itemsPerPage = var1;
      this.preloader = new Preloader(this, var1);
      this.data = this.preloader.getData();
      this.preloadedData = this.preloader.getPreloadedData();
   }

   public BaseRecyclerFragment(int var1, int var2) {
      super(var1);
      this.itemsPerPage = var2;
      this.preloader = new Preloader(this, var2);
      this.data = this.preloader.getData();
      this.preloadedData = this.preloader.getPreloadedData();
   }

   protected void beforeSetAdapter() {}

   protected void cancelLoading() {
      if(this.currentRequest != null) {
         this.currentRequest.cancel();
         this.currentRequest = null;
      }
      if(this.mRequest != null) {
         this.mRequest.cancel(false);
         this.mRequest = null;
      }
   }

   protected void doLoadData() {
      this.doLoadData(0, this.itemsPerPage * 2);
   }

   protected abstract void doLoadData(int var1, int var2);

   protected abstract Adapter getAdapter();

   protected int getSpanCount() {
      return 1;
   }

   public boolean isDataLoading() {
      return this.dataLoading;
   }

   public boolean isRefreshing() {
      return this.refreshing;
   }

   public void loadData(int var1, int var2) {
      this.dataLoading = true;
      this.doLoadData(var1, var2);
   }

   public void onAppendItems(List var1) {}

   public void onAttach(Activity var1) {
      if(TextUtils.isEmpty(this.emptyText)) {
         this.emptyText = var1.getString(R.string.empty);//var1.getString(2131231119);
      }
      super.onAttach(var1);
   }

   public void onClearItems() {}

   public View onCreateContentView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      View var6 = var1.inflate(this.listLayoutId, (ViewGroup)null);
      this.list = (UsableRecyclerView)var6.findViewById(R.id.list);
      this.list.setListener(this);
      this.emptyView = var6.findViewById(R.id.empty);
      this.refreshLayout = (SwipeRefreshLayoutI)var6.findViewById(R.id.refresh_layout);
      this.contentWrap = (ViewGroup)var6.findViewById(R.id.content_wrap);
      ((TextView)this.emptyView.findViewById(R.id.empty_text)).setText(this.emptyText);
      this.emptyButton = (Button)this.emptyView.findViewById(R.id.empty_button);
      this.emptyButton.setText(this.emptyButtonText);
      Button var7 = this.emptyButton;
      byte var4;
      if(this.emptyButtonVisible) {
         var4 = 0;
      } else {
         var4 = 8;
      }

      var7.setVisibility(var4);
      this.emptyButton.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            BaseRecyclerFragment.this.onEmptyViewBtnClick();
         }
      });
      LayoutManager var8 = this.onCreateLayoutManager();
      if(var8 instanceof GridLayoutManager) {
         final SpanSizeLookup var5 = ((GridLayoutManager)var8).getSpanSizeLookup();
         ((GridLayoutManager)var8).setSpanSizeLookup(new SpanSizeLookup() {
            public int getSpanSize(int var1) {
               if(BaseRecyclerFragment.this.list != null) {
                  if(var1 == BaseRecyclerFragment.this.list.getAdapter().getItemCount() - 1 && BaseRecyclerFragment.this.preloader.isFooterVisible() && BaseRecyclerFragment.this.footerView != null) {
                     return ((GridLayoutManager)BaseRecyclerFragment.this.list.getLayoutManager()).getSpanCount();
                  }

                  if(var5 != null) {
                     return var5.getSpanSize(var1);
                  }
               }

               return 1;
            }
         });
      }

      this.list.setLayoutManager(var8);
      this.list.setHasFixedSize(true);
      this.refreshLayout.setOnRefreshListener(this);
      this.refreshLayout.setEnabled(this.refreshEnabled);
      this.list.setEmptyView(this.emptyView);
      Adapter var9 = this.getAdapter();
      this.footerView = this.onCreateFooterView(var1);
      this.list.setAdapter(var9);
      if(this.footerView != null) {
         this.footerProgress = this.footerView.findViewById(R.id.load_more_progress);
         this.footerError = this.footerView.findViewById(R.id.load_more_error);
         this.footerError.setVisibility(View.GONE);
         this.list.addFooterView(this.footerView);
         this.footerError.findViewById(R.id.error_retry).setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               BaseRecyclerFragment.this.onErrorRetryClick();
            }
         });
         this.preloader.setFooterViews(this.footerProgress, this.footerError);
      }

      if(this.refreshAfterCreate) {
         this.refresh();
      }

      return var6;
   }

   protected View onCreateFooterView(LayoutInflater var1) {
      return var1.inflate(R.layout.appkit_load_more, (ViewGroup)null);
   }

   protected LayoutManager onCreateLayoutManager() {
      return new GridLayoutManager(this.getActivity(), this.getSpanCount());
   }

   protected void onDataLoaded(List var1) {
      this.dataLoading = false;
      this.currentRequest = null;
      this.loaded = true;
      this.data.clear();
      this.data.addAll(var1);
      this.updateList();
      if(this.list != null) {
         if(this.refreshing) {
            this.refreshDone();
         }

         V.setVisibilityAnimated((View)this.refreshLayout, 0);
         V.setVisibilityAnimated(this.progress, 8);
      }
   }

   protected void onDataLoaded(List var1, boolean var2) {
      this.loaded = true;
      this.mRequest = null;
      this.currentRequest = null;
      if(this.refreshing) {
         this.data.clear();
         this.preloadedData.clear();
         this.onClearItems();
      }

      this.dataLoading = false;
      this.preloader.onDataLoaded(var1, var2);
      if(this.refreshing) {
         this.refreshDone();
      }

      V.setVisibilityAnimated((View)this.refreshLayout, 0);
      V.setVisibilityAnimated(this.progress, 8);
   }

   public void onDataLoaded(PaginatedList var1) {
      boolean var3 = false;
      int var2;
      if(this.refreshing) {
         var2 = 0;
      } else {
         var2 = this.data.size() + this.preloadedData.size();
      }

      if(var1.size() + var2 < var1.total()) {
         var3 = true;
      }

      this.onDataLoaded(var1, var3);
   }

   public void onDestroyView() {
      super.onDestroyView();
      if(this.list != null) {
         this.list.setAdapter((Adapter)null);
      }

      this.list = null;
      this.emptyView = null;
      this.emptyButton = null;
      this.progress = null;
      this.errorView = null;
      this.contentWrap = null;
      this.footerError = null;
      this.footerProgress = null;
      this.footerView = null;
      this.refreshLayout = null;
   }

   @Override
   public void onViewCreated(View var1, Bundle var2) {
      super.onViewCreated(var1, var2);
      this.list.addOnScrollListener(new RecyclerView.OnScrollListener() {
         @Override
         public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int position = ((GridLayoutManager)list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if(position > 0) {
               currentScrollOffset += dy;
            } else {
               currentScrollOffset = 0;
            }
            if(currentScrollOffset <= 16 && currentScrollOffset >= 0) {
               ViewCompat.setElevation(getAppbar(), currentScrollOffset /2);
            } else if(currentScrollOffset > 16) {
               ViewCompat.setElevation(getAppbar(), 8);
            } else if(currentScrollOffset < 0) {
               currentScrollOffset = 0;
            }
         }

         @Override
         public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
         }
      });
   }

   protected void onEmptyViewBtnClick() {}

   public void onError(ErrorResponse var1) {
      this.dataLoading = false;
      this.currentRequest = null;
      if(this.errorView != null) {
         if(this.refreshing) {
            this.refreshDone();
         }

         if(this.refreshing) {
            var1.showToast(this.getActivity());
         } else if(this.data.size() > 0) {
            this.preloadingFailed = true;
            var1.bindErrorView(this.footerError);
            V.setVisibilityAnimated(this.footerError, 0);
            V.setVisibilityAnimated(this.footerProgress, 8);
         } else {
            super.onError(var1);
         }
      }
   }

   protected void onErrorRetryClick() {
      if(this.preloadingFailed) {
         this.preloadingFailed = false;
         V.setVisibilityAnimated(this.footerProgress, 0);
         V.setVisibilityAnimated(this.footerError, 8);
         this.onScrolledToLastItem();
      } else {
         super.onErrorRetryClick();
      }
   }

   protected void onPrependItems(List var1) {}

   public void onRefresh() {
      this.refreshing = true;
      if(this.footerView != null) {
         this.footerError.setVisibility(View.GONE);
         this.footerProgress.setVisibility(View.VISIBLE);
      }

      this.preloadingFailed = false;
      this.doLoadData();
   }

   public void onScrollStarted() {}

   public void onScrolledToLastItem() {
      if(!this.refreshing && !this.preloadingFailed) {
         this.preloader.onScrolledToLastItem();
      }
   }

   protected void refresh() {
      if(!this.loaded) {
         this.loadData();
      } else if(this.refreshLayout != null) {
         this.refreshLayout.post(new Runnable() {
            public void run() {
               if(BaseRecyclerFragment.this.refreshLayout != null) {
                  BaseRecyclerFragment.this.refreshLayout.setRefreshing(true);
                  BaseRecyclerFragment.this.refreshLayout.setEnabled(false);
               }

            }
         });
         this.onRefresh();
         this.refreshAfterCreate = false;
      } else {
         this.refreshAfterCreate = true;
      }
   }

   public void refreshDone() {
      this.refreshing = false;
      if(this.refreshLayout != null) {
         this.refreshLayout.setRefreshing(false);
         this.refreshLayout.setEnabled(this.refreshEnabled);
      }

   }

   public void reload() {
      this.loaded = false;
      this.data.clear();
      this.onClearItems();
      this.showProgress();
      this.loadData();
   }

   protected void setEmptyButtonText(@StringRes int var1) {
      this.setEmptyButtonText(this.getString(var1));
   }

   protected void setEmptyButtonText(CharSequence var1) {
      this.emptyButtonText = var1;
      if(this.emptyButton != null) {
         this.emptyButton.setText(var1);
      }

   }

   protected void setEmptyButtonVisible(boolean var1) {
      this.emptyButtonVisible = var1;
      if(this.emptyButton != null) {
         Button var3 = this.emptyButton;
         byte var2;
         if(var1) {
            var2 = 0;
         } else {
            var2 = 8;
         }

         var3.setVisibility(var2);
      }

   }

   protected void setEmptyText(@StringRes int var1) {
      this.setEmptyText(this.getString(var1));
   }

   protected void setEmptyText(CharSequence var1) {
      this.emptyText = var1;
      if(this.emptyView != null) {
         ((TextView)this.emptyView.findViewById(R.id.empty_text)).setText(var1);
      }

   }

   public void setListLayoutId(int var1) {
      this.listLayoutId = var1;
   }

   protected void setRefreshEnabled(boolean var1) {
      this.refreshEnabled = var1;
      if(this.refreshLayout != null) {
         this.refreshLayout.setEnabled(var1);
      }

   }

   public void updateList() {
      this.updateListRunnable.run();
   }
}
