package com.quirodev.usagestatsmanagersample;

import android.app.ActionBar;
import android.app.AppOpsManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.SettingInjectorService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.quirodev.data.dbprovider;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class
MainActivity extends AppCompatActivity implements UsageContract.View {

    private ProgressBar progressBar;
    private TextView permissionMessage;
    private Button button;
    private UsageContract.Presenter presenter;
    private UsageStatAdapter adapter;
    private TextView mSwitchText;
    private Context mContext;
    public  dbprovider mdb;
    //this is used to show the average time smartphone used for today
    private long mtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwitchText= (TextView) findViewById(R.id.enable_text);
        //app transition animation
        // https://guides.codepath.com/android/Shared-Element-Activity-Transition
        mdb=new dbprovider(getApplicationContext());

        //items for spinner
       /* String [] spinneritems={"Today","Yesterday","Weekly","Monthly"};
        Spinner sp=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> newadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinneritems);
        newadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(newadapter);*/

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        permissionMessage = (TextView) findViewById(R.id.grant_permission_message);
        button =(Button) findViewById(R.id.permissionbutton);

        //as we want to display our data as the linear vertical list , then this is the linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mdb=new dbprovider(mContext.);
        //divider decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider, getTheme()));
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new UsageStatAdapter();

        recyclerView.setAdapter(adapter);

        permissionMessage.setVisibility(VISIBLE);
        button.setVisibility(VISIBLE);
        button.setOnClickListener(v -> openSettings());
        mSwitchText.setVisibility(View.GONE);
        presenter = new UsagePresenter(this, this);
        newmethod();
    }

    private void openSettings() {
        Intent intent=new Intent(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public boolean hasPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (appOps != null) {
            int mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), context.getPackageName());
            return mode == AppOpsManager.MODE_ALLOWED;
        }
        return false;
    }

    //when you come back again to that activity after setting on usage access

    protected void newmethod(){
        if (hasPermission(getApplicationContext())) {
            permissionMessage.setVisibility(GONE);
            showProgressBar(true);
            button.setVisibility(GONE);
            mSwitchText.setVisibility(View.VISIBLE);
            new MyAsyncTask().execute(0);

        }
        else{

           onUserHasNoPermission();

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (hasPermission(getApplicationContext())) {
            permissionMessage.setVisibility(GONE);
            button.setVisibility(GONE);
            mSwitchText.setVisibility(View.VISIBLE);
            showProgressBar(true);
            new MyAsyncTask().execute(0);
        }
        else{
            onUserHasNoPermission();
        }
    }

    //this function was automatically called after onResume() meth
   /* @Override
    public void onUsageStatsRetrieved(List<UsageStatsWrapper> list) {
        showProgressBar(false);
        permissionMessage.setVisibility(GONE);
        adapter.setList(list);
    }*/

    @Override
    public void onUserHasNoPermission() {
        showProgressBar(false);
        permissionMessage.setVisibility(VISIBLE);
    }

    private void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(VISIBLE);
        } else {
            progressBar.setVisibility(GONE);
        }
    }
    //asysctask to get app data in background
   /*private class myasynctask extends AsyncTask<Integer,Void,List<UsageStatsWrapper>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<UsageStatsWrapper> doInBackground(Integer... integers) {

            //Log.v("message","hello"+integers[0]);

            return presenter.retrieveUsageStats();
        }
        protected void onPostExecute(List<UsageStatsWrapper> appitems){
            adapter.setList(appitems);
           // for(UsageStatsWrapper item: appitems){
             //   Log.v("message",String.valueOf(item.getUsageStats().getTotalTimeInForeground()));

            //}
            showProgressBar(false);
        }
    */
    class MyAsyncTask extends AsyncTask<Integer, Void, List<AppItem1>> {

        //when ever we do swipe
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<AppItem1> doInBackground(Integer...integers) {
            return appitem.getApps(getApplicationContext(),0,1);
        }

        @Override
        protected void onPostExecute (List <AppItem1> appItems) {
            int mTotal = 0;

            //to show averagetime
            //mSwitchText.setText(String.format(getResources().getString(R.string.total), AppUtil.formatMilliSeconds(mTotal)));
            //mSwipe.setRefreshing(false);
                Log.v("size",String.valueOf(appItems.size()));
            for(AppItem1 item :appItems){
                    //Log.v("testing5",item.mPackageName+"event time"+item.mEventTime+"event type"+item.mEventType+"usage time"+item.mUsageTime);

                //inserting appname and app time into the database
                if(item!=null)
                   mdb.insert(item);

                if (item.mUsageTime <= 0) continue;
                mTotal += item.mUsageTime;
            }
            Bundle bundle = new Bundle();
            String total_time_used = " Total time Used : " + String.valueOf(DateUtils.covertingtime(mTotal));
            bundle.putString("total_time_used", total_time_used );
            FragmentManager FM = getFragmentManager();
            FragmentTransaction FT = FM.beginTransaction();
            FragmentOne F1 = new FragmentOne();
            F1.setArguments(bundle);
            FT.add(R.id.fragment1,F1);
            FT.commit();
            adapter.setList(appItems);
            showProgressBar(false);
        }
    }


}
