package com.example.shubham.myapplication3.hotels.hotelDetailsSlideTabsFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
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
import java.util.Map;

/**
 * Created by Shubham on 11-10-2015.
 */
public class HotelAddReviews extends android.support.v4.app.Fragment {

    private static final String TAG = "HotelAddReviews";
    public ProgressDialog pdialog;
    EditText comment;
    RatingBar ratingBar;
    Button add_review;
    String user_email,title,id;
    TextView restaurant_name;

    public HotelAddReviews(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movie_add_reviews, container, false);

        Bundle bundle = this.getArguments();
        user_email = bundle.getString("email");
        title = bundle.getString("name");
        id = bundle.getString("id");
        //Toast.makeText(getActivity(),area+"hjhjh"+region,Toast.LENGTH_SHORT).show();


        TextView aa =(TextView) rootView.findViewById(R.id.textView3);
        aa.setText("  Rate the Hotel:");
        restaurant_name = (TextView) rootView.findViewById(R.id.textView);
        restaurant_name.setText(title);
        comment = (EditText) rootView.findViewById(R.id.comment);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingbar);
        ratingBar.setVisibility(View.VISIBLE);
        add_review = (Button) rootView.findViewById(R.id.addreview);
        add_review.setEnabled(true);

        add_review.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (user_email == null)
                    Toast.makeText(getActivity(), "Please LogIn First", Toast.LENGTH_SHORT).show();
                else
                    submitReview();
            }
        });
        return rootView;
    }

    public void submitReview(){
        Log.d(TAG, "submitReview");
        if(!validate()){
            return;
        }
        pdialog = new ProgressDialog(getActivity());
        pdialog.setMessage("Submitting..");
        pdialog.show();
        float irate = this.ratingBar.getRating();
        String irating = String.valueOf(irate);
        String comment = this.comment.getText().toString();

        final String url = "http://172.16.110.69/laravel/public/appAddHotelReview";
        Map<String ,String> params = new HashMap<String,String>();
        params.put("comment",comment);
        params.put("id",id);
        params.put("irating", irating);
        params.put("email",user_email);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        pdialog.dismiss();
                        try {
                            String ress= response.getString("msg");
                            Toast.makeText(getActivity(),ress,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
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
        add_review.setEnabled(false);


    }

    public boolean validate(){
        boolean valid = true;

        float rating = this.ratingBar.getRating();
        if(rating == 0.0) {
            Toast.makeText(getActivity(),"Rate the Hotel",Toast.LENGTH_SHORT).show();
            valid= false;
        }
        return valid;
    }


}
