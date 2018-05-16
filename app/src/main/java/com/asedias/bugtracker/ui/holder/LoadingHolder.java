package com.asedias.bugtracker.ui.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vkontakte.android.ui.holder.RecyclerHolder;

/**
 * Created by rorom on 12.04.2018.
 */

public class LoadingHolder extends RecyclerHolder {
    public LoadingHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object var1) {
        itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
    }
}
