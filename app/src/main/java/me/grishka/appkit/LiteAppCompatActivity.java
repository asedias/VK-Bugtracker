package me.grishka.appkit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;

public class LiteAppCompatActivity extends Activity {

   protected AppCompatDelegate delegate;


   protected void onCreate(Bundle var1) {
      this.delegate = AppCompatDelegate.create(this, (AppCompatCallback)null);
      this.delegate.installViewFactory();
      this.getWindow().setCallback(this);
      super.onCreate(var1);
   }
}
