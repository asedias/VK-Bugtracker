package com.vkontakte.android.ui.recyclerview;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.util.SparseIntArray;
import android.view.View;

import com.asedias.bugtracker.BugTrackerApp;
import com.vkontakte.android.ui.cardview.CardDrawable;

import me.grishka.appkit.utils.V;

public class CardItemDecoration extends ItemDecoration {

   public static final int BACKGROUND_COLOR = -1315344;
   private SparseIntArray mAdapterToChildPosition = new SparseIntArray();
   private int mBackgroundPaddingBottom;
   private int mBackgroundPaddingLeft;
   private int mBackgroundPaddingRight;
   private int mBackgroundPaddingTop;
   private final Paint mBackgroundPaint;
   private boolean mBuildAdapterToChildProjection = true;
   private final CardDrawable mCardBackground;
   private int mCardsSpacing;
   private boolean mClipToPadding = true;
   private boolean mFullSize;
   private int mPaddingBottom;
   private int mPaddingLeft;
   private int mPaddingRight;
   private int mPaddingTop;
   @Nullable
   private Provider mProvider;


   public CardItemDecoration(@Nullable Provider var1, boolean var2) {
      Resources var4 = BugTrackerApp.context.getResources();
      float var3 = (float) V.dp(2.0F);
      this.mFullSize = var2;
      this.mCardBackground = new CardDrawable(var4, -1, var3, var2);
      this.mBackgroundPaint = new Paint();
      this.mBackgroundPaint.setColor(-1315344);
      this.mProvider = var1;
      Rect var5 = new Rect();
      this.mCardBackground.getPadding(var5);
      this.mBackgroundPaddingLeft = var5.left;
      this.mBackgroundPaddingTop = var5.top;
      this.mBackgroundPaddingRight = var5.right;
      this.mBackgroundPaddingBottom = var5.bottom;
   }

   private void drawBackground(Canvas var1, View var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      if(this.mBackgroundPaint.getColor() != 0) {
         int var9 = this.mCardBackground.getRadius();
         var1.drawRect(0.0F, (float)(var4 - var7), (float)(this.mBackgroundPaddingLeft + var3), (float)(var6 + var8), this.mBackgroundPaint);
         var1.drawRect((float)(var5 - this.mBackgroundPaddingRight), (float)(var4 - var7), (float)var2.getWidth(), (float)(var6 + var8), this.mBackgroundPaint);
         var1.drawRect((float)(this.mBackgroundPaddingLeft + var3), (float)(var4 - var7), (float)(var5 - this.mBackgroundPaddingRight), (float)(this.mBackgroundPaddingTop + var4), this.mBackgroundPaint);
         var1.drawRect((float)(this.mBackgroundPaddingLeft + var3), (float)(var6 - this.mBackgroundPaddingBottom), (float)(var5 - this.mBackgroundPaddingRight), (float)(var6 + var8), this.mBackgroundPaint);
         var1.drawRect((float)(this.mBackgroundPaddingLeft + var3), (float)(this.mBackgroundPaddingTop + var4), (float)(this.mBackgroundPaddingLeft + var3 + var9), (float)(this.mBackgroundPaddingTop + var4 + var9), this.mBackgroundPaint);
         var1.drawRect((float)(this.mBackgroundPaddingLeft + var3), (float)(var6 - this.mBackgroundPaddingBottom - var9), (float)(this.mBackgroundPaddingLeft + var3 + var9), (float)(var6 - this.mBackgroundPaddingBottom), this.mBackgroundPaint);
         var1.drawRect((float)(var5 - this.mBackgroundPaddingRight - var9), (float)(this.mBackgroundPaddingTop + var4), (float)(var5 - this.mBackgroundPaddingRight), (float)(this.mBackgroundPaddingTop + var4 + var9), this.mBackgroundPaint);
         var1.drawRect((float)(var5 - this.mBackgroundPaddingRight - var9), (float)(var6 - this.mBackgroundPaddingBottom - var9), (float)(var5 - this.mBackgroundPaddingRight), (float)(var6 - this.mBackgroundPaddingBottom), this.mBackgroundPaint);
      }

      this.mCardBackground.setBounds(var3, var4, var5, var6);
      this.mCardBackground.draw(var1);
   }

