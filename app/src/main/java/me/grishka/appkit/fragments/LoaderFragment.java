package me.grishka.appkit.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.asedias.bugtracker.R;
import com.asedias.bugtracker.async.base.DocumentRequest;

import me.grishka.appkit.api.APIRequest;
import me.grishka.appkit.api.ErrorResponse;
import me.grishka.appkit.utils.V;

public abstract class LoaderFragment extends ContainerFragment implements OnRefreshListener {

   private boolean autoRetry;
   protected View content;
   protected ViewGroup contentView;
   protected APIRequest currentRequest;
   protected boolean dataLoading;
   private boolean errorReceiverRegistered;
   protected View errorView;
   private int layoutID;
   public boolean loaded;
   protected View progress;
   private BroadcastReceiver receiver;
   protected DocumentRequest mRequest;

   public LoaderFragment() {
      this(R.layout.appkit_loader_fragment);
   }

   protected LoaderFragment(@LayoutRes int var1) {
      this.receiver = new BroadcastReceiver() {
         public void onReceive(Context var1, Intent var2) {
            boolean var3 = false;
            if(!this.isInitialStickyBroadcast() && "android.net.conn.CONNECTIVITY_CHANGE".equals(var2.getAction())) {
               if(!var2.getBooleanExtra("noConnectivity", false)) {
                  var3 = true;
               }

               if(var3) {
                  LoaderFragment.this.onErrorRetryClick();
                  return;
               }
            }

         }
      };
      this.errorReceiverRegistered = false;
      this.autoRetry = true;
      this.layoutID = var1;
   }

   public void dataLoaded() {
      this.loaded = true;
      this.showContent();
   }

   protected abstract void doLoadData();

   @LayoutRes
   protected int getLayout() {
      return this.layoutID;
   }

   public boolean isRetryInNetworkConnect() {
      return this.autoRetry;
   }

   public void loadData() {
      this.showProgress();
      this.dataLoading = true;
      this.doLoadData();
   }

   public abstract View onCreateContentView(LayoutInflater var1, ViewGroup var2, Bundle var3);

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      this.contentView = (ViewGroup)var1.inflate(this.layoutID, (ViewGroup)null);
      View loader_content = this.contentView.findViewById(R.id.appkit_loader_content);
      ViewGroup parent = (ViewGroup)loader_content.getParent();
      this.content = this.onCreateContentView(var1, parent, var3);
      this.content.setLayoutParams(loader_content.getLayoutParams());
      this.progress = this.contentView.findViewById(R.id.loading);
      this.errorView = this.contentView.findViewById(R.id.error);
      this.content.setVisibility(this.loaded ? View.VISIBLE : View.GONE);
      this.progress.setVisibility(!this.loaded ? View.VISIBLE : View.GONE);
      parent.addView(this.content, parent.indexOfChild(loader_content));
      //parent.removeView(loader_content);
      View error = this.errorView.findViewById(R.id.error_retry);
      if(error != null) {
         error.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               LoaderFragment.this.onErrorRetryClick();
            }
         });
      }

      return this.contentView;
   }

   public void onDestroy() {
      super.onDestroy();
      if(this.currentRequest != null) {
         this.currentRequest.cancel();
         this.currentRequest = null;
      }

      if(this.errorReceiverRegistered) {
         try {
            this.getActivity().unregisterReceiver(this.receiver);
         } catch (Exception var2) {
            ;
         }

         this.errorReceiverRegistered = false;
      }

   }

   public void onDestroyView() {
      super.onDestroyView();
      this.content = null;
      this.errorView = null;
      this.progress = null;
      this.contentView = null;
   }

   public void onError(ErrorResponse var1) {
      this.dataLoading = false;
      this.currentRequest = null;
      if(this.errorView != null) {
         var1.bindErrorView(this.errorView);
         V.setVisibilityAnimated(this.errorView, 0);
         V.setVisibilityAnimated(this.progress, 8);
         V.setVisibilityAnimated(this.content, 8);
         if(!this.errorReceiverRegistered && this.autoRetry) {
            this.getActivity().registerReceiver(this.receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.errorReceiverRegistered = true;
            return;
         }
      }

   }

   protected void onErrorRetryClick() {
      V.setVisibilityAnimated(this.errorView, 8);
      V.setVisibilityAnimated(this.progress, 0);
      this.loadData();
   }

   protected void setLayout(int var1) {
      if(this.content != null) {
         throw new IllegalStateException("Can\'t set layout when view is already created");
      } else {
         this.layoutID = var1;
      }
   }

   public void setRetryOnNetworkConnect(boolean var1) {
      this.autoRetry = var1;
   }

   protected void showContent() {
      if(this.content != null) {
         V.setVisibilityAnimated(this.content, 0);
         V.setVisibilityAnimated(this.errorView, 8);
         V.setVisibilityAnimated(this.progress, 8);
      }

      if(this.errorReceiverRegistered) {
         try {
            this.getActivity().unregisterReceiver(this.receiver);
         } catch (Exception var2) {
            ;
         }

         this.errorReceiverRegistered = false;
      }

   }

   protected void showProgress() {
      if(this.content != null) {
         V.setVisibilityAnimated(this.content, 8);
         V.setVisibilityAnimated(this.errorView, 8);
         V.setVisibilityAnimated(this.progress, 0);
      }

      if(this.errorReceiverRegistered) {
         try {
            this.getActivity().unregisterReceiver(this.receiver);
         } catch (Exception var2) {
            ;
         }

         this.errorReceiverRegistered = false;
      }

   }
}
