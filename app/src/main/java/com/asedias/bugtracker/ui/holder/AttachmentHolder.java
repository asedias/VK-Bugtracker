package com.asedias.bugtracker.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asedias.bugtracker.R;
import com.asedias.bugtracker.model.reportview.Attachment;
import com.asedias.bugtracker.model.reportview.DocItem;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

/**
 * Created by rorom on 14.04.2018.
 */

public class AttachmentHolder extends RecyclerHolder {

    public ImageView icon;
    public TextView title;
    public TextView subtitle;

    public AttachmentHolder(View itemView) {
        super(itemView);
        this.icon = itemView.findViewById(R.id.icon);
        this.title = itemView.findViewById(R.id.title);
        this.subtitle = itemView.findViewById(R.id.subtitle);
    }

    @Override
    public void bind(Object var1) {
        if(var1 instanceof RecyclerSectionAdapter.Data) {
            bind(((RecyclerSectionAdapter.Data) var1).object);
        } else if(var1 instanceof DocItem) {
            bind(((DocItem)var1).doc);
        }
    }

    public void bind(Attachment doc) {
        this.title.setText(doc.title);
        this.subtitle.setText(doc.subtitle);
        int icon_id = R.drawable.ic_doc;
        switch (doc.type) {
            case 1: {
                icon_id = R.drawable.ic_doc_text;
                break;
            }
            case 6: {
                icon_id = R.drawable.ic_doc_video;
                break;
            }
        }
        this.icon.setImageResource(icon_id);
    }
}
