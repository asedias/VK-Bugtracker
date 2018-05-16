package com.vkontakte.android.navigation;

import android.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.asedias.bugtracker.MainActivity;

public class ToolbarHelper {

   public static boolean canGoBack(Fragment var0) {
      //if(var0.getArguments() != null && var0.getArguments().getBoolean("taskRoot", false)) {
         //return true;
      //} else {
         return !var0.getActivity().isTaskRoot();
      //}
   }

   public static boolean hasNavigationDrawer(Fragment var0) {
      return false;
   }

   public static void onToolbarNavigationClick(Fragment var0) {
      //if(var0.getActivity() instanceof MainActivity) {
         //((MainActivity) var0.getActivity()).mNavigator.onBackPressed();
      //} else {
         //var0.getActivity().finish();
      //}
      if(canGoBack(var0)) {
         var0.getActivity().finish();
      }
   }

   public static void onViewCreated(Toolbar var0) {
      //if(Global.isTablet && var0 != null) {
         //var0.setNavigationIcon((Drawable)null);
      //}
   }
}
