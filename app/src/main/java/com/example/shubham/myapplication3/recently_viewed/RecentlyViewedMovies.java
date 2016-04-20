package com.example.shubham.myapplication3.recently_viewed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;
import com.example.shubham.myapplication3.location.GPSTracker;
import com.example.shubham.myapplication3.movieDetailsSlideTabsFragment.MovieDetailsTabsFragementActivity;
import com.example.shubham.myapplication3.movies.MovieList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shubham on 25-11-2015.
 */
public class RecentlyViewedMovies extends ActionBarActivity {

        private static final String TAG = RecentlyViewedMovies.class.getSimpleName();
        private ProgressDialog pDialog;         // for loading bar
        private ArrayList<MovieList> movieLists= new ArrayList<MovieList>();
        private ListView listView;
        private RecentlyViewedMoviesListAdapter adapter;
        private TextView tv;
        private SwipeRefreshLayout swipeRefreshLayout;
        // for search

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_movie);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            EditText input= (EditText) findViewById(R.id.inputSearch);
            input.setVisibility(View.GONE);

            tv = (TextView) findViewById(R.id.textView13);
            tv.setVisibility(View.GONE);

            Spinner filter = (Spinner) findViewById(R.id.spinner);
            Spinner sort = (Spinner) findViewById(R.id.spinner2);
            filter.setVisibility(View.GONE);
            sort.setVisibility(View.GONE);
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(true);
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    swipeRefreshLayout.setRefreshing(false);

                                    fetchdata();
                                }
                            }, 2000);
                }
            });

            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    fetchdata();
                }
            });



            RelativeLayout rll = (RelativeLayout) findViewById(R.id.rll);
            rll.setBackgroundColor(Color.parseColor("#fbcda7"));

            listView = (ListView)findViewById(R.id.list);
            adapter = new RecentlyViewedMoviesListAdapter(this, movieLists);
            listView.setAdapter(adapter);

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    boolean enable = false;
                    if (listView != null && listView.getChildCount() > 0) {
                        // check if the first item of the list is visible
                        boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                        // check if the top of the first item is visible
                        boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                        // enabling or disabling the refresh layout
                        enable = firstItemVisible && topOfFirstItemVisible;
                    }
                    swipeRefreshLayout.setEnabled(enable);
                }
            });

//            fetchdata();
            // click on list
            listView.setOnItemClickListener(new ListClickHandler());
        }

        //fethind data of movies
    private void fetchdata(){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        tv.setVisibility(View.GONE);
        movieLists.clear();
        SharedPreferences pref = getSharedPreferences("USERID", 0);
        String user_email = pref.getString("user", null);
        if(user_email == null){
            tv.setText("Please Login First");
            tv.setVisibility(View.VISIBLE);
        }
        else {
            pDialog.show();
            tv.setVisibility(View.GONE);
            String url = "http://172.16.110.69/laravel/public/appRecentlyViewedMovies" + "?id="+user_email;
            final JsonArrayRequest movieReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());
                            hidePDialog();
                            int flag = 1;
                            for (int i = 0; i < response.length(); i++) {
                                flag = 0;
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    MovieList movie = new MovieList();
                                    movie.setTitle(obj.getString("mname"));
                                    movie.setMid(obj.getInt("movi_id"));
                                    movie.setImageUrl(obj.getString("image"));
                                    movie.setVideoUrl(obj.getString("trailer"));
                                    Double ourRating = obj.getDouble("our_rating");
                                    Double rating = obj.getDouble("rating");
                                    if (rating == 0.0)
                                        movie.setRating(ourRating);
                                    else
                                        movie.setRating(rating);
                                    movie.setCast(obj.getString("mcast"));
                                    movie.setDirector(obj.getString("mdirector"));
                                    movie.setGenre(obj.getString("mgenre"));
                                    movie.setType(obj.getString("mcertificate"));
                                    movie.setDescription(obj.getString("mdescription"));
                                    movieLists.add(movie);       //add movies from url to listarray-movielists
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(flag == 1)
                            {
                                tv.setText("Nothing Viewed");
                                tv.setVisibility(View.VISIBLE);
                            }
                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    hidePDialog();
                }

            });
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(movieReq);
        }

    }







    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        GPSTracker gps;
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }
        else if(id == R.id.action_nearby){
            gps = new GPSTracker(this);
            // check if GPS enabled
            if(gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                // \n is for new line
                //save it in database
                SharedPreferences pref = getSharedPreferences("USERID", 0);
                String user_email = pref.getString("user", null);
                //Toast.makeText(MainMovies.this, user_email, Toast.LENGTH_SHORT).show();
                if (user_email == null)
                    Toast.makeText(RecentlyViewedMovies.this, "Please LogIn First", Toast.LENGTH_SHORT).show();
                else {
                    String loc_url = "http://172.16.110.69/laravel/public/applocation" + "?email=" + user_email;
                    HashMap<String, Double> params = new HashMap<>();
                    params.put("latitude", latitude);
                    params.put("longitude", longitude);
                    //request and response
                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, loc_url, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, response.toString());
                                    try {
                                        Toast.makeText(RecentlyViewedMovies.this, response.getString("msg"), Toast.LENGTH_SHORT).cancel();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            Toast.makeText(RecentlyViewedMovies.this, "Check Your Network Connection", Toast.LENGTH_SHORT);
                        }
                    });
                    AppController.getInstance().addToRequestQueue(req);
                    //end of storing in database
                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }
            }else{
                gps.showSettingsAlert();
            }
        }
        return super.onOptionsItemSelected(item);
    }




    // list click handler
    public class ListClickHandler implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            MovieList movie1 = movieLists.get(position);
            Intent intent = new Intent(RecentlyViewedMovies.this, MovieDetailsTabsFragementActivity.class);
            intent.putExtra("mname", movie1.getTitle());
            intent.putExtra("m_id", movie1.getMid());
            intent.putExtra("image", movie1.getImageUrl());
            intent.putExtra("rating", movie1.getRating());
            intent.putExtra("mcast", movie1.getCast());
            intent.putExtra("mcertificate",movie1.getType());
            intent.putExtra("mdirector", movie1.getDirector());
            intent.putExtra("mgenre",movie1.getGenre());
            intent.putExtra("mdescription",movie1.getDescription());
            intent.putExtra("trailer",movie1.getVideoUrl());
            startActivity(intent);
        }
    }


    public void deleteIconClickHandler(View v){
        ImageView del = (ImageView) v.findViewById(R.id.imageView6);
        int id = adapter.getposition((View) v.getParent().getParent());
        SharedPreferences pref = getSharedPreferences("USERID", 0);
        String user_email = pref.getString("user", null);
        delete(id, user_email);

    }


    public void delete(int id,String email){
        final String url = "http://172.16.110.69/laravel/public/appDeleteRecentlyViewedMovies";
        Map<String ,String> params = new HashMap<String,String>();
        String mid = String.valueOf(id);
        params.put("mid",mid);
        params.put("email", email);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String ress= response.getString("msg");
                            Toast.makeText(RecentlyViewedMovies.this,ress,Toast.LENGTH_SHORT).cancel();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        fetchdata();
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(RecentlyViewedMovies.this, "Check Your Network Connection", Toast.LENGTH_SHORT);

            }
        });
        AppController.getInstance().addToRequestQueue(req);


    }

  }
