package com.asedias.bugtracker.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.asedias.bugtracker.model.ProfileItem;
import com.asedias.bugtracker.model.Report;
import com.asedias.bugtracker.model.ReportItem;
import com.asedias.bugtracker.ui.ThemeManager;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

/**
 * Created by rorom on 11.04.2018.
 */
public class TitleHolder extends RecyclerHolder {

    public TitleHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object var1) {
        if(var1 instanceof Report) {
            bind((Report)var1);
        }
        if(var1 instanceof String) {
            bind((String)var1);
        }
        if(var1 instanceof ReportItem) {
            bind(((ReportItem) var1).title);
        }
        if(var1 instanceof RecyclerSectionAdapter.Data) {
            bind(((RecyclerSectionAdapter.Data) var1).object);
        }
    }

    public void bind(Report text) {
        setText(text.title);
    }

    public void bind(ProfileItem text) {
        setText(text.title);
    }

    public void bind(String text) {
        setText(text);
    }

    public void setText(String text) {
        ((TextView) itemView).setText(text);
        ((TextView) itemView).setTextColor(ThemeManager.currentTextColor);
    }
}
