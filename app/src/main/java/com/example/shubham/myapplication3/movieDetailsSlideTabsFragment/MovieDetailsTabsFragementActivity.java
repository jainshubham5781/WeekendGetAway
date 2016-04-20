package com.example.shubham.myapplication3.movieDetailsSlideTabsFragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.toolbox.ImageLoader;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.adapter.MovieDetailsViewPagerAdapter;
import com.example.shubham.myapplication3.app.AppController;
import com.example.shubham.myapplication3.location.GPSTracker;
import com.example.shubham.myapplication3.movieDetailsSlideTabsFragment.movieReadReview.MovieReadReviews;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MovieDetailsTabsFragementActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String title,image,cast,director,genre,type,movierating,description,m_id,trailer;
       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

           Intent intent = getIntent();
           title = intent.getStringExtra("mname");
           image = intent.getStringExtra("image");
           trailer = intent.getStringExtra("trailer");
           int mid = intent.getIntExtra("m_id",0);
           m_id = String.valueOf(mid);
           Double rating = intent.getDoubleExtra("rating", 0);
           movierating=rating.toString();
           type = intent.getStringExtra("mcertificate");
           genre = intent.getStringExtra("mgenre");
           director = intent.getStringExtra("mdirector");
           cast = intent.getStringExtra("mcast");
           description = intent.getStringExtra("mdescription");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



    }

    private void setupViewPager(ViewPager viewPager) {


        MovieDetailsViewPagerAdapter adapter = new MovieDetailsViewPagerAdapter(getSupportFragmentManager());
        MovieDescription movieDescription = new MovieDescription();
        MovieReadReviews movieReadReviews = new MovieReadReviews();
        MovieAddReviews movieAddReviews =new MovieAddReviews();
        adapter.addFragment(movieDescription, "Details");
        adapter.addFragment(movieReadReviews, "Reviews");
        adapter.addFragment(movieAddReviews, "Write a Review");
        viewPager.setAdapter(adapter);
        Bundle bundle =new Bundle();
        addDataToDescription(bundle);

        movieDescription.setArguments(bundle);
        movieReadReviews.setArguments(bundle);
        movieAddReviews.setArguments(bundle);

    }

    public void addDataToDescription(Bundle bundle){
        SharedPreferences pref = getSharedPreferences("USERID", 0);
        String user_email = pref.getString("user", null);
        bundle.putString("email",user_email);
        bundle.putString("mname", title);
        bundle.putString("image", image);
        bundle.putString("rating", movierating);
        bundle.putString("mcast", cast);
        bundle.putString("mcertificate", type);
        bundle.putString("mdirector", director);
        bundle.putString("mgenre", genre);
        bundle.putString("trailer",trailer);
        bundle.putString("mdescription", description);
        bundle.putString("m_id", m_id);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
