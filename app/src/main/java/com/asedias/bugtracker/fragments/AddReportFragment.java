package com.asedias.bugtracker.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.FragmentWrapperActivity;
import com.asedias.bugtracker.R;
import com.asedias.bugtracker.async.base.Callback;
import com.asedias.bugtracker.async.base.GetDocument;
import com.asedias.bugtracker.async.base.PostRequestParser;
import com.asedias.bugtracker.ui.adapter.AddReportAdapter;
import com.asedias.bugtracker.model.NewReportItem;
import com.asedias.bugtracker.others.ReportSettings;
import com.vkontakte.android.fragments.CardRecyclerFragment;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import static com.asedias.bugtracker.ui.adapter.AddReportAdapter.TYPE_BACKGROUND;
import static com.asedias.bugtracker.ui.adapter.AddReportAdapter.TYPE_HEADER;
import static com.asedias.bugtracker.ui.adapter.AddReportAdapter.TYPE_INPUT;
import static com.asedias.bugtracker.ui.adapter.AddReportAdapter.TYPE_PRODUCT;
import static com.asedias.bugtracker.ui.adapter.AddReportAdapter.TYPE_SEVERITY;
import static com.asedias.bugtracker.ui.adapter.AddReportAdapter.TYPE_SWITCH;
import static com.asedias.bugtracker.ui.adapter.AddReportAdapter.TYPE_TAGS;

/**
 * Created by rorom on 24.04.2018.
 */

public class AddReportFragment extends CardRecyclerFragment {

    private static AddReportAdapter mAdapter;
    public static ReportSettings mSettings = new ReportSettings();

    public AddReportFragment() {
        super(10);
    }

    @Override
    public void onAttach(Activity var1) {
        super.onAttach(var1);
        this.setHasOptionsMenu(true);
        this.setRefreshEnabled(false);
        this.setTitle(R.string.addr_title);
        if(!loaded) loadData();
    }

