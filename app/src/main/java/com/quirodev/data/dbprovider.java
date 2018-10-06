package com.quirodev.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.quirodev.usagestatsmanagersample.AppItem1;

import java.util.ArrayList;
import java.util.List;

public class dbprovider {

    private static dbhelper mdbhelper;
    private static dbprovider instance;


    //we make the instance of the sqllitehllper class to
    //get the instance of the database and use mthod like
    //getwritabledatabase() and getreadabledatabase()
    public  dbprovider(Context context){
        mdbhelper=new dbhelper(context);
    }

    //public static dbprovider getinstance(){
      //  return instance;
    //}

    public void method(){
        Log.v("taking easy","yoiod");
    }
    public void insert(AppItem1 appitem){


          ContentValues values=itemToContentValue(appitem);
          mdbhelper.getWritableDatabase().insert(dbcontract.appdata.TABLE_NAME,null,values);

    }
    private ContentValues itemToContentValue(AppItem1 item) {
        ContentValues values = new ContentValues();
        values.put(dbcontract.appdata.APP_NAME,item.appname);
        values.put(dbcontract.appdata.APP_DURATION,item.mUsageTime);
        return values;
    }


    public boolean exist(AppItem1 appitem){
        //SQLiteDatabase database = mdbhelper.getWritableDatabase();
        Cursor cursor=null;
        try{
            String[] cols={
                    dbcontract.appdata.APP_NAME,
                    dbcontract.appdata.APP_DURATION,
                    dbcontract.appdata._ID
            };
            cursor = mdbhelper.getWritableDatabase().query(
                    dbcontract.appdata.TABLE_NAME,
                    cols,
                    null,null
                    ,null,
                    null,
                    null
            );
            if (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(dbcontract.appdata._ID));
                return id > 0;
            }
        }finally {
            if (cursor!=null)
                cursor.close();
            }
          return false;
    }
    public List<AppItem1> getapp(){
        Cursor cursor=null;
        List <AppItem1> items=new ArrayList<>();
        try{
            String[] cols={
                    dbcontract.appdata.APP_NAME,
                    dbcontract.appdata.APP_DURATION,
                    dbcontract.appdata._ID
            };
          cursor = mdbhelper.getReadableDatabase().query(
              dbcontract.appdata.TABLE_NAME,
              cols,
              null,null
              ,null,
              null,
              null
          );
          while(cursor.moveToNext()){
              items.add(cursorToItem(cursor));
          }


        }
        finally {
            if(cursor!=null)
            cursor.close();
        }
        return items;

    }
    private AppItem1 cursorToItem(Cursor cursor){
        AppItem1 appitem=new AppItem1();
        appitem.appname=cursor.getString(cursor.getColumnIndex(dbcontract.appdata.APP_NAME));
        appitem.mUsageTime=cursor.getLong(cursor.getColumnIndex(dbcontract.appdata.APP_DURATION));
        return appitem;
    }


}