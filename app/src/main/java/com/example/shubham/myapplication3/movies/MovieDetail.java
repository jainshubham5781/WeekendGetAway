package com.example.shubham.myapplication3.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;

/**
 * Created by Shubham on 28-09-2015.
 */
public class MovieDetail extends ActionBarActivity{
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_description);
        //Toast.makeText(this, "MovieDetails", Toast.LENGTH_LONG).show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

//        //movie_title
//        String title = intent.getStringExtra("title");
//        TextView textView = (TextView) findViewById(R.id.movie_title);
//        textView.setText(title);

        //movie_title
        String title = intent.getStringExtra("mname");
        TextView textView = (TextView) findViewById(R.id.movie_title);
        textView.setText(title);

        //movie_image
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        String image = intent.getStringExtra("image");
        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.movie_image);
        imageView.setImageUrl(image, imageLoader);

        //movie_rating
        Double rating = intent.getDoubleExtra("rating", 0);
        String movierating=rating.toString();
        textView = (TextView) findViewById(R.id.movie_rating);
        textView.setText(movierating);

        //movie_type
        String type = intent.getStringExtra("mcertificate");
        textView = (TextView) findViewById(R.id.movie_type);
        textView.setText(type);


        //movie_genre
        String genre = intent.getStringExtra("mgenre");
        textView = (TextView) findViewById(R.id.movie_genre);
        textView.setText(genre);

        //movie_director
        String director = intent.getStringExtra("mdirector");
        textView = (TextView) findViewById(R.id.movie_director);
        textView.setText(director);

        //movie_cast
        String cast = intent.getStringExtra("mcast");
        textView = (TextView) findViewById(R.id.movie_cast);
        textView.setText(cast);

        //movie_description
        String description = intent.getStringExtra("mdescription");
        textView = (TextView) findViewById(R.id.movie_description);
        textView.setText("Description : "+description);

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

