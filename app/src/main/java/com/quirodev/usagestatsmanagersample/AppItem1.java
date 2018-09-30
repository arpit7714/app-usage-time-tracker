package com.quirodev.usagestatsmanagersample;

import android.graphics.drawable.Drawable;

import java.util.Locale;

/**
 * App Item
 * Created by zb on 18/12/2017.
 */

public class AppItem1 {
    public String appname;
    public Drawable appicon;
    public String mPackageName;
    public long mEventTime;
    public long mUsageTime;
    public int mEventType;
    public int mCount;
    public long mMobile;
    public long mLastTimeUsed;

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "name:%s package_name:%s time:%d total:%d type:%d system:%b count:%d lasttime:%d",
                appname, mPackageName, mEventTime, mUsageTime, mEventType, mCount,mLastTimeUsed);
    }

    public AppItem1 copy() {
        AppItem1 newItem = new AppItem1();
        newItem.appname = this.appname;
        newItem.mPackageName = this.mPackageName;
        newItem.mEventTime = this.mEventTime;
        newItem.mUsageTime = this.mUsageTime;
        newItem.mEventType = this.mEventType;
        newItem.mCount = this.mCount;
        newItem.mLastTimeUsed = this.mLastTimeUsed;
        return newItem;
    }
}