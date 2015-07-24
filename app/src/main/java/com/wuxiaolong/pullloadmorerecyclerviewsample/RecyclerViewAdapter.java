package com.wuxiaolong.pullloadmorerecyclerviewsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;
import java.util.Random;

/**
 * Created by WuXiaolong on 2015/7/2.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<String> dataList;
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;

    public List<String> getDataList() {
        return dataList;
    }

    public RecyclerViewAdapter(Context context, PullLoadMoreRecyclerView pullLoadMoreRecyclerView, List<String> dataList) {
        this.dataList = dataList;
        mContext = context;
        mPullLoadMoreRecyclerView = pullLoadMoreRecyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (mPullLoadMoreRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered_recycler_view_item, parent, false);
        } else {

            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(dataList.get(position));
        if (mPullLoadMoreRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            holder.title.setHeight(200 + new Random().nextInt(500));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public int getViewHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }
}