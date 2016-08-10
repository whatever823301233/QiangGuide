package com.qiang.qiangguide.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.bean.Exhibit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnNearExhibitFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NearExhibitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearExhibitFragment extends BaseFragment {

    private OnNearExhibitFragmentInteractionListener mListener;

    public NearExhibitFragment() {
    }

    public static NearExhibitFragment newInstance() {
        return new NearExhibitFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    void initView() {
        setContentView(R.layout.fragment_near_exhibit);
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

    public interface OnNearExhibitFragmentInteractionListener {

        void onExhibitChoose(Exhibit exhibit);
        
    }
}
