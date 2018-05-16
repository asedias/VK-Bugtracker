package com.asedias.bugtracker.model.reportview;

import com.asedias.bugtracker.model.ReportItem;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by rorom on 14.04.2018.
 */

public class PhotoList extends ReportItem {

    public ArrayList<Photo> photos = new ArrayList<>();

    public PhotoList() {}

    public PhotoList(Element html) {
        //page_post_sized_thumbs  clear_fix
        Elements photosl = html.getElementsByClass("page_post_thumb_wrap");
        for(int i = 0; i < photosl.size(); i++) {
            String temp = photosl.get(i).attr("onclick");
            Photo photo = new Photo().parsePhoto(temp);
            photos.add(photo);
        }
    }

    public int size() {
        return photos.size();
    }

}
