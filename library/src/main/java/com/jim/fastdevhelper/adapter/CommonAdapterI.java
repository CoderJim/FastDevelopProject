package com.jim.fastdevhelper.adapter;

/**
 * Created by coder_lw on 16/12/27.
 */

public interface CommonAdapterI<T> {
    /**
     * 创建一个item对象
     * 由所有的具体的adapter去创建，因为不同的adapter需要的item都不一样
     * @return  新建的item对象
     */
    CommonItemI createItem();

    /**
     * 将当前数据源中的数据进行二次转换  生成新的对象给item使用
     * @param data  当前数据源中的数据条
     * @param position  当前position
     * @return  新的item需要的数据类型对象
     */
    Object convertDataToItem(T data, int position);
}
