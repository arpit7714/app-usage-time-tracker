package com.quirodev.usagestatsmanagersample;

import android.app.usage.NetworkStatsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.quirodev.usagestatsmanagersample.AppItem1;

public class appitem {
    public static long getStartTime() {
        //it will return the Calender based on the current time and default time zone
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        //set the calender time and will be equal to the 0:0:0
        //return the current time in milliseconds

        return cal.getTimeInMillis();
    }
    public static List<AppItem1> getApps(Context context, int sort, int offset) {

        // value of sort is 0 and value of offset is 1
        PackageManager packagemanager=context.getPackageManager();

        List<AppItem1> items = new ArrayList<>();
        //object of usagestats manager
        Log.v("testing2", String.valueOf(sort) + " " + String.valueOf(offset));

        UsageStatsManager manager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        if (manager != null) {
            String prevPackage = "";

            //contains time along withtheir package name
            Map<String, Long> startPoints = new HashMap<>();

            Map<String, ClonedEvent> endPoints = new HashMap<>();

            //range[0]=start time from calendar and range[1]=end time (system.currenttimeinmillis())
            //whatever the events happen in this time range were get stored in the events variable
            //The events returned by queryEvents() are kept by the system only for a few days.
            // So, we might have to maintain a local database to maintain those stats locally.
            UsageEvents events = manager.queryEvents(getStartTime(), System.currentTimeMillis());
            //Log.v("testing3",DataManager.covertingtime(range[0])+"   "+DataManager.covertingtime(range[1])+"items"+String.valueOf(items.size()));
            UsageEvents.Event event = new UsageEvents.Event();
            Log.v("testing4", events.toString());
            while (events.hasNextEvent()) {

                events.getNextEvent(event);

                //event type signify event in background or in foreground

                int eventType = event.getEventType();
                //the time at which this event occours
                long eventTime = event.getTimeStamp();
                String eventPackage = event.getPackageName();
                Log.v("testing 1", String.valueOf(eventTime) + "package name" + eventPackage);

                if (eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    //to find the package is already present in the items or not and return the object of item.
                    AppItem1 item = containItem(items, eventPackage);
                    //inserting the packagename in the items list so that if there more events for a single packagename then only one packagename present
                    // in the list
                    if (item == null) {
                        item = new AppItem1();
                        item.mPackageName = eventPackage;
                        try {
                            ApplicationInfo ai = packagemanager.getApplicationInfo(item.mPackageName, 0);
                            Drawable appicon=packagemanager.getApplicationIcon(ai);
                            String appname=packagemanager.getApplicationLabel(ai).toString();
                            item.appname=appname;
                            item.appicon=appicon;
                            // item.mEventTime=eventTime;
                        }
                        catch (PackageManager.NameNotFoundException e){
                            throw new IllegalArgumentException(e);
                        }
                        items.add(item);
                    }
                    //startpoint is the map contains the packagename nad the eventtime
                    if (!startPoints.containsKey(eventPackage)) {
                        startPoints.put(eventPackage, eventTime);
                    }
                }

                //endpoints is the map contains the packagename and the clonnedevent
                if (eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    if (startPoints.size() > 0 && startPoints.containsKey(eventPackage)) {
                        endPoints.put(eventPackage, new ClonedEvent(event));
                    }
                }

                if (TextUtils.isEmpty(prevPackage))
                    prevPackage = eventPackage;

                if (!prevPackage.equals(eventPackage)) {
                    if (startPoints.containsKey(prevPackage) && endPoints.containsKey(prevPackage)) {
                        ClonedEvent lastEndEvent = endPoints.get(prevPackage);
                        AppItem1 listItem = containItem(items, prevPackage);
                        if (listItem != null) { // update list item info
                            listItem.mEventTime = lastEndEvent.timeStamp;
                            long duration = lastEndEvent.timeStamp - startPoints.get(prevPackage);
                            if (duration <= 0) duration = 0;
                            listItem.mUsageTime += duration;
                            if (duration > 5000) {
                                listItem.mCount++;
                            }
                        }
                        startPoints.remove(prevPackage);
                        endPoints.remove(prevPackage);
                    }
                    prevPackage = eventPackage;
                }
            }
        }
        List<AppItem1> newList = new ArrayList<>();

        //it is sorted on the basis of the spinner present on the mainactivity.
        if (sort == 0) {
            Collections.sort(newList, new Comparator<AppItem1>() {
                @Override
                public int compare(AppItem1 left, AppItem1 right) {
                    return (int) (right.mUsageTime - left.mUsageTime);
                }
            });
        } else if (sort == 1) {
            Collections.sort(newList, new Comparator<AppItem1>() {
                @Override
                public int compare(AppItem1 left, AppItem1 right) {
                    return (int) (right.mEventTime - left.mEventTime);
                }
            });
        } else if (sort == 2) {
            Collections.sort(newList, new Comparator<AppItem1>() {
                @Override
                public int compare(AppItem1 left, AppItem1 right) {
                    return right.mCount - left.mCount;
                }
            });
        } else {
            Collections.sort(newList, new Comparator<AppItem1>() {
                @Override
                public int compare(AppItem1 left, AppItem1 right) {
                    return (int) (right.mMobile - left.mMobile);
                }
            });
        }
        Log.v("size2",String.valueOf(items.size()));
        return items;
    }

    private static AppItem1 containItem(List<AppItem1> items, String packageName) {
        for (AppItem1 item : items) {
            if (item.mPackageName.equals(packageName)) return item;
        }
        return null;
    }

}
class ClonedEvent {

    String packageName;
    String eventClass;
    long timeStamp;
    int eventType;

    ClonedEvent(UsageEvents.Event event) {
        packageName = event.getPackageName();
        eventClass = event.getClassName();
        timeStamp = event.getTimeStamp();
        eventType = event.getEventType();
    }
}
