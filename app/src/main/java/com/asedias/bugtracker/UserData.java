package com.asedias.bugtracker;

import android.content.SharedPreferences;

/**
 * Created by rorom on 17.04.2018.
 */

public class UserData {

    private static int uid;
    private static String accessToken;
    private static String name;
    private static String photo;
    private static String subtitle;
    private static SharedPreferences preferences = BugTrackerApp.context.getSharedPreferences("user", 0);

    public UserData() {
        accessToken = preferences.getString("access_token", "");
        uid = Integer.parseInt(preferences.getString("user_id", "0"));
        name = preferences.getString("user_name", "");
        photo = preferences.getString("user_photo", "https://vk.com/images/camera_200.png");
        subtitle = preferences.getString("user_subtitle", "");
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static String getUID() {
        return String.valueOf(uid);
    }

    public static String getName() {
        return name;
    }

    public static String getPhoto() {
        return photo;
    }

    public static String getSubtitle() {
        return subtitle;
    }

}
