package com.asedias.bugtracker.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.asedias.bugtracker.R;
import com.asedias.bugtracker.model.reportview.InfoItem;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

/**
 * Created by rorom on 14.04.2018.
 */

public class ReportInfoHolder extends RecyclerHolder {

    public TextView label;
    public TextView data;
    public TextView subdata;

    public ReportInfoHolder(View itemView) {
        super(itemView);
        this.label = itemView.findViewById(R.id.label);
        this.data = itemView.findViewById(R.id.data);
        this.subdata = itemView.findViewById(R.id.subdata);
    }

    @Override
    public void bind(Object var1) {
        if(var1 instanceof RecyclerSectionAdapter.Data) {
            bind(((RecyclerSectionAdapter.Data) var1).object);
        } else if(var1 instanceof InfoItem) {
            bind((InfoItem)var1);
        }
    }

    public void bind(InfoItem info) {
        this.label.setText(info.info_label);
        if(info.info_subtitle.length() > 0) {
            this.subdata.setVisibility(View.VISIBLE);
            this.subdata.setText(info.info_subtitle);
        } else {
            this.subdata.setVisibility(View.GONE);
        }
        this.data.setText(info.info_data);
    }
}
