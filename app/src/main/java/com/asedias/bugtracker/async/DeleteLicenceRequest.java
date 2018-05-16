package com.asedias.bugtracker.async;

import android.app.Activity;

import com.asedias.bugtracker.async.base.DialogDocumentRequest;
import com.asedias.bugtracker.async.base.DocumentRequest;

import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rorom on 12.05.2018.
 */

public class DeleteLicenceRequest extends DialogDocumentRequest<String> {
    public DeleteLicenceRequest(Activity context, String id, String hash) {
        super(context);
        this.param("act", "a_delete_licence_request");
        this.param("hash", hash);
        this.param("id", id);
    }

    @Override
    protected String parse(String doc) {
        Pattern p = Pattern.compile("<.+\\(this, [0-9][0-9]?, [0-9][0-9]?, '(\\w+)'\\).+>");
        Matcher m = p.matcher(doc);
        while (m.find()) {
            if(!m.group(1).contains("<")) return m.group(1);
        }
        return "NO HASH";
    }
}
