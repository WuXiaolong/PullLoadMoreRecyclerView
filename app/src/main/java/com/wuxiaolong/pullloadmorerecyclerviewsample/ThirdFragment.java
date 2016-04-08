package com.wuxiaolong.pullloadmorerecyclerviewsample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdFragment extends Fragment {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private int mCount = 1;
    private StaggeredRecycleViewAdapter mRecyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        mPullLoadMoreRecyclerView.setStaggeredGridLayout(2);
        mRecyclerViewAdapter = new StaggeredRecycleViewAdapter(getActivity(), setList(mCount));
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreListener());
    }

    private List<Map<String, String>> setList(int page) {
        List<Map<String, String>> dataList = new ArrayList<>();
        int start = 30 * (mCount - 1);
        Map<String, String> map;
        for (int i = start; i < 30 * page; i++) {
            map = new HashMap<>();
            map.put("text", "Third" + i);
            map.put("height", (100 + 5 * i) + "");
            dataList.add(map);
        }
        return dataList;

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
