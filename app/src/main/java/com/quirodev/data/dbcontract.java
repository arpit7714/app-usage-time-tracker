package com.quirodev.data;

import android.provider.BaseColumns;
//this is the java class used to define
//the column names of the datsbase table
 public final class dbcontract {

    private dbcontract(){}

    //represent a single table in the database
      public static final class appdata implements BaseColumns{
        //name of the table
        public static final String TABLE_NAME="usagetable";
        public static final String _AL="date";
        public static final String APP_NAME="appname";
        public  static final String APP_DURATION="duraion";
        public static final String _ID=BaseColumns._ID;

    }

}
