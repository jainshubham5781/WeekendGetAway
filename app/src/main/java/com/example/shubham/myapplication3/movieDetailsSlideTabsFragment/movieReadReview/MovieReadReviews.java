package com.example.shubham.myapplication3.movieDetailsSlideTabsFragment.movieReadReview;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
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
import com.example.shubham.myapplication3.adapter.MovieDetailsReadReviewsListAdapter;
import com.example.shubham.myapplication3.app.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Shubham on 11-10-2015.
 */
public class MovieReadReviews extends android.support.v4.app.Fragment{
    private static final String TAG = "MovieReadReview";
    private ProgressDialog pDialog;         // for loading bar
    private ArrayList<MovieReviewList> movieReviewLists= new ArrayList<MovieReviewList>();
    private ListView listView;
    private TextView avgRating;
    private String title,m_id;
      private TextView noReview;
    private MovieDetailsReadReviewsListAdapter adapter;
    public MovieReadReviews() {
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
        //movie_title
        title = bundle.getString("mname");
        m_id = bundle.getString("m_id");


        listView = (ListView) rootView.findViewById(R.id.list2);
        adapter = new MovieDetailsReadReviewsListAdapter(getActivity(), movieReviewLists);
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
        final String url = "http://172.16.110.69/laravel/public/appreadmoviereviewRating";

        Map<String ,String> params = new HashMap<String,String>();
        params.put("title", title);
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
        movieReviewLists.clear();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String url ="http://172.16.110.69/laravel/public/appreadmoviereviewReviews"+"?title="+m_id;
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
                                MovieReviewList movie = new MovieReviewList();
                                    movie.setId(obj.getString("user_id"));
                                    movie.setName(obj.getString("posted_at"));
                                    movie.setComment(obj.getString("review"));
                                    movie.setStar(obj.getDouble("rating"));
                                    movieReviewLists.add(movie);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(flag == 1)
                            noReview.setVisibility(View.VISIBLE);
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();

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


}

