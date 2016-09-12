package com.qiang.qiangguide.adapter.adapterImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qiang.qiangguide.AppManager;
import com.qiang.qiangguide.R;
import com.qiang.qiangguide.bean.MultiAngleImg;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.util.BitmapUtil;
import com.qiang.qiangguide.util.DensityUtil;
import com.qiang.qiangguide.util.FileUtil;
import com.qiang.qiangguide.util.LogUtil;
import com.qiang.qiangguide.volley.QVolley;

import java.io.File;
import java.util.List;

/**
 * Created by Qiang on 2016/9/12.
 *
 * 自定义横向多角度图片的adapter
 */
public class MultiAngleImgAdapter extends RecyclerView.Adapter<MultiAngleImgAdapter.ViewHolder>{

    private Context context;
    private List<MultiAngleImg> list;
    private LayoutInflater inflater;

    private OnItemClickListener onItemClickListener;//点击监听

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MultiAngleImgAdapter(Context c) {
        this.context = c.getApplicationContext();
        inflater = LayoutInflater.from(context);
    }
    public MultiAngleImgAdapter(Context c, List<MultiAngleImg> list) {
        this.context = c.getApplicationContext();
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_multi_angle_img, parent, false);
        //FontManager.applyFont(context, view);
        ViewHolder viewHolder = new ViewHolder(view,onItemClickListener);
        viewHolder.ivMultiAngle = (ImageView) view.findViewById(R.id.ivMultiAngle);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MultiAngleImg multiAngleImg=list.get(position);
        String iconUrl = multiAngleImg.getUrl();

        //ImageUtil.displayImage(url, holder.ivMultiAngle,true,false);
        String name= FileUtil.changeUrl2Name(iconUrl);
        String museumId= AppManager.getInstance(context).getMuseumId();
        String path= Constants.LOCAL_PATH+museumId+"/"+name;
        File file=new File(path);
        if(file.exists()){
            LogUtil.i("","图片路径为："+path);
            Bitmap bm= BitmapUtil.decodeSampledBitmapFromFile(
                    path,
                    DensityUtil.dp2px(context,120),DensityUtil.dp2px(context,120)
            );
            Bitmap roundBm=BitmapUtil.getRoundedCornerBitmap(bm);
            holder.ivMultiAngle.setImageBitmap(roundBm);
        }else{
            String url=Constants.BASE_URL+iconUrl;
            QVolley.getInstance(null).loadImageIcon(url,holder.ivMultiAngle,0,0);
            //holder.ivExhibitIcon.displayImage(url);
        }


    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    /**
     * 更新列表
     * @param list MultiAngleImg 集合
     */
    public void updateData(List<MultiAngleImg> list){
        this.list=list;
        notifyDataSetChanged();
    }

    /**
     * 内部接口，用于点击事件
     */
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }



    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnItemClickListener onItemClickListener;
        ImageView ivMultiAngle;
        public ViewHolder(View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener=onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClick(v,getPosition());
            }
        }
    }

}
