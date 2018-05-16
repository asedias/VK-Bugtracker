package com.asedias.bugtracker.async;

import com.asedias.bugtracker.async.base.DocumentRequest;
import com.asedias.bugtracker.model.AuthorItem;
import com.asedias.bugtracker.model.reportview.Attachment;
import com.asedias.bugtracker.model.reportview.BigTextItem;
import com.asedias.bugtracker.model.reportview.CommentItem;
import com.asedias.bugtracker.model.reportview.DocItem;
import com.asedias.bugtracker.model.reportview.InfoItem;
import com.asedias.bugtracker.model.reportview.PhotoList;
import com.asedias.bugtracker.model.reportview.TextItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rorom on 11.05.2018.
 */

public class GetReport extends DocumentRequest<GetReport.Result> {
    public GetReport(String id) {
        //super("act=show&al=1&id=" + id);
        super(false);
        this.param("act", "show");
        this.param("id", id);
    }

    @Override
    protected GetReport.Result parse(String doc) {
        Document document = Jsoup.parse(doc);
        GetReport.Result result = new GetReport.Result();
        Elements ui_crumbs = document.getElementsByClass("ui_crumb");
        Element report = document.getElementsByClass("bt_report_one").get(0);
        Elements info = report.getElementsByClass("bt_report_one_info_row clear_fix");
        Elements comments = document.getElementsByClass("bt_report_cmt");

        result.name = ui_crumbs.get(1).html();
        result.author = new AuthorItem(report.getElementsByClass("bt_report_one_author clear_fix").get(0));
        result.title = new BigTextItem(report.getElementsByClass("bt_report_one_title").html());
        result.description = new TextItem(report.getElementsByClass("bt_report_one_descr").get(0).html());
        if(!report.getElementsByClass("bt_report_one_attachs").get(0).html().isEmpty()) {
            Elements ph = report.getElementsByClass("page_post_sized_thumbs  clear_fix");
            Elements docs = report.getElementsByClass("page_doc_row");
            if(ph.size() > 0) result.photos = new PhotoList(ph.get(0));
            for (int i = 0; i < docs.size(); i++) {
                result.docs.add(new DocItem(new Attachment(docs.get(i))));
            }
        }
        for(int i = 0; i < info.size(); i++) {
            result.information.add(new InfoItem(info.get(i)));
        }
        for(int i = 0; i < comments.size(); i++) {
            result.comments.add(new CommentItem(comments.get(i)));
        }
        return result;
    }

    public class Result {
        public String name;
        public AuthorItem author;
        public BigTextItem title;
        public TextItem description;
        public PhotoList photos = new PhotoList();
        public List<DocItem> docs = new ArrayList<>();
        public List<InfoItem> information = new ArrayList<>();
        public List<CommentItem> comments = new ArrayList<>();
    }

}
