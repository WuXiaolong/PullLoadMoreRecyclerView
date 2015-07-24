package com.wuxiaolong.pullloadmorerecyclerviewsample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private int mCount = 1;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) findViewById(R.id.pullLoadMoreRecyclerView);
        mPullLoadMoreRecyclerView.setRefreshing(true);
        new DataAsyncTask().execute();
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setPullLoadMoreListener(new PullLoadMoreListener());

    }

    private List<String> setList() {
        List<String> dataList = new ArrayList<>();
        int start = 20 * (mCount - 1);
        for (int i = start; i < 20 * mCount; i++) {
            dataList.add("测试数据" + i);
        }
        return dataList;

    }

    class DataAsyncTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return setList();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            if (mRecyclerViewAdapter == null) {
                mRecyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, mPullLoadMoreRecyclerView, strings);
                mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
            } else {
                mRecyclerViewAdapter.getDataList().addAll(strings);
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
        }
    }

    class PullLoadMoreListener implements PullLoadMoreRecyclerView.PullLoadMoreListener {
        @Override
        public void onRefresh() {
            setRefresh();
            new DataAsyncTask().execute();
        }

        @Override
        public void onLoadMore() {
            mCount = mCount + 1;
            new DataAsyncTask().execute();
        }
    }

    private void setRefresh() {

        mRecyclerViewAdapter.getDataList().clear();

        mCount = 1;

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
            mRecyclerViewAdapter = null;
        }
        if (id == R.id.action_gridlayout) {
            mPullLoadMoreRecyclerView.setGridLayout();
            setRefresh();
            mRecyclerViewAdapter = null;

        }
        if (id == R.id.action_staggeredgridlayout) {
            mPullLoadMoreRecyclerView.setStaggeredGridLayout();
            setRefresh();
        }

        mPullLoadMoreRecyclerView.setRefreshing(true);

        new DataAsyncTask().execute();
        return true;
    }
}
