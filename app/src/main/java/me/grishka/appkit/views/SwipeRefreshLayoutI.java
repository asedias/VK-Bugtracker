package me.grishka.appkit.views;

import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

public interface SwipeRefreshLayoutI {

   int getVisibility();

   boolean isReversed();

   boolean post(Runnable var1);

   void setColorSchemeResources(@ColorRes int... var1);

   void setEnabled(boolean var1);

   void setOnRefreshListener(OnRefreshListener var1);

   void setRefreshing(boolean var1);

   void setReversed(boolean var1);

   void setVisibility(int var1);
}
