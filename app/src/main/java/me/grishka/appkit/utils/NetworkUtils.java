package me.grishka.appkit.utils;

import android.os.Build;
import android.os.Build.VERSION;

public class NetworkUtils {

   private static String userAgent = "AppKit (" + Build.MANUFACTURER + " " + Build.MODEL + "; Android/" + VERSION.RELEASE + ")";


   public static String getUserAgent() {
      return userAgent;
   }

   public static void setUserAgent(String var0) {
      userAgent = var0;
   }
}
