package com.asedias.bugtracker.others;

import android.support.v7.util.DiffUtil;

import java.util.ArrayList;

/**
 * Created by rorom on 16.04.2018.
 */

public class DiffUtilCallback extends DiffUtil.Callback {

    ArrayList oldList;
    ArrayList newList;

    public DiffUtilCallback(ArrayList oldList, ArrayList newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).hashCode() == newList.get(newItemPosition).hashCode();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
