package com.qiang.qiangguide.adapter.adapterImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qiang.qiangguide.manager.AppManager;
import com.qiang.qiangguide.R;
import com.qiang.qiangguide.adapter.BaseRecyclerAdapter;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.util.AndroidUtil;
import com.qiang.qiangguide.util.BitmapUtil;
import com.qiang.qiangguide.util.FileUtil;
import com.qiang.qiangguide.volley.QVolley;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiang on 2016/8/1.
 *
 * 主页横向滑动图片的adapter
 */
public class MuseumIconAdapter extends BaseRecyclerAdapter<MuseumIconAdapter.ViewHolder> {


    private Context context;
    private List<String> list;
    private LayoutInflater inflater;
    private String  museumId;


    public MuseumIconAdapter(Context context) {
        this.context = context.getApplicationContext();
        inflater=LayoutInflater.from(this.context);
        this.list = new ArrayList<>();
    }

    public MuseumIconAdapter(Context context,String museumId) {
        this.context = context.getApplicationContext();
        inflater=LayoutInflater.from(this.context);
        this.museumId=museumId;
        this.list = new ArrayList<>();
    }

    public void setMuseumId(String museumId) {
        this.museumId = museumId;
    }


    public void updateData(List<String> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_museum_icon, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
        return viewHolder;
    }


    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int realPosition) {
        if(!(viewHolder instanceof ViewHolder)){return;}
        ViewHolder holder= (ViewHolder) viewHolder;
        String  url = list.get(realPosition);
        boolean isFileExists=FileUtil.checkFileExists(url,museumId);
        if(isFileExists){
            String name= FileUtil.changeUrl2Name(url);
            Bitmap bitmap=BitmapUtil.decodeSampledBitmapFromFile(Constants.LOCAL_PATH+museumId+"/"+name,
                    AndroidUtil.getMobileWidth(AppManager.getInstance(context).getCurrentActivity()),
                    AndroidUtil.getMobileHeight(AppManager.getInstance(context).getCurrentActivity()));
            holder.imageView.setImageBitmap(bitmap);
        }else{
            QVolley.getInstance(null).loadImage(Constants.BASE_URL+url,holder.imageView,0,0);
            //holder.imageView.displayImage(Constants.BASE_URL+url);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


}
