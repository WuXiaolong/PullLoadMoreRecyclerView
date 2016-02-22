package com.wuxiaolong.pullloadmorerecyclerviewsample;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class SecondFragment extends Fragment {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private int mCount = 1;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frist, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
        //mPullLoadMoreRecyclerView.setRefreshing(true);
        mPullLoadMoreRecyclerView.setGridLayout(2);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity(), setList(mCount));//1
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreListener());
    }

    private void getData(final int page) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { // if load data and success
                if (page == 1) {
                    // refresh
                    mRecyclerViewAdapter.getDataList().clear();
                } else {
                    // load more
                }
                mCount = page;
                mRecyclerViewAdapter.getDataList().addAll(setList(page));
                mRecyclerViewAdapter.notifyDataSetChanged();
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
            }
        }, 3000);
    }

    private List<String> setList(int page) {
        List<String> dataList = new ArrayList<>();
        int start = 20 * (mCount - 1);
        for (int i = start; i < 20 * page; i++) {
            dataList.add("Second" + i);
        }
        return dataList;

    }


    class PullLoadMoreListener implements PullLoadMoreRecyclerView.PullLoadMoreListener {
        @Override
        public void onRefresh() {
            getData(1);
        }

        @Override
        public void onLoadMore() {
            getData(mCount + 1);
        }
    }

}
