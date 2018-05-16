package com.asedias.bugtracker.async;

import android.app.Activity;

import com.asedias.bugtracker.async.base.DialogDocumentRequest;

/**
 * Created by rorom on 15.05.2018.
 */

public class AddBookmark extends DialogDocumentRequest {
    public AddBookmark(Activity activity, String id, String hash, boolean bookmarked) {
        super(activity);
        this.param("act", "a_subscribe");
        this.param("hash", hash);
        this.param("id", id);
        this.param("v", (bookmarked ? "0" : "1"));
    }
}
