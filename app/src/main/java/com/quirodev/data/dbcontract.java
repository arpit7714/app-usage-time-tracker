package com.quirodev.data;

import android.provider.BaseColumns;
//this is the java class used to define
//the column names of the datsbase table
 class dbcontract {

    public dbcontract(){}

    //represent a single table in the database
      class appdata implements BaseColumns{
        //name of the table
        static final String TABLE_NAME="usagetable";
        static final String APP_NAME="appname";
        static final String APP_DURATION="duraion";
        static final String _ID=BaseColumns._ID;

    }

}
