package com.asedias.bugtracker.model.reportview;

import com.asedias.bugtracker.model.ReportItem;

/**
 * Created by rorom on 14.04.2018.
 */

public class TextItem extends ReportItem {

    public TextItem(String text) {
        this.title = text;
    }
}
