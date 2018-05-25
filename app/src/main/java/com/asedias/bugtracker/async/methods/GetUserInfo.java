package com.asedias.bugtracker.async.methods;

import com.asedias.bugtracker.UserData;
import com.asedias.bugtracker.async.DocumentRequest;
import com.asedias.bugtracker.async.base.PostRequestParser;
import com.asedias.bugtracker.model.ProfileItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by rorom on 17.05.2018.
 */

public class GetUserInfo extends DocumentRequest<GetUserInfo.Result> {

    public GetUserInfo(String id) {
        super(true);
        this.param("act", "reporter");
        this.param("id", id);
        this.param("al_id", UserData.getUID());
    }

    @Override
    protected Result parse(PostRequestParser doc) {
        Document document = doc.html;
        return new Result().fromClass(new ProfileItem().parseUser(document.getElementsByClass("bt_reporter_block").get(0)));
    }

    public class Result {
        public String name;
        public String photo;
        public String subtitle;

        public Result fromClass(ProfileItem item) {
            this.name = item.title;
            this.photo = item.photo_url;
            this.subtitle = item.subtitle;
            return this;
        }
    }

}
