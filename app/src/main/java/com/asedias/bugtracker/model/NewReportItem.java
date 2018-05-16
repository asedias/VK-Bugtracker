package com.asedias.bugtracker.model;

import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.view.View;

/**
 * Created by rorom on 18.04.2018.
 */

public class NewReportItem {
    public int TYPE;
    public Spannable title;
    public String subtitle;
    public String input_hint;
    public int img_state = View.VISIBLE;
    public int progress = View.VISIBLE;
    public boolean[] selected;
    public String input_data;
    public boolean switch_data = false;

    public CharSequence[] prodList = null;
    public CharSequence[] sevList = null;
    public CharSequence[] items = null;

    public NewReportItem(@LayoutRes int TYPE) {
        this.TYPE = TYPE;
    }

    public NewReportItem Icon(int id) {
        this.img_state = id;
        return this;
    }

    public NewReportItem Progress(int v) {
        this.progress = v;
        return this;
    }

    public NewReportItem List(CharSequence[] list) {
        this.items = list;
        return this;
    }

    public NewReportItem Selected(boolean[] bool) {
        this.selected = bool;
        return this;
    }


    public NewReportItem Title(Spannable title) {
        this.title = title;
        return this;
    }

    public NewReportItem Title(String title) {
        Title(Spannable.Factory.getInstance().newSpannable(title));
        return this;
    }

    public NewReportItem SubTitle(String sub) {
        this.subtitle = sub;
        return this;
    }

    public NewReportItem InputHint(String hint) {
        this.input_hint = hint;
        return this;
    }

}
