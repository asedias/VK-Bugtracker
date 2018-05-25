package com.asedias.bugtracker.async.methods;

import android.util.Log;

import com.asedias.bugtracker.async.DocumentRequest;
import com.asedias.bugtracker.async.base.PostRequestParser;
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
import java.util.regex.Pattern;

/**
 * Created by rorom on 11.05.2018.
 */

public class GetReport extends DocumentRequest<GetReport.Result> {
    public GetReport(String id) {
        //super("act=show&al=1&id=" + id);
        super(true);
        this.param("act", "show");
        this.param("id", id);
    }

    @Override
    protected GetReport.Result parse(PostRequestParser doc) {
        Log.d("URL", this.mTask.request.url().toString());
        Document document = doc.html;
        GetReport.Result result = new GetReport.Result();
        Elements ui_crumbs = document.select(".ui_crumb");
        Element report = document.select(".bt_report_one").get(0);
        Elements info = report.select(".bt_report_one_info_row");
        Elements comments = document.select(".bt_report_cmt");

        result.name = ui_crumbs.get(1).html();
        result.author = new AuthorItem(report.select(".bt_report_one_author").get(0));
        result.title = new BigTextItem(report.select(".bt_report_one_title").html());
        result.description = new TextItem(report.select(".bt_report_one_descr").get(0).html());
        if(!report.select(".bt_report_one_attachs").get(0).html().isEmpty()) {
            Elements ph = report.select(".page_post_sized_thumbs");
            Elements docs = report.select(".page_doc_row");
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
        result.comment_hash = Pattern.compile(".*bugreportHash = '(\\w+)';.*" , Pattern.DOTALL).matcher(doc.external_info).replaceAll("$1");
        Log.d("REPORT", result.comment_hash);
        return result;
    }

    public class Result {
        public String name;
        public AuthorItem author;
        public BigTextItem title;
        public String comment_hash;
        public TextItem description;
        public PhotoList photos = new PhotoList();
        public List<DocItem> docs = new ArrayList<>();
        public List<InfoItem> information = new ArrayList<>();
        public List<CommentItem> comments = new ArrayList<>();
    }

}
