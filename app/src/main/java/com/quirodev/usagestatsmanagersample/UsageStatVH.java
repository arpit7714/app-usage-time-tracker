package com.quirodev.usagestatsmanagersample;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UsageStatVH extends RecyclerView.ViewHolder {

    private ImageView appIcon;
    private TextView appName;
    //private TextView eventtime;
    private TextView totaltimeused;
    private  TextView lasttimeused;
    public AppItem1 app1;
    public UsageStatVH(View itemView) {
        super(itemView);

        //eventtime=(TextView) itemView.findViewById(R.id.eventtime);
        appIcon = (ImageView) itemView.findViewById(R.id.icon);
        appName = (TextView) itemView.findViewById(R.id.title);
        totaltimeused = (TextView) itemView.findViewById(R.id.total_time_used);

    }

    public void bindTo(AppItem1 usageStatsWrapper) {
        appIcon.setImageDrawable(usageStatsWrapper.appicon);
        appName.setText(usageStatsWrapper.appname);
        totaltimeused.setText(String.valueOf(DateUtils.covertingtime(usageStatsWrapper.mUsageTime)));
        //app1 = usageStatsWrapper;
        //eventtime.setText(usageStatsWrapper.mEventType);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appIcon.buildDrawingCache();
                Bitmap image=appIcon.getDrawingCache();
                Intent intent = new Intent(v.getContext(), appitemdisplay.class);
                intent.putExtra(appitemdisplay.abc,usageStatsWrapper.appname);
                intent.putExtra(appitemdisplay.usagetime1,String.valueOf(DateUtils.covertingtime(usageStatsWrapper.mUsageTime)));
                intent.putExtra(appitemdisplay.pkname1,usageStatsWrapper.mPackageName);

                Bundle extras=new Bundle();
                extras.putParcelable("icon",image);
                intent.putExtras(extras);
                v.getContext().startActivity(intent);
                //Toast.makeText(v.getContext(), "os version is: " + "hello", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
