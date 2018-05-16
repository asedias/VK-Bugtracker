package com.asedias.bugtracker.model.reportview;

import android.util.Log;

import com.asedias.bugtracker.model.ReportItem;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rorom on 14.04.2018.
 */

public class CommentItem extends ReportItem {

    public String comment = "";
    public String meta;
    public PhotoList photos = new PhotoList();
    public List<DocItem> docs = new ArrayList<>();

    public CommentItem(Element comment) {
        this.title = comment.getElementsByClass("bt_report_cmt_author_a").get(0).html();
        this.comment = comment.getElementsByClass("bt_report_cmt_text").get(0).html();
        this.subtitle = comment.getElementsByClass("bt_report_cmt_date").get(0).html();
        this.photo_url = comment.getElementsByClass("bt_report_cmt_img").get(0).attr("src");
        if(this.photo_url.contains("images")) {
            this.photo_url = "https://vk.com" + this.photo_url;
        }
        Elements metas = comment.getElementsByClass("bt_report_cmt_meta_row");
        if(metas.size() > 0) {
            this.meta = metas.get(0).html();
        }
        Elements ph = comment.getElementsByClass("page_post_sized_thumbs  clear_fix");
        Elements docs = comment.getElementsByClass("page_doc_row");
        if (ph.size() > 0) {
            if (ph.size() > 0) this.photos = new PhotoList(ph.get(0));
            Log.d("COMMENT", "WITH PHOTOS " + ph.size());
        } else {
            Log.d("COMMENT", "WITHOUT PHOTOS");
        }
        if (comment.getElementsByClass("page_doc_row").size() > 0) {
            for (int i = 0; i < docs.size(); i++) {
                this.docs.add(new DocItem(new Attachment(docs.get(i))));
            }
            Log.d("COMMENT", "WITH DOCS " + docs.size());
        } else {
            Log.d("COMMENT", "WITHOUT DOCS");
        }
    }

}
