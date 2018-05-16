package com.asedias.bugtracker.model.reportview;

import org.jsoup.nodes.Element;

/**
 * Created by rorom on 14.04.2018.
 */

public class Attachment {
    public int type;
    public String title;
    public String subtitle;
    public final int TYPE_DOC = 0;
    public final int TYPE_TEXT = 1;
    public final int TYPE_VIDEO = 6;

    public Attachment(Element doc) {
        this.title = doc.getElementsByClass("page_doc_title").get(0).html();
        this.subtitle = doc.getElementsByClass("page_doc_size").get(0).html();
        String className = doc.getElementsByClass("page_doc_icon").get(0).className();
        this.type = Integer.parseInt(className.substring(className.length()-1));
    }
}
