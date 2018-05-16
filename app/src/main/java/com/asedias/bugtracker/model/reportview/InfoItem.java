package com.asedias.bugtracker.model.reportview;

import com.asedias.bugtracker.model.ReportItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

/**
 * Created by rorom on 14.04.2018.
 */

public class InfoItem extends ReportItem {
    public String info_label = "";
    public String info_data = "";
    public String info_subtitle = "";
    public InfoItem(Element info) {
        this.info_label = info.getElementsByClass("bt_report_one_info_row_label").get(0).html();
        Element data = info.getElementsByClass("bt_report_one_info_row_value").get(0);
        if(data.html().contains("bugtracker_no_device")) {
            if (info.getElementsByClass("bugtracker_device user_device wide").size() > 0) {
                this.info_data = info.getElementsByClass("bugtracker_device_marketing_name").get(0).html();
                this.info_subtitle = info.getElementsByClass("bugtracker_device_model line_block").get(0).html();
            } else {
                this.info_data = info.getElementsByClass("bugtracker_no_device").get(0).html();
            }
        } else {
            this.info_data = Jsoup.clean(data.html(), Whitelist.simpleText());
        }
    }
}
