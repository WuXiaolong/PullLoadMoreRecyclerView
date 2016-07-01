> 实现RecyclerView下拉刷新和上拉加载更多以及RecyclerView线性、网格、瀑布流效果演示。如果你只需要简单地实现下拉刷新和上拉加载效果，不需要定制，那PullLoadMoreRecyclerView最适合不过。

# 效果预览
![](https://github.com/WuXiaolong/PullLoadMoreRecyclerView/raw/master/screenshots/screenshots.gif)

# 使用方法

## build.gradle文件
```java
dependencies {
  compile 'com.wuxiaolong.pullloadmorerecyclerview:library:1.0.8'
}
```

## xml引用
```js
 <com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView
        android:id="@+id/pullLoadMoreRecyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_margin="10dp" />
```

## 设置线性布局
```java
 mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
 mPullLoadMoreRecyclerView.setLinearLayout();
```

## 设置网格布局
```java
 mPullLoadMoreRecyclerView.setGridLayout(2);//参数为列数
```

## 设置交错网格布局，即瀑布流效果
```java
 mPullLoadMoreRecyclerView.setStaggeredGridLayout(2);//参数为列数
```

## 绑定适配器
```java
  mRecyclerViewAdapter = new RecyclerViewAdapter();
  mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public RecyclerViewAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
```

## 调用下拉刷新和加载更多
```java
 mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

            }
        });
```

## 刷新结束
```java
mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
```

## 其他方法

### 显示下拉刷新
```
mPullLoadMoreRecyclerView.setRefreshing(true);
```
### 不需要下拉刷新
```java
mPullLoadMoreRecyclerView.setPullRefreshEnable(false);
```

### 不需要上拉刷新
```java
mPullLoadMoreRecyclerView.setPushRefreshEnable(false);
```

### 设置上拉刷新文字
```java
mPullLoadMoreRecyclerView.setFooterViewText("loading");
```

### 设置上拉刷新文字颜色
```java
mPullLoadMoreRecyclerView.setFooterViewTextColor(R.color.white);
```

### 设置加载更多背景色
```java
mPullLoadMoreRecyclerView.setFooterViewBackgroundColor(R.color.colorBackground);
```

### 设置下拉刷新颜色
```java
 mPullLoadMoreRecyclerView.setColorSchemeResources(android.R.color.holo_red_dark,android.R.color.holo_blue_dark);
```

### 快速Top
```java
  mPullLoadMoreRecyclerView.scrollToTop();
```

# 更多交流
Android技术交流群
③群：370527306<a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=0a992ba077da4c8325cbfef1c9e81f0443ffb782a0f2135c1a8f7326baac58ac"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="剩者为王③群" title="剩者为王③群"></a>


# AndroidProgrammer
我的微信公众号：Android高手进阶之路，让我们共同学习，每天进步一点点。欢迎微信扫一扫关注。
![](http://7q5c2h.com1.z0.glb.clouddn.com/qrcode_AndroidProgrammer.jpg)

# 个人博客
[http://wuxiaolong.me/](http://wuxiaolong.me/)

# 作者微博
[吴小龙同學](http://weibo.com/u/2175011601)

# License
Apache 2.0
