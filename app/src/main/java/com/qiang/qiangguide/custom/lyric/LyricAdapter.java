package com.qiang.qiangguide.custom.lyric;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xq823 on 2016/8/22.
 *
 * 歌词适配器
 */
public class LyricAdapter extends BaseAdapter {

    private static final String TAG = LyricAdapter.class.getSimpleName();

    /**
     * 歌词句子集合
     */
    List<LyricSentence> mLyricSentences = null;
    Context mContext = null;
    /**
     * 当前的句子索引号
     */
    int mIndexOfCurrentSentence = 0;
    float mCurrentSize = 16;
    float mNotCurrentSize = 13;

    public LyricAdapter(Context context) {
        if(context==null){return;}
        mContext = context.getApplicationContext();
        mLyricSentences = new ArrayList<>();
        mIndexOfCurrentSentence = 0;
    }

    /**
     * 设置歌词，由外部调用
     */
    public void setLyric(List<LyricSentence> lyric) {
        if(mLyricSentences==null){return;}
        mLyricSentences.clear();
        if (lyric != null) {
            mLyricSentences.addAll(lyric);
            //LogUtil.i(TAG, "歌词句子数目=" + mLyricSentences.size());
        }
        mIndexOfCurrentSentence = 0;
    }

    @Override
    public boolean isEmpty() {
        // 歌词为空时，让ListView显示EmptyView
        if (mLyricSentences == null) {
            LogUtil.i(TAG, "isEmpty:null");
            return true;
        } else if (mLyricSentences.size() == 0) {
            LogUtil.i(TAG, "isEmpty:size=0");
            return true;
        } else {
            LogUtil.i(TAG, "isEmpty:not empty");
            return false;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        // 禁止在列表条目上点击
        return false;
    }

    @Override
    public int getCount() {
        return mLyricSentences.size();
    }

    @Override
    public Object getItem(int position) {
        return mLyricSentences.get(position).getContentText();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.line_lyric,parent, false);
            holder.lyric_line = (TextView) convertView.findViewById(R.id.lyric_line_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position >= 0 && position < mLyricSentences.size()) {
            holder.lyric_line.setText(mLyricSentences.get(position).getContentText());
        }
        if (mIndexOfCurrentSentence == position) {
            // 当前播放到的句子设置为白色，字体大小更大
            holder.lyric_line.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.lyric_line.setTextSize(mCurrentSize);
        } else {
            // 其他的句子设置为暗色，字体大小较小
            holder.lyric_line.setTextColor(mContext.getResources().getColor(R.color.white_1000));
            holder.lyric_line.setTextSize(mNotCurrentSize);
        }
        return convertView;
    }

    public void setCurrentSentenceIndex(int index) {
        mIndexOfCurrentSentence = index;
    }

    static class ViewHolder {
        TextView lyric_line;
    }

}
