package com.zhouwei.api.bindview;


import com.zhouwei.api.bindview.finder.Finder;

/**
 * Created by zhouwei on 2017/12/27.
 */

public interface Injector<T> {
    /**
     * @param host   目标
     * @param source 来源
     * @param finder
     */
    void inject(T host, Object source, Finder finder);
}
