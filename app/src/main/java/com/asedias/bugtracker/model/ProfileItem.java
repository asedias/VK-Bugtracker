package com.asedias.bugtracker.model;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

/**
 * Created by rorom on 11.04.2018.
 */

public class ProfileItem {
    public String photo_url;
    public String title;
    public String subtitle;
    public String id;
    public boolean card = false;

    public boolean isTitle() {
        if(photo_url.isEmpty() && subtitle.isEmpty()) return true;
        return false;
    }

    public ProfileItem Card() {
        card = true;
        return this;
    }

    public ProfileItem setTitle(String title) {
        this.title = title;
        this.subtitle = "";
        this.photo_url = "";
        return this;
    }

    public ProfileItem parseUser(Element user) {
        //user = document.getElementsByClass("bt_reporter_block").get(0)
        this.photo_url = user.getElementsByClass("bt_reporter_icon_img").get(0).attr("src");
        this.title = user.getElementsByClass("bt_reporter_name").get(0).getElementsByClass("mem_link").html();
        this.subtitle = Jsoup.clean(user.getElementsByClass("bt_reporter_content_block").get(0).html(), Whitelist.simpleText());
        return this;
    }

    public ProfileItem parseProduct(Element item) {
        //item = document.getElementsByClass("bt_reporter_product clear_fix").get(i)
        this.photo_url = "123";
        this.title = Jsoup.clean(item.getElementsByClass("bt_reporter_product_title").get(0).getAllElements().get(0).html(), Whitelist.simpleText());
        String reports = item.getElementsByClass("bt_reporter_product_nreports").get(0).html();
        if(Integer.parseInt(reports.split(" ")[0]) > 0) {
            this.subtitle = reports;
        } else {
            this.subtitle = "";
        }
        this.id = item.attr("id");
        return this;
    }

    public ProfileItem parseBigProduct(Element item) {
        this.photo_url = item.getElementsByClass("bt_prod_one_photo__img").get(0).attr("src");
        this.title = Jsoup.clean(item.getElementsByClass("bt_product_row_title").get(0).html(), Whitelist.simpleText());
        this.subtitle = "";
        this.id = item.attr("id");
        return this;
    }


}
