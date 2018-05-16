package com.asedias.bugtracker.model;

import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by rorom on 11.04.2018.
 */

public class Report {
    public String title;
    public String state;
    public String time;
    public ArrayList<String> tags = new ArrayList<>();
    public String id;
    public String hash;
    public boolean bookmarked = false;

    public Report(Element report) {
        this.title = report.getElementsByClass("bt_report_title_link").get(0).html();
        this.time = report.getElementsByClass("bt_report_info_details").get(0).html();
        String regex = "^.+\\(this,([0-9]+),'(\\w+)'\\).$";
        Element fav = report.getElementsByClass("bt_report_fav").get(0);
        this.bookmarked = fav.className().contains("checked");
        String onclick = fav.attr("onclick");
        this.hash = onclick.replaceAll(regex, "$2");
        if(this.time.contains("<")) {
            String[] temp = report.getElementsByClass("bt_report_info_details").get(0).html().split("\n");
            this.time = temp[0];
            this.time += " \u2027 ";
            this.time += report.getElementsByClass("bt_report_info_details").get(0).getAllElements().get(2).html();
        }
        this.id = report.attr("id").substring(9);
        this.state = report.getElementsByClass("bt_report_info__value").get(0).html();
        Elements tags = report.getElementsByClass("bt_report_tags").get(0).getElementsByClass("bt_tag_label");
        this.tags.clear();
        for(int i = 0; i < tags.size(); i++) {
            this.tags.add(tags.get(i).html());
        }

    }

    public Report(String title) {
        this.title = title;
    }
}
