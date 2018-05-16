package com.asedias.bugtracker.ui.holder;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.asedias.bugtracker.model.reportview.PhotoList;
import com.asedias.bugtracker.others.ImageGridParser;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

/**
 * Created by rorom on 14.04.2018.
 */

public class PhotosHolder  extends RecyclerHolder {

    public PhotosHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object var1) {
        if(var1 instanceof RecyclerSectionAdapter.Data) {
            bind(((RecyclerSectionAdapter.Data) var1).object);
        } else if(var1 instanceof PhotoList) {
            bind((PhotoList) var1);
        }
    }

    public void bind(PhotoList list) {
        new ImageGridParser((Activity)itemView.getContext(), list.photos, (ViewGroup)itemView);
    }

}
