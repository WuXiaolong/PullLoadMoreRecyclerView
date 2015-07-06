package com.wuxiaolong.pullloadmorerecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by WuXiaolong on 2015/7/2.
 * http://udn.yyuap.com/portal.php?mod=view&aid=187
 */
public class PullLoadMoreRecyclerView extends LinearLayout {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager = null;
    private PullLoadMoreListener mPullLoadMoreListener;
    private boolean hasMore = true;
    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private LinearLayout mFooterView;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager = null;
    private Context mContext;

    public PullLoadMoreRecyclerView(Context context) {
        super(context);
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutOnRefresh());

        mRecyclerView = (RecyclerView) view.findViewById(com.wuxiaolong.pullloadmorerecyclerview.R.id.recycler_view);
        mRecyclerView.setVerticalScrollBarEnabled(true);

        mRecyclerView.setHasFixedSize(true);
//        setLinearLayout(LinearLayoutManager.VERTICAL);
        setStaggeredGridLayout();
//        setGridLayout();
        mRecyclerView.addOnScrollListener(new RecyclerViewOnScroll());

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

    public class ItemDivider extends RecyclerView.ItemDecoration {

        private Drawable mDrawable;

        public ItemDivider(Context context, int resId) {
            //在这里我们传入作为Divider的Drawable对象
            mDrawable = context.getResources().getDrawable(resId);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                //以下计算主要用来确定绘制的位置
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDrawable.getIntrinsicHeight();
                mDrawable.setBounds(left, top, right, bottom);
                mDrawable.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, int position, RecyclerView parent) {
            outRect.set(0, 0, 0, mDrawable.getIntrinsicWidth());
        }
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
            int lastVisibleItem = 0;
            int firstVisibleItem = 0;
            int totalItemCount = 0;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            totalItemCount = layoutManager.getItemCount();
            if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                firstVisibleItem = ((LinearLayoutManager) layoutManager)
                        .findFirstCompletelyVisibleItemPosition();
            } else if (layoutManager instanceof GridLayoutManager) {
                //通过LayoutManager找到当前显示的最后的item的position
                lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                firstVisibleItem = ((GridLayoutManager) layoutManager)
                        .findFirstCompletelyVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                lastVisibleItem = findMax(lastPositions);
                ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(lastPositions);
                firstVisibleItem = findMax(lastPositions) - 1;
                Log.d("wxl", "firstVisibleItem=" + firstVisibleItem);
//                firstVisibleItem = ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions();


            }
            if (firstVisibleItem == 0) {
                if (!isLoadMore) {
                    setPullRefreshEnable(true);
                }
            } else {
                setPullRefreshEnable(false);
            }


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

    //找到数组中的最大值
    private int findMax(int[] lastPositions) {

        int max = lastPositions[0];
        for (int value : lastPositions) {
            //       int max    = Math.max(lastPositions,value);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] lastPositions) {
        int min = lastPositions[0];
        for (int value : lastPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    /**
     * 线性布局管理器
     */
    public void setLinearLayout(int orientation) {
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    public void setGridLayout() {
        // 网格布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    /**
     * 交错网格布局管理器
     */

    public void setStaggeredGridLayout() {
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
    }


    @SuppressWarnings("ResourceType")
    public int getOrientation() {
        return mLinearLayoutManager.getOrientation();
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


    public interface PullLoadMoreListener {
        public void onRefresh();

        public void onLoadMore();
    }
}
