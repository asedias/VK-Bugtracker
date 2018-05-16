package com.asedias.bugtracker.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.asedias.bugtracker.R;
import com.asedias.bugtracker.ui.holder.ProfileProductHolder;
import com.asedias.bugtracker.ui.holder.TitleHolder;
import com.asedias.bugtracker.model.ProfileItem;

import java.util.List;

/**
 * Created by rorom on 11.04.2018.
 */

public class ProfileAdapter extends RecyclerView.Adapter {


    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;
    private final int TYPE_ITEM_LAST = 2;
    private List<ProfileItem> mItems;
    private LayoutInflater mInflater;

    public ProfileAdapter(List<ProfileItem> mItems) {
        this.mItems = mItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) return new TitleHolder(mInflater.inflate(R.layout.report_header, parent, false));
        return new ProfileProductHolder(mInflater.inflate(R.layout.profile_header, parent, false));
        //return null; if(viewType == TYPE_ITEM)
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TitleHolder) ((TitleHolder) holder).bind(mItems.get(position).title);
        if(holder instanceof ProfileProductHolder) {
            ((ProfileProductHolder) holder).bind(mItems.get(position));
            if(position == 0||position == mItems.size()-1||mItems.get(position).card) {
                ((ProfileProductHolder) holder).setHeader();
            } else {
                ((ProfileProductHolder) holder).header = false;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mItems.get(position).isTitle()) return TYPE_HEADER;
        return TYPE_ITEM;
    }

    public void setInflater(LayoutInflater inflater) {
        this.mInflater = inflater;
    }
}
