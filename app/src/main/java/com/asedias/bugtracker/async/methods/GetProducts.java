package com.asedias.bugtracker.async.methods;

import android.util.Log;

import com.asedias.bugtracker.async.DocumentRequest;
import com.asedias.bugtracker.async.base.PostRequestParser;
import com.asedias.bugtracker.model.ProductItem;
import com.vkontakte.android.ui.holder.RecyclerSectionAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.asedias.bugtracker.ui.adapter.ProductsAdapter.TYPE_PRODUCT;

/**
 * Created by rorom on 12.05.2018.
 */

public class GetProducts extends DocumentRequest<List<RecyclerSectionAdapter.Data>> {
    public GetProducts(boolean all) {
        super(true);
        this.param("act", "products");
        if(all) this.param("section", "all");
    }

    @Override
    protected List<RecyclerSectionAdapter.Data> parse(PostRequestParser text) {
        Log.d("URL", this.mTask.request.url().toString());
        Document doc = text.html;
        List<RecyclerSectionAdapter.Data> list = new ArrayList<>();
        Elements products = doc.getElementsByClass("bt_product_row");
        for(int i = 0; i < products.size(); i++) {
            ProductItem product = new ProductItem();
            Element elements = products.get(i);
            Element content = elements.child(2);
            product.photo_url = elements.child(1).child(0).child(0).attr("src");
            product.title = content.child(0).child(0).html();
            product.subtitle = content.child(1).html();
            if(content.children().size() == 3 && content.child(2).html().length() > 0) {
                product.subtitle += "<br>" + content.child(2).html();
            }
            Element join = elements.getElementsByClass("bt_product_row_join").get(0);
            if(join.getElementsByClass("bt_product_row_join__note").size() > 0) {
                product.cancel = true;
                String note = elements.getElementsByClass("bt_product_row_join__note").get(0).html();
                String regex = "(.+), (\\r\\n|\\r|\\n).+\\(([0-9][0-9]?),'(\\w+)'.+";
                product.hash = note.replaceAll(regex, "$4");
                product.id = note.replaceAll(regex, "$3");
                product.subtitle += "<br>" +  note.replaceAll(regex, "$1");
            }
            if(join.children().size() == 1 && join.child(0).attr("onclick").length() > 0) {
                product.join = true;
                String regex = ".+\\(this, ([0-9][0-9]?), ..?, '(\\w+)'.+";
                product.hash = join.html().replaceAll(regex, "$2");
                product.id = join.html().replaceAll(regex, "$1");
            }
            if(!product.join && !product.cancel) {
                product.my = true;
            }
            RecyclerSectionAdapter.Data data = (i == 0) ? RecyclerSectionAdapter.Data.top(TYPE_PRODUCT, product) : RecyclerSectionAdapter.Data.middle(TYPE_PRODUCT, product);
            list.add(data);
        }
        return list;
    }
}
