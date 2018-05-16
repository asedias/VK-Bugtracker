package me.grishka.appkit.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.vkontakte.android.ui.holder.RecyclerHolder;

import java.util.ArrayList;

import me.grishka.appkit.preloading.PrefetchInfoProvider;
import me.grishka.appkit.preloading.RecyclerToListViewScrollListener;
import me.grishka.appkit.utils.AutoAssignMaxRecycledViewPool;

public class UsableRecyclerView extends RecyclerView {

   private int clickStartTimeout;
   private ViewHolder clickingViewHolder;
   private boolean drawHighlightOnTop = false;
   private View emptyView;
   private AdapterDataObserver emptyViewObserver = new AdapterDataObserver() {
      public void onChanged() {
         //UsableRecyclerView.this.preloader.update(UsableRecyclerView.this.recyclerViewDelegate.getFirstVisiblePosition(), UsableRecyclerView.this.recyclerViewDelegate.getVisibleItemCount());
         UsableRecyclerView.this.updateEmptyViewVisibility();
      }
      public void onItemRangeChanged(int var1, int var2) {
         //UsableRecyclerView.this.preloader.update(UsableRecyclerView.this.recyclerViewDelegate.getFirstVisiblePosition(), UsableRecyclerView.this.recyclerViewDelegate.getVisibleItemCount());
      }
      public void onItemRangeChanged(int var1, int var2, Object var3) {
         //UsableRecyclerView.this.preloader.update(UsableRecyclerView.this.recyclerViewDelegate.getFirstVisiblePosition(), UsableRecyclerView.this.recyclerViewDelegate.getVisibleItemCount());
      }
      public void onItemRangeInserted(int var1, int var2) {
         //UsableRecyclerView.this.preloader.update(UsableRecyclerView.this.recyclerViewDelegate.getFirstVisiblePosition(), UsableRecyclerView.this.recyclerViewDelegate.getVisibleItemCount());
         UsableRecyclerView.this.updateEmptyViewVisibility();
      }
      public void onItemRangeMoved(int var1, int var2, int var3) {
         //UsableRecyclerView.this.preloader.update(UsableRecyclerView.this.recyclerViewDelegate.getFirstVisiblePosition(), UsableRecyclerView.this.recyclerViewDelegate.getVisibleItemCount());
      }
      public void onItemRangeRemoved(int var1, int var2) {
         //UsableRecyclerView.this.preloader.update(UsableRecyclerView.this.recyclerViewDelegate.getFirstVisiblePosition(), UsableRecyclerView.this.recyclerViewDelegate.getVisibleItemCount());
         UsableRecyclerView.this.updateEmptyViewVisibility();
      }
   };
   private FooterRecyclerAdapter footerAdapter;
   private Drawable highlight;
   private Rect highlightBounds = new Rect();
   private SelectorBoundsProvider highlightBoundsProvider;
   private View highlightedView;
   private float lastTouchX;
   private float lastTouchY;
   @Nullable
   protected Listener listener = null;
   private int longClickTimeout;
   private OnSizeChangeListener onSizeChangeListener;
   private Runnable postedClickStart;
   private Runnable postedLongClick;
   //private ListPreloader preloader;
   private final RecyclerViewDelegate recyclerViewDelegate = new RecyclerViewDelegate(this);
   private RecyclerToListViewScrollListener scrollListener;
   private float touchDownX;
   private float touchDownY;
   private int touchSlop;


   public UsableRecyclerView(Context var1) {
      super(var1);
      this.init();
   }

