package com.quirodev.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//this is used to create a table in the database
public class dbhelper extends SQLiteOpenHelper {


    //private static final String LOG_TAG=dbhelper.class.getSimpleName();
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="apphistory.db";

    public dbhelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_APP_HISTORY_TABLE=
                "CREATE TABLE "+dbcontract.appdata.TABLE_NAME+" ("
                +dbcontract.appdata._ID+" INTEGER PRIMARY KEY,"+
                dbcontract.appdata.APP_NAME+" TEXT,"+
                dbcontract.appdata.APP_DURATION+" INTEGER)";

        //it opens a connection to the database and creates a
        //a table in the databse
        db.execSQL(SQL_CREATE_APP_HISTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String SQL_DELETE_ENTRIES="DROP TABLE IF EXISTS " + dbcontract.appdata.TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
