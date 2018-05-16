package com.asedias.bugtracker.others;

import android.graphics.Color;
import android.text.Spannable;
import android.util.Log;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.R;
import com.asedias.bugtracker.model.NewReportItem;
import com.asedias.bugtracker.ui.TextColorSpan;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rorom on 19.04.2018.
 */

public class ReportSettings {
    public HashMap<Integer, Product> products = new HashMap<>();
    public ArrayList<ArrayItem> severity = new ArrayList<>();
    public Product[] productsArray;
    public boolean[] selectedTags;
    public boolean[] selectedPlatforms;
    public boolean[] selectedPlatformsAndroid;
    public boolean[] selectedPlatformsIOS;
    public String hash;
    public int confidential = 0;
    public int vulnerability = 0;

    public int currentID = 0;
    public int currentSeverity = 3;

    public static class Product {
        public Spannable name;
        public int id;
        public ArrayList<ArrayItem> tags = new ArrayList<>();
        public ArrayList<ArrayItem> devices = new ArrayList<>();
        public ArrayList<ArrayItem> platforms = new ArrayList<>();
        public ArrayList<ArrayItem> platforms_android = new ArrayList<>();
        public ArrayList<ArrayItem> platforms_ios = new ArrayList<>();

        public Product(Spannable name) {
            this.name = name;
        }
    }

    public static class ArrayItem {
        public int id;
        public String name;

