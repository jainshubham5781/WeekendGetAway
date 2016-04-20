package com.example.shubham.myapplication3.food.restaurantDetailsSlideTabsFragment.RestaurantMenu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.toolbox.NetworkImageView;
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
public class RestaurantMenuFragment extends Fragment {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private static final String TAG = "RestaurantMenuFragment";
    private ProgressDialog pDialog;
    private List<RestaurantMenu> restaurantLists = new ArrayList<RestaurantMenu>();


    private RestaurantMenuCardviewAdapter restaurantMenuCardviewAdapter;
    private int cc;
    private String id;
    private Button veg,nonVeg,beverages;
    private TextView tv;

    public RestaurantMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.restaurant_menu, container, false);

        Bundle bundle = this.getArguments();
        id = bundle.getString("id");

        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.cardList2);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        restaurantMenuCardviewAdapter = new RestaurantMenuCardviewAdapter(this,restaurantLists);
        recList.setAdapter(restaurantMenuCardviewAdapter);
        restaurantLists.clear();

        veg = (Button) rootView.findViewById(R.id.button7);
        nonVeg = (Button) rootView.findViewById(R.id.button8);
        beverages = (Button) rootView.findViewById(R.id.button9);
        tv = (TextView) rootView.findViewById(R.id.textView10);
        veg.setEnabled(true);
        nonVeg.setEnabled(true);
        beverages.setEnabled(true);

        veg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = "1";
                fetchData(val);
                veg.setEnabled(false);
                nonVeg.setEnabled(true);
                beverages.setEnabled(true);

            }
        });

        nonVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = "2";
                fetchData(val);
                veg.setEnabled(true);
                nonVeg.setEnabled(false);
                beverages.setEnabled(true);
            }
        });

        beverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = "3";
                fetchData(val);
                veg.setEnabled(true);
                nonVeg.setEnabled(true);
                beverages.setEnabled(false);
            }
        });
        return rootView;
    }

    private void fetchData(String value){
        final String url = "http://172.16.110.69/laravel/public/appRestaurantMenu";
        restaurantLists.clear();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        tv.setVisibility(View.VISIBLE);
        Map<String ,String> params = new HashMap<String,String>();
        params.put("value",value);
        params.put("id", id);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        pDialog.dismiss();
                        try {
                           JSONArray obj1 = response.getJSONArray("msg");
                            for (int i = 0; i < obj1.length(); i++) {
                                try {
                                    JSONObject obj= obj1.getJSONObject(i);
                                    RestaurantMenu restaurantMenu = new RestaurantMenu();
                                    restaurantMenu.setTitle(obj.getString("foodname"));
                                    restaurantMenu.setAmount(obj.getInt("price"));
                                    restaurantLists.add(restaurantMenu);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        restaurantMenuCardviewAdapter.notifyDataSetChanged();
                        if(!restaurantLists.isEmpty()) {
                            tv.setVisibility(View.GONE);
                            tv.setText("Not Available");
                        }

                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Check Your Network Connection", Toast.LENGTH_SHORT);
            }
        });
        AppController.getInstance().addToRequestQueue(req);
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
