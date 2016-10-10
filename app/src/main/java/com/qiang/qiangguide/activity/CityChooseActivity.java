package com.qiang.qiangguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.ICityChooseView;
import com.qiang.qiangguide.adapter.adapterImpl.CityAdapter;
import com.qiang.qiangguide.bean.City;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.custom.SideBar;
import com.qiang.qiangguide.presenter.CityChoosePresenter;
import com.qiang.qiangguide.util.KeyBoardUtil;
import com.qiang.qiangguide.util.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CityChooseActivity extends ActivityBase implements ICityChooseView {

    private CityAdapter adapter;//适配器
    //private ClearEditText mClearEditText;
    private List<City> cities;
    private String chooseCity;
    //private AMapLocationClient locationClient;
    //private AMapLocationClientOption locationOption;
    private TextView currentCity,suggestCity;
    private ProgressBar loadingProgress;

    private CityChoosePresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_choose);
        presenter = new CityChoosePresenter(this);

        setToolbar();
        findView();
        addListener();
        presenter.initData();
    }

    @Override
    void errorRefresh() {
        presenter.onErrorFresh();
    }

    private void setToolbar() {
        View v = findViewById(R.id.toolbar);
        if (v == null) {return;}
        Toolbar toolbar = (Toolbar) v;
        setSupportActionBar(toolbar);
        TextView toolbarTitle = (TextView) v.findViewById(R.id.toolbar_title);
        if (toolbarTitle == null) {return;}
        toolbarTitle.setText(getResources().getString(R.string.city_title));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void addListener() {

        adapter.setOnItemClickListener(new CityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                City city = adapter.getItem(position);
                presenter.onCityChoose(city);
            }
        });

    }

    private void findView() {
        initErrorView();
        //实例化汉字转拼音类
        SideBar sideBar = (SideBar) findViewById(R.id.sidebar);
        TextView dialog = (TextView) findViewById(R.id.dialog);
        if (sideBar != null) {
            sideBar.setTextView(dialog);
        }
        RecyclerView cityRecyclerView = (RecyclerView) findViewById(R.id.city_recyclerView);
        loadingProgress = (ProgressBar) findViewById(R.id.loading_progress);
        LinearLayoutManager linearLayoutManage=new LinearLayoutManager(getActivity());
        linearLayoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        if (cityRecyclerView != null) {
            cityRecyclerView.setLayoutManager(linearLayoutManage);
        }
        //mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        currentCity = (TextView) findViewById(R.id.currentCity);
        suggestCity = (TextView) findViewById(R.id.suggestCity);
        //refreshBtn=(Button)mErrorView.findViewById(R.id.refreshBtn);

        //mClearEditText.clearFocus();
        cities = new ArrayList<>();
        // 根据a-z进行排序源数据
        Collections.sort(cities, pinyinComparator);
        adapter = new CityAdapter(this);
        if (cityRecyclerView != null) {
            cityRecyclerView.setAdapter(adapter);
            //去除滑动到末尾时的阴影
            cityRecyclerView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        }
    }

    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingProgress.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingProgress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void hideKeyboard() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                KeyBoardUtil.hideKeyBoard(getActivity());
            }
        });
    }

    @Override
    public void refreshView() {
        if(adapter == null||cities == null){return;}
        adapter.updateData(cities);
    }

    @Override
    public String getCurrentInput() {
        return null;
    }

    @Override
    public List<City> getListCities() {
        return null;
    }

    @Override
    public void setListCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public void updateTipCities(List<City> cities) {

    }

    @Override
    public void setLocationCity(City city) {

    }

    @Override
    public void clearInputCity() {

    }

    @Override
    public void toNextActivity(String cityName) {
        showToast(cityName);
        Intent intent = new Intent(getActivity(),MuseumChooseActivity.class);
        intent.putExtra(Constants.INTENT_CITY,cityName);
        Utility.startActivity(getActivity(),intent);
        finish();
    }


    @Override
    public City getChooseCity() {
        return null;
    }

    @Override
    public void setChooseCity(City city) {

    }

    private Comparator<City> pinyinComparator = new  Comparator<City>(){

        @Override
        public int compare(City lhs, City rhs) {
            if (lhs.getAlpha().equals("@")
                    || rhs.getAlpha().equals("#")) {
                return -1;
            } else if (lhs.getAlpha().equals("#")
                    || rhs.getAlpha().equals("@")) {
                return 1;
            } else {
                return lhs.getAlpha().compareTo(rhs.getAlpha());
            }
        }
    };


}
