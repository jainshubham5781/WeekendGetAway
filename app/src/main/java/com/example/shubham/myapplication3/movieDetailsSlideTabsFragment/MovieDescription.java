package com.example.shubham.myapplication3.movieDetailsSlideTabsFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;


/**
 * Created by Shubham on 12-10-2015.
 */
public class MovieDescription extends Fragment {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    TextView videoUrl;

    public MovieDescription() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movie_description, container, false);

        Bundle bundle = this.getArguments();

        //movie_title
        String title = bundle.getString("mname");
        TextView textView = (TextView) rootView.findViewById(R.id.movie_title);
        textView.setText(title);

        //movie_image
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        String image = bundle.getString("image");
        NetworkImageView imageView = (NetworkImageView) rootView.findViewById(R.id.movie_image);
        imageView.setImageUrl(image, imageLoader);

        //movie_rating
        String rating = bundle.getString("rating");
        textView = (TextView) rootView.findViewById(R.id.movie_rating);
        textView.setText(rating);

        //movie_type
        String type = bundle.getString("mcertificate");
        textView = (TextView) rootView.findViewById(R.id.movie_type);
        textView.setText(type);


        //movie_genre
        String genre = bundle.getString("mgenre");
        textView = (TextView) rootView.findViewById(R.id.movie_genre);
        textView.setText(genre);

        //movie_director
        String director = bundle.getString("mdirector");
        textView = (TextView) rootView.findViewById(R.id.movie_director);
        textView.setText(director);

        //movie_cast
        String cast = bundle.getString("mcast");
        textView = (TextView) rootView.findViewById(R.id.movie_cast);
        textView.setText(cast);

        //movie_description
        String description = bundle.getString("mdescription");
        textView = (TextView) rootView.findViewById(R.id.movie_description);
        textView.setText("Description : "+description);

        final String trailer = bundle.getString("trailer");
        videoUrl = (TextView) rootView.findViewById(R.id.textView20);

        videoUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer));
                startActivity(intent);
            }
        });


        return rootView;
    }
}