   public UsableRecyclerView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init();
   }

   public UsableRecyclerView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init();
   }

   private void init() {
      this.touchSlop = ViewConfiguration.get(this.getContext()).getScaledTouchSlop();
      this.clickStartTimeout = ViewConfiguration.getTapTimeout();
      this.longClickTimeout = ViewConfiguration.getLongPressTimeout();
      TypedArray var1 = this.getContext().obtainStyledAttributes(new int[]{16843534});
      this.setSelector(var1.getDrawable(0));
      var1.recycle();
      this.setRecycledViewPool(new AutoAssignMaxRecycledViewPool(25));
      this.recyclerViewDelegate.setOnScrollListener(new AbsListView.OnScrollListener() {
         public void onScroll(AbsListView var1, int var2, int var3, int var4) {
            if(var2 + var3 >= var4 - 1 && var3 != 0 && var4 != 0 && UsableRecyclerView.this.listener != null) {
               UsableRecyclerView.this.listener.onScrolledToLastItem();
            }

            if(UsableRecyclerView.this.listener != null && UsableRecyclerView.this.listener instanceof ExtendedListener) {
               ((ExtendedListener)UsableRecyclerView.this.listener).onScroll(var2, var3, var4);
            }

         }
         public void onScrollStateChanged(AbsListView var1, int var2) {
            if(var2 != 0 && var2 == 1 && UsableRecyclerView.this.listener != null) {
               UsableRecyclerView.this.listener.onScrollStarted();
            }

         }
      });
      //this.preloader = new ListPreloader(25);
      //this.scrollListener = new RecyclerToListViewScrollListener(this.preloader);
      //this.addOnScrollListener(this.scrollListener);
   }

   private void updateEmptyViewVisibility() {
      if(this.emptyView != null) {
         View var2 = this.emptyView;
         byte var1;
         if(this.isEmpty()) {
            var1 = 0;
         } else {
            var1 = 8;
         }

         var2.setVisibility(var1);
      }

   }

   public void addFooterView(View var1) {
      var1.setLayoutParams(new LayoutParams(-1, -2));
      if(this.footerAdapter == null) {
         this.footerAdapter = new FooterRecyclerAdapter(this.getAdapter());
         this.footerAdapter.footerViews.add(var1);
         super.setAdapter(this.footerAdapter);
      } else {
         this.footerAdapter.footerViews.add(var1);
         this.footerAdapter.notifyDataSetChanged();
      }
   }

   public void addScrollListener(AbsListView.OnScrollListener var1) {
      this.scrollListener.addScrollListener(var1);
   }

   protected void dispatchDraw(Canvas var1) {
      if(this.drawHighlightOnTop) {
         super.dispatchDraw(var1);
      }

      if(this.highlight != null) {
         if(this.highlightedView != null) {
            if(this.highlightBoundsProvider != null) {
               this.highlightBoundsProvider.getSelectorBounds(this.highlightedView, this.highlightBounds);
            } else {
               this.highlightBounds.set(this.highlightedView.getLeft(), this.highlightedView.getTop(), this.highlightedView.getRight(), this.highlightedView.getBottom());
            }
         }

         this.highlight.setBounds(this.highlightBounds);
         if(VERSION.SDK_INT >= 21) {
            this.highlight.setHotspot(this.lastTouchX, this.lastTouchY);
         }

         this.highlight.draw(var1);
      }

      if(!this.drawHighlightOnTop) {
         super.dispatchDraw(var1);
      }

   }

   public ViewHolder findViewHolderForAdapterPosition(int var1) {
      return (ViewHolder)super.findViewHolderForAdapterPosition(var1);
   }

   public Adapter<RecyclerHolder> getAdapter() {
      Adapter<RecyclerHolder> var2 = (Adapter<RecyclerHolder>)super.getAdapter();
      Adapter<RecyclerHolder> var1;
      if(var2 instanceof FooterRecyclerAdapter) {
         var1 = ((FooterRecyclerAdapter)var2).wrapped;
      } else {
         var1 = var2;
         if(var2 instanceof RecyclerViewAdapterWrapper) {
            return ((RecyclerViewAdapterWrapper)var2).wrapped;
         }
      }

      return var1;
   }

   public ViewHolder getChildViewHolder(View var1) {
      return (ViewHolder)super.getChildViewHolder(var1);
   }

   public int getCount() {
      Adapter<RecyclerHolder> var1 = this.getAdapter();
      return var1 != null?var1.getItemCount():0;
   }

   public OnSizeChangeListener getOnSizeChangeListener() {
      return this.onSizeChangeListener;
   }

   public boolean isEmpty() {
      return this.getAdapter() != null && this.getAdapter().getItemCount() == 0;
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      if(this.onSizeChangeListener != null) {
         this.onSizeChangeListener.onSizeChange(var1, var2, var3, var4);
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      if(var1.getAction() == 0 && this.getScrollState() == 0) {
         float var2 = var1.getX();
         this.lastTouchX = var2;
         this.touchDownX = var2;
         var2 = var1.getY();
         this.lastTouchY = var2;
         this.touchDownY = var2;
         this.highlightedView = null;
         View var5 = this.findChildViewUnder(var1.getX(), var1.getY());
         if(var5 != null) {
            ViewHolder var4 = this.getChildViewHolder(var5);
            if(var4 != null && var4 instanceof Clickable) {
               if(var4 instanceof DisableableClickable && ((DisableableClickable)var4).isEnabled() || !(var4 instanceof DisableableClickable)) {
                  this.clickingViewHolder = var4;
                  this.highlightedView = var5;
                  if(this.postedClickStart != null) {
                     this.removeCallbacks(this.postedClickStart);
                  }

                  ClickStartRunnable var8 = new ClickStartRunnable(null);
                  this.postedClickStart = var8;
                  this.postDelayed(var8, (long)this.clickStartTimeout);
               }

               if(var4 instanceof LongClickable) {
                  LongClickRunnable var7 = new LongClickRunnable(null);
                  this.postedLongClick = var7;
                  this.postDelayed(var7, (long)this.longClickTimeout);
               }
            }
         }
      }

      if(var1.getAction() == 3) {
         this.clickingViewHolder = null;
         if(this.highlightedView != null) {
            this.highlightedView.setPressed(false);
            this.highlight.setState(EMPTY_STATE_SET);
            if(this.postedClickStart != null) {
               this.removeCallbacks(this.postedClickStart);
               this.postedClickStart = null;
            }

            if(this.postedLongClick != null) {
               this.removeCallbacks(this.postedLongClick);
               this.postedLongClick = null;
            }
         }
      }

      if(var1.getAction() == 2 && this.clickingViewHolder != null) {
         this.lastTouchX = var1.getX();
         this.lastTouchY = var1.getY();
         if(Math.abs(var1.getX() - this.touchDownX) > (float)this.touchSlop || Math.abs(var1.getY() - this.touchDownY) > (float)this.touchSlop) {
            this.clickingViewHolder = null;
            if(this.highlightedView != null) {
               this.highlightedView.setPressed(false);
               this.highlight.setState(EMPTY_STATE_SET);
               if(this.postedClickStart != null) {
                  this.removeCallbacks(this.postedClickStart);
                  this.postedClickStart = null;
               }

               if(this.postedLongClick != null) {
                  this.removeCallbacks(this.postedLongClick);
                  this.postedLongClick = null;
               }
            }
         }
      }

      if(var1.getAction() == 1) {
         this.lastTouchX = var1.getX();
         this.lastTouchY = var1.getY();
         if(this.postedLongClick != null) {
            this.removeCallbacks(this.postedLongClick);
            this.postedLongClick = null;
         }

         if(this.clickingViewHolder != null && (Math.abs(var1.getX() - this.touchDownX) < (float)this.touchSlop || Math.abs(var1.getY() - this.touchDownY) < (float)this.touchSlop)) {
            ((Clickable)this.clickingViewHolder).onClick();
            this.playSoundEffect(0);
            if(this.postedClickStart != null) {
               this.removeCallbacks(this.postedClickStart);
               this.postedClickStart.run();
               this.postedClickStart = null;
            }

            this.clickingViewHolder = null;
            this.postDelayed(new Runnable() {
               public void run() {
                  if(UsableRecyclerView.this.highlightedView != null) {
                     UsableRecyclerView.this.highlightedView.setPressed(false);
                  }

                  UsableRecyclerView.this.highlight.setState(UsableRecyclerView.EMPTY_STATE_SET);
               }
            }, 50L);
         }
      }

      try {
         boolean var3 = super.onTouchEvent(var1);
         return var3;
      } catch (Exception var6) {
         Log.e("error", "error", var6);
         return false;
      }
   }

   public void removeScrollListener(AbsListView.OnScrollListener var1) {
      this.scrollListener.removeScrollListener(var1);
   }

   public void setAdapter(RecyclerView.Adapter var1) {
      if(this.getAdapter() != null) {
         this.getAdapter().unregisterAdapterDataObserver(this.emptyViewObserver);
      }

      /*if(var1 instanceof PrefetchInfoProvider) {
         this.preloader.setProvider((PrefetchInfoProvider)var1);
      }*/

      RecyclerViewAdapterWrapper var2;
      if(var1 == null) {
         var2 = null;
      } else {
         var2 = new RecyclerViewAdapterWrapper((Adapter<RecyclerHolder>)var1);
      }

      super.setAdapter(var2);
      if(var2 != null) {
         var2.registerAdapterDataObserver(this.emptyViewObserver);
      }

      this.updateEmptyViewVisibility();
   }

   public void setDrawSelectorOnTop(boolean var1) {
      this.drawHighlightOnTop = var1;
   }

   public void setEmptyView(View var1) {
      this.emptyView = var1;
      this.updateEmptyViewVisibility();
   }

   public void setListener(@Nullable Listener var1) {
      this.listener = var1;
   }

   public void setOnSizeChangeListener(OnSizeChangeListener var1) {
      this.onSizeChangeListener = var1;
   }

   public void setSelector(@DrawableRes int var1) {
      this.setSelector(this.getResources().getDrawable(var1));
   }

   public void setSelector(Drawable var1) {
      if(this.highlight != null) {
         this.highlight.setCallback((Callback)null);
      }

      this.highlight = var1;
      if(this.highlight != null) {
         this.highlight.setCallback(this);
      }
   }

   public void setSelectorBoundsProvider(SelectorBoundsProvider var1) {
      this.highlightBoundsProvider = var1;
   }

   protected boolean verifyDrawable(Drawable var1) {
      return super.verifyDrawable(var1) || var1 == this.highlight;
   }

   public abstract static class Adapter<R extends UsableRecyclerView.ViewHolder> extends RecyclerView.Adapter<R> implements PrefetchInfoProvider {

      public int getImageCountForItem(int var1) {
         return 0;
      }

      public String getImageURL(int var1, int var2) {
         return null;
      }
   }

   private class ClickStartRunnable implements Runnable {

      private ClickStartRunnable() {}

      // $FF: synthetic method
      ClickStartRunnable(Object var2) {
         this();
      }

      public void run() {
         if(UsableRecyclerView.this.clickingViewHolder != null) {
            UsableRecyclerView.this.postedClickStart = null;
            UsableRecyclerView.this.highlightedView.setPressed(true);
            UsableRecyclerView.this.highlight.setState(UsableRecyclerView.PRESSED_ENABLED_FOCUSED_STATE_SET);
         }
      }
   }

   public interface Clickable {

      void onClick();
   }

   public interface DisableableClickable extends Clickable {

      boolean isEnabled();
   }

   public interface ExtendedListener extends Listener {

      void onScroll(int var1, int var2, int var3);
   }

   private static class FooterRecyclerAdapter extends RecyclerViewAdapterWrapper {

      private static final int FOOTER_TYPE_FIRST = -1000;
      private ArrayList footerViews = new ArrayList();


      public FooterRecyclerAdapter(Adapter<RecyclerHolder> var1) {
         super(var1);
      }

      public int getItemCount() {
         return this.wrapped.getItemCount() + this.footerViews.size();
      }

      public long getItemId(int var1) {
         return var1 < this.wrapped.getItemCount()?this.wrapped.getItemId(var1):0L;
      }

      public int getItemViewType(int var1) {
         return var1 < this.wrapped.getItemCount()?this.wrapped.getItemViewType(var1):var1 - 1000 - this.wrapped.getItemCount();
      }

      public void onBindViewHolder(ViewHolder var1, int var2) {
         if(var2 < this.wrapped.getItemCount()) {
            super.onBindViewHolder(var1, var2);
         }

      }

      public ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         return (ViewHolder)(var2 >= -1000 && var2 < this.footerViews.size() - 1000?new FooterViewHolder((View)this.footerViews.get(var2 + 1000)):(ViewHolder)this.wrapped.onCreateViewHolder(var1, var2));
      }

      public boolean onFailedToRecycleView(ViewHolder var1) {
         return !(var1 instanceof FooterViewHolder) && this.wrapped.onFailedToRecycleView(var1);
      }

      public void onViewAttachedToWindow(ViewHolder var1) {
         if(!(var1 instanceof FooterViewHolder)) {
            this.wrapped.onViewAttachedToWindow(var1);
         }

      }

      public void onViewDetachedFromWindow(ViewHolder var1) {
         if(!(var1 instanceof FooterViewHolder)) {
            this.wrapped.onViewDetachedFromWindow(var1);
         }

      }

      public void onViewRecycled(ViewHolder var1) {
         if(!(var1 instanceof FooterViewHolder)) {
            this.wrapped.onViewRecycled(var1);
         }

      }
   }

   private static class FooterViewHolder extends ViewHolder {

      public FooterViewHolder(View var1) {
         super(var1);
      }
   }

   public interface Listener {

      void onScrollStarted();

      void onScrolledToLastItem();
   }

   private class LongClickRunnable implements Runnable {

      private LongClickRunnable() {}

      // $FF: synthetic method
      LongClickRunnable(Object var2) {
         this();
      }

      public void run() {
         if(UsableRecyclerView.this.clickingViewHolder != null) {
            UsableRecyclerView.this.postedLongClick = null;
            UsableRecyclerView.this.highlightedView.setPressed(false);
            UsableRecyclerView.this.highlight.setState(UsableRecyclerView.EMPTY_STATE_SET);
            if(((LongClickable)UsableRecyclerView.this.clickingViewHolder).onLongClick()) {
               UsableRecyclerView.this.performHapticFeedback(0);
            }

            UsableRecyclerView.this.clickingViewHolder = null;
         }
      }
   }

   public interface LongClickable {

      boolean onLongClick();
   }

   public interface OnSizeChangeListener {

      void onSizeChange(int var1, int var2, int var3, int var4);
   }

   public interface SelectorBoundsProvider {

      void getSelectorBounds(View var1, Rect var2);
   }

   public static class ViewHolder extends RecyclerView.ViewHolder {

      public ViewHolder(View var1) {
         super(var1);
      }
   }
}
