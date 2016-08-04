package com.qiang.qiangguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.ITopicView;
import com.qiang.qiangguide.adapter.adapterImpl.ExhibitAdapter;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.custom.recyclerView.QRecyclerView;
import com.qiang.qiangguide.presenter.TopicPresenter;

import java.util.List;

public class TopicActivity extends ActivityBase implements ITopicView{

    private QRecyclerView qRecyclerView;
    private ExhibitAdapter exhibitAdapter;
    private String  museumId;
    private TopicPresenter presenter;
    private List<Exhibit> allExhibitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        presenter=new TopicPresenter(this);
        Intent intent=getIntent();
        museumId=intent.getStringExtra(Constants.INTENT_MUSEUM_ID);
        findView();
        presenter.initAllExhibitList();
    }

    private void findView() {

        qRecyclerView =(QRecyclerView) findViewById(R.id.qRecyclerView);
        //设置上拉刷新文字
        qRecyclerView.setFooterViewText("loading");
        //设置上拉刷新文字颜色
        qRecyclerView.setFooterViewTextColor(R.color.white_1000);
        //设置加载更多背景色
        qRecyclerView.setFooterViewBackgroundColor(R.color.colorAccent);
        qRecyclerView.setLinearLayout();

        qRecyclerView.setOnPullLoadMoreListener(pullLoadMoreListener);
        qRecyclerView.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.layout_recycler_empty_view, null));//setEmptyView

        exhibitAdapter=new ExhibitAdapter(this);

        qRecyclerView.setAdapter(exhibitAdapter);

    }


    private QRecyclerView.PullLoadMoreListener pullLoadMoreListener=new  QRecyclerView.PullLoadMoreListener(){

        @Override
        public void onRefresh() {
        }

        @Override
        public void onLoadMore() {

        }
    };




    @Override
    void errorRefresh() {

    }

    @Override
    public void showLoading() {
        //显示下拉刷新
        qRecyclerView.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        qRecyclerView.setPullLoadMoreCompleted();
    }

    @Override
    public String getMuseumId() {
        return museumId;
    }

    @Override
    public void refreshView() {
        exhibitAdapter.notifyDataSetChanged();
    }

    @Override
    public List<String> getChoosedLabels() {
        return null;
    }

    @Override
    public void addChooseLabels(String label) {

    }

    @Override
    public void removeLabel() {

    }

    @Override
    public void removeLabels(List<String> labels) {

    }

    @Override
    public void toMapView(List<Exhibit> exhibits) {

    }

    @Override
    public void showAllExhibits() {
        exhibitAdapter.updateData(allExhibitList);
    }

    @Override
    public void setAllExhibitList(List<Exhibit> exhibitList) {
        this.allExhibitList =exhibitList;
    }
}
