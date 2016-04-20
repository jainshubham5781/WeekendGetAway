package com.example.shubham.myapplication3.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.navigation_bar.NavDrawerItem;


import java.util.ArrayList;

/**
 * Created by Shubham on 29-09-2015.
 */
public class NavDrawerListAdapter extends BaseAdapter {

    //private Context context;
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Activity activity, ArrayList<NavDrawerItem> navDrawerItems){
        this.activity = activity;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.drawer_list_item, null);


      //  ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        //imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        NavDrawerItem nav = navDrawerItems.get(position);
        txtTitle.setText(nav.getTitle());

        // displaying count
        // check whether it set visible or not

        if(nav.getCounterVisibility()){
            txtCount.setText(nav.getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }

        return convertView;
    }

}