        public ArrayItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public boolean FillSettings(AddReportInfo info) {
        try {
            JSONObject versionJson = new JSONObject(info.versions);
            JSONArray productJson = new JSONArray(info.products);
            JSONArray severityJson = new JSONArray(info.severity);
            JSONArray AndroidJson = new JSONArray(info.platform_v_android);
            JSONArray IOSJson = new JSONArray(info.platform_v_ios);
            JSONObject infoJson = new JSONObject(info.info);
            ArrayList<CharSequence> names = new ArrayList<>();
            for (int i = 0; i < productJson.length(); i++) {
                JSONArray temp = productJson.getJSONArray(i);
                int id = temp.getInt(0);
                String name = temp.getString(1);
                String version = versionJson.getString(id+"");
                Spannable titleSpan;
                if(version.length() > 0) {
                    titleSpan = Spannable.Factory.getInstance().newSpannable(String.format("%s %s", name, version));
                    titleSpan.setSpan(new TextColorSpan(Color.parseColor("#888888")), name.length(), titleSpan.length(), 0);
                } else {
                    titleSpan = Spannable.Factory.getInstance().newSpannable(name);
                }
                ReportSettings.Product product = new ReportSettings.Product(titleSpan);
                JSONObject inf = infoJson.getJSONObject(id+"");
                product.id = id;
                product.tags = ParseArray(inf.getJSONArray("tags"));
                product.devices = ParseArray(inf.getJSONArray("devices"));
                product.platforms = ParseArray(inf.getJSONArray("platforms"));
                product.platforms_ios = ParseArray(IOSJson);
                product.platforms_android = ParseArray(AndroidJson);
                ArrayList<ArrayItem> platforms = ParseArray(inf.getJSONArray("platforms_versions"));
                for(int i2 = 0; i2 < platforms.size(); i2++) {
                    ArrayItem item = platforms.get(i2);
                    if(item.id < 10000) {
                        product.platforms_ios.add(item);
                    } else {
                        product.platforms_android.add(item);
                    }
                }
                products.put(id, product);
                names.add(name);
            }
            severity = ParseArray(severityJson);
            productsArray = products.values().toArray(new ReportSettings.Product[products.size()]);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    public CharSequence[] GetProductNames() {
        CharSequence[] items = new CharSequence[productsArray.length];
        for(int i = 0; i < productsArray.length; i++) {
            items[i] = productsArray[i].name;
        }
        return items;
    }

    public CharSequence[] GetSeverityNames() {
        CharSequence[] items = new CharSequence[severity.size()];
        for(int i = 0; i < severity.size(); i++) {
            items[i] = severity.get(i).name;
        }
        return items;
    }

    public CharSequence[] GetProductPlatforms() {
        ReportSettings.Product product = products.get(currentID);
        CharSequence[] items = new CharSequence[product.platforms.size()];
        for(int i = 0; i < product.platforms.size(); i++) {
            items[i] = product.platforms.get(i).name;
        }
        this.selectedPlatforms = new boolean[product.platforms.size()];
        return items;
    }

    public CharSequence[] GetProductPlatformsAndroid() {
        ReportSettings.Product product = products.get(currentID);
        CharSequence[] items = new CharSequence[product.platforms_android.size()];
        for(int i = 0; i < product.platforms_android.size(); i++) {
            items[i] = product.platforms_android.get(i).name;
        }
        this.selectedPlatformsAndroid = new boolean[product.platforms_android.size()];
        return items;
    }

    public CharSequence[] GetProductPlatformsIOS() {
        ReportSettings.Product product = products.get(currentID);
        CharSequence[] items = new CharSequence[product.platforms_ios.size()];
        for(int i = 0; i < product.platforms_ios.size(); i++) {
            items[i] = product.platforms_ios.get(i).name;
        }
        this.selectedPlatformsIOS = new boolean[product.platforms_ios.size()];
        return items;
    }

    public CharSequence[] GetProductTags() {
        ReportSettings.Product product = products.get(currentID);
        CharSequence[] items = new CharSequence[product.tags.size()];
        for(int i = 0; i < product.tags.size(); i++) {
            items[i] = product.tags.get(i).name;
        }
        this.selectedTags = new boolean[product.tags.size()];
        return items;
    }


    public ArrayList<ReportSettings.ArrayItem> ParseArray(JSONArray array) throws JSONException {
        ArrayList<ReportSettings.ArrayItem> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONArray temp = array.getJSONArray(i);
            list.add(new ReportSettings.ArrayItem(temp.getInt(0), temp.getString(1)));
        }
        return list;
    }

    public HashMap<Boolean, String> getReportURL(ArrayList<RecyclerSectionAdapter.Data> list) {
        HashMap<Boolean, String> result = new HashMap<>();
        NewReportItem tags = (NewReportItem) list.get(list.size() - 4).object;
        NewReportItem android = (NewReportItem) list.get(list.size() - 6).object;
        NewReportItem ios = (NewReportItem) list.get(list.size() - 5).object;
        NewReportItem platforms = (NewReportItem) list.get(list.size() - 7).object;
        String title = ((NewReportItem) list.get(3).object).input_data;
        String description = ((NewReportItem) list.get(4).object).input_data;
        if(title == null) {
            result.put(false, BugTrackerApp.String(R.string.error_title));
        }
        if(description == null) {
            result.put(false, BugTrackerApp.String(R.string.error_desc));
        }
        boolean confidential = ((NewReportItem) list.get(5).object).switch_data;
        boolean vulnerability = ((NewReportItem) list.get(list.size() - 3).object).switch_data;
        //toggle('bt_form_version_block', version != '');
        //toggle('bt_form_phone_block', productId == 7 || productId == 8);
        //toggle('bt_form_region_block', productId == 7 || productId == 8);
        ReportSettings.Product product = products.get(currentID);
        String temp = "https://vk.com/al_bugtracker.php?act=a_save&al=0&box=0&id=&region_id=0&phone=&hash="+hash;
        temp += "&title="+title;
        temp += "&descr="+description;
        temp += "&product="+currentID;
        temp += "&confidential=" + (confidential ? "1" : "0");
        temp += "&vulnerability=" + (vulnerability ? "1" : "0");
        temp += "&tags=";
        for (int i = 0; i < tags.items.length; i++) {
            if (tags.selected[i]) {
                temp += product.tags.get(i).id + ",";
            }
        }
        if(temp.substring(temp.length()-1).contains(",")) {
            temp = temp.substring(0, temp.length()-1);
        }
        temp += "&platforms=";
        for (int i = 0; i < platforms.items.length; i++) {
            if(platforms.selected[i]) {
                temp += product.platforms.get(i).id + ",";
            }
        }
        if(temp.substring(temp.length()-1).contains(",")) {
            temp = temp.substring(0, temp.length()-1);
        }
        temp += "&platforms_versions=";
        if(android.items != null) for (int i = 0; i < android.items.length; i++) {
            if (android.selected[i]) {
                temp += product.platforms_android.get(i).id + ",";
            }
        }
        if(ios.items != null) for (int i = 0; i < ios.items.length; i++) {
            if (ios.selected[i]) {
                temp += product.platforms_ios.get(i).id + ",";
            }
        }
        if(temp.substring(temp.length()-1).contains(",")) {
            temp = temp.substring(0, temp.length()-1);
        }
        temp += "&severity="+currentSeverity;
        result.put(true, temp);
        return result;
    }

    public static class AddReportInfo {
        public String products;
        public String info;
        public String severity;
        public String versions;
        public String platform_v_android;
        public String platform_v_ios;

        public void PrintAll() {
            Log.d("INFORMATION", products.substring(0, 20) //Array
                    + "\n" + versions.substring(0, 20) //Object
                    + "\n" + info.substring(0, 20) //Object
                    + "\n" + platform_v_android.substring(0, 20) //Array
                    + "\n" + platform_v_ios.substring(0, 20) //Array
                    + "\n" + severity.substring(0, 20) //Array
            );
        }

    }


}
