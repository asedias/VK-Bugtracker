package com.asedias.bugtracker.ui.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.asedias.bugtracker.model.ProductItem;
import com.asedias.bugtracker.ui.holder.ProductHolder;
import com.asedias.bugtracker.ui.holder.SearchHolder;
import com.vkontakte.android.ui.holder.RecyclerHolder;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

import java.util.List;

import me.grishka.appkit.views.DividerItemDecoration;

/**
 * Created by rorom on 25.04.2018.
 */

public class ProductsAdapter extends RecyclerSectionAdapter<RecyclerHolder> implements RecyclerSectionAdapter.DataDelegate, DividerItemDecoration.Provider {

    public static final int TYPE_SEARCH = 0;
    public static final int TYPE_PRODUCT = 1;

    @Override
    public List<Data> getData() {
        return this.data;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_SEARCH: {
                return new SearchHolder(parent);
            }
            default:
            case TYPE_PRODUCT: {
                return new ProductHolder(parent);
            }
        }
    }

    @Override
    public boolean needDrawDividerAfter(int var1) {
        return getItemViewType(var1) == TYPE_PRODUCT;
    }
}
