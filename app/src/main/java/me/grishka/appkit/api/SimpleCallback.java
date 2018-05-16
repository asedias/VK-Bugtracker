package me.grishka.appkit.api;

import android.app.Fragment;

import me.grishka.appkit.fragments.LoaderFragment;

public abstract class SimpleCallback implements Callback {

   private Fragment fragment;


   public SimpleCallback(Fragment var1) {
      this.fragment = var1;
   }

   public void onError(ErrorResponse var1) {
      if(this.fragment instanceof LoaderFragment) {
         ((LoaderFragment)this.fragment).onError(var1);
      }

   }
}