   static boolean is(int var0, int var1) {
      return (var0 & var1) == var1;
   }

   static boolean isnt(int var0, int var1) {
      return (var0 & var1) == 0;
   }

   public CardItemDecoration clipToPadding(boolean var1) {
      this.mClipToPadding = var1;
      return this;
   }

   public void getItemOffsets(Rect var1, View var2, RecyclerView var3, State var4) {
      if(this.mProvider != null) {
         int var5 = var3.getChildAdapterPosition(var2);
         if(is(this.mProvider.getBlockType(var5), 4)) {
            var1.bottom += this.mCardsSpacing;
         }
      }

   }

   public int getPaddingBottom() {
      return this.mPaddingBottom;
   }

   public int getPaddingLeft() {
      return this.mPaddingLeft;
   }

   public int getPaddingRight() {
      return this.mPaddingRight;
   }

   public int getPaddingTop() {
      return this.mPaddingTop;
   }

   public float getShadowSize() {
      return this.mCardBackground.getShadowSize();
   }

   public void onDraw(Canvas var1, RecyclerView var2, State var3) {
      if(var2 != null) {
         if(this.mProvider == null) {
            this.drawBackground(var1, var2, var2.getPaddingLeft() - this.mBackgroundPaddingLeft - this.mPaddingLeft, var2.getPaddingTop() - this.mCardBackground.getRadius() - this.mPaddingTop, var2.getWidth() - var2.getPaddingRight() + this.mBackgroundPaddingRight + this.mPaddingRight, var2.getHeight() - var2.getPaddingBottom() + this.mCardBackground.getRadius() + this.mPaddingBottom, 0, 0);
            return;
         }

         var2.getLayoutManager();
         int var4;
         if(this.mFullSize) {
            var4 = 0;
         } else if(this.mClipToPadding) {
            var4 = var2.getPaddingLeft() - this.mBackgroundPaddingLeft - this.mPaddingLeft;
         } else {
            var4 = Integer.MAX_VALUE;
         }

         int var5;
         if(this.mFullSize) {
            var5 = var2.getWidth();
         } else if(this.mClipToPadding) {
            var5 = var2.getWidth() - var2.getPaddingRight() + this.mBackgroundPaddingRight + this.mPaddingRight;
         } else {
            var5 = Integer.MIN_VALUE;
         }

         int var15 = 0;
         int var13 = 0;
         int var14 = 0;
         int var8 = this.mCardsSpacing;
         int var7 = var4;
         int var12 = var5;
         boolean var17 = false;
         boolean var16 = false;
         boolean var11 = false;
         int var6;
         if(this.mBuildAdapterToChildProjection) {
            var6 = RecyclerViewUtils.getRealFirstPosition(var2);
         } else {
            var6 = 0;
         }

         int var21;
         if(this.mBuildAdapterToChildProjection) {
            var21 = RecyclerViewUtils.mapAdapterToChildPositions(var2, this.mAdapterToChildPosition, var6);
         } else {
            var21 = var2.getChildCount() - 1;
         }

         boolean var22;
         for(int var26 = var2.getAdapter().getItemCount(); var6 <= var21; var17 = var22) {
            int var9;
            if(this.mBuildAdapterToChildProjection) {
               var9 = this.mAdapterToChildPosition.get(var6, -1);
            } else {
               var9 = var6;
            }

            int var18;
            int var19;
            boolean var23;
            int var24;
            int var25;
            int var30;
            if(var9 == -1) {
               var22 = var17;
               var23 = var16;
               var17 = var11;
               var30 = var8;
               var8 = var14;
               var24 = var13;
               var18 = var12;
               var25 = var15;
               var19 = var7;
            } else {
               View var27 = var2.getChildAt(var9);
               int var34;
               if(this.mBuildAdapterToChildProjection) {
                  var34 = var6;
               } else {
                  var34 = var2.getChildAdapterPosition(var27);
               }

               int var35 = this.mProvider.getBlockType(var34);
               var9 = var7;
               int var10 = var12;
               if(!this.mFullSize) {
                  var9 = var7;
                  var10 = var12;
                  if(!this.mClipToPadding) {
                     var18 = var7;
                     if(is(var35, 8)) {
                        var18 = Math.min(var27.getLeft() - this.mBackgroundPaddingLeft - this.mPaddingLeft, var7);
                     }

                     var9 = var18;
                     var10 = var12;
                     if(is(var35, 16)) {
                        var10 = Math.max(var27.getRight() + this.mBackgroundPaddingRight + this.mPaddingRight, var12);
                        var9 = var18;
                     }
                  }
               }

               var7 = var15;
               var12 = var14;
               boolean var31 = var16;
               boolean var32 = var17;
               if(var35 != 0) {
                  var7 = var15;
                  var12 = var14;
                  var31 = var16;
                  var32 = var17;
                  if(!var17) {
                     var15 = var27.getTop() + Math.round(var27.getTranslationY()) - this.mBackgroundPaddingTop - this.mPaddingTop;
                     var16 = true;
                     var17 = false;
                     var7 = var15;
                     var12 = var14;
                     var31 = var17;
                     var32 = var16;
                     if(var34 == 0) {
                        var12 = var2.getPaddingTop() - this.mBackgroundPaddingTop - this.mPaddingTop;
                        var32 = var16;
                        var31 = var17;
                        var7 = var15;
                     }
                  }
               }

               if(var35 != 0) {
                  var13 = var27.getBottom() + Math.round(var27.getTranslationY()) + this.mBackgroundPaddingBottom + this.mPaddingBottom;
               }

               boolean var20 = var11;
               var17 = var31;
               var16 = var32;
               if(is(var35, 4)) {
                  var20 = var11;
                  var17 = var31;
                  var16 = var32;
                  if(!var31) {
                     var16 = false;
                     var17 = true;
                     var20 = true;
                  }
               }

               boolean var29 = var20;
               boolean var28 = var17;
               var11 = var16;
               if(var34 == var21) {
                  var29 = var20;
                  var28 = var17;
                  var11 = var16;
                  if(var16) {
                     var29 = var20;
                     var28 = var17;
                     var11 = var16;
                     if(!var17) {
                        var29 = true;
                        var11 = false;
                        var28 = false;
                     }
                  }
               }

               int var33;
               label93: {
                  if(var34 != var26 - 1) {
                     var33 = var8;
                     if(var34 != var21) {
                        break label93;
                     }
                  }

                  var33 = var2.getBottom() - var13;
               }

               var19 = var9;
               var25 = var7;
               var18 = var10;
               var24 = var13;
               var8 = var12;
               var30 = var33;
               var17 = var29;
               var23 = var28;
               var22 = var11;
               if(var29) {
                  this.drawBackground(var1, var2, var9, var7, var10, var13, var12, var33);
                  var17 = false;
                  var8 = 0;
                  var30 = this.mCardsSpacing;
                  var19 = var4;
                  var18 = var5;
                  var25 = var7;
                  var24 = var13;
                  var23 = var28;
                  var22 = var11;
               }
            }

            ++var6;
            var7 = var19;
            var15 = var25;
            var12 = var18;
            var13 = var24;
            var14 = var8;
            var8 = var30;
            var11 = var17;
            var16 = var23;
         }
      }

   }

   public CardItemDecoration setBackgroundColor(@ColorInt int var1) {
      this.mBackgroundPaint.setColor(var1);
      return this;
   }

   public CardItemDecoration setCardsSpacing(int var1) {
      this.mCardsSpacing = var1;
      return this;
   }

   public CardItemDecoration setFullSize(boolean var1) {
      this.mFullSize = var1;
      return this;
   }

   public CardItemDecoration setPadding(int var1, int var2, int var3, int var4) {
      this.mPaddingLeft = var1;
      this.mPaddingTop = var2;
      this.mPaddingRight = var3;
      this.mPaddingBottom = var4;
      return this;
   }

   public CardItemDecoration setProvider(@Nullable Provider var1) {
      this.mProvider = var1;
      return this;
   }

   public interface Provider {

      int BOTTOM = 4;
      int LEFT = 8;
      int MIDDLE = 1;
      int NONE = 0;
      int RIGHT = 16;
      int TOP = 2;


      int getBlockType(int var1);
   }
}
