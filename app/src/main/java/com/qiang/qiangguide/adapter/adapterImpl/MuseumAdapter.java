package com.qiang.qiangguide.adapter.adapterImpl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.adapter.BaseRecyclerAdapter;
import com.qiang.qiangguide.bean.Museum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiang on 2016/7/31.
 */
public class MuseumAdapter extends BaseRecyclerAdapter<MuseumAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Museum> museumList;

    public MuseumAdapter(Context c){
        Context context = c.getApplicationContext();
        inflater=LayoutInflater.from(context);
        museumList=new ArrayList<>();
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
        holder.museumListIcon = (ImageView) view.findViewById(R.id.museumListIcon);
        holder.museumFlagIsDownload = (TextView) view.findViewById(R.id.museumFlagIsDownload);
        holder.museumImportantAlert = (TextView) view.findViewById(R.id.museumImportantAlert);
        return holder;
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int realPosition) {
        ViewHolder holder;
        Museum museum=museumList.get(realPosition);
        if(!(viewHolder instanceof ViewHolder)){
            return;
        }
        holder= (ViewHolder) viewHolder;
        holder.museumName.setText(museum.getName());
        holder.museumAddress.setText(museum.getAddress());
        holder.museumListOpenTime.setText(museum.getOpentime());
    }

    @Override
    public int getItemCount() {
        return museumList==null?0:museumList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView museumName,museumAddress,museumListOpenTime,
                museumImportantAlert,museumFlagIsDownload;
        public ImageView museumListIcon;
        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

}
