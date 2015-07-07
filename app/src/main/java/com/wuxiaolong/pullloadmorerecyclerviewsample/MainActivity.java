package com.wuxiaolong.pullloadmorerecyclerviewsample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    int mCount = 1;
    RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) findViewById(R.id.pullLoadMoreRecyclerView);
//        mPullLoadMoreRecyclerView.setGridLayout(LinearLayoutManager.VERTICAL);
        setList();
        mPullLoadMoreRecyclerView.setPullLoadMoreListener(new PullLoadMoreListener());

    }

    private void setList() {
        List<String> dataList = new ArrayList<>();
        int start = 30 * (mCount - 1);
        for (int i = start; i < 30 * mCount; i++) {
            dataList.add("测试数据" + i);
        }

        if (mRecyclerViewAdapter == null) {
            mRecyclerViewAdapter = new RecyclerViewAdapter(this, dataList);
            mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        } else {
            mRecyclerViewAdapter.getDataList().addAll(dataList);
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
    }

    class PullLoadMoreListener implements PullLoadMoreRecyclerView.PullLoadMoreListener {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            setRefresh();
                        }
                    }, 3000);
        }

        @Override
        public void onLoadMore() {
            new Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            mCount = mCount + 1;
                            setList();
                        }
                    }, 3000);
        }
    }

    private void setRefresh() {
        mRecyclerViewAdapter = null;
        mCount = 1;
        setList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_linearlayout) {
            mPullLoadMoreRecyclerView.setLinearLayout();
            setRefresh();
            return true;
        }
        if (id == R.id.action_gridlayout) {
            mPullLoadMoreRecyclerView.setGridLayout();
            setRefresh();
            return true;
        }
//        if (id == R.id.action_staggeredgridlayout) {
//            mPullLoadMoreRecyclerView.setStaggeredGridLayout();
//            setRefresh();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
