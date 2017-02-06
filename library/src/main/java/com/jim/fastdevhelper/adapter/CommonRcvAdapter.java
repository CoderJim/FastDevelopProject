package com.jim.fastdevhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coder_lw on 16/12/27.
 */

public abstract class CommonRcvAdapter<T> extends RecyclerView.Adapter<CommonRcvAdapter.CommonRcvHolder> implements CommonAdapterI<T> {
    /**
     * 数据源
     */
    private List<T> mDatas;

    public CommonRcvAdapter(List<T> mDatas) {
        if(mDatas == null){
            mDatas = new ArrayList<>();
        }
        this.mDatas = mDatas;
    }

    @Override
    public CommonRcvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommonRcvHolder(parent.getContext(), parent, createItem());
    }

    @Override
    public void onBindViewHolder(CommonRcvHolder holder, int position) {
        holder.item.bindData(convertDataToItem(mDatas.get(position), position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public Object convertDataToItem(T data, int position) {
        //默认不进行数据转换
        return data;
    }

    /**
     * 内部类ViewHolder
     */
    static class CommonRcvHolder extends RecyclerView.ViewHolder {

        protected CommonItemI item;

        CommonRcvHolder(Context context, ViewGroup parent, CommonItemI item) {
            super(LayoutInflater.from(context).inflate(item.getItemLayoutResId(), parent, false));
            this.item = item;
            this.item.bindViews(itemView);
        }
    }

}
