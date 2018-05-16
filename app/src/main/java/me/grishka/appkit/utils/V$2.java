package me.grishka.appkit.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

final class V$2 extends AnimatorListenerAdapter {

   boolean canceled;
   // $FF: synthetic field
   final View val$view;
   // $FF: synthetic field
   final int val$visibility;


   V$2(View var1, int var2) {
      this.val$view = var1;
      this.val$visibility = var2;
      this.canceled = false;
   }

   public void onAnimationCancel(Animator var1) {
      this.canceled = true;
   }

   public void onAnimationEnd(Animator var1) {
      this.val$view.setTag(2131755132, (Object)null);
      V.access$000().remove(this.val$view);
      if(!this.canceled) {
         this.val$view.setVisibility(this.val$visibility);
         this.val$view.setAlpha(1.0F);
      }
   }

   public void onAnimationStart(Animator var1) {}
}
