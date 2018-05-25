package com.asedias.bugtracker.async.methods;

import android.app.Activity;

import com.asedias.bugtracker.async.DialogDocumentRequest;
import com.asedias.bugtracker.async.base.PostRequestParser;
import com.asedias.bugtracker.model.reportview.CommentItem;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rorom on 17.05.2018.
 */

public class SendComment extends DialogDocumentRequest<List<RecyclerSectionAdapter.Data>> {
    public SendComment(Activity activity, String id, String hash, String message) {
        super(activity);
        this.param("act", "a_send_comment");
        this.param("report_id", id);
        this.param("message", message);
        this.param("hidden", 0);
        this.param("hash", hash);
    }

    @Override
    protected List<RecyclerSectionAdapter.Data> parse(PostRequestParser doc) {
        List<RecyclerSectionAdapter.Data> list = new ArrayList<>();
        Document document = doc.html;
        Elements comments = document.select(".bt_report_cmt");
        for(int i = 0; i < comments.size(); i++) {
            list.add(RecyclerSectionAdapter.Data.middle(7, new CommentItem(comments.get(i))));
        }
        return list;
    }
}
