package com.quirodev.usagestatsmanagersample;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateUtils {

    public static String format(UsageStatsWrapper usageStatsWrapper){

        //DateFormat format = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        String time2=DateUtils.covertingtime(usageStatsWrapper.getUsageStats().getFirstTimeStamp());
        String time1=DateUtils.covertingtime(usageStatsWrapper.getUsageStats().getTotalTimeInForeground());
        String result= time1;
        return result;
    }
    public static  String covertingtime(long time1){
        long seconds =time1/ 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        String time ="";
        if (hours==0){
            time =minutes % 60 + "M:" + seconds % 60+"S";
        }
        if (seconds==0){
            time =  hours % 24 + "H:" + minutes % 60 + "M";
        }
        return time;
    }
}
