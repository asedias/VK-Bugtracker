package me.grishka.appkit.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.View;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContainerFragment extends AppKitFragment {

   private boolean activityCreated = false;
   private ArrayList children = new ArrayList();
   private boolean detached = false;
   private InnerFragmentManager innerFragmentManager;
   private ArrayList runQueue = new ArrayList();


   public FragmentManager getInnerFragmentManager() {
      if(VERSION.SDK_INT >= 17) {
         return this.getChildFragmentManager();
      } else {
         if(this.innerFragmentManager == null) {
            this.innerFragmentManager = new InnerFragmentManager(this.getFragmentManager());
         }

         return this.innerFragmentManager;
      }
   }

   public void onActivityCreated(Bundle var1) {
      super.onActivityCreated(var1);
      this.activityCreated = true;
      this.detached = false;
      Handler var3 = new Handler();
      Iterator var2 = this.runQueue.iterator();

      while(var2.hasNext()) {
         var3.post((Runnable)var2.next());
      }

      this.runQueue.clear();
   }

   public void onDetach() {
      if(this.isRemoving()) {
         Iterator var1 = this.children.iterator();

         while(var1.hasNext()) {
            Fragment var2 = (Fragment)var1.next();
            this.getFragmentManager().beginTransaction().remove(var2).commit();
         }

         this.detached = true;
      }

      super.onDetach();
   }

   private class InnerFragmentManager extends FragmentManager {

      private FragmentManager o;

      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public List<Fragment> getFragments() {
         return this.o.getFragments();
      }

      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean b) {
         this.o.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, b);
      }

      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks fragmentLifecycleCallbacks) {
         this.o.unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks);
      }

      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public Fragment getPrimaryNavigationFragment() {
         return this.o.getPrimaryNavigationFragment();
      }

      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public boolean isStateSaved() {
         return this.o.isStateSaved();
      }

      public InnerFragmentManager(FragmentManager var2) {
         this.o = var2;
      }

      public void addOnBackStackChangedListener(OnBackStackChangedListener var1) {
         this.o.addOnBackStackChangedListener(var1);
      }

      public FragmentTransaction beginTransaction() {
         return ContainerFragment.this.new InnerFragmentTransaction(ContainerFragment.this.getFragmentManager().beginTransaction());
      }

      public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
         this.o.dump(var1, var2, var3, var4);
      }

      public boolean executePendingTransactions() {
         return this.o.executePendingTransactions();
      }

      public Fragment findFragmentById(int var1) {
         return this.o.findFragmentById(var1);
      }

      public Fragment findFragmentByTag(String var1) {
         return this.o.findFragmentByTag(var1 + "_" + ContainerFragment.this.getClass().getName());
      }

      public BackStackEntry getBackStackEntryAt(int var1) {
         return this.o.getBackStackEntryAt(var1);
      }

      public int getBackStackEntryCount() {
         return this.o.getBackStackEntryCount();
      }

      public Fragment getFragment(Bundle var1, String var2) {
         return this.o.getFragment(var1, var2);
      }

      public boolean isDestroyed() {
         return false;
      }

      public void popBackStack() {
         this.o.popBackStack();
      }

      public void popBackStack(int var1, int var2) {
         this.o.popBackStack(var1, var2);
      }

      public void popBackStack(String var1, int var2) {
         this.o.popBackStack(var1, var2);
      }

      public boolean popBackStackImmediate() {
         return this.o.popBackStackImmediate();
      }

      public boolean popBackStackImmediate(int var1, int var2) {
         return this.o.popBackStackImmediate(var1, var2);
      }

      public boolean popBackStackImmediate(String var1, int var2) {
         return this.o.popBackStackImmediate(var1, var2);
      }

      public void putFragment(Bundle var1, String var2, Fragment var3) {
         this.o.putFragment(var1, var2, var3);
      }

      public void removeOnBackStackChangedListener(OnBackStackChangedListener var1) {
         this.o.removeOnBackStackChangedListener(var1);
      }

      public SavedState saveFragmentInstanceState(Fragment var1) {
         return this.o.saveFragmentInstanceState(var1);
      }
   }

   private class InnerFragmentTransaction extends FragmentTransaction {

      private ArrayList fragmentsToAdd = new ArrayList();
      private ArrayList fragmentsToRemove = new ArrayList();
      private FragmentTransaction o;

      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public FragmentTransaction setPrimaryNavigationFragment(Fragment fragment) {
         return this.o.setPrimaryNavigationFragment(fragment);
      }

      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public FragmentTransaction setReorderingAllowed(boolean b) {
         return this.o.setReorderingAllowed(b);
      }

      @RequiresApi(api = Build.VERSION_CODES.O)
      @Override
      public FragmentTransaction runOnCommit(Runnable runnable) {
         return this.o.runOnCommit(runnable);
      }

      public InnerFragmentTransaction(FragmentTransaction var2) {
         this.o = var2;
      }

      public FragmentTransaction add(int var1, Fragment var2) {
         this.o.add(var1, var2);
         this.fragmentsToAdd.add(var2);
         return this;
      }

      public FragmentTransaction add(int var1, Fragment var2, String var3) {
         if(!ContainerFragment.this.detached) {
            this.o.add(var1, var2, var3 + "_" + ContainerFragment.this.getClass().getName());
            this.fragmentsToAdd.add(var2);
         }

         return this;
      }

      public FragmentTransaction add(Fragment var1, String var2) {
         this.o.add(var1, var2 + "_" + ContainerFragment.this.getClass().getName());
         this.fragmentsToAdd.add(var1);
         return this;
      }

      public FragmentTransaction addSharedElement(View var1, String var2) {
         return null;
      }

      public FragmentTransaction addToBackStack(String var1) {
         throw new UnsupportedOperationException("Back stack not supported for inner fragments");
      }

      public FragmentTransaction attach(Fragment var1) {
         if(!ContainerFragment.this.detached) {
            this.o.attach(var1);
            this.fragmentsToAdd.add(var1);
         }

         return this;
      }

      public int commit() {
         if(ContainerFragment.this.activityCreated) {
            this.o.commit();
            ContainerFragment.this.children.addAll(this.fragmentsToAdd);
            ContainerFragment.this.children.removeAll(this.fragmentsToRemove);
         } else {
            ContainerFragment.this.runQueue.add(new Runnable() {
               public void run() {
                  InnerFragmentTransaction.this.o.commit();
                  ContainerFragment.this.children.addAll(InnerFragmentTransaction.this.fragmentsToAdd);
                  ContainerFragment.this.children.removeAll(InnerFragmentTransaction.this.fragmentsToRemove);
               }
            });
         }

         return -1;
      }

      public int commitAllowingStateLoss() {
         if(ContainerFragment.this.activityCreated) {
            this.o.commitAllowingStateLoss();
            ContainerFragment.this.children.addAll(this.fragmentsToAdd);
            ContainerFragment.this.children.removeAll(this.fragmentsToRemove);
         } else {
            ContainerFragment.this.runQueue.add(new Runnable() {
               public void run() {
                  InnerFragmentTransaction.this.o.commitAllowingStateLoss();
                  ContainerFragment.this.children.addAll(InnerFragmentTransaction.this.fragmentsToAdd);
                  ContainerFragment.this.children.removeAll(InnerFragmentTransaction.this.fragmentsToRemove);
               }
            });
         }

         return -1;
      }

      public void commitNow() {
         this.commit();
      }

      public void commitNowAllowingStateLoss() {
         this.commitAllowingStateLoss();
      }

      public FragmentTransaction detach(Fragment var1) {
         this.fragmentsToRemove.add(var1);
         return this.o.detach(var1);
      }

      public FragmentTransaction disallowAddToBackStack() {
         throw new UnsupportedOperationException("Back stack not supported for inner fragments");
      }

      public FragmentTransaction hide(Fragment var1) {
         this.o.hide(var1);
         return this;
      }

      public boolean isAddToBackStackAllowed() {
         throw new UnsupportedOperationException("Back stack not supported for inner fragments");
      }

      public boolean isEmpty() {
         return this.o.isEmpty();
      }

      public FragmentTransaction remove(Fragment var1) {
         this.o.remove(var1);
         this.fragmentsToRemove.add(var1);
         return this;
      }

      public FragmentTransaction replace(int var1, Fragment var2) {
         this.o.replace(var1, var2);
         return this;
      }

      public FragmentTransaction replace(int var1, Fragment var2, String var3) {
         this.o.replace(var1, var2, var3 + "_" + ContainerFragment.this.getClass().getName());
         return this;
      }

      public FragmentTransaction setBreadCrumbShortTitle(int var1) {
         this.o.setBreadCrumbShortTitle(var1);
         return this;
      }

      public FragmentTransaction setBreadCrumbShortTitle(CharSequence var1) {
         this.o.setBreadCrumbShortTitle(var1);
         return this;
      }

      public FragmentTransaction setBreadCrumbTitle(int var1) {
         this.o.setBreadCrumbTitle(var1);
         return this;
      }

      public FragmentTransaction setBreadCrumbTitle(CharSequence var1) {
         this.o.setBreadCrumbTitle(var1);
         return this;
      }

      public FragmentTransaction setCustomAnimations(int var1, int var2) {
         this.o.setCustomAnimations(var1, var2);
         return this;
      }

      public FragmentTransaction setCustomAnimations(int var1, int var2, int var3, int var4) {
         this.o.setCustomAnimations(var1, var2, var3, var4);
         return this;
      }

      public FragmentTransaction setTransition(int var1) {
         this.o.setTransition(var1);
         return this;
      }

      public FragmentTransaction setTransitionStyle(int var1) {
         this.o.setTransitionStyle(var1);
         return this;
      }

      public FragmentTransaction show(Fragment var1) {
         this.o.show(var1);
         return this;
      }
   }
}
