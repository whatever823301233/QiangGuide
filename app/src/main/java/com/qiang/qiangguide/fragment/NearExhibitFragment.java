package com.qiang.qiangguide.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.aInterface.IMainGuideView;
import com.qiang.qiangguide.adapter.adapterImpl.ExhibitAdapter;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.custom.recyclerView.QRecyclerView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnNearExhibitFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NearExhibitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearExhibitFragment extends BaseFragment implements IMainGuideView{

    private OnNearExhibitFragmentInteractionListener mListener;
    private QRecyclerView recyclerView;
    private ExhibitAdapter adapter;
    private List<Exhibit> nearExhibitList;

    public NearExhibitFragment() {
    }

    public static NearExhibitFragment newInstance() {
        return new NearExhibitFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void initView() {
        setContentView(R.layout.fragment_near_exhibit);
        adapter=new ExhibitAdapter(getActivity());
        recyclerView=(QRecyclerView)contentView.findViewById(R.id.qRecyclerView);
        recyclerView.setLinearLayout();
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNearExhibitFragmentInteractionListener) {
            mListener = (OnNearExhibitFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNearExhibitFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void refreshNearExhibitList() {
        if(adapter==null||nearExhibitList==null){return;}
        adapter.updateData(nearExhibitList);
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
    public void showFailedError() {

    }

    @Override
    public void hideErrorView() {

    }

    @Override
    public void showNearExhibits() {

    }

    @Override
    public void setNearExhibits(List<Exhibit> exhibitList) {
        this.nearExhibitList=exhibitList;
    }

    @Override
    public String getMuseumId() {
        return null;
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

    }

    @Override
    public void changeToMapExhibitFragment() {

    }

    @Override
    public String getFragmentFlag() {
        return null;
    }

    @Override
    public void setFragmentFlag(String flag) {

    }

    public interface OnNearExhibitFragmentInteractionListener {

        void onExhibitChoose(Exhibit exhibit);

    }
}
