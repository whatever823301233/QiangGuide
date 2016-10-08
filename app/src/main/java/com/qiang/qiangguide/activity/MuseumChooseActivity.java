package com.qiang.qiangguide.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.IMuseumChooseView;
import com.qiang.qiangguide.adapter.BaseRecyclerAdapter;
import com.qiang.qiangguide.adapter.adapterImpl.MuseumAdapter;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.presenter.MuseumChoosePresenter;
import com.qiang.qiangguide.util.Utility;

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
    private Museum chooseMuseum;
    private MuseumChoosePresenter presenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_choose);
        presenter=new MuseumChoosePresenter(this);
        findView();
        addListener();
        Intent intent= getIntent();
        setIntent(intent);
        city=intent.getStringExtra(Constants.INTENT_CITY);
        presenter.onGetCity();
    }

    private void findView() {
        initToolBar();
        initErrorView();
        recyclerView =(RecyclerView)findViewById(R.id.museumRecyclerView);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter=new MuseumAdapter(this);
        View header=getLayoutInflater().inflate(R.layout.header_museum_list,recyclerView,false);
        adapter.setHeaderView(header);
        recyclerView.setAdapter(adapter);
        //recyclerView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
    }


    private void addListener() {
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Museum museum=adapter.getMuseum(position);
                setChooseMuseum(museum);
                //ProgressBarManager.loadWaitPanel(getActivity(),"正在加载。。。",false);
                presenter.onMuseumChoose();
            }
        });
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
                        case R.id.menu_1:
                            intent=new Intent(getActivity(),DownloadManagerActivity.class);
                            break;
                        /*case R.id.menu_2:
                            intent=new Intent(getActivity(),CollectionActivity.class);
                            break;*/
                        case R.id.menu_3:
                            intent=new Intent(getActivity(),CityChooseActivity.class);
                            break;
                        case R.id.menu_4:
                            intent=new Intent(getActivity(),MuseumChooseActivity.class);
                            break;
                        case R.id.menu_5:
                            intent=new Intent(getActivity(),SettingActivity.class);
                            break;
                        default:break;
                    }
                    if(intent!=null){
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


    public void setChooseMuseum(Museum chooseMuseum) {
        this.chooseMuseum = chooseMuseum;
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
    public void toNextActivity(Intent intent) {
        Utility.startActivity(getActivity(),intent);
        finish();
    }

    @Override
    public void setTitle() {
        if(toolbarTitle!=null&& !TextUtils.isEmpty(city)){
            toolbarTitle.setText(city);
        }
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
    public void closeDrawer() {
        if(mDrawerLayout==null||navigationView==null){return;}
        if(mDrawerLayout.isDrawerOpen(navigationView)){
            mDrawerLayout.closeDrawer(navigationView);
        }
    }

    @Override
    public Museum getChooseMuseum() {
        return chooseMuseum;
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


    /**
     * 这是兼容的 AlertDialog
     */
    @Override
    public  void showDownloadTipDialog() {
  /*
  这里使用了 android.support.v7.app.AlertDialog.Builder
  可以直接在头部写 import android.support.v7.app.AlertDialog
  那么下面就可以写成 AlertDialog.Builder
  */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("收听讲解需要先下载至本地，是否下载？");
        builder.setNegativeButton("取消",dialogListener );
        builder.setPositiveButton("确定", dialogListener);
        builder.show();
    }

    @Override
    public void setDownloadProgress(final int progress, final int totalSize) {
        if(progressDialog==null){
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int pro=progress*100/totalSize;
                progressDialog.setProgress(pro);
            }
        });

    }

    @Override
    public void showProgressDialog() {

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setIcon(R.drawable.ic_file_download_grey600_24dp);
        progressDialog.setTitle("正在下载");
        progressDialog.setMessage("进度");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if(progressDialog==null){return;}
        progressDialog.dismiss();
    }

    private DialogInterface.OnClickListener dialogListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case AlertDialog.BUTTON_POSITIVE:
                    presenter.onDownloadMuseum();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    dialog.dismiss();
            }
        }
    };


}
