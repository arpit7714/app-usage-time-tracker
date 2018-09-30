package com.quirodev.usagestatsmanagersample;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentOne extends Fragment {
    TextView t1;
       @Override

       public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
           // TODO Auto-generated method stub
                   View v = inflater.inflate(R.layout.fragmentone, container,false);
           String myValue = this.getArguments().getString("total_time_used");
           t1= (TextView) v.findViewById(R.id.title_text);
           t1.setText(myValue);
                    return v;

       }
  }