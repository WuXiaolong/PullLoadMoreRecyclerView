package com.wuxiaolong.pullloadmorerecyclerview;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by WuXiaolong on 2016/1/20 0020 21:11.
 */
public class LRecyclerView extends RecyclerView {
    private Context mContext;
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;

    public LRecyclerView(Context context) {
        super(context);
        mContext = context;
    }

    public void initView() {
        LRecyclerView.this.setVerticalScrollBarEnabled(true);

        LRecyclerView.this.setHasFixedSize(true);
        LRecyclerView.this.setItemAnimator(new DefaultItemAnimator());
        //LRecyclerView.this.addItemDecoration(new DividerItemDecoration(
        //getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        LRecyclerView.this.addOnScrollListener(new RecyclerViewOnScroll(mPullLoadMoreRecyclerView));

        LRecyclerView.this.setOnTouchListener(new onTouchRecyclerView());
    }

    /**
     * LinearLayoutManager
     */
    public void setLinearLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LRecyclerView.this.setLayoutManager(linearLayoutManager);
    }

    /**
     * GridLayoutManager
     */

    public void setGridLayout(int spanCount) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LRecyclerView.this.setLayoutManager(gridLayoutManager);
    }


    /**
     * StaggeredGridLayoutManager
     */

    public void setStaggeredGridLayout(int spanCount) {
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(spanCount, LinearLayoutManager.VERTICAL);
        LRecyclerView.this.setLayoutManager(staggeredGridLayoutManager);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return LRecyclerView.this.getLayoutManager();
    }

    public RecyclerView getRecyclerView() {
        return LRecyclerView.this;
    }

    public void scrollToTop() {
        LRecyclerView.this.scrollToPosition(0);
    }


    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            LRecyclerView.this.setAdapter(adapter);
        }
    }

    public void setPullLoadMoreRecyclerView(PullLoadMoreRecyclerView pullLoadMoreRecyclerView) {
        this.mPullLoadMoreRecyclerView = pullLoadMoreRecyclerView;
        initView();
    }

    /**
     * Solve IndexOutOfBoundsException exception
     */
    public class onTouchRecyclerView implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mPullLoadMoreRecyclerView.isRefresh() || mPullLoadMoreRecyclerView.isLoadMore()) {
                return true;
            } else {
                return false;
            }
        }
    }
}