    @Override
    protected void doLoadData(int var1, int var2) {
        //this.mTask = new GetDocument("https://vk.com/bugtracker?al=1&act=add&product=" + mSettings.currentID).setCallback(getCallback());
        //((GetDocument)this.mTask).execute();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        if(mAdapter == null) {
            mAdapter = new AddReportAdapter(getActivity());
        }
        mAdapter.context = getActivity();
        return mAdapter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, R.id.add, 0, android.R.string.ok);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT|MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add: {
                HashMap<Boolean, String> result = mSettings.getReportURL(mAdapter.mItems);
                final ProgressDialog dialog = ProgressDialog.show(getActivity(), BugTrackerApp.String(R.string.loading), BugTrackerApp.String(R.string.loading));
                new GetDocument(result.get(true)).setCallback(new Callback<PostRequestParser>() {
                    String id;
                    @Override
                    public void onBackground(PostRequestParser document) {
                        Pattern p = Pattern.compile(".+id=(\\d+)");
                        //Log.d("REPORT URI", document.baseUri());
                        //id = p.matcher(document.baseUri()).toMatchResult().group(1);
                    }

                    @Override
                    public void onUIThread() {
                        dialog.cancel();
                        Bundle args = new Bundle();
                        args.putString("id", id);
                        FragmentWrapperActivity.startWithFragment(getActivity(), new ReportFragment(), args);
                        getActivity().finish();
                    }
                }).execute();
            }
        }
        return false;
    }

    protected Callback getCallback() {
        return new Callback<Document>() {
            @Override
            public void onBackground(Document document) {
                String temp = document.html().split("<!")[8];
                String[] t = temp.split("\n");
                ReportSettings.AddReportInfo info = new ReportSettings.AddReportInfo();
                for(int i = 0; i < t.length; i++) {
                    if(t[i].contains("cur['btFormDDValues']=")) {
                        info.info = t[i].substring("cur['btFormDDValues']=".length());
                        //Log.d("btFormDDValues", t[i].substring("cur['btFormDDValues']=".length()));
                    } else if(t[i].contains("cur.newBugProductDD = ") && t[i].length() > "cur.newBugProductDD = null;".length()) {
                        info.products = t[i].split(", ")[1];
                        //Log.d("newBugProductDD", t[i].split(", ")[1]);
                    } else if(t[i].contains("cur['btPlatformsVersionsIOS']=")) {
                        info.platform_v_ios = t[i].substring("cur['btPlatformsVersionsIOS']=".length());
                        //Log.d("btPlatformsVersionsIOS", t[i].substring("cur['btPlatformsVersionsIOS']=".length()));
                    } else if(t[i].contains("cur['btPlatformsVersionsAndroid']=")) {
                        info.platform_v_android = t[i].substring("cur['btPlatformsVersionsAndroid']=".length());
                        //Log.d("btPlatformsVersionsAnd", t[i].substring("cur['btPlatformsVersionsAndroid']=".length()));
                    } else if(t[i].contains("cur['btFormProductsVersions']=")) {
                        info.versions = t[i].substring("cur['btFormProductsVersions']=".length());
                        //Log.d("btFormProductsVersions", t[i].substring("cur['btFormProductsVersions']=".length()));
                    } else if(t[i].contains("cur.newBugSeverityDD = ")) {
                        info.severity = t[i].split(", ")[1];
                        //Log.d("cur.newBugSeverityDD", t[i].split(", ")[1]);
                    } else if(t[i].contains("cur['newProductId']=")) {
                        mSettings.currentID = Integer.parseInt(t[i].replace("\"", "").replace(";", "").split("=")[1]);
                    } else if(t[i].contains("cur.newBugBoxSave = BugTracker.saveNewBug.pbind")) {
                        //cur.newBugBoxSave = BugTracker.saveNewBug.pbind('', 'a71d4b45e7a5b680be');
                        mSettings.hash =
                                t[i].substring("cur.newBugBoxSave = BugTracker.saveNewBug.pbind(".length())
                                        .split(", ")[1]
                                        .replace("'", "")
                                        .replace(");", "");
                        //Log.d("hash", mSettings.hash);
                    }
                }
                mSettings.FillSettings(info);
            }

            @Override
            public void onUIThread() {
                super.onUIThread();
                ArrayList arrayList = createList();
                mAdapter.mItems.clear();
                mAdapter.mItems.addAll(arrayList);
                onDataLoaded(arrayList, false);
                //Log.d("aaa", "loaded");
            }
        };
    }
    
    private ArrayList<RecyclerSectionAdapter.Data> createList() {
        ArrayList<RecyclerSectionAdapter.Data> info = new ArrayList<>();

        //info.add(RecyclerSectionAdapter.Data.bottom(TYPE_BACKGROUND, R.drawable.card_top_fix_item));
        /*0*/info.add(RecyclerSectionAdapter.Data.top(TYPE_HEADER, new NewReportItem(TYPE_HEADER).Title(BugTrackerApp.String(R.string.addr_product))));
        /*1*/info.add(RecyclerSectionAdapter.Data.bottom(TYPE_PRODUCT, new NewReportItem(TYPE_PRODUCT).Progress(View.GONE).List(mSettings.GetProductNames()).Title(mSettings.products.get(mSettings.currentID).name)));

        /*2*/info.add(RecyclerSectionAdapter.Data.top(TYPE_HEADER, new NewReportItem(TYPE_HEADER).Title(BugTrackerApp.String(R.string.general_info))));
        /*3*/info.add(RecyclerSectionAdapter.Data.middle(TYPE_INPUT, new NewReportItem(TYPE_INPUT).InputHint(BugTrackerApp.String(R.string.title)).Icon(View.GONE)));
        /*4*/info.add(RecyclerSectionAdapter.Data.middle(TYPE_INPUT, new NewReportItem(TYPE_INPUT).InputHint(BugTrackerApp.String(R.string.description))));
        /*5*/info.add(RecyclerSectionAdapter.Data.middle(TYPE_SWITCH, new NewReportItem(TYPE_SWITCH).Title(BugTrackerApp.String(R.string.addr_hide_doc))));
        /*6*/info.add(RecyclerSectionAdapter.Data.bottom(TYPE_BACKGROUND, R.drawable.card_margin_fix_item));

        /*7*/info.add(RecyclerSectionAdapter.Data.top(TYPE_HEADER, new NewReportItem(TYPE_HEADER).Title(BugTrackerApp.String(R.string.additional_info))));
        /*8*/info.add(RecyclerSectionAdapter.Data.middle(TYPE_TAGS, new NewReportItem(TYPE_TAGS).Title(BugTrackerApp.String(R.string.addr_platforms)).List(mSettings.GetProductPlatforms()).Selected(mSettings.selectedPlatforms)));
        /*9*/info.add(RecyclerSectionAdapter.Data.middle(TYPE_TAGS, new NewReportItem(TYPE_TAGS).Title(BugTrackerApp.String(R.string.addr_android_versions)).List(null)));
        /*10*/info.add(RecyclerSectionAdapter.Data.middle(TYPE_TAGS, new NewReportItem(TYPE_TAGS).Title(BugTrackerApp.String(R.string.addr_ios_versions)).List(null)));
        /*11*/info.add(RecyclerSectionAdapter.Data.middle(TYPE_TAGS, new NewReportItem(TYPE_TAGS).Title(BugTrackerApp.String(R.string.addr_tags)).List(mSettings.GetProductTags()).Selected(mSettings.selectedTags)));
        /*12*/info.add(RecyclerSectionAdapter.Data.middle(TYPE_SWITCH, new NewReportItem(TYPE_SWITCH).Title(BugTrackerApp.String(R.string.addr_vulnerability))));
        /*13*/info.add(RecyclerSectionAdapter.Data.middle(TYPE_SEVERITY, new NewReportItem(TYPE_SEVERITY).List(mSettings.GetSeverityNames()).Title(BugTrackerApp.String(R.string.addr_severity))));
        /*14*/info.add(RecyclerSectionAdapter.Data.bottom(TYPE_BACKGROUND, R.drawable.card_margin_fix_item));
        return info;
    }

    public static void UpdateInfo() {
        ((NewReportItem)mAdapter.mItems.get(mAdapter.getItemCount()-7).object).List(mSettings.GetProductPlatforms()).Selected(mSettings.selectedPlatforms);
        mAdapter.notifyItemChanged(mAdapter.getItemCount()-7);

        ((NewReportItem)mAdapter.mItems.get(mAdapter.getItemCount()-6).object).List(null).Selected(mSettings.selectedPlatformsAndroid);
        mAdapter.notifyItemChanged(mAdapter.getItemCount()-6);

        ((NewReportItem)mAdapter.mItems.get(mAdapter.getItemCount()-5).object).List(null).Selected(mSettings.selectedPlatformsIOS);
        mAdapter.notifyItemChanged(mAdapter.getItemCount()-5);

        ((NewReportItem)mAdapter.mItems.get(mAdapter.getItemCount()-4).object).List(mSettings.GetProductTags()).Selected(mSettings.selectedTags);
        mAdapter.notifyItemChanged(mAdapter.getItemCount()-4);

    }

    public static void setPlatformList(int platform, boolean selected) {
        switch (platform) {
            case 4: { //Android
                ((NewReportItem)mAdapter.mItems.get(mAdapter.getItemCount()-6).object).List(selected ? mSettings.GetProductPlatformsAndroid() : null).Selected(mSettings.selectedPlatformsAndroid);
                mAdapter.notifyItemChanged(mAdapter.getItemCount()-6);
                break;
            }
            case 3: { //IOS
                ((NewReportItem)mAdapter.mItems.get(mAdapter.getItemCount()-5).object).List(selected ? mSettings.GetProductPlatformsIOS() : null).Selected(mSettings.selectedPlatformsIOS);
                mAdapter.notifyItemChanged(mAdapter.getItemCount()-5);
            }
        }
    }

    public static void setPlatformList(String platform) {
        switch (platform.hashCode()) {
            case 803262031: { //"Android".hashCode()
                ((NewReportItem)mAdapter.mItems.get(mAdapter.getItemCount()-6).object).List(mSettings.GetProductPlatformsAndroid()).Selected(mSettings.selectedPlatformsAndroid);
                //mAdapter.notifyItemChanged(mAdapter.getItemCount()-6);
                break;
            }
            case 103437: { //"iOS".hashCode()
                ((NewReportItem)mAdapter.mItems.get(mAdapter.getItemCount()-5).object).List(mSettings.GetProductPlatformsIOS()).Selected(mSettings.selectedPlatformsIOS);
                //mAdapter.notifyItemChanged(mAdapter.getItemCount()-5);
            }
        }
    }


}
