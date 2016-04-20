package com.example.shubham.myapplication3.hotels.hotelDetailsSlideTabsFragment.HotelMenu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Shubham on 12-10-2015.
 */
public class HotelMenuFragment extends Fragment {
    private static final String TAG = "HotelMenuFragment";
    private ProgressDialog pDialog;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    ArrayList<String> arrayList = new ArrayList<String>();
    private String id;
    private TextView title,address,rating,deluxP,superdeluxP,desc;
    private String title1,address1,desc1;
    private int deluxP1,superdeluxP1;
    private Double rating1;
    public HotelMenuFragment() {
        // Required empty public constructor
    }

    public void setDetails(String address1,Double rating ,int deluxP1 , int superdeluxP1,String desc1){
        this.address1=address1;
        this.rating1=rating;
        this.deluxP1=deluxP1;
        this.superdeluxP1=superdeluxP1;
        this.desc1=desc1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_fullscreen_view, container, false);
        Bundle bundle = this.getArguments();
        id = bundle.getString("id");
        String hname= bundle.getString("name");
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        adapter = new FullScreenImageAdapter(this,arrayList);
        viewPager.setAdapter(adapter);
        fetchdata();
        fetch();
        viewPager.setCurrentItem(0);

        title = (TextView) rootView.findViewById(R.id.textView5);
        address = (TextView) rootView.findViewById(R.id.textView22);
        rating = (TextView) rootView.findViewById(R.id.textView23);
        deluxP = (TextView) rootView.findViewById(R.id.textView24);
        superdeluxP = (TextView) rootView.findViewById(R.id.textView25);
        desc = (TextView) rootView.findViewById(R.id.textView26);

        title.setText(hname);
//        address.setText("ADDRESS : "+address1);
//        rating.setText("RATING : "+rating1);
//        deluxP.setText("DELUX Room Price : "+deluxP1);
//        superdeluxP.setText("SUPERDELUX Room Price : "+deluxP1);
//        desc.setText("DESCRIPTION : "+desc1);

        return rootView;
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

    private void fetch(){
        String url = "http://172.16.110.69/laravel/public/appHotelDetails"+"?id="+id;
        final JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Grid", response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj= response.getJSONObject(i);

                                address.setText("ADDRESS : "+obj.getString("address"));
                                rating.setText("RATING : "+obj.getDouble("our_rating"));
                                deluxP.setText("DELUX Room Price : "+obj.getInt("deluxeprice"));
                                superdeluxP.setText("SUPERDELUX Room Price : "+obj.getInt("superdeluxeprice"));
                                desc.setText("DESCRIPTION : "+obj.getString("description"));
                               // setDetails(obj.getString("address"),obj.getDouble("out_rating"),obj.getInt("deluxeprice"),obj.getInt("superdeluxeprice"),obj.getString("description"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                 //       adapter.notifyDataSetChanged();
                    }

                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d("Grid", "Error: " + error.getMessage());
                pDialog.dismiss();
            }

        });
        AppController.getInstance().addToRequestQueue(movieReq);
    }


    private void fetchdata(){
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
        String url = "http://172.16.110.69/laravel/public/appmovies"+"?id=";
        final JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Grid", response.toString());
                        pDialog.dismiss();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj= response.getJSONObject(i);
                                arrayList.add(obj.getString("image"));      // adding to list used for sorting
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d("Grid", "Error: " + error.getMessage());
                pDialog.dismiss();
            }

        });
        AppController.getInstance().addToRequestQueue(movieReq);
    }
}

