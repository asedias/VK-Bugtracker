package com.asedias.bugtracker.ui.holder;

import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.asedias.bugtracker.model.reportview.BigTextItem;
import com.asedias.bugtracker.model.reportview.TextItem;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

/**
 * Created by rorom on 11.04.2018.
 */
public class TextHolder extends RecyclerHolder {

    public TextHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object var1) {
        if(var1 instanceof RecyclerSectionAdapter.Data) {
            bind(((RecyclerSectionAdapter.Data) var1).object);
        } else if(var1 instanceof TextItem) {
            bind(((TextItem) var1).title);
        } else if(var1 instanceof BigTextItem) {
            bindBig(((BigTextItem) var1).title);
        }
    }

    public void bind(String text) {
        setText(text);
    }

    public void bindBig(String text) {
        ((TextView) itemView).setTextSize(16);
        setText(text);
    }

    private void setText(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ((TextView) itemView).setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else {
            ((TextView) itemView).setText(Html.fromHtml(text));
        }
        ((TextView) itemView).setMovementMethod(LinkMovementMethod.getInstance());
    }
}
