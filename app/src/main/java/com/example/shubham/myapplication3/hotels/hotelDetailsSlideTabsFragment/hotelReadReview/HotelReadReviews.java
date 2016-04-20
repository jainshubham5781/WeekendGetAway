package com.example.shubham.myapplication3.hotels.hotelDetailsSlideTabsFragment.hotelReadReview;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.example.shubham.myapplication3.hotels.hotelDetailsSlideTabsFragment.HotelAdapter.HotelDetailsReadReviewsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Shubham on 11-10-2015.
 */
public class HotelReadReviews extends android.support.v4.app.Fragment{
    private static final String TAG = "HotelReadReview";
    private ProgressDialog pDialog;         // for loading bar
    private ArrayList<HotelReviewList> hotelReviewLists = new ArrayList<HotelReviewList>();
    private ListView listView;
    private TextView avgRating;
    private String id;
    private TextView noReview;
    private HotelDetailsReadReviewsListAdapter adapter;
    public HotelReadReviews() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movie_read_reviews, container, false);

        Bundle bundle = this.getArguments();

        id = bundle.getString("id");


        listView = (ListView) rootView.findViewById(R.id.list2);
        adapter = new HotelDetailsReadReviewsListAdapter(getActivity(), hotelReviewLists);
        listView.setAdapter(adapter);
        noReview = (TextView) rootView.findViewById(R.id.noReview);
        noReview.setText("No Reviews Yet !!");
        noReview.setEnabled(false);
        noReview.setVisibility(View.GONE);
        avgRating = (TextView) rootView.findViewById(R.id.textView11);

        showrating();
        showReview();

        return rootView;
    }

    private void showrating(){
        final String url = "http://172.16.110.69/laravel/public/appReadHotelReviewRating";
        Map<String ,String> params = new HashMap<String,String>();
        params.put("id",id);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String avgRate= response.getString("avgRating");
                            String totalUser= response.getString("total");
                            if(totalUser != "0") {
                                avgRating.setText("Average Rating: " + avgRate + "/5 based on " + totalUser + " users.");
                            }
                            else
                                avgRating.setText("Average Rating: " + avgRate + "/5 .");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Check Your Network Connection", Toast.LENGTH_SHORT).show();

            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }


    public void showReview(){
        hotelReviewLists.clear();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String url ="http://172.16.110.69/laravel/public/appReadHotelReviewReviews"+"?id="+id;
        final JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        int flag = 1;
                        for (int i = 0; i < response.length(); i++) {
                                flag=0;
                            try {
                                JSONObject obj= response.getJSONObject(i);
                                HotelReviewList hotelReviewList = new HotelReviewList();
                                    hotelReviewList.setId(obj.getString("user_id"));
                                    hotelReviewList.setName(obj.getString("posted_at"));
                                    hotelReviewList.setComment(obj.getString("review"));
                                    hotelReviewList.setStar(obj.getDouble("rating"));
                                    hotelReviewLists.add(hotelReviewList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(flag == 1)
                            noReview.setVisibility(View.VISIBLE);
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


}

