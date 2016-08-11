package com.qiang.qiangguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.IMainGuideView;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.fragment.MapFragment;
import com.qiang.qiangguide.fragment.NearExhibitFragment;
import com.qiang.qiangguide.presenter.MainGuidePresenter;
import com.qiang.qiangguide.util.LogUtil;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.List;

public class MainGuideActivity extends ActivityBase implements IMainGuideView,
        NearExhibitFragment.OnNearExhibitFragmentInteractionListener,
        MapFragment.OnMapFragmentInteractionListener,
        BeaconConsumer
{


    public static final String EXTRA_CURRENT_MEDIA_DESCRIPTION = "com.example.android.uamp.CURRENT_MEDIA_DESCRIPTION";
    private NearExhibitFragment exhibitListFragment;
    private MapFragment mapFragment;

    private RadioGroup radioGroupTitle;
    private TextView tvToast;
    private MainGuidePresenter presenter;
    private String fragmentFlag;

    private BeaconManager beaconManager;
    private String museumId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guide);
        findView();
        addListener();
        presenter=new MainGuidePresenter(this);
        Intent intent=getIntent();
        String flag=intent.getStringExtra(INTENT_FRAGMENT_FLAG);
        museumId=intent.getStringExtra(Constants.INTENT_MUSEUM_ID);
        setFragmentFlag(flag);
        presenter.setDefaultFragment();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    private void addListener() {
        radioGroupTitle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                presenter.onCheckedRadioButton(checkedId);
            }
        });
    }

    private void findView() {
        initToolBar();
        initErrorView();
        exhibitListFragment = NearExhibitFragment.newInstance();
        mapFragment = MapFragment.newInstance();
        mapFragment.setMuseumId(museumId);
        radioGroupTitle=(RadioGroup)findViewById(R.id.radioGroupTitle);
        tvToast=(TextView)findViewById(R.id.tvToast);
    }

    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_radio);
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id 'toolbar'");
        }
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
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


    @Override
    void errorRefresh() {

    }

    @Override
    public void onExhibitChoose(Exhibit exhibit) {

    }

    @Override
    public void refreshNearExhibitList() {
        if(exhibitListFragment!=null){
            exhibitListFragment.refreshNearExhibitList();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toNextActivity(Intent intent) {

    }

    @Override
    public void showNearExhibits() {

    }

    @Override
    public void setNearExhibits(List<Exhibit> exhibitList) {
        if(exhibitList==null||exhibitList.size()==0){return;}
        if(exhibitListFragment!=null){
            exhibitListFragment.setNearExhibits(exhibitList);
        }
    }

    @Override
    public String getMuseumId() {
        return museumId;
    }

    @Override
    public void setChooseExhibit(Exhibit exhibit) {

    }

    @Override
    public Exhibit getChooseExhibit() {
        return null;
    }

    @Override
    public void toPlay() {

    }

    @Override
    public void changeToNearExhibitFragment() {
        FragmentManager fm = getSupportFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        if (exhibitListFragment == null) {
            exhibitListFragment = NearExhibitFragment.newInstance();
        }
        // 使用当前Fragment的布局替代id_content的控件
        transaction.replace(R.id.llExhibitListContent, exhibitListFragment);
        transaction.commit();
    }

    @Override
    public void changeToMapExhibitFragment() {
        FragmentManager fm = getSupportFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
        }
        transaction.replace(R.id.llExhibitListContent, mapFragment);
        transaction.commit();
    }

    @Override
    public String getFragmentFlag() {
        return fragmentFlag;
    }

    @Override
    public void setFragmentFlag(String flag) {
        this.fragmentFlag=flag;
    }


    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                presenter.rangeBeaconsInRegion(beacons,region);
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region(Constants.BEACON_LAYOUT, null, null, null));
        } catch (RemoteException e) {
            LogUtil.e("",e);
        }

    }
}
