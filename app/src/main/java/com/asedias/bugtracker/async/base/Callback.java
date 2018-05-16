package com.asedias.bugtracker.async.base;


import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * Created by rorom on 11.04.2018.
 */

public class Callback<I> {

    public void onError(Exception e) {}
    public void onSuccess(I document) {}
    public void onBackground(I document) {}
    public void onUIThread() {}
}
