package com.qiang.qiangguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.IMainGuideView;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.fragment.MapFragment;
import com.qiang.qiangguide.fragment.NearExhibitFragment;
import com.qiang.qiangguide.manager.MyBluetoothManager;
import com.qiang.qiangguide.presenter.MainGuidePresenter;
import com.qiang.qiangguide.service.MediaIDHelper;
import com.qiang.qiangguide.util.LogUtil;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.List;

import static com.qiang.qiangguide.service.MediaIDHelper.MEDIA_ID_MUSEUM_ID;

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
    private Exhibit chooseExhibit;
    private String mMediaId;
    private RadioButton radioButtonMap;
    private RadioButton radioButtonList;
    private List<Exhibit> nearExhibits;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guide);
        presenter = new MainGuidePresenter(this);
        findView();
        addListener();
        Intent intent = getIntent();
        getIntentMsg(intent);
        MyBluetoothManager.openBluetooth(getApplicationContext());
        beaconManager = BeaconManager.getInstanceForApplication(getApplicationContext());
        beaconManager.bind(this);
    }

    private void getIntentMsg(Intent intent) {
        String flag = intent.getStringExtra(INTENT_FRAGMENT_FLAG);
        String tempMuseumId = intent.getStringExtra(Constants.INTENT_MUSEUM_ID);
        if(!TextUtils.isEmpty(tempMuseumId)){
            museumId = tempMuseumId;
        }
        setFragmentFlag(flag);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentMsg(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setDefaultFragment();
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
        radioGroupTitle = (RadioGroup)findViewById(R.id.radioGroupTitle);
        radioButtonMap = (RadioButton)findViewById(R.id.radioButtonMap);
        radioButtonList = (RadioButton)findViewById(R.id.radioButtonList);

        tvToast = (TextView)findViewById(R.id.tvToast);
    }

    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_radio);
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id 'toolbar'");
        }
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
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
                Intent intent = new Intent(getActivity(),CityChooseActivity.class);
                startActivity(intent);
                return true;
            }
        });

    }


    @Override
    void errorRefresh() {

    }

    @Override
    public void onExhibitChoose() {
        Exhibit exhibit = exhibitListFragment.getChooseExhibit();
        setChooseExhibit(exhibit);
        presenter.onExhibitChoose();

    }

    @Override
    public void refreshNearExhibitList() {
        if(exhibitListFragment != null){
            exhibitListFragment.refreshNearExhibitList();
        }
    }

    @Override
    public void showLoading() {
        if(exhibitListFragment != null){
            exhibitListFragment.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if(exhibitListFragment != null){
            exhibitListFragment.hideLoading();
        }
    }

    @Override
    public void toNextActivity(Intent intent) {

    }

    @Override
    public void showNearExhibits() {

    }

    @Override
    public void setNearExhibits(List<Exhibit> exhibitList) {
        if(exhibitList == null || exhibitList.size() == 0){return;}
        this.nearExhibits = exhibitList;
        if(exhibitListFragment != null){
            exhibitListFragment.setNearExhibits(exhibitList);
        }
    }

    @Override
    public String getMuseumId() {
        return museumId;
    }

    @Override
    public void setChooseExhibit(Exhibit exhibit) {
        chooseExhibit = exhibit;
    }

    @Override
    public Exhibit getChooseExhibit() {
        return chooseExhibit ;
    }

    @Override
    public void toPlay() {
        String hierarchyAwareMediaID = MediaIDHelper.createMediaID(
                getChooseExhibit().getId(),
                MEDIA_ID_MUSEUM_ID,
                museumId);
        MediaControllerCompat.TransportControls controls = getSupportMediaController().getTransportControls();
        controls.playFromMediaId(hierarchyAwareMediaID,null);
    }

    @Override
    public void changeToNearExhibitFragment() {
        try{
            FragmentManager fm = getSupportFragmentManager();
            // 开启Fragment事务
            FragmentTransaction transaction = fm.beginTransaction();
            if (exhibitListFragment == null) {
                exhibitListFragment = NearExhibitFragment.newInstance();
            }
            // 使用当前Fragment的布局替代id_content的控件
            transaction.replace(R.id.llExhibitListContent, exhibitListFragment);
            transaction.commit();
            radioButtonList.setChecked(true);
        }catch (Exception e){
            LogUtil.e("",e);
        }

    }

    @Override
    public void changeToMapExhibitFragment() {
        try{
            FragmentManager fm = getSupportFragmentManager();
            // 开启Fragment事务
            FragmentTransaction transaction = fm.beginTransaction();
            if (mapFragment == null) {
                mapFragment = MapFragment.newInstance();
            }
            transaction.replace(R.id.llExhibitListContent, mapFragment);
            transaction.commit();
            radioButtonMap.setChecked(true);
        }catch (Exception e){
            LogUtil.e("",e);
        }

    }

    @Override
    public String getFragmentFlag() {
        return fragmentFlag;
    }

    @Override
    public void setFragmentFlag(String flag) {
        this.fragmentFlag = flag;
    }

    @Override
    public void autoPlayExhibit(Exhibit exhibit) {
        String hierarchyAwareMediaID = MediaIDHelper.createMediaID(
                exhibit.getId(),
                MEDIA_ID_MUSEUM_ID,
                exhibit.getMuseumId());
        MediaControllerCompat.TransportControls controls = getSupportMediaController().getTransportControls();
        controls.playFromMediaId(hierarchyAwareMediaID,null);
    }

    @Override
    public List<Exhibit> getNearExhibits() {
        return nearExhibits;
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


    @Override
    protected void onMediaControllerConnected() {
        onConnected();
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

    private MediaBrowserCompat.SubscriptionCallback mSubscriptionCallback = new MediaBrowserCompat.SubscriptionCallback() {
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


}
