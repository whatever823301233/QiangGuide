package com.qiang.qiangguide.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Qiang on 2016/7/13.
 *
 */
public abstract class TableInfo {

    private final String mTableName;
    private ArrayList<String> mCreateSql = new ArrayList<>();
    private ArrayList<String> mUpgradeOwnToTwoSql = new ArrayList<>();
    private ArrayList<String> mUpgradeTwoToThreeSql = new ArrayList<>();


    public TableInfo( String tableName, ArrayList<String> createSql ) {

        this.mTableName = tableName;
        if( createSql != null ) {
            this.mCreateSql = createSql;
        }
    }

    public String getTableName() {
        return mTableName;
    }

    public ArrayList<String> getCreateSql() {
        return mCreateSql;
    }

    public void setUpgradeOwnToTwoSql( ArrayList<String> upgradeOwnToTwoSql ) {

        if( upgradeOwnToTwoSql != null ) {
            mUpgradeOwnToTwoSql = upgradeOwnToTwoSql;
        }

    }

    public ArrayList<String> getUpgradeOwnToTwoSql() {

        return mUpgradeOwnToTwoSql;
    }


    public void setUpgradeTwoToThreeSql( ArrayList<String> upgradeTwoToThreeSql ) {

        if( upgradeTwoToThreeSql != null ) {
            mUpgradeTwoToThreeSql = upgradeTwoToThreeSql;
        }

    }


    public ArrayList<String> getUpgradeTwoToThreeSql() {

        return mUpgradeTwoToThreeSql;
    }


    public abstract void upgrade(SQLiteDatabase db, int oldVersion, int newVersion );

}
