package com.qiang.qiangguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.ITopicView;
import com.qiang.qiangguide.adapter.BaseRecyclerAdapter;
import com.qiang.qiangguide.adapter.adapterImpl.ExhibitAdapter;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.custom.recyclerView.QRecyclerView;
import com.qiang.qiangguide.fragment.PlaybackControlsFragment;
import com.qiang.qiangguide.presenter.TopicPresenter;
import com.qiang.qiangguide.service.MediaIDHelper;
import com.qiang.qiangguide.util.Utility;

import java.util.List;

import static com.qiang.qiangguide.service.MediaIDHelper.MEDIA_ID_MUSEUM_ID;

public class TopicActivity extends ActivityBase implements ITopicView{

    private QRecyclerView qRecyclerView;
    private ExhibitAdapter exhibitAdapter;
    private String  museumId;
    private TopicPresenter presenter;
    private List<Exhibit> allExhibitList;
    private Exhibit chooseExhibit;
    private String mMediaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        presenter=new TopicPresenter(this);
        Intent intent=getIntent();
        museumId=intent.getStringExtra(Constants.INTENT_MUSEUM_ID);
        findView();
        addListener();
        presenter.initAllExhibitList();
        mControlsFragment = new PlaybackControlsFragment();
        showPlaybackControls();
    }

    @Override
    protected void onStart() {
        super.onStart();
        hidePlaybackControls();
    }


    public void onConnected() {
        if (mMediaId == null) {
            mMediaId = MediaIDHelper.createBrowseCategoryMediaID(MEDIA_ID_MUSEUM_ID,museumId);
        }
        //updateTitle();

        // Unsubscribing before subscribing is required if this mediaId already has a subscriber
        // on this MediaBrowser instance. Subscribing to an already subscribed mediaId will replace
        // the callback, but won't trigger the initial callback.onChildrenLoaded.
        //
        // This is temporary: A bug is being fixed that will make subscribe
        // consistently call onChildrenLoaded initially, no matter if it is replacing an existing
        // subscriber or not. Currently this only happens if the mediaID has no previous
        // subscriber or if the media content changes on the service side, so we need to
        // unsubscribe first.
        getMediaBrowser().unsubscribe(mMediaId);
        getMediaBrowser().subscribe(mMediaId, mSubscriptionCallback);

    }

    private MediaBrowserCompat.SubscriptionCallback mSubscriptionCallback=new MediaBrowserCompat.SubscriptionCallback() {
        @Override
        public void onChildrenLoaded(@NonNull String parentId, List<MediaBrowserCompat.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);
        }

        @Override
        public void onError(@NonNull String parentId) {
            super.onError(parentId);
        }

        @Override
        public void onError(@NonNull String parentId, @NonNull Bundle options) {
            super.onError(parentId, options);
        }
    };


    private void addListener() {
        exhibitAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Exhibit exhibit=exhibitAdapter.getExhibit(position);
                setChooseExhibit(exhibit);
                presenter.onExhibitChoose();
            }
        });
    }


    @Override
    protected void onMediaControllerConnected() {
        onConnected();
    }

    @Override
    public void toPlay(){
        String hierarchyAwareMediaID = MediaIDHelper.createMediaID(
                getChooseExhibit().getId(),
                MEDIA_ID_MUSEUM_ID,
                museumId);
        MediaControllerCompat.TransportControls controls= getSupportMediaController().getTransportControls();
        controls.playFromMediaId(hierarchyAwareMediaID,null);
    }


    private void findView() {
        initToolBar();
        initErrorView();
        qRecyclerView =(QRecyclerView) findViewById(R.id.qRecyclerView);
        //设置上拉刷新文字颜色
        assert qRecyclerView != null;
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
        public void onRefresh() {}
        @Override
        public void onLoadMore() {}
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
    public List<String> getChooseLabels() {
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
    public void toNextActivity(Intent intent) {
        Utility.startActivity(getContext(),intent);
    }

    @Override
    public void showAllExhibits() {
        exhibitAdapter.updateData(allExhibitList);
    }

    @Override
    public void setAllExhibitList(List<Exhibit> exhibitList) {
        this.allExhibitList =exhibitList;
    }

    @Override
    public void setChooseExhibit(Exhibit exhibit) {
        this.chooseExhibit=exhibit;
    }

    @Override
    public Exhibit getChooseExhibit() {
        return chooseExhibit;
    }


    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id 'toolbar'");
        }
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("专题");

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

}
