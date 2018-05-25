package com.asedias.bugtracker.ui.holder;

import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asedias.bugtracker.R;
import com.asedias.bugtracker.async.methods.DeleteLicenceRequest;
import com.asedias.bugtracker.async.methods.JoinProduct;
import com.asedias.bugtracker.async.DocumentRequest;
import com.asedias.bugtracker.fragments.ProductListFragment;
import com.asedias.bugtracker.model.ProductItem;
import com.asedias.bugtracker.ui.CropCircleTransformation;
import com.squareup.picasso.Picasso;
import com.vkontakte.android.ui.holder.RecyclerHolder;

/**
 * Created by rorom on 25.04.2018.
 */

public class ProductHolder extends RecyclerHolder<ProductItem> {

    private TextView title = (TextView)this.$(R.id.title);
    private TextView subtitle = (TextView)this.$(R.id.subtitle);
    private ImageView photo = (ImageView)this.$(R.id.icon);
    private Button join = (Button) this.$(R.id.join);
    private Button cancel = (Button) this.$(R.id.cancel);
    private ProductItem data;

    public ProductHolder(@NonNull ViewGroup var1) {
        super(R.layout.product_item, var1);
    }

    @Override
    public void bind(ProductItem var1) {
        this.data = var1;
        this.title.setText(var1.title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.subtitle.setText(Html.fromHtml(var1.subtitle, Html.FROM_HTML_MODE_LEGACY));
        } else {
            this.subtitle.setText(Html.fromHtml(var1.subtitle));
        }
        this.cancel.setVisibility(var1.cancel ? View.VISIBLE : View.GONE);
        this.cancel.setOnClickListener(cancelRequest());
        this.join.setVisibility(var1.join ? View.VISIBLE : View.GONE);
        this.join.setOnClickListener(joinProduct());
        Picasso.with(itemView.getContext()).load(var1.photo_url).transform(new CropCircleTransformation()).into(this.photo);
    }

    private View.OnClickListener cancelRequest() {
        return v -> new DeleteLicenceRequest(ProductListFragment.mActivity, data.id, data.hash).setCallback(new DocumentRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String obj) {
                if(data.hash.length() == 18) {
                    data.hash = obj;
                } else {
                    Log.d("HASH", "WRONG HASH: " + obj);
                }
            }

            @Override
            public void onUIThread() {
                if(data.hash.length() == 18) {
                    data.cancel = false;
                    data.join = true;
                    cancel.post(() -> {
                        cancel.setVisibility(View.GONE);
                        join.setVisibility(View.VISIBLE);
                        join.setOnClickListener(joinProduct());
                    });
                }
            }
        }).run();
    }

    private View.OnClickListener joinProduct() {
        return v -> new JoinProduct(ProductListFragment.mActivity, data.id, data.hash).setCallback(new DocumentRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String obj) {
                if(data.hash.length() == 18) {
                    data.hash = obj;
                    //Log.d("NEW HASH", "NEW HASH: " + obj);
                } else {
                    Log.d("NEW HASH", "WRONG HASH: " + obj);
                }
            }

            @Override
            public void onUIThread() {
                if(data.hash.length() == 18) {
                    data.join = false;
                    data.cancel = true;
                    join.post(() -> {
                        join.setVisibility(View.GONE);
                        cancel.setVisibility(View.VISIBLE);
                        cancel.setOnClickListener(cancelRequest());
                    });
                }
            }
        }).run();
    }
}
