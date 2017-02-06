package com.jim.fastdevhelper.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by coder_lw on 16/12/28.
 * {@link CommonRcvAdapter} 的包装类，用于增加HeaderView和FooterView
 */
public class RcvAdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 第一个HeaderView的ViewType的值
     */
    private static final int BASE_ITEM_TYPE_HEADER = 10000;
    /**
     * 第一个FootererView的ViewType的值
     */
    public static final int BASE_ITEM_TYPE_FOOTER = 20000;
    /**
     * 被包装的Adapter
     */
    private RecyclerView.Adapter srcAdapter;

    private RecyclerView.LayoutManager layoutManager;

    /**
     * 包含了所有的HeaderView的对象
     */
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    /**
     * 包含了所有的FooterView的对象
     */
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();

    public RcvAdapterWrapper(RecyclerView.Adapter srcAdapter, RecyclerView.LayoutManager layoutManager) {
        this.srcAdapter = srcAdapter;
        //设置数据改变的监听器
        this.srcAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart + getHeaderCount(), itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart + getHeaderCount(), itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            }
        });

        this.layoutManager = layoutManager;

        if (this.layoutManager instanceof GridLayoutManager) {
            //设置HeaderView和FooterView独占一行
            setSpanSizeLookup(this, (GridLayoutManager) this.layoutManager);
        }

    }

    /**
     * 设置item的span size
     *
     * @param adapter           适配器
     * @param gridLayoutManager GridLayoutManager
     */
    private void setSpanSizeLookup(final RecyclerView.Adapter adapter, final GridLayoutManager gridLayoutManager) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (isHeaderView(position)) {//HeaderView
                    return gridLayoutManager.getSpanCount();//占据spanCount个span 即一行
                } else if (isFooterView(position)) {//FooterView
                    return gridLayoutManager.getSpanCount();//占据spanCount个span 即一行
                } else {//普通item
                    return 1;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {//HeaderView
            return mHeaderViews.keyAt(position);
        } else if (isFooterView(position)) {//FooterView
            return mFooterViews.keyAt(position - mHeaderViews.size() - srcAdapter.getItemCount());
        } else {//普通的item 使用被包装的adapter方法
            return srcAdapter.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据ViewType 返回不同的item类型
        if (mHeaderViews.get(viewType) != null) {
            return new DefaultViewHolder(mHeaderViews.get(viewType));
        } else if (mFooterViews.get(viewType) != null) {
            return new DefaultViewHolder(mFooterViews.get(viewType));
        } else {
            return srcAdapter.createViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        //普通item，由原Adapter实现
        if (mHeaderViews.get(viewType) == null && mFooterViews.get(viewType) == null) {
            srcAdapter.onBindViewHolder(holder, position - mHeaderViews.size());
        }
    }

    @Override
    public int getItemCount() {
        //待包装的adapter的数据条目
        int count = srcAdapter.getItemCount();
        //HeaderView的个数
        int headerViewCount = getHeaderCount();
        //FooterView的个数
        int footerViewCount = getFooterCount();

        return count + headerViewCount + footerViewCount;
    }

    /**
     * 获取HeaderView的个数
     *
     * @return HeaderView的个数
     */
    public int getHeaderCount() {
        return mHeaderViews != null ? mHeaderViews.size() : 0;
    }

    /**
     * 获取FooterView的个数
     *
     * @return FooterView的个数
     */
    public int getFooterCount() {
        return mFooterViews != null ? mFooterViews.size() : 0;
    }

    /**
     * 是否是HeaderView
     *
     * @param position 当前item的position
     * @return true 是  false 不是
     */
    private boolean isHeaderView(int position) {
        if (mHeaderViews == null) {
            return false;
        }
        return position < mHeaderViews.size();
    }

    /**
     * 是否是FooterView
     *
     * @param position 当前item的position
     * @return true 是  false 不是
     */
    private boolean isFooterView(int position) {
        if (mFooterViews == null) {
            return false;
        }
        return position >= getHeaderCount() + srcAdapter.getItemCount();
    }

    //    ------------  提供一个默认实现的ViewHolder给HeaderView和FooterView使用  ------------------
    class DefaultViewHolder extends RecyclerView.ViewHolder {

        public DefaultViewHolder(View itemView) {
            super(itemView);
        }
    }

//    -------------------------  添加和移除HeaderView，FooterView  --------------------------

    /**
     * 添加一个HeaderView
     *
     * @param view headerView
     */
    public void addHeaderView(View view) {
        mHeaderViews.put(BASE_ITEM_TYPE_HEADER + mHeaderViews.size(), view);
        notifyDataSetChanged();
    }

    /**
     * 移除一个HeaderView
     *
     * @param view headerView
     */
    public void removeHeaderView(View view) {
        mHeaderViews.remove(mHeaderViews.indexOfValue(view));
        notifyDataSetChanged();
    }

    /**
     * 添加一个FooterView
     *
     * @param view footerView
     */
    public void addFooterView(View view) {
        mFooterViews.put(BASE_ITEM_TYPE_FOOTER + mFooterViews.size(), view);
        notifyDataSetChanged();
    }

    /**
     * 移除一个FooterView
     *
     * @param view footerView
     */
    public void removeFooterView(View view) {
        mFooterViews.remove(mFooterViews.indexOfValue(view));
        notifyDataSetChanged();
    }

}
