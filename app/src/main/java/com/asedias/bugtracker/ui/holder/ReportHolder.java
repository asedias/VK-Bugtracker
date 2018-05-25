package com.asedias.bugtracker.ui.holder;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.FragmentWrapperActivity;
import com.asedias.bugtracker.R;
import com.asedias.bugtracker.async.methods.AddBookmark;
import com.asedias.bugtracker.async.DocumentRequest;
import com.asedias.bugtracker.fragments.ReportFragment;
import com.asedias.bugtracker.model.Report;
import com.asedias.bugtracker.others.FlowLayout;
import com.asedias.bugtracker.ui.Fonts;
import com.vkontakte.android.ui.holder.RecyclerHolder;

import java.util.ArrayList;

import static com.asedias.bugtracker.ui.ThemeManager.currentTextColor;

/**
 * Created by rorom on 11.04.2018.
 */
public class ReportHolder extends RecyclerHolder<Report> {

    public TextView mTitle;
    public TextView mTime;
    public TextView mState;
    public FlowLayout mTagsLayout;
    public ImageView mBookmark;
    private LayoutInflater mInflater;
    private boolean expanded = false;
    private FlowLayout.LayoutParams mTagLP = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private DisplayMetrics mMetrics = new DisplayMetrics();
    private int startPosition = 0;

    public ReportHolder(View itemView, LayoutInflater inflater) {
        super(itemView);
        this.mInflater = inflater;
        this.mBookmark = itemView.findViewById(R.id.bookmark);
        this.mTitle = (TextView) itemView.findViewById(R.id.title);
        this.mTime = (TextView) itemView.findViewById(R.id.time);
        this.mState = (TextView) itemView.findViewById(R.id.state);
        this.mTagsLayout = itemView.findViewById(R.id.tags_layout);
        mTagLP.horizontal_spacing = (int)BugTrackerApp.dp(6);
        mTagLP.vertical_spacing = (int)BugTrackerApp.dp(6);
    }

    public void expandTags(ImageView v, ArrayList<String> tags) {
        if(!expanded) {
            for (int i = this.startPosition; i < tags.size(); i++) {
                TextView temp = (TextView) mInflater.inflate(R.layout.tag_item, null);
                temp.setText(tags.get(i));
                temp.setTypeface(Fonts.Regular);
                this.mTagsLayout.addView(temp, this.mTagsLayout.getChildCount()-1, mTagLP);
                v.setImageResource(R.drawable.ic_dots_expand);
                v.setRotation(180);
            }
        } else {
            v.setImageResource(R.drawable.ic_dots_expand);
            this.mTagsLayout.removeViews(this.startPosition, tags.size()-this.startPosition);
            v.setRotation(0);
        }
        expanded = !expanded;
    }

    public void bind(final Report report) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.mTitle.setText(Html.fromHtml(report.title, Html.FROM_HTML_MODE_LEGACY));
        } else {
            this.mTitle.setText(Html.fromHtml(report.title));
        }
        this.mTitle.setTextColor(currentTextColor);
        this.mTime.setText(report.time);
        this.mState.setText(report.state);
        this.mBookmark.setImageResource(report.bookmarked ? R.drawable.ic_report_bookmark_checked : R.drawable.ic_report_bookmark);
        this.mTagsLayout.removeAllViews();
        float more_width = BugTrackerApp.dp(41);
        float textPadding = BugTrackerApp.dp(24);
        float fullwidth = 0;
        expanded = false;
        //16+16+41+6 = 79
        float width = BugTrackerApp.mMetrics.widthPixels - BugTrackerApp.dp(85);
        for(int i = 0; i < report.tags.size(); i++) {
            final TextView temp = (TextView) mInflater.inflate(R.layout.tag_item, null);
            fullwidth += temp.getPaint().measureText(report.tags.get(i)) + textPadding;
            if(fullwidth < width) {
                temp.setText(report.tags.get(i));
                temp.setTypeface(Fonts.Regular);
                this.mTagsLayout.addView(temp, mTagLP);
            } else {
                this.startPosition = i;
                ImageView imageView = new ImageView(itemView.getContext());
                imageView.setImageResource(R.drawable.ic_dots_expand);
                imageView.setBackgroundResource(R.drawable.background_tag);
                imageView.setOnClickListener(v -> expandTags((ImageView)v, report.tags));
                FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams();
                lp.height = (int) BugTrackerApp.dp(28);
                lp.width = (int) more_width;
                this.mTagsLayout.addView(imageView, lp);
                break;
            }

        }

        this.mBookmark.setOnClickListener(v -> new AddBookmark((Activity)itemView.getContext(), report.id, report.hash, report.bookmarked).setCallback(new DocumentRequest.RequestCallback() {
            @Override
            public void onUIThread() {
                report.bookmarked = !report.bookmarked;
                mBookmark.setImageResource(report.bookmarked ? R.drawable.ic_report_bookmark_checked : R.drawable.ic_report_bookmark);
            }
        }).run());

        itemView.setOnClickListener(view -> {
            Bundle args = new Bundle();
            args.putString("id", report.id);
            FragmentWrapperActivity.startWithFragment((Activity)itemView.getContext(), new ReportFragment(), args);
            //((MainActivity)itemView.getContext()).mNavigator.go(new ReportFragment(), args);
        });
    }

}
