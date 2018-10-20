package com.quirodev.usagestatsmanagersample;

import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.os.Process.myUid;

public class UsagePresenter implements UsageContract.Presenter {

    private static final int flags = PackageManager.GET_META_DATA |
            PackageManager.GET_SHARED_LIBRARY_FILES |
            PackageManager.GET_UNINSTALLED_PACKAGES;

    private UsageStatsManager usageStatsManager;
    private PackageManager packageManager;
    private UsageContract.View view;
    private final Context context;

    public UsagePresenter(Context context, UsageContract.View view) {
        usageStatsManager = (UsageStatsManager) context.getSystemService(context.USAGE_STATS_SERVICE);
        packageManager = context.getPackageManager();
        this.view = view;
        this.context = context;
    }

    public long getStartTime() {
        //it will return the Calender based on the current time and default time zone
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, -1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        //set the calender time and will be equal to the 0:0:0
        //return the current time in milliseconds

        return cal.getTimeInMillis();
    }
   // @Override
    public List<UsageStatsWrapper> retrieveUsageStats() {
        /*if (!checkForPermission(context)) {
            //this is the method present in the MainActivity.java
            view.onUserHasNoPermission();
            return ;
        }*/
        //it will contain the list of string of all installed apps with its package name
        List<String> installedApps = getInstalledAppList();
        //the mehthod that query all the statistics in the given range present in the argument of this method
        //getStartTime() is the starting time and System.currentTimeinMillis() is the ending time
        //we can also query the data daily , weekly , monthly , yearly
        SimpleDateFormat sdf=new SimpleDateFormat("MMMM dd ,yyyy");

        Map<String,UsageStats> alllist = usageStatsManager.queryAndAggregateUsageStats(getStartTime(),System.currentTimeMillis());
        //List<UsageStats> islist=usageStatsManager.queryUsageStats(0,getStartTime(),System.currentTimeMillis());
        Log.v("testing1",sdf.format(getStartTime())+sdf.format(System.currentTimeMillis()));
        List <UsageStats> newlist= usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,getStartTime(),System.currentTimeMillis());
        UsageEvents events=usageStatsManager.queryEvents(getStartTime(),System.currentTimeMillis());

        //it the list which will contain the statistics of all the application
         List<UsageStats> stats = new ArrayList<>();
         stats.addAll(alllist.values());

        List<UsageStatsWrapper> finalList = buildUsageStatsWrapper(installedApps,newlist);
        //onUsageStatsRetrieved is the function present in the MainActivity.java
        //view if the interface which is present in the UsageContract and connects onUsgaeStats Received
        //view.onUsageStatsRetrieved(finalList);
        return finalList;
    }

   /* private boolean checkForPermission(Context context) {
        //Api for interacting the application operation traking
        //get the object or instance of the AppOpsManager
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        //do a quick check for whether an application might be able to perform an operation
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), context.getPackageName());
        return mode == MODE_ALLOWED;
    }*/

    private List<String> getInstalledAppList(){
        //return the list of all application packages that are installed for the current user
        List<ApplicationInfo> infos = packageManager.getInstalledApplications(flags);

        List<String> installedApps = new ArrayList<>();

        for (ApplicationInfo info : infos){
            installedApps.add(info.packageName);
        }
        //it will return the name of the all application installed in the user phone
        return installedApps;
    }

    private List<UsageStatsWrapper> buildUsageStatsWrapper(List<String> packageNames, List<UsageStats> usageStatses) {
       //it is the usgae statswrapper object which will contain the usagestats, applicationname and applicationicon
        List<UsageStatsWrapper> list = new ArrayList<>();

        //packageNames contains string values of the app and UsgaeStatses contain usgaestatistics

        for (String name : packageNames) {
            boolean added = false;
            for (UsageStats stat : usageStatses) {
                if (name.equals(stat.getPackageName())) {
                    added = true;
                    list.add(fromUsageStat2(stat));
                }
            }
            if (!added) {
                list.add(fromUsageStat1(name));
            }
        }
        Collections.sort(list);
        return list;
    }

    private UsageStatsWrapper fromUsageStat1(String packageName) throws IllegalArgumentException {
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 0);
            return new UsageStatsWrapper(null, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString());

        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private UsageStatsWrapper fromUsageStat2(UsageStats usageStats) throws IllegalArgumentException {
        try {
            //getApplicationInfo is the function which retrive all the information about the application
            //0 signify as the (argument) installed apps
            //usgaeStats.getPackageName() will reurtun the full name of the packages
            ApplicationInfo ai = packageManager.getApplicationInfo(usageStats.getPackageName(), 0);
            // it will return the application icon , application label (app name) and its statistics in the form of the usgaestatswrapper object
            return new UsageStatsWrapper(usageStats, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString());

        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
