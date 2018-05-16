package com.asedias.bugtracker.ui.holder;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.R;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

/**
 * Created by rorom on 11.04.2018.
 */
public class ExpandHolder extends RecyclerHolder<RecyclerSectionAdapter.Data> {

    public ExpandHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(RecyclerSectionAdapter.Data var1) {
        SpannableString icon = new SpannableString(" i");
        TextView text = (TextView)itemView;
        Drawable d = getResources().getDrawable(R.drawable.arrow_down);
        if((boolean)var1.object) {
            text.setText(R.string.hide);
            d = getResources().getDrawable(R.drawable.arrow_up);
        } else {
            text.setText(R.string.show_more_info);
        }
        d.setBounds(0, 0, (int) BugTrackerApp.dp(18), (int)BugTrackerApp.dp(18));
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
        icon.setSpan(span, 1, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        text.append(icon);
    }
}
