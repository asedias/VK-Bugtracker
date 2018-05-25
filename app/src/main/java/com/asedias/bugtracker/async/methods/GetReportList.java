package com.asedias.bugtracker.async.methods;

import com.asedias.bugtracker.async.DocumentRequest;
import com.asedias.bugtracker.async.base.PostRequestParser;
import com.asedias.bugtracker.model.Report;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rorom on 15.05.2018.
 */

public class GetReportList extends DocumentRequest<GetReportList.Result> {

    public int ud = 0;

    public GetReportList(String mid, int udate, boolean bookmarks) {
        super(true);
        this.ud = udate;
        if(bookmarks) {
            this.param("act", "bookmarks");
        }
        this.param("load", 1);
        this.param("max_udate", udate);
        this.param("mid", mid);
        if(Integer.valueOf(mid) != 0) {
            this.param("status", 100);
        }
    }

    @Override
    protected Result parse(PostRequestParser doc) {
        Result result = new Result();
        result.udate = Integer.parseInt(doc.external_info.replace("<!int>", ""));
        Document document = doc.html;
        Elements reports = document.getElementsByClass("bt_report_row");
        if(reports.size() > 0) {
            Elements reports_found = document.getElementsByClass("bt_reports_found");
            if(reports_found.size() > 0) {
                String num = reports_found.get(0).html().split("\n")[0];
                result.reports.add(RecyclerSectionAdapter.Data.top(0, new Report(num)));
            }
            if(this.ud > 0) {
                reports.remove(0);
            }
            for (int i = 0; i < reports.size(); i++) {
                if(i != reports.size() - 1) {
                    result.reports.add(RecyclerSectionAdapter.Data.top(1, new Report(reports.get(i))));
                } else {
                    result.reports.add(RecyclerSectionAdapter.Data.top(1, new Report(reports.get(i))));
                }
            }
        }
        result.more = result.udate > 0 && reports.size() > 0;
        return result;
    }

    public class Result {
        public List<RecyclerSectionAdapter.Data> reports = new ArrayList<>();
        public int udate;
        public boolean more;
    }
}
