package com.qiang.qiangguide.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Qiang on 2016/7/13.
 *
 * 时间工具类
 */
public class TimeUtil {

    /**
     *
     * 返回当前时间
     * @return
     *String hh:mm:ss yyyy年MM月dd日
     *String
     * @since  1.0.0
     */
    public static synchronized String getNowTimeString() {
        SimpleDateFormat Fmt=new SimpleDateFormat("HH:mm:ss yyyy年MM月dd日");
        Date now=new Date();
        return Fmt.format(now);
    }

    /**
     *
     * 比较现在时间是不是在给定的时间段里。 比如时间"201407221445"
     * @param timeStart	起始时间
     * @param timeEnd	终止时间
     *boolean
     * @since  1.0.0
     * @return boolean
     */
    public boolean betweenTheTimeMinu(String timeStart, String timeEnd) {
        SimpleDateFormat Fmt1 = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            Date dateStart = Fmt1.parse(timeStart);
            Date dateEndDate = Fmt1.parse(timeEnd);
            Date dateNowDate = new Date();
            Long dateStartLong = dateStart.getTime();
            Long dateEndLong = dateEndDate.getTime();
            Long dateNowLong = dateNowDate.getTime();

            if (dateNowLong > dateStartLong && dateNowLong < dateEndLong) {
                return true;
            } else {
                return false;
            }

        } catch (ParseException e) {
            LogUtil.e("",e);
            return false;
        }
    }


    /**
     *
     * 获得星期几
     * @return
     * int	1是星期日、2是星期一、3是星期二、4是星期三、5是星期四、6是星期五、7是星期六
     * @since 1.0.0
     */
    public static String getDayOfWeek(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR));
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return mYear + "年" + mMonth + "月" + mDay +"日"+"/星期"+ mWay;
    }


}
