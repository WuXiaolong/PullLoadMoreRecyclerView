package com.wuxiaolong.pullloadmorerecyclerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by WuXiaolong on 2015/7/2.
 */
public class PullLoadMoreRecyclerView extends LinearLayout {
    static final int VERTICAL = LinearLayout.VERTICAL;
    static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private PullLoadMoreListener mPullLoadMoreListener;
    private boolean hasMore = true;
    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private LinearLayout mFooterView;

    public PullLoadMoreRecyclerView(Context context) {
        super(context);
    }

    public PullLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(com.wuxiaolong.pullloadmorerecyclerview.R.layout.pull_loadmore_layout, null);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(com.wuxiaolong.pullloadmorerecyclerview.R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutOnRefresh());

        recyclerView = (RecyclerView) view.findViewById(com.wuxiaolong.pullloadmorerecyclerview.R.id.recycler_view);
        recyclerView.setVerticalScrollBarEnabled(true);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerViewOnScroll());


        recyclerView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (isRefresh) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );



        mFooterView = (LinearLayout) view.findViewById(com.wuxiaolong.pullloadmorerecyclerview.R.id.footer_linearlayout);
        mFooterView.setVisibility(View.GONE);
        this.addView(view);

    }

    /**
     * 下拉至顶部刷新监听
     */
    class SwipeRefreshLayoutOnRefresh implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            if (!isRefresh) {
                isRefresh = true;
                refresh();
            }
        }
    }

    /**
     * 监听上拉至底部滚动监听
     */
    class RecyclerViewOnScroll extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItem = linearLayoutManager
                    .findLastVisibleItemPosition();
            int firstVisibleItem = linearLayoutManager
                    .findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItem == 0) {
                if (!isLoadMore) {
                    setPullRefreshEnable(true);
                }
            } else {
                setPullRefreshEnable(false);
            }

            int totalItemCount = linearLayoutManager.getItemCount();
            /**
             * 无论水平还是垂直
             */
            if (!isRefresh && hasMore && (lastVisibleItem >= totalItemCount - 1)
                    && !isLoadMore && (dx > 0 || dy > 0)) {
                isLoadMore = true;
                loadMore();
            }

        }
    }

    public void setOrientation(int orientation) {
        if (orientation != LinearLayout.HORIZONTAL && orientation != LinearLayout.VERTICAL) {
            linearLayoutManager.setOrientation(VERTICAL);
        } else {
            linearLayoutManager.setOrientation(orientation);
        }
    }

    @SuppressWarnings("ResourceType")
    public int getOrientation() {
        return linearLayoutManager.getOrientation();
    }


    public void setPullLoadMoreEnable(boolean enable) {
        this.hasMore = enable;
    }

    public boolean getPullLoadMoreEnable() {
        return hasMore;
    }

    public void setPullRefreshEnable(boolean enable) {
        swipeRefreshLayout.setEnabled(enable);
    }

    public boolean getPullRefreshEnable() {
        return swipeRefreshLayout.isEnabled();
    }


    public void loadMore() {
        if (mPullLoadMoreListener != null && hasMore) {
            mFooterView.setVisibility(View.VISIBLE);
            mPullLoadMoreListener.onLoadMore();

        }
    }

    /**
     * 加载更多完毕,为防止频繁网络请求,isLoadMore为false才可再次请求更多数据
     */
    public void setLoadMoreCompleted() {
        isLoadMore = false;
        mFooterView.setVisibility(View.GONE);
    }

    public void setPullLoadMoreCompleted() {
        isRefresh = false;
        swipeRefreshLayout.setRefreshing(false);

        isLoadMore = false;
        mFooterView.setVisibility(View.GONE);


    }

    public void setStopRefresh() {
        isRefresh = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    public void setPullLoadMoreListener(PullLoadMoreListener listener) {
        mPullLoadMoreListener = listener;
    }

    public void refresh() {
        recyclerView.setVisibility(View.VISIBLE);
        if (mPullLoadMoreListener != null) {
            mPullLoadMoreListener.onRefresh();
        }
    }

    public void scrollToTop() {
        recyclerView.scrollToPosition(0);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {

            recyclerView.setAdapter(adapter);
        }
    }


    public interface PullLoadMoreListener {
        public void onRefresh();

        public void onLoadMore();
    }
}
