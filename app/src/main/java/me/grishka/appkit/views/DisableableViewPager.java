package me.grishka.appkit.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DisableableViewPager extends ViewPager {

   private boolean swipeEnabled = true;


   public DisableableViewPager(Context var1) {
      super(var1);
   }

   public DisableableViewPager(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public boolean isSwipeEnabled() {
      return this.swipeEnabled;
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      return this.swipeEnabled && super.onInterceptTouchEvent(var1);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      return this.swipeEnabled && super.onTouchEvent(var1);
   }

   public void setSwipeEnabled(boolean var1) {
      this.swipeEnabled = var1;
   }
}
