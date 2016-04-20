package com.example.shubham.myapplication3.food.restaurantDetailsSlideTabsFragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.toolbox.ImageLoader;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;
import com.example.shubham.myapplication3.food.restaurantDetailsSlideTabsFragment.RestaurantAdapter.RestaurantDetailsViewPagerAdapter;
import com.example.shubham.myapplication3.food.restaurantDetailsSlideTabsFragment.RestaurantMenu.RestaurantMenuFragment;
import com.example.shubham.myapplication3.food.restaurantDetailsSlideTabsFragment.restaurantReadReview.RestaurantReadReviews;


public class RestaurantDetailsTabsFragementActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String name,id;
       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

           Intent intent = getIntent();
           name = intent.getStringExtra("name");
           int res_id = intent.getIntExtra("id",0);
           id = String.valueOf(res_id);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           getSupportActionBar().hide();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {


        RestaurantDetailsViewPagerAdapter adapter = new RestaurantDetailsViewPagerAdapter(getSupportFragmentManager());
        RestaurantMenuFragment restaurantMenuFragment = new RestaurantMenuFragment();
        RestaurantReadReviews restaurantReadReviews = new RestaurantReadReviews();
        RestaurantAddReviews restaurantAddReviews =new RestaurantAddReviews();
        adapter.addFragment(restaurantMenuFragment, "Menu");
        adapter.addFragment(restaurantReadReviews, "Reviews");
        adapter.addFragment(restaurantAddReviews, "Write a Review");
        viewPager.setAdapter(adapter);

        Bundle bundle =new Bundle();
        addDataToDescription(bundle);

        restaurantMenuFragment.setArguments(bundle);
        restaurantReadReviews.setArguments(bundle);
        restaurantAddReviews.setArguments(bundle);

    }

    public void addDataToDescription(Bundle bundle){
        SharedPreferences pref = getSharedPreferences("USERID", 0);
        String user_email = pref.getString("user", null);
        bundle.putString("email",user_email);
        bundle.putString("name", name);
        bundle.putString("id", id);

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
