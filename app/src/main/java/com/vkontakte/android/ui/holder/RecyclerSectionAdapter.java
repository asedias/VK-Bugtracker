package com.vkontakte.android.ui.holder;

import com.vkontakte.android.ui.CardItemDecorator;
import com.vkontakte.android.ui.recyclerview.CardItemDecoration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.grishka.appkit.views.UsableRecyclerView;

public abstract class RecyclerSectionAdapter<R extends RecyclerHolder> extends UsableRecyclerView.Adapter<R> implements CardItemDecorator.Provider, CardItemDecoration.Provider {

   public List<Data> data = new ArrayList<>();
   public final DataDelegate dataDelegate;


   public RecyclerSectionAdapter() {
      this.dataDelegate = null;
   }

   public RecyclerSectionAdapter(DataDelegate var1) {
      this.dataDelegate = var1;
   }

   public void add(int var1, Data var2) {
      this.data.add(var1, var2);
      this.notifyDataSetChanged();
   }

   public void add(Data var1) {
      this.data.add(var1);
      this.notifyItemInserted(this.data.size() - 2);
   }

   public void addAll(Collection var1) {
      this.addAll(var1, true);
   }

   public void addAll(Collection var1, boolean var2) {
      int var3 = this.data.size();
      this.data.addAll(var1);
      if(var2) {
         this.notifyItemRangeInserted(var3, var1.size());
      }

   }

   public void clear() {
      this.clear(true);
   }

   public void clear(boolean var1) {
      this.data.clear();
      if(var1) {
         this.notifyDataSetChanged();
      }

   }

   public int getBlockType(int var1) {
      return ((Data)this.getData().get(var1)).decoratorType;
   }

   protected List getData() {
      return this.dataDelegate == null?this.data:this.dataDelegate.getData();
   }

   public int getItemCount() {
      return this.getData().size();
   }

   public int getItemViewType(int var1) {
      return ((Data)this.getData().get(var1)).type;
   }

   public void onBindViewHolder(RecyclerHolder var1, int var2) {
      var1.bind(((Data)this.getData().get(var2)).object);
   }

   public void replace(Data var1, Data var2) {
      int var3 = this.data.indexOf(var1);
      if(var3 >= 0 && var3 < this.data.size()) {
         this.data.set(var3, var2);
         this.notifyItemChanged(var3);
      }

   }

   public void setData(List var1) {
      Object var2 = var1;
      if(var1 == null) {
         var2 = new ArrayList();
      }

      this.data = (List)var2;
      this.notifyDataSetChanged();
   }

   public void setData(List var1, int var2, int var3) {
      Object var4 = var1;
      if(var1 == null) {
         var4 = new ArrayList();
      }

      this.data = (List)var4;
      this.notifyItemRangeInserted(var2, var3);
   }

   public static class Data{

      public int decoratorType;
      public final Object object;
      public final int type;


      public Data(int var1, Object var2) {
         this(var1, var2, 0);
      }

      private Data(int var1, Object var2, int var3) {
         this.type = var1;
         this.object = var2;
         this.decoratorType = var3;
      }

      public static Data bottom(int var0, Object var1) {
         return new Data(var0, var1, 4);
      }

      public static Data middle(int var0, Object var1) {
         return new Data(var0, var1, 1);
      }

      public static Data none(int var0, Object var1) {
         return new Data(var0, var1);
      }

      public static Data top(int var0, Object var1) {
         return new Data(var0, var1, 2);
      }

      public static Data topBottom(int var0, Object var1) {
         return new Data(var0, var1, 6);
      }
   }

   public interface DataDelegate {

      List getData();
   }
}
