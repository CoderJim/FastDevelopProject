package com.jim.fastdevhelper.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jim.fastdevhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coder_lw on 16/12/27.
 */

public abstract class CommonLvAdapter<T> extends BaseAdapter implements CommonAdapterI<T>{
    /**
     * 数据源
     */
    private List<T> mDatas;
    /**
     * 布局加载器
     */
    private LayoutInflater mInflater;

    public CommonLvAdapter(List<T> mDatas) {
        if(mDatas == null){//如果传递的数据源为空，创建一个list
            mDatas = new ArrayList<>();
        }

        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(mInflater == null){
            mInflater = LayoutInflater.from(parent.getContext());
        }
        //item对象，功能类似ViewHolder
        CommonItemI item;
        if(convertView == null){
            //具体的子类去创建item
            item = createItem();
            //加载item对应的布局
            convertView = mInflater.inflate(item.getItemLayoutResId(), parent, false);
            //将当前的item对象绑定到convertView上
            convertView.setTag(R.id.convertview_item_tag, item);
            //初始化view
            item.bindViews(convertView);
        } else {
            //重用item
            item = (CommonItemI) convertView.getTag(R.id.convertview_item_tag);
        }
        //绑定数据到item
        item.bindData(convertDataToItem(mDatas.get(position), position),position);
        return convertView;
    }

    @Override
    public Object convertDataToItem(T data, int position) {
        //默认不进行数据转换，需要转换时，子类具体处理
        return data;
    }
}
