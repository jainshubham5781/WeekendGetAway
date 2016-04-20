package com.example.shubham.myapplication3.movies;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shubham.myapplication3.R;

/**
 * Created by Shubham on 11-10-2015.
 */

//////////////////////////////////////xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx//////////////////////////////////





public class MovieReview extends ActionBarActivity{
    private static final String TAG = "MovieReview";

    RatingBar ratingbar;
    Button button;
    EditText comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_review);

        addListenerOnButtonClick();

    }

    public void addListenerOnButtonClick(){
        ratingbar=(RatingBar)findViewById(R.id.ratingbar);
        button=(Button)findViewById(R.id.addreview);
        comment= (EditText) findViewById(R.id.comment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Performing action on Button Click
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Getting the rating and displaying it on the toast
                addreview();
            }
        });
    }

    private void addreview(){
        String rating=String.valueOf(ratingbar.getRating());
        String comm = comment.getText().toString();

        Toast.makeText(MovieReview.this, rating + comm, Toast.LENGTH_SHORT).show();
    }

}
