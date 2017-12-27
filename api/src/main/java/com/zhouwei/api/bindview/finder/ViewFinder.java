package com.zhouwei.api.bindview.finder;

import android.content.Context;
import android.view.View;

/**
 * Created by zhouwei on 2017/12/27.
 */


public class ViewFinder implements Finder {

    @Override
    public Context getContext(Object source) {
        return ((View) source).getContext();
    }

    @Override
    public View findView(Object source, int id) {
        return ((View) source).findViewById(id);
    }
}