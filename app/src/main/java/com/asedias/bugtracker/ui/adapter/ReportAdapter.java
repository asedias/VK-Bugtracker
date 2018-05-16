package com.asedias.bugtracker.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.R;
import com.asedias.bugtracker.async.GetReport;
import com.asedias.bugtracker.model.TitleItem;
import com.asedias.bugtracker.ui.holder.AttachmentHolder;
import com.asedias.bugtracker.ui.holder.ExpandHolder;
import com.asedias.bugtracker.ui.holder.PhotosHolder;
import com.asedias.bugtracker.ui.holder.ProfileProductHolder;
import com.asedias.bugtracker.ui.holder.ReportInfoHolder;
import com.asedias.bugtracker.ui.holder.TextHolder;
import com.asedias.bugtracker.ui.holder.TitleHolder;
import com.asedias.bugtracker.model.reportview.CommentItem;
import com.vkontakte.android.ui.CardItemDecorator;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;
import com.vkontakte.android.ui.holder.commons.BackgroundHolder;

import me.grishka.appkit.views.DividerItemDecoration;

/**
 * Created by rorom on 11.04.2018.
 */

public class ReportAdapter extends RecyclerSectionAdapter<RecyclerHolder> implements CardItemDecorator.Provider, DividerItemDecoration.Provider {

    private final int TYPE_HEADER = 0;
    private final int TYPE_AUTHOR = 1;
    private final int TYPE_TEXT = 2;
    private final int TYPE_BIG_TEXT = 3;
    private final int TYPE_INFO = 4;
    private final int TYPE_DOC = 5;
    private final int TYPE_PHOTOS = 6;
    private final int TYPE_COMMENT = 7;
    private final int TYPE_EXTEND = 8;
    private final int TYPE_BOTTOM = -1;
    private boolean extendedInfo = false;
    private GetReport.Result mResult;

    public void createData(GetReport.Result result) {
        this.mResult = result;
        data.clear();
        data.add(RecyclerSectionAdapter.Data.top(TYPE_AUTHOR, result.author));
        data.add(RecyclerSectionAdapter.Data.middle(TYPE_BIG_TEXT, result.title));
        data.add(RecyclerSectionAdapter.Data.middle(TYPE_TEXT, result.description));
        if(result.photos.size() > 0) data.add(RecyclerSectionAdapter.Data.middle(TYPE_PHOTOS, result.photos));
        for (int i = 0; i < result.docs.size(); i++) {
            data.add(RecyclerSectionAdapter.Data.middle(TYPE_DOC, result.docs.get(i)));
        }
        data.add(RecyclerSectionAdapter.Data.middle(TYPE_BOTTOM, R.drawable.card_margin_fix_item));
        data.add(RecyclerSectionAdapter.Data.middle(TYPE_HEADER, new TitleItem(BugTrackerApp.String(R.string.report_information))));
        for(int i = 0; i < (extendedInfo ? result.information.size() : 2); i++) {
            data.add(RecyclerSectionAdapter.Data.middle(TYPE_INFO, result.information.get(i)));
        }
        data.add(RecyclerSectionAdapter.Data.middle(TYPE_EXTEND, extendedInfo));
        int size = result.comments.size();
        if(size > 0) {
            String comments = BugTrackerApp.QuantityString(R.plurals.good_comments, size, size);
            data.add(RecyclerSectionAdapter.Data.top(TYPE_HEADER , new TitleItem(comments)));
            for(int i = 0; i < result.comments.size(); i++) {
                data.add(RecyclerSectionAdapter.Data.middle(TYPE_COMMENT, result.comments.get(i)));
            }
        }
        data.add(RecyclerSectionAdapter.Data.bottom(TYPE_BOTTOM, R.drawable.card_margin_fix_item));
    }

    public void showhideInfo() {
        int start = 1 + //AUTHOR
                1 + //TITLE
                1 + //DESCRIPTION
                (mResult.photos.size() > 0 ? 1 : 0) + //PHOTOS
                mResult.docs.size() + //DOCS
                1 + //MARGIN
                1;//HEADER
        if(!extendedInfo) {
            extendedInfo = true;
            for(int i = 2; i < mResult.information.size(); i++) {
                data.add(start + i, RecyclerSectionAdapter.Data.middle(TYPE_INFO, mResult.information.get(i)));
            }
            notifyItemRangeInserted(start + 2, mResult.information.size() - 2);
        } else {
            extendedInfo = false;
            for(int i = 2; i < mResult.information.size(); i++) {
                data.remove(start+2);
            }
            notifyItemRangeRemoved(start+2, mResult.information.size() - 2);
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = ((Activity)parent.getContext()).getLayoutInflater();
        switch (viewType) {
            case TYPE_HEADER: {
                return new TitleHolder(mInflater.inflate(R.layout.report_header, parent, false));
            }
            case TYPE_TEXT: {
                return new TextHolder(mInflater.inflate(R.layout.report_text, parent, false));
            }
            case TYPE_BIG_TEXT: {
                return new TextHolder(mInflater.inflate(R.layout.report_text, parent, false));
            }
            case TYPE_INFO: {
                return new ReportInfoHolder(mInflater.inflate(R.layout.report_info, parent, false));
            }
            case TYPE_DOC: {
                return new AttachmentHolder(mInflater.inflate(R.layout.report_doc, parent, false));
            }
            case TYPE_PHOTOS: {
                return new PhotosHolder(new FrameLayout(parent.getContext()));
            }
            case TYPE_EXTEND:
                return new ExpandHolder(mInflater.inflate(R.layout.report_expand, parent, false));
            case TYPE_BOTTOM:
                return new BackgroundHolder(parent);
            case TYPE_COMMENT:
                return new ProfileProductHolder(mInflater.inflate(R.layout.report_comment, parent, false));
            default: {
                return new ProfileProductHolder(mInflater.inflate(R.layout.report_author, parent, false));
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, final int position) {
        holder.bind(data.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getBlockType(int var1) {
        return data.get(var1).decoratorType;
    }

    @Override
    public boolean needDrawDividerAfter(int var1) {
        try {
            return data.get(var1).type == TYPE_AUTHOR && data.get(var1).object instanceof CommentItem;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private void Click(View v, int position) {
        if(getItemViewType(position) == TYPE_EXTEND) {
            data.set(position, RecyclerSectionAdapter.Data.middle(TYPE_EXTEND, !extendedInfo));
            notifyItemChanged(position);
            showhideInfo();
        }
    }
}
