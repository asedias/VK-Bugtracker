package com.asedias.bugtracker.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.asedias.bugtracker.R;
import com.asedias.bugtracker.ui.holder.VersionHolder;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;
import com.vkontakte.android.ui.holder.commons.BackgroundHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rorom on 11.04.2018.
 */

public class AddReportAdapter extends RecyclerSectionAdapter<RecyclerHolder> implements RecyclerSectionAdapter.DataDelegate {

    public static final int TYPE_HEADER = R.layout.report_header;
    public static final int TYPE_PRODUCT = R.layout.addr_product;
    public static final int TYPE_INPUT = R.layout.addr_input;
    public static final int TYPE_TAGS = R.layout.addr_tags;
    public static final int TYPE_DOC = R.layout.report_doc;
    public static final int TYPE_SWITCH = R.layout.addr_switch;
    public static final int TYPE_SEVERITY = R.layout.addr_severity;
    public static final int TYPE_BACKGROUND = R.drawable.card_margin_fix_item;
    public ArrayList<RecyclerSectionAdapter.Data> mItems;
    private LayoutInflater mInflater;
    public static int AndroidPosition = 0;
    public static int IOSPosition = 0;
    public static Activity context;

    public AddReportAdapter(Activity context1) {
        context = context1;
        this.mItems = new ArrayList<>();
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mInflater == null) {
            mInflater = ((Activity) parent.getContext()).getLayoutInflater();
        }
        if(viewType == TYPE_BACKGROUND) return new BackgroundHolder(parent);
        return new VersionHolder(mInflater.inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).type;
    }

    @Override
    public List getData() {
        return mItems;
    }
}
