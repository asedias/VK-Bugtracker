package com.asedias.bugtracker.async.methods;

import android.app.Activity;

import com.asedias.bugtracker.async.DialogDocumentRequest;
import com.asedias.bugtracker.async.base.PostRequestParser;

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
    protected String parse(PostRequestParser doc) {
        String regex = ".+\\(([0-9][0-9]?),'(\\w+)'\\).+";
        return doc.html.toString().replace("\n", "").replaceAll(regex, "$2");
    }
}
