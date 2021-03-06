package com.example.shubham.myapplication3.food.restaurantDetailsSlideTabsFragment.RestaurantAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.food.restaurantDetailsSlideTabsFragment.restaurantReadReview.RestaurantReviewList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham on 09-11-2015.
 */
public class RestaurantDetailsReadReviewsListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<RestaurantReviewList> restaurantReviewLists;
    public RestaurantDetailsReadReviewsListAdapter(Activity activity, ArrayList<RestaurantReviewList> restaurantReviewLists)
    {
        this.activity=activity;
        this.restaurantReviewLists = restaurantReviewLists;
    }

    //size of list
    public int getCount() { return restaurantReviewLists.size();}

    public Object getItem(int location) {
        return restaurantReviewLists.get(location);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.movie_read_reviews_list, null);


        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView comment = (TextView) convertView.findViewById(R.id.review);
        TextView star = (TextView) convertView.findViewById(R.id.star);

        // getting movie data for the row
        RestaurantReviewList m = restaurantReviewLists.get(position);
        // title
        id.setText(m.getId());
        comment.setText(m.getComment());
        time.setText(m.getName());

        Double ss=m.getStar();
        star.setText(ss.toString()+"/5");
        return convertView;
    }


}
