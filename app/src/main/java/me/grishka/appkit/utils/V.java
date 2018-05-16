package me.grishka.appkit.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

public class V {

    private static Context appContext;
    private static HashMap visibilityAnims = new HashMap();


    // $FF: synthetic method
    static HashMap access$000() {
        return visibilityAnims;
    }

    public static void cancelVisibilityAnimation(View var0) {
        if(visibilityAnims.containsKey(var0)) {
            ((Animator)visibilityAnims.get(var0)).cancel();
            var0.setAlpha(1.0F);
        }
    }

    public static int dp(float var0) {
        if(appContext == null) {
            throw new IllegalStateException("Application context is not set, call V.setApplicationContext() before using these methods");
        } else {
            return Math.round(appContext.getResources().getDisplayMetrics().density * var0);
        }
    }

    public static View findClickableChild(ViewGroup var0, int var1, int var2) {
        for(int var3 = 0; var3 < var0.getChildCount(); ++var3) {
            View var4 = var0.getChildAt(var3);
            if(var4.getLeft() < var1 && var4.getRight() > var1 && var4.getTop() < var2 && var4.getBottom() > var2) {
                if(var4.isClickable()) {
                    return var4;
                }

                if(var4 instanceof ViewGroup) {
                    var4 = findClickableChild((ViewGroup)var4, var1 - var4.getLeft(), var2 - var4.getTop());
                    if(var4 != null) {
                        return var4;
                    }
                }
            }
        }

        return null;
    }

    public static Point getViewOffset(@Nullable View var0, @Nullable View var1) {
        int[] var2 = new int[]{0, 0};
        int[] var3 = new int[]{0, 0};
        if(var0 != null && var1 != null) {
            var0.getLocationOnScreen(var2);
            var1.getLocationOnScreen(var3);
        }

        return new Point(var2[0] - var3[0], var2[1] - var3[1]);
    }

    public static void setApplicationContext(Context var0) {
        if(appContext == null) {
            appContext = var0.getApplicationContext();
        }

    }

    public static void setVisibilityAnimated(View var0, int var1) {
        setVisibilityAnimated(var0, var1, false, 300);
    }

    public static void setVisibilityAnimated(View var0, int var1, boolean var2, int var3) {
        if(var0 != null) {
            boolean var7;
            if(var1 == 0) {
                var7 = true;
            } else {
                var7 = false;
            }

            boolean var5;
            if(var0.getVisibility() == View.VISIBLE && var0.getTag(2131755132) == null) {
                var5 = true;
            } else {
                var5 = false;
            }

            boolean var6;
            if(var2) {
                if(var0.getVisibility() == View.VISIBLE && var0.getScaleX() == 1.0F && var0.getScaleY() == 1.0F) {
                    var6 = true;
                } else {
                    var6 = false;
                }
            } else {
                var6 = var5;
            }

            if(var7 != var5 || var6 != var5) {
                if(visibilityAnims.containsKey(var0)) {
                    ((Animator)visibilityAnims.get(var0)).cancel();
                    visibilityAnims.remove(var0);
                }

                ArrayList var8 = new ArrayList();
                AnimatorSet var9 = new AnimatorSet();
                if(var7) {
                    float var4;
                    Property var10;
                    if(var2) {
                        var10 = View.SCALE_X;
                        if(var0.getScaleX() < 1.0F) {
                            var4 = var0.getScaleX();
                        } else {
                            var4 = 0.1F;
                        }

                        var8.add(ObjectAnimator.ofFloat(var0, var10, new float[]{var4, 1.0F}));
                        var10 = View.SCALE_Y;
                        if(var0.getScaleY() < 1.0F) {
                            var4 = var0.getScaleY();
                        } else {
                            var4 = 0.1F;
                        }

                        var8.add(ObjectAnimator.ofFloat(var0, var10, new float[]{var4, 1.0F}));
                    }

                    var10 = View.ALPHA;
                    if(var0.getAlpha() < 1.0F) {
                        var4 = var0.getAlpha();
                    } else {
                        var4 = 0.0F;
                    }

                    var8.add(ObjectAnimator.ofFloat(var0, var10, new float[]{var4, 1.0F}));
                    var9.playTogether(var8);
                    var9.addListener(new V$1(var0, var1));
                    var9.setDuration((long)var3);
                    visibilityAnims.put(var0, var9);
                    var9.start();
                    return;
                }

                if(var2) {
                    var8.add(ObjectAnimator.ofFloat(var0, View.SCALE_X, new float[]{0.1F}));
                    var8.add(ObjectAnimator.ofFloat(var0, View.SCALE_Y, new float[]{0.1F}));
                }

                var8.add(ObjectAnimator.ofFloat(var0, View.ALPHA, new float[]{0.0F}));
                var9.playTogether(var8);
                var9.addListener(new V$2(var0, var1));
                var0.setTag(2131755132, Boolean.valueOf(true));
                var9.setDuration((long)var3);
                visibilityAnims.put(var0, var9);
                var9.start();
                return;
            }
        }

    }
}
