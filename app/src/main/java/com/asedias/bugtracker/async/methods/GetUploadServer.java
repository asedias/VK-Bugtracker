package com.asedias.bugtracker.async.methods;

import com.asedias.bugtracker.async.VKApiRequest;

import org.json.JSONObject;

/**
 * Created by rorom on 19.05.2018.
 */

public class GetUploadServer extends VKApiRequest<String> {

    public GetUploadServer() {
        super("docs.getUploadServer");
    }

    @Override
    protected String parse(JSONObject response) {
        return response.optJSONObject("response").optString("upload_url", "");
    }
}
