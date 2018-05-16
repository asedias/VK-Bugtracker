package me.grishka.appkit.api;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PaginatedList extends ArrayList {

   public PaginatedList() {}

   public PaginatedList(int var1) {
      super(var1);
   }

   public PaginatedList(Collection var1) {
      super(var1);
   }

   public abstract int total();
}
