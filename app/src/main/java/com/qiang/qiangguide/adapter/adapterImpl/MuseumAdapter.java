package com.qiang.qiangguide.adapter.adapterImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.adapter.BaseRecyclerAdapter;
import com.qiang.qiangguide.bean.Museum;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.custom.NetImageView;
import com.qiang.qiangguide.util.BitmapUtil;
import com.qiang.qiangguide.util.DensityUtil;
import com.qiang.qiangguide.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiang on 2016/7/31.
 */
public class MuseumAdapter extends BaseRecyclerAdapter<MuseumAdapter.ViewHolder> {

    private final Context context;
    private LayoutInflater inflater;
    private List<Museum> museumList;

    public MuseumAdapter(Context c){
        context = c.getApplicationContext();
        inflater=LayoutInflater.from(context);
        museumList=new ArrayList<>();
    }

    public Museum getMuseum(int position){
        return museumList.get(position);
    }


    public void updateData(List<Museum> museumList){
        this.museumList=museumList;
        //museumList.addAll(museumList); // TODO: 2016/7/31 卧槽，add 不可以？
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.item_museum,parent,false);
        ViewHolder holder=new ViewHolder(view);
        holder.museumName = (TextView) view.findViewById(R.id.museumName);
        holder.museumAddress = (TextView) view.findViewById(R.id.museumAddress);
        holder.museumListOpenTime = (TextView) view.findViewById(R.id.museumListOpenTime);
        holder.museumListIcon = (NetImageView) view.findViewById(R.id.museumListIcon);
        holder.museumFlagIsDownload = (TextView) view.findViewById(R.id.museumFlagIsDownload);
        holder.museumImportantAlert = (TextView) view.findViewById(R.id.museumImportantAlert);
        return holder;
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int realPosition) {
        Museum museum=museumList.get(realPosition);
        ViewHolder holder;
        if(!(viewHolder instanceof ViewHolder)){return;}
        holder= (ViewHolder) viewHolder;
        holder.museumName.setText(museum.getName());
        holder.museumAddress.setText(museum.getAddress());
        holder.museumListOpenTime.setText(museum.getOpentime());

        String iconUrl=museum.getIconurl();
        String name= FileUtil.changeUrl2Name(iconUrl);
        String museumId=museum.getId();
        String path= Constants.LOCAL_PATH+museumId+"/"+name;
        File file=new File(path);
        if(file.exists()){
            Bitmap bm=BitmapUtil.decodeSampledBitmapFromFile(path,
                    DensityUtil.dp2px(context,120),DensityUtil.dp2px(context,120));
            holder.museumListIcon.setImageBitmap(bm);
        }else{
            String url=Constants.BASE_URL+iconUrl;
            holder.museumListIcon.displayImage(url);
        }

    }

    @Override
    public int getItemCount() {
        return museumList==null?0:museumList.size()+1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView museumName,museumAddress,museumListOpenTime,
                museumImportantAlert,museumFlagIsDownload;
        public NetImageView museumListIcon;
        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

}
