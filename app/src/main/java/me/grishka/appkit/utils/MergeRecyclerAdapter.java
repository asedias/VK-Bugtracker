package me.grishka.appkit.utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import me.grishka.appkit.preloading.PrefetchInfoProvider;
import me.grishka.appkit.views.UsableRecyclerView;

public class MergeRecyclerAdapter extends UsableRecyclerView.Adapter implements PrefetchInfoProvider {

   private ArrayList adapters = new ArrayList();
   private HashMap observers = new HashMap();
   private SparseArray viewTypeMapping = new SparseArray();


   public void addAdapter(int var1, UsableRecyclerView.Adapter var2) {
      if(this.adapters.contains(var2)) {
         throw new IllegalArgumentException("Adapter " + var2 + " is already added!");
      } else {
         this.adapters.add(var1, var2);
         InternalDataObserver var3 = new InternalDataObserver(var2);
         var2.registerAdapterDataObserver(var3);
         this.observers.put(var2, var3);
         this.notifyDataSetChanged();
      }
   }

   public void addAdapter(UsableRecyclerView.Adapter var1) {
      this.addAdapter(this.adapters.size(), var1);
   }

   public Adapter getAdapterAt(int var1) {
      return (Adapter)this.adapters.get(var1);
   }

   public int getAdapterCount() {
      return this.adapters.size();
   }

   public UsableRecyclerView.Adapter getAdapterForPosition(int var1) {
      int var2 = 0;

      int var3;
      for(Iterator var4 = this.adapters.iterator(); var4.hasNext(); var2 += var3) {
         UsableRecyclerView.Adapter var5 = (UsableRecyclerView.Adapter)var4.next();
         var3 = var5.getItemCount();
         if(var1 >= var2 && var1 < var2 + var3) {
            return var5;
         }
      }

      return null;
   }

   public int getAdapterPosition(int var1) {
      int var2 = 0;
      Iterator var4 = this.adapters.iterator();

      int var3;
      while(true) {
         var3 = var1;
         if(!var4.hasNext()) {
            break;
         }

         var3 = ((Adapter)var4.next()).getItemCount();
         if(var1 >= var2 && var1 < var2 + var3) {
            var3 = var1 - var2;
            break;
         }

         var2 += var3;
      }

      return var3;
   }

   public int getImageCountForItem(int var1) {
      UsableRecyclerView.Adapter var2 = this.getAdapterForPosition(var1);
      return var2 instanceof PrefetchInfoProvider?((PrefetchInfoProvider)var2).getImageCountForItem(this.getAdapterPosition(var1)):0;
   }

   public String getImageURL(int var1, int var2) {
      UsableRecyclerView.Adapter var3 = this.getAdapterForPosition(var1);
      return var3 instanceof PrefetchInfoProvider?((PrefetchInfoProvider)var3).getImageURL(this.getAdapterPosition(var1), var2):null;
   }

   public int getItemCount() {
      int var1 = 0;

      for(Iterator var2 = this.adapters.iterator(); var2.hasNext(); var1 += ((Adapter)var2.next()).getItemCount()) {
         ;
      }

      return var1;
   }

   public long getItemId(int var1) {
      return this.getAdapterForPosition(var1).getItemId(this.getAdapterPosition(var1));
   }

   public int getItemViewType(int var1) {
      UsableRecyclerView.Adapter var2 = this.getAdapterForPosition(var1);
      var1 = var2.getItemViewType(this.getAdapterPosition(var1));
      this.viewTypeMapping.put(var1, var2);
      return var1;
   }

   public int getPositionForAdapter(Adapter var1) {
      int var2 = 0;

      Adapter var4;
      for(Iterator var3 = this.adapters.iterator(); var3.hasNext(); var2 += var4.getItemCount()) {
         var4 = (Adapter)var3.next();
         if(var4 == var1) {
            break;
         }
      }

      return var2;
   }

   public void onBindViewHolder(UsableRecyclerView.ViewHolder var1, int var2) {
      this.getAdapterForPosition(var2).onBindViewHolder(var1, this.getAdapterPosition(var2));
   }

   public UsableRecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      return (UsableRecyclerView.ViewHolder)((UsableRecyclerView.Adapter)this.viewTypeMapping.get(var2)).onCreateViewHolder(var1, var2);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      onBindViewHolder((UsableRecyclerView.ViewHolder)holder, position);
   }

   public void removeAdapter(Adapter var1) {
      this.adapters.remove(var1);
      var1.unregisterAdapterDataObserver((AdapterDataObserver)this.observers.get(var1));
      this.observers.remove(var1);
      this.notifyDataSetChanged();
   }

   public void removeAdapterAt(int var1) {
      this.removeAdapter((Adapter)this.adapters.get(var1));
   }

   public void removeAllAdapters() {
      Iterator var1 = this.adapters.iterator();

      while(var1.hasNext()) {
         UsableRecyclerView.Adapter var2 = (UsableRecyclerView.Adapter)var1.next();
         var2.unregisterAdapterDataObserver((AdapterDataObserver)this.observers.get(var2));
         this.observers.remove(var2);
      }

      this.adapters.clear();
      this.notifyDataSetChanged();
   }

   private class InternalDataObserver extends AdapterDataObserver {

      private Adapter adapter;


      public InternalDataObserver(Adapter var2) {
         this.adapter = var2;
      }

      public void onChanged() {
         MergeRecyclerAdapter.this.notifyDataSetChanged();
      }

      public void onItemRangeChanged(int var1, int var2) {
         MergeRecyclerAdapter.this.notifyItemRangeChanged(MergeRecyclerAdapter.this.getPositionForAdapter(this.adapter) + var1, var2);
      }

      public void onItemRangeChanged(int var1, int var2, Object var3) {
         MergeRecyclerAdapter.this.notifyItemRangeChanged(MergeRecyclerAdapter.this.getPositionForAdapter(this.adapter) + var1, var2, var3);
      }

      public void onItemRangeInserted(int var1, int var2) {
         MergeRecyclerAdapter.this.notifyItemRangeInserted(MergeRecyclerAdapter.this.getPositionForAdapter(this.adapter) + var1, var2);
      }

      public void onItemRangeMoved(int var1, int var2, int var3) {
         if(var3 != 1) {
            throw new UnsupportedOperationException("Can\'t move more than one item");
         } else {
            var3 = MergeRecyclerAdapter.this.getPositionForAdapter(this.adapter);
            MergeRecyclerAdapter.this.notifyItemMoved(var3 + var1, var3 + var2);
         }
      }

      public void onItemRangeRemoved(int var1, int var2) {
         MergeRecyclerAdapter.this.notifyItemRangeRemoved(MergeRecyclerAdapter.this.getPositionForAdapter(this.adapter) + var1, var2);
      }
   }
}
