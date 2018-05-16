package com.asedias.bugtracker.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.asedias.bugtracker.R;
import com.asedias.bugtracker.ui.holder.ReportHolder;
import com.asedias.bugtracker.ui.holder.TitleHolder;
import com.vkontakte.android.ui.CardItemDecorator;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;
import com.vkontakte.android.ui.holder.commons.BackgroundHolder;

import java.util.ArrayList;

import me.grishka.appkit.views.DividerItemDecoration;

/**
 * Created by rorom on 11.04.2018.
 */

public class ReportsAdapter extends RecyclerSectionAdapter<RecyclerHolder> implements CardItemDecorator.Provider, DividerItemDecoration.Provider {

    private final int TYPE_HEADER = 0;
    private final int TYPE_REPORT = 1;
    private static final int TYPE_BOTTOM = 3;
    public ArrayList<RecyclerSectionAdapter.Data> mItems = new ArrayList<RecyclerSectionAdapter.Data>();
    private LayoutInflater mInflater;

    public ReportsAdapter(ArrayList<RecyclerSectionAdapter.Data> mItems) {
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mInflater == null) {
            mInflater = ((Activity) parent.getContext()).getLayoutInflater();
        }
        switch (viewType) {
            case TYPE_REPORT: {
                return new ReportHolder(mInflater.inflate(R.layout.report_item, parent, false), mInflater);
            }
            case TYPE_HEADER: {
                return new TitleHolder(mInflater.inflate(R.layout.report_header, parent, false));
            }
            case TYPE_BOTTOM: {
                return new BackgroundHolder(parent);
            }

        }
        throw new RuntimeException("mInflater == null");
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).hashCode();
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        holder.bind(mItems.get(position).object);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).type;
    }

    public void setInflater(LayoutInflater inflater) {
        this.mInflater = inflater;
    }

    @Override
    public int getBlockType(int var1) {
        return mItems.get(var1).decoratorType;
    }

    @Override
    public boolean needDrawDividerAfter(int var1) {
        return mItems.get(var1).type == TYPE_REPORT;
    }
}
