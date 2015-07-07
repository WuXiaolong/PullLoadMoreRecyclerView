package com.wuxiaolong.pullloadmorerecyclerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by WuXiaolong on 2015/7/2.
 */
public class PullLoadMoreRecyclerView extends LinearLayout {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PullLoadMoreListener mPullLoadMoreListener;
    private boolean hasMore = true;
    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private LinearLayout mFooterView;
    private Context mContext;

    public PullLoadMoreRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public PullLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(com.wuxiaolong.pullloadmorerecyclerview.R.layout.pull_loadmore_layout, null);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(com.wuxiaolong.pullloadmorerecyclerview.R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutOnRefresh(this));

        mRecyclerView = (RecyclerView) view.findViewById(com.wuxiaolong.pullloadmorerecyclerview.R.id.recycler_view);
        mRecyclerView.setVerticalScrollBarEnabled(true);

        mRecyclerView.setHasFixedSize(true);
        setLinearLayout();
        // 设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(
        //getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        mRecyclerView.addOnScrollListener(new RecyclerViewOnScroll(this));

        mRecyclerView.setOnTouchListener(
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
     * 线性布局管理器
     */
    public void setLinearLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * 网格布局管理器
     */

    public void setGridLayout() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * 交错网格布局管理器
     */

    public void setStaggeredGridLayout() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }


    public void setPullRefreshEnable(boolean enable) {
        swipeRefreshLayout.setEnabled(enable);
    }

    public boolean getPullRefreshEnable() {
        return swipeRefreshLayout.isEnabled();
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
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

    public void setPullLoadMoreCompleted() {
        isRefresh = false;
        swipeRefreshLayout.setRefreshing(false);

        isLoadMore = false;
        mFooterView.setVisibility(View.GONE);

    }


    public void setPullLoadMoreListener(PullLoadMoreListener listener) {
        mPullLoadMoreListener = listener;
    }

    public void refresh() {
        mRecyclerView.setVisibility(View.VISIBLE);
        if (mPullLoadMoreListener != null) {
            mPullLoadMoreListener.onRefresh();
        }
    }

    public void scrollToTop() {
        mRecyclerView.scrollToPosition(0);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mRecyclerView.setAdapter(adapter);
        }
    }

    public boolean isLoadMore() {
        return isLoadMore;
    }

    public void setIsLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public interface PullLoadMoreListener {
        public void onRefresh();

        public void onLoadMore();
    }
}
