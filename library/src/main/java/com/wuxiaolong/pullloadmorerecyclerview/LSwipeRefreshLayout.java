package com.wuxiaolong.pullloadmorerecyclerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by WuXiaolong on 2016/1/20  20:59.
 */
public class LSwipeRefreshLayout extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;

    public LSwipeRefreshLayout(Context context) {
        super(context);
        initView();
    }

    public LSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView() {
        this.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        this.setOnRefreshListener(this);
    }

    public void setPullLoadMoreRecyclerView(PullLoadMoreRecyclerView pullLoadMoreRecyclerView) {
        this.mPullLoadMoreRecyclerView = pullLoadMoreRecyclerView;
    }

    public void setSwipeRefreshEnable(boolean enable) {
        this.setEnabled(enable);
    }

    public boolean getSwipeRefreshEnable() {
        return this.isEnabled();
    }


    public void setColorSchemeResources(int... colorResIds) {
        this.setColorSchemeResources(colorResIds);

    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return this;
    }

    public void setRefreshing(final boolean isRefreshing) {
        this.post(new Runnable() {

            @Override
            public void run() {
                if (mPullLoadMoreRecyclerView.getPullRefreshEnable())
                    LSwipeRefreshLayout.this.setRefreshing(isRefreshing);
            }
        });

    }

    @Override
    public void onRefresh() {
        if (!mPullLoadMoreRecyclerView.isRefresh()) {
            mPullLoadMoreRecyclerView.setIsRefresh(true);
            mPullLoadMoreRecyclerView.refresh();
        }
    }
}
