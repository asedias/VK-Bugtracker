package com.asedias.bugtracker.async.base;

import android.util.Log;

import com.asedias.bugtracker.UserData;
import com.asedias.bugtracker.async.base.DocumentRequest;

import org.jsoup.nodes.Document;

/**
 * Created by rorom on 16.05.2018.
 */

public class GetLoginQHash extends DocumentRequest<String> {
    public GetLoginQHash(String ip_h, String to) {
        //super("https://login.vk.com/?_origin=https%3A%2F%2Fvk.com&ip_h="+ip_h+"&role=al_frame&to="+to);
        //h=1; s=1; l=86185582;
        super(true);
        this.mTask.cookie("h", "1");
        this.mTask.cookie("s", "1");
        this.mTask.cookie("l", UserData.getUID());
    }

    @Override
    protected String parse(String doc) {
        Log.d("cookie", "NEW: " + this.mTask.response.headers());
        return "";
    }
}
