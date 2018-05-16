package me.grishka.appkit.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class StubListAdapter extends BaseAdapter {

   private static StubListAdapter instance = new StubListAdapter();


   public static StubListAdapter getInstance() {
      return instance;
   }

   public int getCount() {
      return 0;
   }

   public Object getItem(int var1) {
      return null;
   }

   public long getItemId(int var1) {
      return 0L;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      return null;
   }
}
