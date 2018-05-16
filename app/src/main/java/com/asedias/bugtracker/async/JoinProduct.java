package com.asedias.bugtracker.async;

import android.app.Activity;

import com.asedias.bugtracker.async.base.DialogDocumentRequest;
import com.asedias.bugtracker.async.base.DocumentRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rorom on 12.05.2018.
 */

public class JoinProduct extends DialogDocumentRequest<String> {
    public JoinProduct(Activity context, String id, String hash) {
        super(context);
        this.param("act", "a_join_product");
        this.param("id", id);
        this.param("hash", hash);
    }

    @Override
    protected String parse(String doc) {
        String regex = ".+\\(([0-9][0-9]?),'(\\w+)'\\).+";
        return doc.replace("\n", "").replaceAll(regex, "$2");
    }
}
