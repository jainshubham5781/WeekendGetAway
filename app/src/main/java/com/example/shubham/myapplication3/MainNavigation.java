package com.example.shubham.myapplication3;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shubham.myapplication3.adapter.NavDrawerListAdapter;
import com.example.shubham.myapplication3.location.GPSTracker;
import com.example.shubham.myapplication3.navigation_bar.ContactUsFragment;
import com.example.shubham.myapplication3.navigation_bar.HomeFragment;
import com.example.shubham.myapplication3.navigation_bar.ProfileFragment;
import com.example.shubham.myapplication3.navigation_bar.RecentlyViewedFragment;
import com.example.shubham.myapplication3.navigation_bar.RegisterFragment;
import com.example.shubham.myapplication3.navigation_bar.NavDrawerItem;
import com.example.shubham.myapplication3.navigation_bar.WishListFragment;

import java.util.ArrayList;

/**
 * Created by Shubham on 05-10-2015.
 */
public class MainNavigation extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String user_email;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    //   private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);





        SharedPreferences pref = getSharedPreferences("USERID", 0);
        user_email= pref.getString("user", null);
        Toast.makeText(this,user_email,Toast.LENGTH_SHORT);

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

//        // nav drawer icons from resources
//        navMenuIcons = getResources()
//                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();



        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
        // Find People
        if(user_email == null)
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
        else
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7]));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3]));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4]));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5]));


//        // Recycle the typed array
//        navMenuIcons.recycle();

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(this,
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,R.string.app_name,R.string.app_name){
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        GPSTracker gps;

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_nearby:
                gps = new GPSTracker(this);
                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                //  fragment = new FindPeopleFragment();
                if(user_email == null)
                    fragment = new RegisterFragment();
                else
                    fragment = new ProfileFragment();
                break;
            case 2:
                  fragment = new ContactUsFragment();
                  break;
            case 3:
                fragment = new RecentlyViewedFragment();
                break;
            case 4:
                fragment = new WishListFragment();
                break;
//            case 5:
//                fragment = new WhatsHotFragment();
//                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
}




