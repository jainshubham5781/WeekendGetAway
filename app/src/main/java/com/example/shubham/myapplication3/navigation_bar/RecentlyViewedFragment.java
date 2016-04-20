package com.example.shubham.myapplication3.navigation_bar;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.recently_viewed.RecentlyViewedHotels;
import com.example.shubham.myapplication3.recently_viewed.RecentlyViewedMovies;
import com.example.shubham.myapplication3.recently_viewed.RecentlyViewedRestaurants;

/**
 * Created by Shubham on 23-11-2015.
 */
public class RecentlyViewedFragment extends Fragment{
    private static final String TAG = "RecentyViewedFragment";
    private Button movie,restaurant,hotel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recently_viewed_fragment, container, false);
        movie = (Button) rootView.findViewById(R.id.button6);
        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMovies();
            }
        });

        restaurant = (Button) rootView.findViewById(R.id.button10);
        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRestaurants();
            }
        });
        hotel = (Button) rootView.findViewById(R.id.button11);
        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHotels();
            }
        });


        return rootView;
    }


    private void goMovies(){
        Intent intent = new Intent(getActivity(), RecentlyViewedMovies.class);
        startActivity(intent);
    }

    private void goRestaurants(){
        Intent intent = new Intent(getActivity(), RecentlyViewedRestaurants.class);
        startActivity(intent);
    }

    private void goHotels(){
        Intent intent = new Intent(getActivity(), RecentlyViewedHotels.class);
        startActivity(intent);
    }



}
