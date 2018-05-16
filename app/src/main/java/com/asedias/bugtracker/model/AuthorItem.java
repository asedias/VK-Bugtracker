package com.asedias.bugtracker.model;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by rorom on 14.04.2018.
 */

public class AuthorItem extends ReportItem {
    public AuthorItem(Element user) {
        Elements content = user.getElementsByClass("bt_report_one_author_content").get(0).getAllElements();
        this.title = content.get(1).html();
        this.subtitle = content.get(2).html();
        this.photo_url = user.getElementsByClass("bt_report_one_author__img").get(0).attr("src");
    }
}
