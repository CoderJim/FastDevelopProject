package com.jim.fastdevhelper.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Created by coder_lw on 16/12/27.
 */

public interface CommonItemI<T> {

    /**
     * 获取当前item对应的layout布局资源ID
     * @return layout资源ID
     */
    @LayoutRes
    int getItemLayoutResId();

    /**
     * 通过findViewById初始化item中的view
     */
    void bindViews(View rootView);

    /**
     * 将数据绑定到item上
     * @param t 当前item对应的数据
     * @param position  当前item的position
     */
    void bindData(T t, int position);
}
