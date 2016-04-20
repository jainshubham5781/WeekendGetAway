package com.example.shubham.myapplication3.location;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
/////////////////////////////// no use //////////////////////////////////////
/**
 * Created by Shubham on 04-10-2015.
 */
public class LocationActivity extends Activity{
    public static final String TAG = LocationActivity.class.getSimpleName();

    Button btnShowLocation;

    // GPSTracker class
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                btnShowLocation = (Button) findViewById(R.id.button);

                // show location button click event
                btnShowLocation.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // create class object
                        gps = new GPSTracker(LocationActivity.this);

                        // check if GPS enabled
                        if(gps.canGetLocation()){

                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();

//                            //
//                            //save it in database
//                            SharedPreferences pref = getSharedPreferences("USERID", 0);
//                            String user_email= pref.getString("user", null);
//                            Toast.makeText(LocationActivity.this, user_email, Toast.LENGTH_SHORT).show();
//
//                            String url = "http://172.16.105.47/laravel/public/applocation";
//                            //url += "?email" +user_email;
//                            HashMap<String, Double> params = new HashMap<>();
//                            params.put("latitude", latitude);
//                            params.put("longitude", longitude);
//                            //request and response
//                            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
//                                    new Response.Listener<JSONObject>() {
//                                        @Override
//                                        public void onResponse(JSONObject response) {
//                                            Log.d(TAG, response.toString());
//                                            try {
//                                                Toast.makeText(LocationActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
//
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }, new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    VolleyLog.d(TAG, "Error: " + error.getMessage());
//                                    Toast.makeText(LocationActivity.this, "Check Your Network Connection", Toast.LENGTH_SHORT);
//                                }
//                            });
//                            AppController.getInstance().addToRequestQueue(req);
                            //end of storing in database

                            // \n is for new line
                            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                        }else{
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            gps.showSettingsAlert();
                        }

                    }
                });
            }

        }

