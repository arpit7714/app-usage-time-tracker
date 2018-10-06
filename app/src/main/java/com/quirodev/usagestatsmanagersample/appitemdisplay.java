package com.quirodev.usagestatsmanagersample;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quirodev.data.dbhelper;
import com.quirodev.data.dbcontract.appdata;
import com.quirodev.data.dbprovider;

import java.util.List;


public class appitemdisplay extends AppCompatActivity{
    public static final String abc="App_name";
    public static String appname1;
    public static String pkname1;
    public static String usagetime1;
    public String pkname;
    //to access database
    public static dbprovider obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_display);
        obj=new dbprovider(getApplicationContext());
        Intent intent=getIntent();
        displaydatabaseinfo();
        if (intent!=null){
            appname1=intent.getStringExtra(abc);
           // String pkname;
            pkname=intent.getStringExtra(pkname1);
            TextView appname=(TextView) findViewById(R.id.appname);
            appname.setText(appname1);
            TextView usagetime=(TextView) findViewById(R.id.usagetime);
            String usage = intent.getStringExtra(usagetime1);
            usagetime.setText(usage);
            ImageView appicon=(ImageView) findViewById(R.id.appicon);
            appicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDetail();
                }
            });
            //Log.v("testing1",pkname);
            Bundle extras=intent.getExtras();
            Bitmap bmp=(Bitmap) extras.getParcelable("icon");
            appicon.setImageBitmap(bmp);
        }
        List<AppItem1> items=obj.getapp();
        for(AppItem1 item :items){
            Log.v("database",String.valueOf(items.size()));
            Log.v("database",item.appname+"usage time"+item.mUsageTime);

        }
    }
    private void openDetail() {

        Intent intent = new Intent(
                android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + pkname));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void displaydatabaseinfo(){
        dbhelper mdbhelper= new dbhelper(getApplicationContext());
        SQLiteDatabase db= mdbhelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select * from "+appdata.TABLE_NAME,null);
        int appcolumnindex=cursor.getColumnIndex(appdata.APP_NAME);
        int appduration=cursor.getColumnIndex(appdata.APP_DURATION);
        TextView displayview=(TextView) findViewById(R.id.database);
        displayview.setText("Number of rows in database table: " + cursor.getCount());
        while(cursor.moveToNext()){
            String appname=cursor.getString(appcolumnindex);
            String duration=cursor.getString(appduration);
            displayview.append("\n"+appname+"  "+duration+" ");
        }
        cursor.close();

    }

}
