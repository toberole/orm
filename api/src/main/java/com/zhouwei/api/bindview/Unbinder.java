package com.zhouwei.api.bindview;


import android.support.annotation.UiThread;

/**
 * Created by zhouwei on 2017/12/27.
 */

public interface Unbinder {

    @UiThread
    void unbind();

    Unbinder EMPTY = new Unbinder() {
        @Override
        public void unbind() {

        }
    };
}