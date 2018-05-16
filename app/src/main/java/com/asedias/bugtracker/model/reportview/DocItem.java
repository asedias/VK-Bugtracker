package com.asedias.bugtracker.model.reportview;

import com.asedias.bugtracker.model.ReportItem;

/**
 * Created by rorom on 14.04.2018.
 */

public class DocItem extends ReportItem {
    public Attachment doc;
    public DocItem(Attachment attachment) {
        this.doc = attachment;
    }
}
