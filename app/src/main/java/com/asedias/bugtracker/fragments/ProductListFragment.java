package com.asedias.bugtracker.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.asedias.bugtracker.BugTrackerApp;
import com.asedias.bugtracker.LoginActivity;
import com.asedias.bugtracker.R;
import com.asedias.bugtracker.async.GetProducts;
import com.asedias.bugtracker.async.base.Callback;
import com.asedias.bugtracker.async.base.DocumentRequest;
import com.asedias.bugtracker.async.base.GetDocument;
import com.asedias.bugtracker.model.ProductItem;
import com.asedias.bugtracker.others.AccessDeniedException;
import com.asedias.bugtracker.others.AuthorizationNeededException;
import com.asedias.bugtracker.others.InternetException;
import com.asedias.bugtracker.ui.adapter.ProductsAdapter;
import com.vkontakte.android.fragments.CardRecyclerFragment;
import com.vkontakte.android.ui.CardItemDecorator;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import me.grishka.appkit.api.ErrorResponse;
import me.grishka.appkit.utils.V;
import me.grishka.appkit.views.DividerItemDecoration;
import me.grishka.appkit.views.UsableRecyclerView;

import static com.asedias.bugtracker.ui.adapter.ProductsAdapter.TYPE_PRODUCT;
import static com.asedias.bugtracker.ui.adapter.ProductsAdapter.TYPE_SEARCH;

/**
 * Created by rorom on 25.04.2018.
 */

public class ProductListFragment extends CardRecyclerFragment {

    ProductsAdapter mAdapter = new ProductsAdapter();
    public static Activity mActivity;

    public ProductListFragment() {
        super(10);
    }

    @Override
    public void onAttach(Activity var1) {
        super.onAttach(var1);
        this.setTitle(R.string.prefs_products);
        if(!this.loaded) loadData();
        this.mActivity = getActivity();
    }

    protected void updateDecorator() {
        DividerItemDecoration decoration = new DividerItemDecoration(new ColorDrawable(637534208), V.dp(0.5f));
        decoration.setProvider((DividerItemDecoration.Provider) getAdapter());
        decoration.setPaddingLeft((int) BugTrackerApp.dp(88));
        list.addItemDecoration(decoration);
    }

    @Override
    public void onRefresh() {
        showProgress();
        super.onRefresh();
    }

    @Override
    protected void doLoadData(int var1, int var2) {
        this.mRequest = new GetProducts(true);
        this.mRequest.setCallback(new DocumentRequest.RequestCallback<List<RecyclerSectionAdapter.Data>>() {
            List<RecyclerSectionAdapter.Data> list;
            @Override
            public void onSuccess(List<RecyclerSectionAdapter.Data> obj) {
                list = obj;
            }

            @Override
            public void onError(Exception e) {
                if(e instanceof AuthorizationNeededException) {
                    Intent intent = new Intent(BugTrackerApp.context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }
                if(e instanceof AccessDeniedException) {
                    getActivity().finish();
                }
                if(e instanceof InternetException) {
                    ProductListFragment.this.onError(new ErrorResponse() {
                        @Override
                        public void bindErrorView(View var1) {

                        }

                        @Override
                        public void showToast(Context var1) {

                        }
                    });
                }
            }

            @Override
            public void onUIThread() {
                mAdapter.clear();
                mAdapter.add(RecyclerSectionAdapter.Data.topBottom(TYPE_SEARCH, null));
                mAdapter.addAll(list);
                onDataLoaded(mAdapter.data, false);
            }
        });
        this.mRequest.run();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }
}
