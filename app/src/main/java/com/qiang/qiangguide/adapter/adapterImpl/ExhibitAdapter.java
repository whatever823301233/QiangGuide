package com.qiang.qiangguide.adapter.adapterImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.adapter.BaseRecyclerAdapter;
import com.qiang.qiangguide.bean.Exhibit;
import com.qiang.qiangguide.config.Constants;
import com.qiang.qiangguide.db.handler.ExhibitHandler;
import com.qiang.qiangguide.util.BitmapUtil;
import com.qiang.qiangguide.util.DensityUtil;
import com.qiang.qiangguide.util.FileUtil;
import com.qiang.qiangguide.volley.QVolley;

import java.io.File;
import java.util.List;

/**
 * Created by Qiang on 2016/8/4.
 */
public class ExhibitAdapter extends BaseRecyclerAdapter<ExhibitAdapter.ViewHolder> {


    private  Context context;
    private LayoutInflater inflater;
    private List<Exhibit> exhibitList;

    public ExhibitAdapter(Context c){
        this.context=c.getApplicationContext();
        inflater=LayoutInflater.from(context);
    }

    public void updateData(List<Exhibit> exhibits){
        this.exhibitList =exhibits;
        notifyDataSetChanged();
    }


    public Exhibit getExhibit(int position){
        return exhibitList==null?null:exhibitList.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_exhibit,parent,false);
        ViewHolder holder=new ViewHolder(view);
        holder.tvExhibitName = (TextView) view.findViewById(R.id.tvExhibitName);
        holder.tvExhibitYears = (TextView) view.findViewById(R.id.tvExhibitYears);
        holder.tvExhibitPosition = (TextView) view.findViewById(R.id.tvExhibitPosition);
        holder.ivExhibitIcon = (ImageView) view.findViewById(R.id.ivExhibitIcon);
        holder.tvExhibitDistance = (TextView) view.findViewById(R.id.tvExhibitDistance);
        holder.llCollectionBtn = (LinearLayout) view.findViewById(R.id.llCollectionBtn);
        holder.ivCollection = (ImageView) view.findViewById(R.id.ivCollection);
        holder.exhibitNumber = (TextView) view.findViewById(R.id.number);
        holder.like = (TextView) view.findViewById(R.id.like);
        holder.ivPlayAnim = (ImageView) view.findViewById(R.id.ivPlayAnim);
        return holder;
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder,final int realPosition) {
        ViewHolder holder=(ViewHolder)viewHolder;
        final Exhibit exhibit=exhibitList.get(realPosition);
        holder.tvExhibitName.setText(exhibit.getName());
        holder.tvExhibitYears.setText(exhibit.getLabels());
        holder.tvExhibitYears.setText(exhibit.getLabels());
        int text=exhibit.getNumber();
        /**此处直接赋值int会调用 一下方法，到资源中找此id，从而异常
         * @android.view.RemotableViewMethod
        public final void setText(@StringRes int resid) {
        setText(getContext().getResources().getText(resid));
        }*/
        holder.exhibitNumber.setText(String.valueOf(text));
        holder.tvExhibitPosition.setText(exhibit.getContent());
        holder.like.setText("收藏");
        String iconUrl=exhibit.getIconurl();
        String name= FileUtil.changeUrl2Name(iconUrl);
        String museumId=exhibit.getMuseumId();
        String path= Constants.LOCAL_PATH+museumId+"/"+name;
        File file=new File(path);
        if(file.exists()){
            //LogUtil.i("","图片路径为："+path);
            Bitmap bm= BitmapUtil.decodeSampledBitmapFromFile(
                    path,
                    DensityUtil.dp2px(context,120),
                    DensityUtil.dp2px(context,120)
            );
            Bitmap roundBm=BitmapUtil.getRoundedCornerBitmap(bm);
            holder.ivExhibitIcon.setImageBitmap(roundBm);
        }else{
            String url=Constants.BASE_URL+iconUrl;
            QVolley.getInstance(null).loadImageIcon(url,holder.ivExhibitIcon,0,0);
            //holder.ivExhibitIcon.displayImage(url);
        }

        holder.llCollectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isFavorite= exhibit.getIsFavorite();
                if(isFavorite==0){
                    exhibit.setIsFavorite(1);
                    ExhibitHandler.updateExhibit(exhibit);
                    notifyItemChanged(realPosition);
                }else{
                    exhibit.setIsFavorite(0);
                    ExhibitHandler.updateExhibit(exhibit);
                    notifyItemChanged(realPosition);
                }
            }
        });
        int isFavorite= exhibit.getIsFavorite();
        if(isFavorite==0){
            holder.ivCollection.setImageDrawable(context.getResources().getDrawable(R.drawable.iv_heart_empty));
        }else{
            holder.ivCollection.setImageDrawable(context.getResources().getDrawable(R.drawable.iv_heart_full));
        }
    }

    @Override
    public int getItemCount() {
        return exhibitList ==null?0: exhibitList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvExhibitName, tvExhibitYears,
                tvExhibitPosition,tvExhibitDistance;
        TextView exhibitNumber,like;
        ImageView ivExhibitIcon;
        ImageView ivCollection,ivPlayAnim;
        LinearLayout llCollectionBtn;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
