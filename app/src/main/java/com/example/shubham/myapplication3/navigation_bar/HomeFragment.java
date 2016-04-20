package com.example.shubham.myapplication3.navigation_bar;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shubham.myapplication3.MainNavigation;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;
import com.example.shubham.myapplication3.food.MainRestaurant;
import com.example.shubham.myapplication3.hotels.MainHotels;
import com.example.shubham.myapplication3.movies.MainMovies;
import com.example.shubham.myapplication3.register.SignUp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Shubham on 05-10-2015.
 */
public class HomeFragment extends Fragment{
    private static final String TAG = "MainPage";
    private ImageView movies,restaurants,hotels;
    public int currentimageindex=0;
    private Animation rotateimage;
    public Activity activity;
    Timer timer;
    TimerTask task;

    private int[] IMAGE_MOVIE = {
            R.drawable.ma, R.drawable.mb, R.drawable.mc,
            R.drawable.md,R.drawable.me, R.drawable.mf, R.drawable.mg,
            R.drawable.mh
    };
    private int[] IMAGE_RESTAURANT = {
            R.drawable.ra, R.drawable.rb, R.drawable.rc,
            R.drawable.rd
    };
    private int[] IMAGE_HOTEL = {
            R.drawable.h1, R.drawable.h2, R.drawable.h3,
            R.drawable.h4,R.drawable.h5,R.drawable.h6
    };

    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_main, container, false);
        activity=getActivity();

        movies = (ImageView) rootView.findViewById(R.id.imageView8);
        movies.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gotomovies();
            }
        });
        restaurants = (ImageView) rootView.findViewById(R.id.imageView9);
        restaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoFood();
            }
        });
        hotels = (ImageView) rootView.findViewById(R.id.imageView10);
        hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoHotels();
            }
        });

        final Handler mHandler = new Handler();
        final Runnable mUpdateResults = new Runnable() {
            public void run() {
                AnimateandSlideShow();
            }
        };
        int delay = 1000; // delay for 1 sec.
        int period = 3000; // repeat every 4 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                mHandler.post(mUpdateResults);
            }
        }, delay, period);
        return rootView;
    }


    private void gotomovies(){
        rotateimage.setFillAfter(true);
        Intent intent= new Intent(getActivity(), MainMovies.class);
        startActivity(intent);
    }

    private void gotoFood(){
        rotateimage.setFillAfter(true);
        Intent intent = new Intent(getActivity(), MainRestaurant.class);
        startActivity(intent);
    }

    private void gotoHotels(){
        rotateimage.setFillAfter(true);
        Intent intent = new Intent(getActivity(), MainHotels.class);
        startActivity(intent);
    }

    private void AnimateandSlideShow() {

        movies.setImageResource(IMAGE_MOVIE[currentimageindex%IMAGE_MOVIE.length]);
        restaurants.setImageResource(IMAGE_RESTAURANT[currentimageindex % IMAGE_RESTAURANT.length]);
        hotels.setImageResource(IMAGE_HOTEL[currentimageindex % IMAGE_HOTEL.length]);
        currentimageindex++;

        rotateimage = AnimationUtils.loadAnimation(activity, R.anim.fade_in2);
        movies.startAnimation(rotateimage);
        restaurants.startAnimation(rotateimage);
        hotels.startAnimation(rotateimage);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rotateimage.setFillAfter(true);
    }
}
