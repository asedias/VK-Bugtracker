package com.asedias.bugtracker.ui.holder;

import android.app.Activity;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.R;
import com.asedias.bugtracker.model.AuthorItem;
import com.asedias.bugtracker.model.reportview.CommentItem;
import com.asedias.bugtracker.model.ProfileItem;
import com.asedias.bugtracker.model.ReportItem;
import com.asedias.bugtracker.others.ImageGridParser;
import com.asedias.bugtracker.ui.CropCircleTransformation;
import com.squareup.picasso.Picasso;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

/**
 * Created by rorom on 11.04.2018.
 */

public class ProfileProductHolder extends RecyclerHolder {

    public TextView name;
    public ImageView photo;
    public TextView subtitle;
    public TextView meta;
    public TextView comment;
    public LinearLayout attachments;
    public boolean header = false;
    public boolean author = false;

    public ProfileProductHolder(View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.title);
        this.photo = itemView.findViewById(R.id.icon);
        this.subtitle = itemView.findViewById(R.id.subtitle);
        this.comment = itemView.findViewById(R.id.comment);
        this.meta = itemView.findViewById(R.id.meta);
        this.attachments = itemView.findViewById(R.id.attachments);
    }

    @Override
    public void bind(Object var1) {
        if(var1 instanceof ProfileItem) {
            bind((ProfileItem)var1);
        } else if(var1 instanceof CommentItem) {
            bindComment((CommentItem) var1);
        } else if(var1 instanceof AuthorItem) {
            bindAuthor((AuthorItem) var1);
        } else if(var1 instanceof ReportItem) {
            bind((ReportItem)var1);
        } else if(var1 instanceof RecyclerSectionAdapter.Data) {
            bind(((RecyclerSectionAdapter.Data) var1).object);
        }
    }

    public void bind(ProfileItem item) {
        this.name.setText(item.title);
        this.subtitle.setText(item.subtitle);
        Picasso.with(BugTrackerApp.context).load(item.photo_url).placeholder(BugTrackerApp.Drawable(R.drawable.placeholder_user)).transform(new CropCircleTransformation()).into(this.photo);
    }

    public ProfileProductHolder setHeader() {
        this.header = true;
        return this;
    }

    public ProfileProductHolder bind(ReportItem item) {
        this.name.setText(item.title);
        this.subtitle.setText(item.subtitle);
        if(this.comment != null) {
            this.comment.setVisibility(View.GONE);
            this.meta.setVisibility(View.GONE);
        }
        Picasso.with(BugTrackerApp.context).load(item.photo_url).placeholder(BugTrackerApp.Drawable(R.drawable.placeholder_user)).transform(new CropCircleTransformation()).into(this.photo);
        return this;
    }

    public ProfileProductHolder bindAuthor(ReportItem item) {
        author = true;
        this.name.setText(item.title);
        this.subtitle.setText(item.subtitle);
        Picasso.with(BugTrackerApp.context).load(item.photo_url).placeholder(BugTrackerApp.Drawable(R.drawable.placeholder_user)).transform(new CropCircleTransformation()).into(this.photo);
        return this;
    }


    public ProfileProductHolder bindComment(CommentItem item) {
        this.name.setText(item.title);
        this.subtitle.setText(item.subtitle);
        attachments.removeAllViews();
        if(item.photos.size() > 0) {
            int width = (int) (BugTrackerApp.mMetrics.widthPixels - BugTrackerApp.dp(16+72));
            new ImageGridParser((Activity)itemView.getContext(), item.photos.photos, attachments, width);
        }
        if(item.docs.size() > 0) {
            for(int i = 0; i < item.docs.size(); i++) {
                View doc = View.inflate((Activity)itemView.getContext(), R.layout.report_doc, attachments);
                new AttachmentHolder(doc).bind(item.docs.get(0));
            }
        }

        if(!item.comment.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.comment.setText(Html.fromHtml(item.comment, Html.FROM_HTML_MODE_COMPACT));
            } else {
                this.comment.setText(Html.fromHtml(item.comment));
            }
            this.comment.setVisibility(View.VISIBLE);
        } else {
            this.comment.setVisibility(View.GONE);
        }
        this.comment.setMovementMethod(LinkMovementMethod.getInstance());
        if(item.meta != null && !item.meta.toString().isEmpty()) {
            this.meta.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.meta.setText(Html.fromHtml(item.meta, Html.FROM_HTML_MODE_COMPACT));
            } else {
                this.name.setText(Html.fromHtml(item.meta));
            }
        } else {
            this.meta.setVisibility(View.GONE);
        }
        Picasso.with(BugTrackerApp.context).load(item.photo_url).placeholder(BugTrackerApp.Drawable(R.drawable.placeholder_user)).transform(new CropCircleTransformation()).into(this.photo);
        return this;
    }
}
