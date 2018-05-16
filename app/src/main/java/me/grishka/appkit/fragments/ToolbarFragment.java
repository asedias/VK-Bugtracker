package me.grishka.appkit.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asedias.bugtracker.R;

public abstract class ToolbarFragment extends ContainerFragment {

   protected View content;
   protected int layoutID;


   public ToolbarFragment() {
      this(R.layout.appkit_toolbar_fragment);
   }

   protected ToolbarFragment(@LayoutRes int var1) {
      this.layoutID = var1;
   }

   @LayoutRes
   protected int getLayout() {
      return this.layoutID;
   }

   public abstract View onCreateContentView(LayoutInflater var1, ViewGroup var2, Bundle var3);

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      this.content = var1.inflate(this.layoutID, (ViewGroup)null);
      ((ViewGroup)this.content.findViewById(R.id.appkit_content)).addView(this.onCreateContentView(var1, var2, var3));
      return this.content;
   }

   public void onDestroyView() {
      super.onDestroyView();
      this.content = null;
   }

   protected void setLayout(int var1) {
      if(this.content != null) {
         throw new IllegalStateException("Can\'t set layout when view is already created");
      } else {
         this.layoutID = var1;
      }
   }
}
