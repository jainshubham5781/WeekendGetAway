package com.example.shubham.myapplication3.recently_viewed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.shubham.myapplication3.hotels.Hotel;
import com.example.shubham.myapplication3.hotels.hotelDetailsSlideTabsFragment.HotelDetailsTabsFragementActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shubham on 25-11-2015.
 */
public class RecentlyViewedHotels extends ActionBarActivity {
    private static final String TAG = RecentlyViewedHotels.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Hotel> hotelLists = new ArrayList<Hotel>();
    private RecentlyViewedHotelCardviewAdapter restaurantCardviewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int position;
    private RecyclerView recList;
    private TextView tv;
    private LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_main_restaurant);
        //back icon on actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        restaurantCardviewAdapter = new RecentlyViewedHotelCardviewAdapter(this, hotelLists);
        recList.setAdapter(restaurantCardviewAdapter);
        hotelLists.clear();

        EditText input= (EditText) findViewById(R.id.inputSearch);
        input.setVisibility(View.GONE);
        tv = (TextView) findViewById(R.id.textView14);
        tv.setVisibility(View.GONE);
        Spinner filter = (Spinner) findViewById(R.id.spinner);
        Spinner sort = (Spinner) findViewById(R.id.spinner2);
        filter.setVisibility(View.GONE);
        sort.setVisibility(View.GONE);




        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.GREEN,Color.BLUE,Color.CYAN);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);

                                fetch();
                            }
                        }, 1000);
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                fetch();
            }
        });
        recList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // TODO Auto-generated method stub
                //super.onScrollStateChanged(recyclerView, newState);
                try {
                    int firstPos = llm.findFirstCompletelyVisibleItemPosition();
                    if (firstPos > 0) {
                        swipeRefreshLayout.setEnabled(false);
                    } else {
                        swipeRefreshLayout.setEnabled(true);
                        if (recList.getScrollState() == 1)
                            if (swipeRefreshLayout.isRefreshing())
                                recList.stopScroll();
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Scroll Error : " + e.getLocalizedMessage());
                }
            }
        });



//        recList.addOnItemTouchListener(new RecyclerItemClickListener(
//                this, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View itemView, int position) {
//                RecentlyViewedRestaurants.this.position = position;
//                openDetails(itemView);
//
//
//            }
//        }));


    }

    private void fetch(){
        hotelLists.clear();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        tv.setVisibility(View.GONE);
        SharedPreferences pref = getSharedPreferences("USERID", 0);
        String user_email = pref.getString("user", null);
        if(user_email == null){
            tv.setText("Please Login First");
            tv.setVisibility(View.VISIBLE);
        }
        else {
            final String url = "http://172.16.110.69/laravel/public/appRecentlyViewedHotels"+"?id="+user_email;
            tv.setVisibility(View.GONE);
            pDialog.show();
            final JsonArrayRequest movieReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());
                            hidePDialog();

                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    Hotel hotel = new Hotel();
                                    //hotel.setImageUrl(obj.getString("image"));
                                    hotel.setName(obj.getString("hname"));
                                    //hotel.setType(obj.getString("type"));
                                    hotel.setRegion(obj.getString("name"));
                                    hotel.setArea(obj.getString("aname"));
                                    hotel.setId(obj.getInt("id"));
                                    Double ourRating = obj.getDouble("our_rating");
                                    Double rating = obj.getDouble("rating");
                                    if (rating == 0.0)
                                        hotel.setRating(ourRating);
                                    else
                                        hotel.setRating(rating);

                                    hotelLists.add(hotel);//add movies from url to listarray-movielists
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            restaurantCardviewAdapter.notifyDataSetChanged();
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



//
//    private void openDetails(View itemView) {
//        Restaurant restaurant = hotelLists.get(position);
//        Intent intent = new Intent(RecentlyViewedRestaurants.this, RestaurantDetailsTabsFragementActivity.class);
//        intent.putExtra("name", restaurant.getName());
//        intent.putExtra("id", restaurant.getId());
//        startActivity(intent);
//
//    }


    public void openDetail(View itemView) {
        int id = restaurantCardviewAdapter.getposition((View) itemView);
        Hotel restaurant = hotelLists.get(id);
        Intent intent = new Intent(RecentlyViewedHotels.this, HotelDetailsTabsFragementActivity.class);
        intent.putExtra("name", restaurant.getName());
        intent.putExtra("id", restaurant.getId());
        startActivity(intent);
    }


    public void hotelDeleteIconClickHandler(View v){
        ImageView del = (ImageView) v.findViewById(R.id.imageView7);
        int id = restaurantCardviewAdapter.getposition((View) v.getParent());
        SharedPreferences pref = getSharedPreferences("USERID", 0);
        String user_email = pref.getString("user", null);
        delete(id, user_email);

    }


    public void delete(int sid,String email){
        Hotel hotel = hotelLists.get(sid);
        int id = hotel.getId();
        final String url = "http://172.16.110.69/laravel/public/appDeleteRecentlyViewedHotels";
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
                            Toast.makeText(RecentlyViewedHotels.this, ress, Toast.LENGTH_SHORT).cancel();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        fetch();
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(RecentlyViewedHotels.this, "Check Your Network Connection", Toast.LENGTH_SHORT);

            }
        });
        AppController.getInstance().addToRequestQueue(req);


    }



}
