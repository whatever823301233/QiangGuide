package com.qiang.qiangguide.custom.recyclerView;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by Qiang on 2016/7/21.
 */
public class SwipeRefreshLayoutOnRefresh implements SwipeRefreshLayout.OnRefreshListener {

    private QRecyclerView recyclerView;

    public SwipeRefreshLayoutOnRefresh(QRecyclerView pullLoadMoreRecyclerView) {
        this.recyclerView = pullLoadMoreRecyclerView;
    }

    @Override
    public void onRefresh() {
        if (!recyclerView.isRefresh()) {
            recyclerView.setIsRefresh(true);
            recyclerView.refresh();
        }
    }

}
