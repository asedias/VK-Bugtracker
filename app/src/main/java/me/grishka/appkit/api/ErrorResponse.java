package me.grishka.appkit.api;

import android.content.Context;
import android.view.View;

public abstract class ErrorResponse {

   public abstract void bindErrorView(View var1);

   public abstract void showToast(Context var1);
}
