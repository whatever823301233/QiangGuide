package com.qiang.qiangguide.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.custom.lyric.LyricAdapter;
import com.qiang.qiangguide.custom.lyric.LyricDownloadManager;
import com.qiang.qiangguide.custom.lyric.LyricLoadHelper;
import com.qiang.qiangguide.custom.lyric.LyricSentence;
import com.qiang.qiangguide.util.LogUtil;

import java.io.File;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LyricFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LyricFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LyricFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private ListView lvLyric;
    private TextView tvContent;
    private LyricLoadHelper mLyricLoadHelper;
    private LyricAdapter mLyricAdapter;
    private Exhibit exhibit;
    private String lyricUrl;
    private String museumId;

    public LyricFragment() {
    }

    public static LyricFragment newInstance() {
        return new LyricFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setExhibit(Exhibit exhibit) {
        this.exhibit = exhibit;
    }

    @Override
    void initView() {
        setContentView(R.layout.fragment_lyric);
        initView(contentView);

    }

    private void initView(View view) {
        lvLyric=(ListView)view.findViewById(R.id.lvLyric);
        tvContent=(TextView)view.findViewById(R.id.tvContent);
        lvLyric.setAdapter(mLyricAdapter);
        lvLyric.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        addListener();
    }

    private void addListener() {

    }


    public void loadLyricByHand() {

        if(TextUtils.isEmpty(lyricUrl)){return;}
        try{
            if(mLyricLoadHelper==null){
                mLyricLoadHelper=new LyricLoadHelper();
                mLyricLoadHelper.setLyricListener(mLyricListener);
            }
            if(mLyricAdapter==null){
                mLyricAdapter = new LyricAdapter(getActivity());
                if(lvLyric!=null){
                    lvLyric.setAdapter(mLyricAdapter);
                }
            }
            String name = lyricUrl.replaceAll("/", "_");
            // 取得歌曲同目录下的歌词文件绝对路径
            String lyricFilePath = Constants.LOCAL_PATH+museumId+"/"+ name;
            File lyricFile = new File(lyricFilePath);
            if (lyricFile.exists()) {
                // 本地有歌词，直接读取
                mLyricLoadHelper.loadLyric(lyricFilePath);
            } else {
                //mIsLyricDownloading = true;
                // 尝试网络获取歌词
                //LogUtil.i("ZHANG", "loadLyric()--->本地无歌词，尝试从网络获取");
                new LyricDownloadAsyncTask().execute(lyricUrl);
            }
        }catch (Exception e){
            LogUtil.e("",e);
        }
    }

    public void notifyTime(long currentPosition) {
        if(mLyricLoadHelper!=null){
            mLyricLoadHelper.notifyTime(currentPosition);
        }
    }

    private  class LyricDownloadAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            LyricDownloadManager mLyricDownloadManager = new LyricDownloadManager(getActivity());
            // 从网络获取歌词，然后保存到本地
            String savePath=Constants.LOCAL_PATH + museumId ;//+ "/"
            String lyricName=params[0];
            // 返回本地歌词路径
            // mIsLyricDownloading = false;
            return mLyricDownloadManager.searchLyricFromWeb(lyricName, savePath);
        }

        @Override
        protected void onPostExecute(String lyricSavePath) {
            // Log.i(TAG, "网络获取歌词完毕，歌词保存路径:" + result);
            // 读取保存到本地的歌曲
            if(mLyricLoadHelper!=null){
                mLyricLoadHelper.loadLyric(lyricSavePath);
            }
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mLyricLoadHelper = new LyricLoadHelper();
        mLyricLoadHelper.setLyricListener(mLyricListener);
        mLyricAdapter = new LyricAdapter(activity);

    }


    private LyricLoadHelper.LyricListener mLyricListener = new LyricLoadHelper.LyricListener() {

        @Override
        public void onLyricLoaded(List<LyricSentence> lyricSentences, int index) {
            if (lyricSentences != null) {
                //LogUtil.i(TAG, "onLyricLoaded--->歌词句子数目=" + lyricSentences.size() + ",当前句子索引=" + index);
                if(mLyricAdapter==null){
                    mLyricAdapter=new LyricAdapter(getActivity());
                    lvLyric.setAdapter(mLyricAdapter);
                }
                mLyricAdapter.setLyric(lyricSentences);
                mLyricAdapter.setCurrentSentenceIndex(index);
                mLyricAdapter.notifyDataSetChanged();
            }
        }
        @Override
        public void onLyricSentenceChanged(int indexOfCurSentence) {
            if(mLyricAdapter==null){return;}
            mLyricAdapter.setCurrentSentenceIndex(indexOfCurSentence);
            mLyricAdapter.notifyDataSetChanged();
            lvLyric.smoothScrollToPositionFromTop(indexOfCurSentence, lvLyric.getHeight() / 2, 500);
            //tvContent.setText(exhibit.getContent());

        }
    };



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setLyricUrl(String lyricUrl) {
        this.lyricUrl = lyricUrl;
    }

    public void refreshLyricContent() {
        loadLyricByHand();
    }

    public void setMuseumId(String museumId) {
        this.museumId = museumId;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
