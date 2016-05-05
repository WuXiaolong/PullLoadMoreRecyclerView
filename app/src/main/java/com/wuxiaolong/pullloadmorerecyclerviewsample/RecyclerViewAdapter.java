package com.wuxiaolong.pullloadmorerecyclerviewsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by WuXiaolong on 2015/7/2.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<String> dataList;

    public List<String> getDataList() {
        return dataList;
    }

    public void removeAllDataList() {
        this.dataList.removeAll(dataList);
    }

    public RecyclerViewAdapter(Context context, List<String> dataList) {
        this.dataList = dataList;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}