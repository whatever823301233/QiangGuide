package com.qiang.qiangguide.db.handler;

import android.database.Cursor;

import com.qiang.qiangguide.bean.Label;
import com.qiang.qiangguide.db.DBHandler;
import com.qiang.qiangguide.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiang on 2016/9/26.
 */
public class LabelHandler {

    /**
     * add Labels
     * @param labelList
     */
    public static  void addLabels(List<Label> labelList) {
        if(labelList==null){return;}
        DBHandler.getInstance().getDB().beginTransaction();  //开始事务
        try {
            for (Label label : labelList) {
                DBHandler.getInstance().getDB().execSQL(
                        "INSERT INTO "+Label.TABLE_NAME +" VALUES(null,?,?,?,?)",
                        new Object[]{
                                label.getId(),
                                label.getMuseumId(),
                                label.getName(),
                                label.getLables()
                        });
            }
            DBHandler.getInstance().getDB().setTransactionSuccessful();  //设置事务成功完成
            LogUtil.i("","addLabels 保存成功");
        } catch (Exception e){
            LogUtil.e("",e);
        }finally {
            DBHandler.getInstance().getDB().endTransaction();    //结束事务
        }
    }


    /**
     * query all Label, return list
     * @return List<Label>
     */
    public static  List<Label> queryLabels(String museumId) {
        List<Label> labelList = new ArrayList<>();
        Cursor c = DBHandler.getInstance().getDB().rawQuery(
                "SELECT * FROM " + Label.TABLE_NAME
                        +" WHERE "+Label.MUSEUM_ID
                        +" = ?",
                new String[]{museumId}
        );
        while (c.moveToNext()) {
            Label l =buildLabelByCursor(c);
            labelList.add(l);
        }
        c.close();
        return labelList;
    }

    private static  Label buildLabelByCursor(Cursor c) {
        Label l = new Label();
        l.set_id(c.getInt(c.getColumnIndex(Label._ID)));
        l.setId(c.getString(c.getColumnIndex(Label.ID)));
        l.setMuseumId(c.getString(c.getColumnIndex(Label.MUSEUM_ID)));
        l.setName(c.getString(c.getColumnIndex(Label.NAME)));
        l.setLables(c.getString(c.getColumnIndex(Label.LABELS)));
        return l;
    }


}
