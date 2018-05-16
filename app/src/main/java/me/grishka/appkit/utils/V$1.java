package me.grishka.appkit.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

final class V$1 extends AnimatorListenerAdapter {

   // $FF: synthetic field
   final View val$view;
   // $FF: synthetic field
   final int val$visibility;


   V$1(View var1, int var2) {
      this.val$view = var1;
      this.val$visibility = var2;
   }

   public void onAnimationCancel(Animator var1) {
      this.val$view.setVisibility(this.val$visibility);
   }

   public void onAnimationEnd(Animator var1) {
      this.val$view.setVisibility(this.val$visibility);
      V.access$000().remove(this.val$view);
   }

   public void onAnimationStart(Animator var1) {
      this.val$view.setVisibility(this.val$visibility);
   }
}
