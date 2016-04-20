package com.example.shubham.myapplication3.wishList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Shubham on 28-11-2015.
 */
public class MoviesWishlist extends ActionBarActivity{

private static final String TAG = MoviesWishlist.class.getSimpleName();
Spinner sort_spinner, filter_spinner;
//private static final String url = "http://api.androidhive.info/json/movies.json";
private ProgressDialog pDialog;         // for loading bar
private ArrayList<MovieList> movieLists= new ArrayList<MovieList>();
private ListView listView;
private MoviesWishlistCustomListAdapter adapter;
// for search
EditText inputSearch;
private ArrayList<MovieList> arraylist= new ArrayList<MovieList>();
private ArrayList<MovieList> sortedList= new ArrayList<MovieList>();
private ArrayList<MovieList> filterlist = new ArrayList<MovieList>();
private ArrayList<String> fav = new ArrayList<String>();
private ArrayList<String> nullfav = new ArrayList<String>();
private SwipeRefreshLayout swipeRefreshLayout;
private String text1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_movie);
            fav.clear();
            fetch();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            listView = (ListView)findViewById(R.id.list);
            adapter = new MoviesWishlistCustomListAdapter(this, movieLists);
            listView.setAdapter(adapter);
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
                            }, 1000);
                }
            });

            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    fetchdata();
                }
            });


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




            // click on list
            listView.setOnItemClickListener(new ListClickHandler());




            // Locate the EditText in listview_main.xml
            inputSearch = (EditText) findViewById(R.id.inputSearch);
            inputSearch.setText("");
            // Capture Text in EditText and seach
            inputSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                    filter(text);
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                    // TODO Auto-generated method stub
                }
            });



            ///spiinner for sorting
            sort_spinner = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> spinneradapter = ArrayAdapter.createFromResource(this,
                    R.array.sort_list, android.R.layout.preference_category);
            spinneradapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
            sort_spinner.setAdapter(spinneradapter);
            sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            // Sort title:nothing to do
                            break;
                        case 1:
                            Collections.sort(sortedList, new Comparator<MovieList>() {
                                @Override
                                public int compare(final MovieList object1, final MovieList object2) {
                                    return object1.getTitle().compareTo(object2.getTitle());
                                }
                            });
                            movieLists.clear();
                            movieLists.addAll(sortedList);
                            Toast.makeText(MoviesWishlist.this, "A-Z Sorted", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            text1 = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                            filter(text1);

                            break;
                        case 2:
                            Collections.reverse(sortedList);
                            Collections.sort(sortedList, new Comparator<MovieList>() {
                                @Override
                                public int compare(final MovieList object1, final MovieList object2) {
                                    return object1.getTitle().compareTo(object2.getTitle());
                                }
                            });
                            Collections.reverse(sortedList);
                            movieLists.clear();
                            movieLists.addAll(sortedList);
                            Toast.makeText(MoviesWishlist.this, "Z-A Sorted", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            text1 = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                            filter(text1);

                            break;
                        case 3:
                            Collections.sort(sortedList, new Comparator<MovieList>() {
                                @Override
                                public int compare(final MovieList object1, final MovieList object2) {
                                    return object1.getRating() > object2.getRating() ? -1 : object1.getRating() < object2.getRating() ? 1 : 0;
                                }
                            });
                            movieLists.clear();
                            movieLists.addAll(sortedList);
                            Toast.makeText(MoviesWishlist.this, "Sorted by Rating", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            text1 = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                            filter(text1);

                            break;
                        case 4:
                            movieLists.clear();
                            sortedList.clear();
                            movieLists.addAll(arraylist);
                            sortedList.addAll(arraylist);
                            Toast.makeText(MoviesWishlist.this, "Custom", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            text1 = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                            filter(text1);

                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            //end of spiinner for sorting


            //spinner for filter location
            filter_spinner = (Spinner) findViewById(R.id.spinner2);
            ArrayAdapter<CharSequence> filteradapter = ArrayAdapter.createFromResource(this,
                    R.array.filter_list, android.R.layout.preference_category);
            filteradapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
            filter_spinner.setAdapter(filteradapter);
            filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String location = filter_spinner.getSelectedItem().toString();
                    if(position !=0)
                        changelocation(position);
                    sort_spinner.setSelection(0);
                    inputSearch.setText("");
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        }

        //fethind data of movies
        private void fetchdata(){
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            movieLists.clear();
            arraylist.clear();
            sortedList.clear();
            filterlist.clear();

            SharedPreferences pref = getSharedPreferences("USERID", 0);
            String user_email = pref.getString("user", null);
            // String url = "http://172.16.105.47/laravel/public/appmoviesfilter"+"?id=";
            String url = "http://172.16.110.69/laravel/public/appmoviesWishlist"+"?email="+user_email+"&"+"id=";
            final JsonArrayRequest movieReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());
                            hidePDialog();

                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj= response.getJSONObject(i);
                                    MovieList movie = new MovieList();
                                    movie.setTitle(obj.getString("mname"));
                                    movie.setMid(obj.getInt("movi_id"));
                                    movie.setImageUrl(obj.getString("image"));
                                    movie.setVideoUrl(obj.getString("trailer"));

                                    Double ourRating = obj.getDouble("our_rating");
                                    Double rating = obj.getDouble("rating");
                                    if(rating == 0.0)
                                        movie.setRating(ourRating);
                                    else
                                        movie.setRating(rating);
                                    movie.setCast(obj.getString("mcast"));
                                    movie.setDirector(obj.getString("mdirector"));
                                    movie.setGenre(obj.getString("mgenre"));
                                    movie.setType(obj.getString("mcertificate"));
                                    movie.setDescription(obj.getString("mdescription"));
                                    movieLists.add(movie);       //add movies from url to listarray-movielists
                                    arraylist.add(movie);          // adding to list used for search
                                    sortedList.add(movie);      // adding to list used for sorting
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();
                            sort_spinner.setSelection(0);
                            filter_spinner.setSelection(0);
                            inputSearch.setText("");
                            swipeRefreshLayout.setRefreshing(false);

                        }

                    },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    hidePDialog();
                }

            });
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(movieReq);

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
                        Toast.makeText(MoviesWishlist.this, "Please LogIn First", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(MoviesWishlist.this, response.getString("msg"), Toast.LENGTH_LONG).cancel();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                Toast.makeText(MoviesWishlist.this, "Check Your Network Connection", Toast.LENGTH_SHORT);
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
        Intent intent = new Intent(MoviesWishlist.this, MovieDetailsTabsFragementActivity.class);
        intent.putExtra("mname", movie1.getTitle());
        intent.putExtra("m_id", movie1.getMid());
        intent.putExtra("image", movie1.getImageUrl());
        intent.putExtra("rating", movie1.getRating());
        intent.putExtra("mcast", movie1.getCast());
        intent.putExtra("mcertificate",movie1.getType());
        intent.putExtra("trailer",movie1.getVideoUrl());
        intent.putExtra("mdirector", movie1.getDirector());
        intent.putExtra("mgenre",movie1.getGenre());
        intent.putExtra("mdescription",movie1.getDescription());
//                intent.putExtra("movie_location", movie.getLocation());

        SharedPreferences pref = getSharedPreferences("USERID", 0);
        String user_email = pref.getString("user", null);
        if(user_email != null)
            addToRecentlyViewed(user_email,movie1.getMid());
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

    public void heartClickHandler(View v){

        SharedPreferences pref = getSharedPreferences("USERID", 0);
        String user_email = pref.getString("user", null);
        if(user_email == null)
            Toast.makeText(MoviesWishlist.this,"LogIn to Add to Wishlist",Toast.LENGTH_SHORT).show();
        else {
            ImageView imageView = (ImageView) v.findViewById(R.id.imageView4);
            int id = adapter.getposition((View) v.getParent().getParent());
            String tag = imageView.getTag().toString();
            if (tag.equalsIgnoreCase("grey")) {
                imageView.setTag("red");
                imageView.setImageResource(R.drawable.imgthing);
                add(id,user_email);
            } else {
                imageView.setTag("grey");
                imageView.setImageResource(R.drawable.hheart);
                delete(id,user_email);
            }
        }
    }

    // fetch data for favourite
    public void fetch(){
        SharedPreferences pref = getSharedPreferences("USERID", 0);
        String user_email = pref.getString("user", null);
        String url = "http://172.16.110.69/laravel/public/appGetfavourite"+"?email="+user_email;
        final JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj= response.getJSONObject(i);
                                int id = obj.getInt("id");
                                String mid = String.valueOf(id);
                                fav.add(mid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    // used in custum list adapter
    public List<String> getList1(){
        final ArrayList<String> favourite = new ArrayList<String>();
        SharedPreferences pref1 = getSharedPreferences("USERID", 0);
        String user_email = pref1.getString("user", null);
        if(user_email != null) {
            //    fetch();
            favourite.clear();
            favourite.addAll(fav);
            return favourite;
        }
        else
        {
            return nullfav;
        }
    }

    public void add(int id,String email){
        //Toast.makeText(MainMovies.this,"add"+id,Toast.LENGTH_SHORT).show();
        final String url1 = "http://172.16.110.69/laravel/public/appAddfavourite";
        Map<String ,String> params = new HashMap<String,String>();
        String mid = String.valueOf(id);
        if(!fav.contains(mid))
            fav.add(mid);
        params.put("mid",mid);
        params.put("email",email);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url1, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String ress= response.getString("msg");
                            Toast.makeText(MoviesWishlist.this,ress,Toast.LENGTH_SHORT).cancel();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MoviesWishlist.this, "Check Your Network Connection", Toast.LENGTH_SHORT);

            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }

    public void delete(int id,String email){

        //Toast.makeText(MainMovies.this,"delete"+id,Toast.LENGTH_SHORT).show();
        final String url = "http://172.16.110.69/laravel/public/appDeletefavourite";
        Map<String ,String> params = new HashMap<String,String>();
        String mid = String.valueOf(id);
        if(fav.contains(mid))
            fav.remove(mid);
        //fav.clear();
        params.put("mid",mid);
        params.put("email",email);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String ress= response.getString("msg");
                            Toast.makeText(MoviesWishlist.this,ress,Toast.LENGTH_SHORT).cancel();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        fetchdata();
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MoviesWishlist.this, "Check Your Network Connection", Toast.LENGTH_SHORT);

            }
        });
        AppController.getInstance().addToRequestQueue(req);


    }

    private void addToRecentlyViewed(String email,int mid){
        final String url = "http://172.16.110.69/laravel/public/appAddToRecentlyViewedMovies";
        Map<String ,String> params = new HashMap<String,String>();
        String m_id = String.valueOf(mid);
        params.put("mid", m_id);
        params.put("email", email);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String ress= response.getString("msg");
                            Toast.makeText(MoviesWishlist.this,ress,Toast.LENGTH_SHORT).cancel();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MoviesWishlist.this, "Check Your Network Connection", Toast.LENGTH_SHORT);

            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }



    // input search
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        movieLists.clear();
        if (charText.length() == 0) {
            movieLists.addAll(sortedList);
        } else {
            for (MovieList wp : sortedList) {
                if (wp.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    movieLists.add(wp);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    // spinner of change location
    public void changelocation(int position){
        int position1=position;
        SharedPreferences pref = getSharedPreferences("USERID", 0);
        String user_email = pref.getString("user", null);
        movieLists.clear();      //add movies from url to listarray-movielists
        arraylist.clear();          // adding to list used for search
        sortedList.clear();      // adding to list used for sorting
        filterlist.clear();
        final String url1 = "http://172.16.110.69/laravel/public/appmoviesWishlist"+"?email="+user_email+"&"+"id="+position1;

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        //url1 +="?id="+position1;
//        Toast.makeText(MainMovies.this,url1,Toast.LENGTH_LONG).show();
        final JsonArrayRequest movieReq = new JsonArrayRequest(url1,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj= response.getJSONObject(i);
                                MovieList movie = new MovieList();
                                movie.setTitle(obj.getString("mname"));
//                              movie.setTitle(obj.getString("title"));
                                movie.setImageUrl(obj.getString("image"));
                                movie.setVideoUrl(obj.getString("trailer"));

                                Double ourRating = obj.getDouble("our_rating");
                                Double rating = obj.getDouble("rating");
                                if(rating == 0.0)
                                    movie.setRating(ourRating);
                                else
                                    movie.setRating(rating);
                                movie.setCast(obj.getString("mcast"));
                                movie.setMid(obj.getInt("movi_id"));
                                movie.setDirector(obj.getString("mdirector"));
                                movie.setGenre(obj.getString("mgenre"));
                                movie.setType(obj.getString("mcertificate"));
                                movie.setDescription(obj.getString("mdescription"));
                                movieLists.add(movie);       //add movies from url to listarray-movielists
                                arraylist.add(movie);          // adding to list used for search
                                sortedList.add(movie);      // adding to list used for sorting
                                filterlist.add(movie);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }

        });
        AppController.getInstance().addToRequestQueue(movieReq);
    }


}
