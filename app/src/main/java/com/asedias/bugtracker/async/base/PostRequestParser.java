package com.asedias.bugtracker.async.base;

import android.util.Log;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by rorom on 23.05.2018.
 */

public class PostRequestParser {

    public long useless_code;               //<!--5699043821548
    public String js_classes;              //<!>bugtracker.css...
    public int language;                   //<!>0
    public int unknown_code_for_language;  //<!>6887
    public int error_code;                 //<!>3 | <!>
    public String hash;                    //<!>bf39cb0d31049f6f9b | <!>
    public Document html;                  //<!><div id="wrap2">... | <!>YnVndHJhY2tlcj9hY3Q9cmVwb3J0ZXImaWQ9MTk3NzcwODM1-->
    public String external_info;           //<!>window.pageSetTitleTimer...
    public JSONObject user_info;           //<!json>{"id": 86185582, ... }-->

    public PostRequestParser() {
        this.error_code = 0;
    }

    public PostRequestParser(String response) {
        String[] array = response.split("<!>");
        this.useless_code = Long.parseLong(array[0].replace("<!--", ""));
        this.js_classes = array[1];
        this.language = Integer.parseInt(array[2]);
        this.unknown_code_for_language = Integer.parseInt(array[3]);
        this.error_code = Integer.parseInt(array[4]);
        if(array.length > 6) {
            if (array[5].length() > 18) {
                this.html = Jsoup.parse(array[5]);
                this.external_info = array[6];
                try {
                    this.user_info = new JSONObject(array[7]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.hash = array[5];
                this.html = Jsoup.parse(array[6]);
                this.external_info = array[7];
            }
        } else if(array.length > 5){
            this.html = Jsoup.parse(array[5]);
        }
    }
    public void PrintAll() {
        Log.d("POST", "useless_code: " + this.useless_code + "\n" +
                                "language: " + this.language + "\n" +
                                "unknown_code_for_language: " + this.unknown_code_for_language + "\n" +
                                "error_code: " + this.error_code + "\n" +
                                "hash: " + this.hash + "\n" +
                                "external_info: " + this.external_info + "\n" +
                                "html: " + this.html + "\n" +
                                "user_info: " + this.user_info
        );
    }


}
