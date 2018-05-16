package com.asedias.bugtracker.model.reportview;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rorom on 14.04.2018.
 */

public class Photo {
    public int height;
    public int width;
    public String url_x;
    public String url_y;
    public String url_z;
    public String url_w;

    public Photo parsePhoto(String temp) {
        String json = temp.split(", ")[2];
        try {
            JSONObject obj = new JSONObject(json).getJSONObject("temp");
            String base = obj.getString("base");
            if(obj.has("x_")) {
                JSONArray x = obj.getJSONArray("x_");
                this.url_x = base + x.getString(0) + ".jpg";
                this.width = x.getInt(1);
                this.height = x.getInt(2);
                Log.e("bugtracker", "width " + this.width + "\n" + "height " + this.height);
            }
            if(obj.has("y_")) {
                JSONArray y = obj.getJSONArray("y_");
                this.url_y = base + y.getString(0) + ".jpg";
                this.width = y.getInt(1);
                this.height = y.getInt(2);
            }
            if(obj.has("z_")) {
                JSONArray z = obj.getJSONArray("z_");
                this.url_z = base + z.getString(0) + ".jpg";
            }
            if(obj.has("w_")) {
                JSONArray w = obj.getJSONArray("w_");
                this.url_w = base + w.getString(0) + ".jpg";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

}
