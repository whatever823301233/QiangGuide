package com.qiang.qiangguide.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.ICollectionView;
import com.qiang.qiangguide.adapter.BaseRecyclerAdapter;
import com.qiang.qiangguide.adapter.adapterImpl.ExhibitAdapter;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.custom.recyclerView.QRecyclerView;
import com.qiang.qiangguide.presenter.CollectionPresenter;
import com.qiang.qiangguide.service.MediaIDHelper;
import com.qiang.qiangguide.util.Utility;

import java.util.List;

import static com.qiang.qiangguide.service.MediaIDHelper.MEDIA_ID_MUSEUM_ID;

public class CollectionActivity extends ActivityBase implements ICollectionView{


    private QRecyclerView qRecyclerView;
    private ExhibitAdapter exhibitAdapter;
    private String  museumId;
    private CollectionPresenter presenter;
    private List<Exhibit> favoriteExhibitList;
    private Exhibit chooseExhibit;
    private String mMediaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        presenter=new CollectionPresenter(this);

        Intent intent=getIntent();
        museumId=intent.getStringExtra(Constants.INTENT_MUSEUM_ID);
        findView();
        addListener();
        presenter.initExhibitList();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onMediaControllerConnected() {
        onConnected();
    }

    private void onConnected() {
        if (mMediaId == null) {
            mMediaId = MediaIDHelper.createBrowseCategoryMediaID(MEDIA_ID_MUSEUM_ID,museumId);
        }

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


    private QRecyclerView.PullLoadMoreListener pullLoadMoreListener =new QRecyclerView.PullLoadMoreListener() {
        @Override
        public void onRefresh() {

        }

        @Override
        public void onLoadMore() {

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
    void errorRefresh() {

    }

    @Override
    public void refreshView() {
        exhibitAdapter.notifyDataSetChanged();
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
    public void toNextActivity(Intent intent) {
        Utility.startActivity(getContext(),intent);
    }

    @Override
    public void onNoData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("您当前未收藏展品!");
        //builder.setNegativeButton("取消",dialogListener );
        builder.setPositiveButton("确定", dialogListener);
        builder.show();
    }


    private DialogInterface.OnClickListener dialogListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case AlertDialog.BUTTON_POSITIVE:
                    finish();
                    break;
            }
        }
    };


    @Override
    public void showFavoriteExhibits() {
        exhibitAdapter.updateData(favoriteExhibitList);
    }

    @Override
    public String getMuseumId() {
        return museumId;
    }

    @Override
    public void setFavoriteExhibitList(List<Exhibit> exhibitList) {
        this.favoriteExhibitList =exhibitList;
    }

    @Override
    public void setChooseExhibit(Exhibit exhibit) {
        this.chooseExhibit=exhibit;
    }

    @Override
    public Exhibit getChooseExhibit() {
        return chooseExhibit;
    }

    @Override
    public void toPlay() {
        String hierarchyAwareMediaID = MediaIDHelper.createMediaID(
                getChooseExhibit().getId(),
                MEDIA_ID_MUSEUM_ID,
                museumId);
        MediaControllerCompat.TransportControls controls= getSupportMediaController().getTransportControls();
        controls.playFromMediaId(hierarchyAwareMediaID,null);
    }

    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id 'toolbar'");
        }
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("收藏");

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

        mToolbar.inflateMenu(R.menu.museum_list_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent=new Intent(getActivity(),CityChooseActivity.class);
                startActivity(intent);
                return true;
            }
        });

    }




}
