package com.example.shubham.myapplication3.navigation_bar;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.wishList.HotelWishlist;
import com.example.shubham.myapplication3.wishList.MoviesWishlist;
import com.example.shubham.myapplication3.wishList.RestaurantWishlist;

/**
 * Created by Shubham on 28-11-2015.
 */
public class WishListFragment extends Fragment {
    private Button movie,restaurant,hotel;
    private static final String TAG = "WishListFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recently_viewed_fragment, container, false);
        movie = (Button) rootView.findViewById(R.id.button6);
        restaurant = (Button) rootView.findViewById(R.id.button10);
        hotel = (Button) rootView.findViewById(R.id.button11);
        TextView tv = (TextView) rootView.findViewById(R.id.textView12);
        tv.setText("Shortlisted Items");
        TextView loginFirst = (TextView) rootView.findViewById(R.id.textView15);
        SharedPreferences pref = getActivity().getSharedPreferences("USERID", 0);
        String user_email = pref.getString("user", null);
        if(user_email == null)
        {
            movie.setVisibility(View.GONE);
            restaurant.setVisibility(View.GONE);
            hotel.setVisibility(View.GONE);
            loginFirst.setVisibility(View.VISIBLE);
            loginFirst.setText("Please Login First");

        }
        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMoviesWishlist();
            }
        });

        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRestaurantsWishlist();
            }
        });

        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHotelsWishlist();
            }
        });

        return rootView;
    }

    private void goMoviesWishlist(){
        Intent intent = new Intent(getActivity(), MoviesWishlist.class);
        startActivity(intent);
    }
    private void goRestaurantsWishlist(){
        Intent intent = new Intent(getActivity(), RestaurantWishlist.class);
        startActivity(intent);
    }

    private void goHotelsWishlist(){
        Intent intent = new Intent(getActivity(), HotelWishlist.class);
        startActivity(intent);
    }



}
