package com.example.shubham.myapplication3.hotels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.shubham.myapplication3.hotels.hotelDetailsSlideTabsFragment.HotelDetailsTabsFragementActivity;

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
 * Created by Shubham on 18-11-2015.
 */
public class MainHotels extends ActionBarActivity {
    private static final String TAG = MainHotels.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Hotel> hotelLists = new ArrayList<Hotel>();
    private List<Hotel> sortedList = new ArrayList<Hotel>();
    private List<Hotel> arraylist = new ArrayList<Hotel>();
    private List<Hotel> filterlist = new ArrayList<Hotel>();
    private HotelCardviewAdapter hotelCardviewAdapter;
    private EditText inputSearch;
    private RecyclerView recList;
    private LinearLayoutManager llm;
    private Spinner sort_spinner, filter_spinner;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String text1;
    private int position;
    private ArrayList<String> fav = new ArrayList<String>();
    private ArrayList<String> nullfav = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_main_restaurant);
        //back icon on actionbar
        fav.clear();
        fetchdata();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        hotelCardviewAdapter = new HotelCardviewAdapter(this, hotelLists);
        recList.setAdapter(hotelCardviewAdapter);
        hotelLists.clear();




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
//                MainRestaurant.this.position = position;
//                openDetails(itemView);
//            }
//        }));




        // Locate the EditText in listview_main.xml
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.setText("");
        inputSearch.setHint("Search Hotels...");
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
                R.array.sort_list1, android.R.layout.preference_category);
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
                        Collections.sort(sortedList, new Comparator<Hotel>() {
                            @Override
                            public int compare(final Hotel object1, final Hotel object2) {
                                return object1.getName().compareTo(object2.getName());
                            }
                        });
                        hotelLists.clear();
                        hotelLists.addAll(sortedList);
                        Toast.makeText(MainHotels.this, "A-Z Sorted", Toast.LENGTH_SHORT).show();
                        hotelCardviewAdapter.notifyDataSetChanged();
                        text1 = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                        filter(text1);

                        break;
                    case 2:
                        Collections.reverse(sortedList);
                        Collections.sort(sortedList, new Comparator<Hotel>() {
                            @Override
                            public int compare(final Hotel object1, final Hotel object2) {
                                return object1.getName().compareTo(object2.getName());
                            }
                        });
                        Collections.reverse(sortedList);
                        hotelLists.clear();
                        hotelLists.addAll(sortedList);
                        Toast.makeText(MainHotels.this, "Z-A Sorted", Toast.LENGTH_SHORT).show();
                        hotelCardviewAdapter.notifyDataSetChanged();
                        text1 = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                        filter(text1);
                        break;
                    case 3:
                        Collections.sort(sortedList, new Comparator<Hotel>() {
                            @Override
                            public int compare(final Hotel object1, final Hotel object2) {
                                return object1.getRating() > object2.getRating() ? -1 : object1.getRating() < object2.getRating() ? 1 : 0;
                            }
                        });
                        hotelLists.clear();
                        hotelLists.addAll(sortedList);
                        Toast.makeText(MainHotels.this, "Sorted by Rating", Toast.LENGTH_SHORT).show();
                        hotelCardviewAdapter.notifyDataSetChanged();
                        text1 = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                        filter(text1);

                        break;
                    case 4:
                        hotelLists.clear();
                        sortedList.clear();
                        hotelLists.addAll(arraylist);
                        sortedList.addAll(arraylist);
                        Toast.makeText(MainHotels.this, "Custom", Toast.LENGTH_SHORT).show();
                        hotelCardviewAdapter.notifyDataSetChanged();
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




    private void fetch(){
        final String url = "http://172.16.110.69/laravel/public/appHotels";
        arraylist.clear();
        hotelLists.clear();
        sortedList.clear();
        filterlist.clear();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        final JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj= response.getJSONObject(i);
                                Hotel hotel = new Hotel();
                                //hotel.setImageUrl(obj.getString("image"));
                                hotel.setName(obj.getString("hname"));
                               // hotel.setType(obj.getString("type"));
                                hotel.setRegion(obj.getString("name"));
                                hotel.setArea(obj.getString("aname"));
                                hotel.setId(obj.getInt("id"));
                                Double ourRating = obj.getDouble("our_rating");
                                Double rating = obj.getDouble("rating");
                                if(rating == 0.0)
                                    hotel.setRating(ourRating);
                                else
                                    hotel.setRating(rating);

                                hotelLists.add(hotel);//add movies from url to listarray-movielists
                                sortedList.add(hotel);
                                arraylist.add(hotel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        hotelCardviewAdapter.notifyDataSetChanged();
                        sort_spinner.setSelection(0);
                        inputSearch.setText("");
                        filter_spinner.setSelection(0);
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

    // input search
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        hotelLists.clear();
        if (charText.length() == 0) {
            hotelLists.addAll(sortedList);
        } else {
            for (Hotel wp : sortedList) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    hotelLists.add(wp);
                }
            }
        }
        hotelCardviewAdapter.notifyDataSetChanged();
    }




    // spinner of change location
    public void changelocation(int position){
        int position1=position;
        hotelLists.clear();      //add movies from url to listarray-movielists
        arraylist.clear();          // adding to list used for search
        sortedList.clear();      // adding to list used for sorting
        filterlist.clear();
        final String url1 = "http://172.16.110.69/laravel/public/appHotelsFilter"+"?id="+position1;

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        final JsonArrayRequest movieReq = new JsonArrayRequest(url1,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj= response.getJSONObject(i);
                                Hotel restaurant = new Hotel();
                                //restaurant.setImageUrl(obj.getString("image"));
                                restaurant.setName(obj.getString("hname"));
                                //restaurant.setType(obj.getString("type"));
                                restaurant.setRegion(obj.getString("name"));
                                restaurant.setArea(obj.getString("aname"));
                                Double ourRating = obj.getDouble("our_rating");
                                Double rating = obj.getDouble("rating");
                                if(rating == 0.0)
                                    restaurant.setRating(ourRating);
                                else
                                    restaurant.setRating(rating);
                                restaurant.setId(obj.getInt("id"));
                                hotelLists.add(restaurant);//add movies from url to listarray-movielists
                                sortedList.add(restaurant);
                                arraylist.add(restaurant);
                                filterlist.add(restaurant);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        hotelCardviewAdapter.notifyDataSetChanged();
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



    public void openDetail(View itemView) {
        int id = hotelCardviewAdapter.getposition((View) itemView);
        //Toast.makeText("")mView) {
        Hotel hotel = hotelLists.get(id);
        Intent intent = new Intent(MainHotels.this, HotelDetailsTabsFragementActivity.class);
        intent.putExtra("name", hotel.getName());
        intent.putExtra("id", hotel.getId());
        SharedPreferences pref = getSharedPreferences("USERID", 0);
        String user_email = pref.getString("user", null);
        if (user_email != null)
            addToRecentlyViewedHotels(user_email, hotel.getId());
        startActivity(intent);
    }

    private void addToRecentlyViewedHotels(String email,int mid){
        final String url = "http://172.16.110.69/laravel/public/appAddToRecentlyViewedHotels";
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
                            Toast.makeText(MainHotels.this,ress,Toast.LENGTH_SHORT).cancel();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainHotels.this, "Check Your Network Connection", Toast.LENGTH_SHORT);

            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }







    public void hotelHeartClickHandler(View v){
        SharedPreferences pref = getSharedPreferences("USERID", 0);
        String user_email = pref.getString("user", null);
        if(user_email == null)
            Toast.makeText(MainHotels.this,"LogIn to Add to Wishlist",Toast.LENGTH_SHORT).show();
        else {
            ImageView imageView = (ImageView) v.findViewById(R.id.imageView5);
            int id = hotelCardviewAdapter.getposition((View) v.getParent());
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
    public void fetchdata(){
        SharedPreferences pref = getSharedPreferences("USERID", 0);
        String user_email = pref.getString("user", null);
        String url = "http://172.16.110.69/laravel/public/appHotelGetfavourite"+"?email="+user_email;
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
    public List<String> getList(){
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

    public void add(int sid,String email){
        Hotel restaurant = hotelLists.get(sid);
        int id = restaurant.getId();
        //Toast.makeText(MainRestaurant.this,"add"+sid,Toast.LENGTH_SHORT).show();
        final String url1 = "http://172.16.110.69/laravel/public/appHotelAddfavourite";
        Map<String ,String> params = new HashMap<String,String>();
        String mid = String.valueOf(id);
        if(!fav.contains(mid))
            fav.add(mid);
        params.put("mid", mid);
        params.put("email", email);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url1, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String ress= response.getString("msg");
                            Toast.makeText(MainHotels.this,ress,Toast.LENGTH_SHORT).cancel();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainHotels.this, "Check Your Network Connection", Toast.LENGTH_SHORT);

            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }

    public void delete(int sid,String email){
        Hotel hotel = hotelLists.get(sid);
        int id = hotel.getId();
        //Toast.makeText(MainRestaurant.this,"delete"+id,Toast.LENGTH_SHORT).show();
        final String url = "http://172.16.110.69/laravel/public/appHotelDeletefavourite";
        Map<String ,String> params = new HashMap<String,String>();
        String mid = String.valueOf(id);
        if(fav.contains(mid))
            fav.remove(mid);
        //fav.clear();
        params.put("mid", mid);
        params.put("email", email);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String ress= response.getString("msg");
                            Toast.makeText(MainHotels.this,ress,Toast.LENGTH_SHORT).cancel();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainHotels.this, "Check Your Network Connection", Toast.LENGTH_SHORT);

            }
        });
        AppController.getInstance().addToRequestQueue(req);


    }




}
