package com.jim.fastdevhelper.recyclerview;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by coder_lw on 17/2/7.
 * 继承自RecyclerView.OnItemTouchListener类的抽象类，增加点击和长按的事件回调
 */

public abstract class OnItemTouchListener implements RecyclerView.OnItemTouchListener{
    /**
     * 手势判断类
     */
    private GestureDetectorCompat gestureDetectorCompat;
    /**
     * RecyclerView
     */
    private RecyclerView recyclerView;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        //处理事件拦截
        recyclerView = rv;
        if(null == gestureDetectorCompat){
            //触发了事件之后  交给MyGestureListener处理
            gestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(), new MyGestureListener());
        }
        gestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        //处理触摸事件
        recyclerView = rv;
        if(null == gestureDetectorCompat){
            //触发了事件之后  交给MyGestureListener处理
            gestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(), new MyGestureListener());
        }
        gestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        //处理事件冲突
    }

    /**
     * RecyclerView item点击事件回调方法
     * @param parent    RecyclerView
     * @param view  当前点击的item的View
     * @param position  当前item的position
     */
    public abstract void onItemClick(RecyclerView parent, View view, int position);

    public abstract void onItemLongClick(RecyclerView parent, View view, int position);

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //点击事件
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if(null != child){
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                onItemClick(recyclerView, viewHolder.itemView, viewHolder.getAdapterPosition());
            }
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            //长按事件
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if(null != child){
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                onItemLongClick(recyclerView, viewHolder.itemView, viewHolder.getAdapterPosition());
            }
            super.onLongPress(e);
        }

    }

}
