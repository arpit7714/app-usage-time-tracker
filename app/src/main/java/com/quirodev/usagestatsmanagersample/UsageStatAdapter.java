package com.quirodev.usagestatsmanagersample;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UsageStatAdapter extends RecyclerView.Adapter<UsageStatVH> {

    private List<AppItem1> list;

    //it will not rerquire any extral list beacuse adapter has been provided with the list in mainactivity with setlist() method
    public UsageStatAdapter(){
        list = new ArrayList<>();
    }

    @Override
    public UsageStatVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usage_stat_item, parent, false);
        return new UsageStatVH(view);
    }

    @Override
    public void onBindViewHolder(UsageStatVH holder, int position) {
        holder.bindTo(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<AppItem1> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}