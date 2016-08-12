package com.qiang.qiangguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.ITopicView;
import com.qiang.qiangguide.adapter.BaseRecyclerAdapter;
import com.qiang.qiangguide.adapter.adapterImpl.ExhibitAdapter;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.custom.ColumnHorizontalScrollView;
import com.qiang.qiangguide.custom.channel.ChannelItem;
import com.qiang.qiangguide.custom.recyclerView.QRecyclerView;
import com.qiang.qiangguide.fragment.PlaybackControlsFragment;
import com.qiang.qiangguide.presenter.TopicPresenter;
import com.qiang.qiangguide.service.MediaIDHelper;
import com.qiang.qiangguide.util.AndroidUtil;
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
    /** 自定义HorizontalScrollView */
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    private LinearLayout mRadioGroup_content;
    private ImageView button_more_columns;
    private LinearLayout ll_more_columns;
    private RelativeLayout rl_column;

    /** 左阴影部分*/
    public ImageView shade_left;
    /** 右阴影部分 */
    public ImageView shade_right;

    /** 请求CODE */
    public final static int CHANNEL_REQUEST = 1;
    private List<ChannelItem> userChannelList;
    private int mScreenWidth;
    private int mItemWidth;
    private int columnSelectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        mScreenWidth = AndroidUtil.getWindowsWidth(this);
        mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7
        presenter=new TopicPresenter(this);
        Intent intent=getIntent();
        museumId=intent.getStringExtra(Constants.INTENT_MUSEUM_ID);
        findView();
        addListener();
        presenter.initAllExhibitList();
        mControlsFragment = new PlaybackControlsFragment();
        showPlaybackControls();
        setChannelView();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        hidePlaybackControls();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     *  当栏目项发生变化时候调用
     * */
    private void setChannelView() {
        presenter.checkChannelList();
        initTabColumn();
    }

    /**
     *  初始化Column栏目项
     * */
    private void initTabColumn() {

            mRadioGroup_content.removeAllViews();
            int count =  userChannelList.size();
            mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
            for(int i = 0; i< count; i++){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , LinearLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 5;
                params.rightMargin = 5;
//			TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item, null);
                TextView columnTextView = new TextView(this);
                columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
                columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
                columnTextView.setGravity(Gravity.CENTER);
                columnTextView.setPadding(5, 5, 5, 5);
                columnTextView.setId(i);
                columnTextView.setText(userChannelList.get(i).getName());
                columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
                if(columnSelectIndex == i){// TODO: 2016/8/12
                    columnTextView.setSelected(true);
                }
                columnTextView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
                            View localView = mRadioGroup_content.getChildAt(i);
                            if (localView != v)
                                localView.setSelected(false);
                            else{
                                localView.setSelected(true);
                                //mViewPager.setCurrentItem(i);// TODO: 2016/8/12
                            }
                        }
                        Toast.makeText(getApplicationContext(), userChannelList.get(v.getId()).getName(), Toast.LENGTH_SHORT).show();
                    }
                });
                mRadioGroup_content.addView(columnTextView, i ,params);
            }
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


        button_more_columns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent_channel = new  Intent(getApplicationContext(), TopicChooseActivity.class);
                intent_channel.putExtra(Constants.INTENT_MUSEUM_ID,museumId);
                startActivityForResult(intent_channel, CHANNEL_REQUEST);
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    @Override
    public void onNoData() {
        showToast("无符合数据..");
    }

    @Override
    public void setUserChannelList(List<ChannelItem> channelItems) {
        this.userChannelList=channelItems;
    }

    private void findView() {
        initToolBar();
        initErrorView();

        mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
        ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
        rl_column = (RelativeLayout) findViewById(R.id.rl_column);
        button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
        shade_left = (ImageView) findViewById(R.id.shade_left);
        shade_right = (ImageView) findViewById(R.id.shade_right);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHANNEL_REQUEST){
            setChannelView();
        }
    }

    @Override
    public void errorRefresh() {

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
    public void refreshExhibitList() {
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
