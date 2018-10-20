package com.quirodev.usagestatsmanagersample;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UsageStatVH extends RecyclerView.ViewHolder {

    private ImageView appIcon;
    private TextView appName;
    //private TextView eventtime;
    private TextView lastTimeUsed;

    public UsageStatVH(View itemView) {
        super(itemView);

        //eventtime=(TextView) itemView.findViewById(R.id.eventtime);
        appIcon = (ImageView) itemView.findViewById(R.id.icon);
        appName = (TextView) itemView.findViewById(R.id.title);
        lastTimeUsed = (TextView) itemView.findViewById(R.id.last_used);
    }

    public void bindTo(AppItem usageStatsWrapper) {
        appIcon.setImageDrawable(usageStatsWrapper.appicon);
        appName.setText(usageStatsWrapper.appname);
        lastTimeUsed.setText(String.valueOf(DateUtils.covertingtime(usageStatsWrapper.mUsageTime)));
        //eventtime.setText(usageStatsWrapper.mEventType);
    }
}
