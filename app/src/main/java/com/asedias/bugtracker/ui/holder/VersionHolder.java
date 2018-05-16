package com.asedias.bugtracker.ui.holder;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asedias.bugtracker.R;
import com.asedias.bugtracker.fragments.AddReportFragment;
import com.asedias.bugtracker.model.NewReportItem;
import com.asedias.bugtracker.others.FlowLayout;
import com.asedias.bugtracker.others.ReportSettings;
import com.asedias.bugtracker.ui.adapter.AddReportAdapter;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

import java.util.HashMap;

import static com.asedias.bugtracker.fragments.AddReportFragment.setPlatformList;

/**
 * Created by rorom on 18.04.2018.
 */

public class VersionHolder extends RecyclerHolder<RecyclerSectionAdapter.Data> {

    private TextView title;
    private TextView subtitle;
    private TextInputLayout input;
    private FlowLayout tags;
    private ImageView action;
    private SwitchCompat aSwitch;
    private ProgressBar progress;
    private HashMap<Integer, View> tagsView = new HashMap<>();

    public final int TYPE_HEADER = R.layout.report_header;
    public final int TYPE_PRODUCT = R.layout.addr_product;
    public final int TYPE_VERSION = R.layout.addr_version;
    public final int TYPE_INPUT = R.layout.addr_input;
    public final int TYPE_DROP_TAGS = R.layout.addr_tags;
    public final int TYPE_DOC = R.layout.report_doc;
    public final int TYPE_SWITCH = R.layout.addr_switch;
    public final int TYPE_SEVERITY = R.layout.addr_severity;
    public int TYPE;

    public VersionHolder(View itemView) {
        super(itemView);
        this.title = itemView.findViewById(R.id.title);
        this.subtitle = itemView.findViewById(R.id.subtitle);
        this.input = itemView.findViewById(R.id.input);
        this.tags = itemView.findViewById(R.id.tags_layout);
        this.action = itemView.findViewById(R.id.action);
        this.aSwitch = itemView.findViewById(R.id.switch1);
        this.progress = itemView.findViewById(R.id.progress);
    }

    public void bind(final RecyclerSectionAdapter.Data data) {
        final NewReportItem item = (NewReportItem)data.object;
        this.TYPE = item.TYPE;
        switch (this.TYPE) {
            case TYPE_HEADER: {
                ((TextView)itemView).setText(item.title);
                break;
            }
            case TYPE_PRODUCT: {
                this.progress.setVisibility(item.progress);
                this.title.setText(item.title);
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddReportAdapter.context);
                builder.setTitle(R.string.addr_product);
                this.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.setItems(item.items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ReportSettings.Product temp = AddReportFragment.mSettings.productsArray[which];
                                title.setText(temp.name);
                                item.title = temp.name;
                                AddReportFragment.mSettings.currentID = temp.id;
                                AddReportFragment.UpdateInfo();
                            }
                        });
                        builder.show();
                    }
                });
                break;
            }
            case TYPE_DROP_TAGS: {
                this.tags.removeAllViews();
                this.tagsView.clear();
                this.title.setText(item.title);
                final boolean isPlatform = item.title.toString().hashCode() == getString(R.string.addr_platforms).hashCode();
                itemView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddReportAdapter.context);
                if(item.items != null && item.items.length > 0 && item.items.length != 1) {
                    this.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder.setMultiChoiceItems(item.items, item.selected, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    if (isChecked) {
                                        TextView tag = (TextView) ((Activity) AddReportAdapter.context).getLayoutInflater().inflate(R.layout.tag_item, null);
                                        tag.setText(item.items[which]);
                                        tags.addView(tag);
                                        tagsView.put(which, tag);
                                    } else {
                                        tags.removeView(tagsView.get(which));
                                    }
                                    if(isPlatform) setPlatformList(which, isChecked);
                                    //UpdatePlatforms();
                                }
                            });
                            if(item.items.length > 0) builder.show();
                            //BottomSheetBehavior.from(builder.show().getListView()).setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    });
                } else if(item.items != null){
                    TextView tag = (TextView) ((Activity) AddReportAdapter.context).getLayoutInflater().inflate(R.layout.tag_item, null);
                    tag.setText(item.items[0]);
                    tags.addView(tag);
                    itemView.setOnClickListener(null);
                    item.selected[0] = true;
                    if(isPlatform) setPlatformList(item.items[0].toString());
                } else if(item.items == null) {
                    itemView.getLayoutParams().height = 0;
                }
                //TextView tag = (TextView) ((Activity)AddReportAdapter.context).getLayoutInflater().inflate(R.layout.tag_item, null);
                //tag.setText("Item 1");
                //this.tags.addView(tag);
                break;
            }
            case TYPE_VERSION: {
                this.title.setText(item.title);
                this.subtitle.setText(item.subtitle);
                break;
            }
            case TYPE_INPUT: {
                this.input.setHint(item.input_hint);
                this.action.setVisibility(item.img_state);
                this.input.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        item.input_data = s.toString();
                    }
                });
                if(item.img_state == View.VISIBLE) {
                    this.action.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent chooser = new Intent(Intent.ACTION_GET_CONTENT);
                            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath().toString());
                            chooser.addCategory(Intent.CATEGORY_OPENABLE);
                            chooser.setDataAndType(uri, "*/*");
                            try {
                                //FragmentWrapperActivity.mFragment.startActivityForResult(chooser, 12333);
                            }
                            catch (android.content.ActivityNotFoundException ex)
                            {
                                Toast.makeText(AddReportAdapter.context, "Please install a File Manager.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            }
            case TYPE_DOC: {
                this.title.setText(item.title);
                this.subtitle.setText(item.subtitle);
                this.progress.setIndeterminate(true);
                this.progress.setProgress(50);
                break;
            }
            case TYPE_SWITCH: {
                this.aSwitch.setText(item.title);
                this.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        item.switch_data = isChecked;
                    }
                });
                break;
            }
            case TYPE_SEVERITY: {
                this.title.setText(item.title);
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddReportAdapter.context);
                builder.setTitle(R.string.addr_severity);
                this.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.setItems(item.items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ReportSettings.ArrayItem temp = AddReportFragment.mSettings.severity.get(which);
                                subtitle.setText(temp.name);
                                AddReportFragment.mSettings.currentSeverity = temp.id;
                            }
                        });
                        builder.show();
                    }
                });
            }
        }
    }
}
