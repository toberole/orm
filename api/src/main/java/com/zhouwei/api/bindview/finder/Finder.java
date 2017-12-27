package com.zhouwei.api.bindview.finder;

import android.content.Context;
import android.view.View;

/**
 * Created by zhouwei on 2017/12/27.
 */

public interface Finder {

    /**
     * 根据source获取Context
     *
     * @param source Context 来源，例如Activity，View
     * @return
     */
    Context getContext(Object source);

    /**
     * 根据id找控件
     *
     * @param source 控件来源
     * @param id     目标控件ID
     * @return
     */
    View findView(Object source, int id);
}

