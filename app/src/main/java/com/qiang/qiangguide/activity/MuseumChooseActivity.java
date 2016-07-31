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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.IMuseumChooseView;
import com.qiang.qiangguide.adapter.adapterImpl.MuseumAdapter;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.presenter.MuseumChoosePresenter;

import java.util.List;

public class MuseumChooseActivity extends ActivityBase implements IMuseumChooseView {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    private RecyclerView recyclerView;
    private String city;//当前所在城市
    private List<Museum> museumList;//博物馆列表
    private MuseumAdapter adapter;//适配器
    private TextView toolbarTitle;


    private MuseumChoosePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_choose);
        presenter=new MuseumChoosePresenter(this);
        initToolBar();
        findView();
        Intent intent= getIntent();
        city=intent.getStringExtra(Constants.INTENT_CITY);
        presenter.onGetCity();
    }



    private void findView() {
        recyclerView =(RecyclerView)findViewById(R.id.museumRecyclerView);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter=new MuseumAdapter(this);
        //View header=getLayoutInflater().inflate(R.layout.header_museum_list,null);
        //adapter.setHeaderView(header);
        recyclerView.setAdapter(adapter);
        //recyclerView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
    }


    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id 'toolbar'");
        }
        toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mToolbar.inflateMenu(R.menu.museum_list_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent=new Intent(getActivity(),CityChooseActivity.class);
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

                    Intent intent=null;
                    switch (item.getItemId()){
                        /*case R.id.menu_1:
                            intent=new Intent(getActivity(),DownloadManagerActivity.class);
                            break;
                        case R.id.menu_2:
                            intent=new Intent(getActivity(),CollectionActivity.class);
                            break;
                        case R.id.menu_3:
                            intent=new Intent(getActivity(),CityChooseActivity.class);
                            break;
                        *//*case R.id.menu_4:
                            intent=new Intent(getActivity(),MuseumListActivity.class);
                            break;*//*
                        case R.id.menu_5:
                            intent=new Intent(getActivity(),SettingActivity.class);
                            break;*/
                        default:break;
                    }
                    if(intent!=null){
                        startActivity(intent);
                    }
                    //closeDrawer();// TODO: 2016/7/31  
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

    @Override
    public void refreshMuseumList() {
        if(adapter==null||museumList==null){return;}
        adapter.updateData(museumList);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toNextActivity() {

    }

    @Override
    public void setTitle() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(toolbarTitle!=null&& !TextUtils.isEmpty(city)){
                    toolbarTitle.setText(city);
                }
            }
        });
    }

    @Override
    public String getCityName() {
        return city;
    }

    @Override
    public void setMuseumList(List<Museum> museumList) {
        this.museumList=museumList;
    }

    @Override
    void errorRefresh() {
        presenter.onErrorFresh();
    }



    private final DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener(){

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









}
