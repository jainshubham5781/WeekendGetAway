package com.example.shubham.myapplication3.wishList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;
import com.example.shubham.myapplication3.movies.MovieList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham on 27-09-2015.
 */
public class MoviesWishlistCustomListAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<MovieList> movieLists;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public MoviesWishlistCustomListAdapter(Activity activity, List<MovieList> movieLists)
    {
        this.activity=activity;
        this.movieLists=movieLists;
    }
    private class ViewHolder {
        NetworkImageView thumbNail;
        TextView title;
        TextView rating;
        ImageView blank,del;
        int id;

    }

    //size of list
    @Override
    public int getCount() { return movieLists.size();}

    @Override
    public Object getItem(int location) {
        return movieLists.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        if (convertView == null){
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_list, null);
            holder = new ViewHolder();
            holder.thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.rating = (TextView) convertView.findViewById(R.id.rating);
            holder.blank = (ImageView) convertView.findViewById(R.id.imageView4);
            holder.del = (ImageView) convertView.findViewById(R.id.imageView6);
            holder.del.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        // getting movie data for the row
        MovieList m = movieLists.get(position);
        holder.thumbNail.setImageUrl(m.getImageUrl(), imageLoader);
        holder.title.setText(m.getTitle());
        holder.id=m.getMid();
        int mid= holder.id;
        holder.rating.setText("Rating: " + m.getRating() + "/10 .");
        if(checkFavorite(mid)){
                holder.blank.setImageResource(R.drawable.imgthing);
                holder.blank.setTag("red");
            }
        else{
                holder.blank.setImageResource(R.drawable.hheart);
                holder.blank.setTag("grey");
            }
        return convertView;
    }

    public boolean checkFavorite(int mid) {
        boolean check = false;
        int id = mid;
        String  sid = String.valueOf(id);
        ArrayList<String> favourites = new ArrayList<String>();
        favourites.addAll(((MoviesWishlist) this.activity).getList1());
        if(favourites.contains(sid))
            check = true;
        return check;
    }

    public int getposition(View v){
        ViewHolder holder = (ViewHolder) v.getTag();
        return holder.id;
    }

}
