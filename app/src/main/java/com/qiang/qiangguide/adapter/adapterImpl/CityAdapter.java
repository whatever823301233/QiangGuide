package com.qiang.qiangguide.adapter.adapterImpl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiang.qiangguide.R;
import com.qiang.qiangguide.bean.City;

import java.util.List;


/**
 * Created by Qiang on 2016/7/30.
 * 城市选择适配器
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder>{

    private List<City> cityList;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public CityAdapter(Context c){
        Context context = c.getApplicationContext();
        inflater=LayoutInflater.from(context);
    }

    public void updateData(List<City> cityList){
        this.cityList=cityList;
        notifyDataSetChanged();
    }

    public City getItem(int position){
        return cityList.get(position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_city,parent,false);
        ViewHolder holder=new ViewHolder(view,onItemClickListener);
        holder.alpha=(TextView) view.findViewById(R.id.itemCityAlpha);
        holder.name=(TextView) view.findViewById(R.id.itemCityName);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        City city=cityList.get(position);
        //根据position获取分类的首字母的char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
            holder.alpha.setVisibility(View.VISIBLE);
            holder.alpha.setText(city.getAlpha());
        }else{
            holder.alpha.setVisibility(View.GONE);
        }
        holder.name.setText(this.cityList.get(position).getName());
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return cityList.get(position).getAlpha().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = cityList.get(i).getAlpha();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if(firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return cityList==null?0:cityList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView alpha,name;

        public ViewHolder(View itemView,final OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(v,getLayoutPosition());
                    }
                }
            });
        }
    }


    /**
     * 内部接口，用于点击事件
     */
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }


}
