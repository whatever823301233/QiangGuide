package com.qiang.qiangguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.IMuseumHomeView;
import com.qiang.qiangguide.adapter.adapterImpl.MuseumIconAdapter;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.presenter.MuseumHomePresenter;
import com.qiang.qiangguide.util.LogUtil;
import com.qiang.qiangguide.util.Utility;

import java.util.List;

public class MuseumHomeActivity extends ActivityBase implements IMuseumHomeView {

    private MuseumHomePresenter presenter;
    private TextView toolbarTitle;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageView ivPlayStateCtrl;
    private TextView tvMuseumIntroduce;
    private RelativeLayout rlGuideHome;
    private RelativeLayout rlMapHome;
    private RelativeLayout rlTopicHome;
    private RelativeLayout rlCollectionHome;
    private RelativeLayout rlNearlyHome;
    private MuseumIconAdapter iconAdapter;
    private Museum currentMuseum;
    private List<String> iconUrls;
    private String introduce;
    private String mediaPath;
    private View onClickView;
    private ProgressBar loadingProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_home);

        presenter = new MuseumHomePresenter(this);
        initToolBar();
        Intent intent =  getIntent();
        setIntent(intent);
        currentMuseum =  (Museum) intent.getSerializableExtra(Constants.INTENT_MUSEUM);
        findView();
        addListener();
        presenter.initData();
    }


    private void addListener() {
        rlGuideHome.setOnClickListener(onClickListener);
        rlMapHome.setOnClickListener(onClickListener);
        rlTopicHome.setOnClickListener(onClickListener);
        ivPlayStateCtrl.setOnClickListener(onClickListener);
        rlCollectionHome.setOnClickListener(onClickListener);
        rlNearlyHome.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setOnClickView(v);
            presenter.onImageButtonClick();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i("",TAG+" onResume");
        if (shouldShowControls()) {
            LogUtil.i("",TAG+" onResume 显示fragment");
            showPlaybackControls();
        } else {
            //LogUtil.d("", "connectionCallback.onConnected: " + "hiding controls because metadata is null");
            LogUtil.i("",TAG+" onResume 隐藏fragment");
            hidePlaybackControls();
        }
    }

    private void findView() {
        initErrorView();
        ivPlayStateCtrl = (ImageView) findViewById(R.id.ivPlayStateCtrl);
        loadingProgress = (ProgressBar) findViewById(R.id.loading_progress);
        //llMuseumLargestIcon = (LinearLayout) findViewById(R.id.llMuseumLargestIcon);
        tvMuseumIntroduce = (TextView) findViewById(R.id.tvMuseumIntroduce);
        rlGuideHome = (RelativeLayout) findViewById(R.id.rlGuideHome);
        rlMapHome = (RelativeLayout) findViewById(R.id.rlMapHome);
        rlTopicHome = (RelativeLayout) findViewById(R.id.rlTopicHome);
        rlCollectionHome = (RelativeLayout) findViewById(R.id.rlCollectionHome);
        rlNearlyHome = (RelativeLayout) findViewById(R.id.rlNearlyHome);

        RecyclerView recycleViewMuseumIcon = (RecyclerView) findViewById(R.id.recycleViewMuseumIcon);

         /*设置为横向*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (recycleViewMuseumIcon != null) {
            recycleViewMuseumIcon.setLayoutManager(linearLayoutManager);
            iconAdapter = new MuseumIconAdapter(this);
            recycleViewMuseumIcon.setAdapter(iconAdapter);
            recycleViewMuseumIcon.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        }


    }

    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id 'toolbar'");
        }
        toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mToolbar.inflateMenu(R.menu.search_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);
                return true;
            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            if (navigationView == null) {
                throw new IllegalStateException("Layout requires a NavigationView with id 'nav_view'");
            }

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {

                    Intent intent = null;
                    switch (item.getItemId()){
                        case R.id.menu_1:
                            intent = new Intent(getActivity(),DownloadManagerActivity.class);
                            break;
                        /*case R.id.menu_2:
                            intent = new Intent(getActivity(),CollectionActivity.class);
                            break;*/
                        case R.id.menu_3:
                            intent = new Intent(getActivity(),CityChooseActivity.class);
                            break;
                        case R.id.menu_4:
                            intent = new Intent(getActivity(),MuseumChooseActivity.class);
                            break;
                        case R.id.menu_5:
                            intent = new Intent(getActivity(),SettingActivity.class);
                            break;
                    }
                    if(intent != null){
                        startActivity(intent);
                    }
                    closeDrawer();
                    return true;
                }
            });
            // Create an ActionBarDrawerToggle that will handle opening/closing of the drawer:
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.app_name, R.string.app_name);
            mDrawerLayout.addDrawerListener(mDrawerListener);
            //populateDrawerItems(navigationView);
            //setSupportActionBar(mToolbar);
            updateDrawerToggle();
        } else {
            setSupportActionBar(mToolbar);
        }

    }

    @Override
    public void closeDrawer(){
        if(mDrawerLayout == null || navigationView == null){return;}
        if(mDrawerLayout.isDrawerOpen(navigationView)){
            mDrawerLayout.closeDrawer(navigationView);
        }
    }
    protected void updateDrawerToggle() {
        if (mDrawerToggle == null) {
            return;
        }
        boolean isRoot = getFragmentManager().getBackStackEntryCount() == 0;
        mDrawerToggle.setDrawerIndicatorEnabled(isRoot);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(!isRoot);
            getSupportActionBar().setDisplayHomeAsUpEnabled(!isRoot);
            getSupportActionBar().setHomeButtonEnabled(!isRoot);
        }
        if (isRoot) {
            mDrawerToggle.syncState();
        }
    }


    public final DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener(){

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };


    @Override
    void errorRefresh() {

    }

    @Override
    public void refreshView() {

    }

    @Override
    public void showLoading() {
        loadingProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingProgress.setVisibility(View.GONE);
    }

    @Override
    public void toNextActivity(Intent intent) {
        Utility.startActivity(getActivity(),intent);
    }

    @Override
    public void setCurrentMuseum(Museum museum) {
        this.currentMuseum = museum;
    }

    @Override
    public Museum getCurrentMuseum() {
        return currentMuseum;
    }

    @Override
    public void setTitle(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    public void setIntroduce(String text) {
        introduce = text;
    }

    @Override
    public void refreshIntroduce() {
        if(TextUtils.isEmpty(introduce)){return;}
        tvMuseumIntroduce.setText(introduce);
    }

    @Override
    public void setIconUrls(List<String> iconUrls) {
        this.iconUrls = iconUrls;
    }

    @Override
    public void refreshIcons() {
        if(iconUrls == null || iconAdapter == null){return;}
        iconAdapter.updateData(iconUrls);
    }

    @Override
    public void refreshMedia() {

    }

    @Override
    public void setAdapterMuseumId() {
        if(currentMuseum == null || iconAdapter == null ) return;
        iconAdapter.setMuseumId(currentMuseum.getId());
    }

    @Override
    public void setMediaPath(String s) {
        this.mediaPath = s;
    }

    @Override
    public void setOnClickView(View view) {
        onClickView = view;
    }

    @Override
    public View getOnClickView() {
        return onClickView;
    }

    @Override
    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen( navigationView );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean flag = presenter.onKeyDown(keyCode, event);
        return flag || super.onKeyDown(keyCode, event);
    }

}
