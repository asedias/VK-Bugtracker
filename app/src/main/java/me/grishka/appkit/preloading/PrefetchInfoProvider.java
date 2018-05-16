package me.grishka.appkit.preloading;


public interface PrefetchInfoProvider {

   int getImageCountForItem(int var1);

   String getImageURL(int var1, int var2);

   int getItemCount();
}
