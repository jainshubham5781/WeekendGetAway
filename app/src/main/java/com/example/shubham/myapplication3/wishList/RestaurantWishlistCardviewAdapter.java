package com.example.shubham.myapplication3.wishList;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;
import com.example.shubham.myapplication3.food.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantWishlistCardviewAdapter extends RecyclerView.Adapter<RestaurantWishlistCardviewAdapter.RestaurantViewHolder> {

    private List<Restaurant> restaurantList;
    private Activity activity;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public RestaurantWishlistCardviewAdapter(RestaurantWishlist mainRestaurant, List<Restaurant> restaurantList) {
            this.restaurantList = restaurantList;
            this.activity = mainRestaurant;
    }



    @Override
    public int getItemCount() {
          return restaurantList.size();
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder restaurantViewHolder, int i) {
        Restaurant ci = restaurantList.get(i);
        restaurantViewHolder.vName.setText(ci.getName());
        restaurantViewHolder.vType.setText("- " + ci.getType());
        restaurantViewHolder.vTitle.setText(ci.getRegion() + "-" + ci.getArea());
        restaurantViewHolder.img.setImageUrl(ci.getImageUrl(), imageLoader);
        restaurantViewHolder.del.setVisibility(View.VISIBLE);
        restaurantViewHolder.id= ci.getId();
        int mid = restaurantViewHolder.id;
        restaurantViewHolder.vv.setTag(i);
        if(checkFavorite(mid)){
            restaurantViewHolder.blank.setImageResource(R.drawable.imgthing);
            restaurantViewHolder.blank.setTag("red");
        }
        else{
            restaurantViewHolder.blank.setImageResource(R.drawable.hheart);
            restaurantViewHolder.blank.setTag("grey");
        }
    }

   @Override
   public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.food_main_restaurant_cardview, viewGroup, false);

        return new RestaurantViewHolder(itemView);
   }

    public boolean checkFavorite(int mid) {
        boolean check = false;
        int id = mid;
        String  sid = String.valueOf(id);
        ArrayList<String> favourites = new ArrayList<String>();
        favourites.addAll(((RestaurantWishlist) this.activity).getList());
        if(favourites.contains(sid))
            check = true;
        return check;
    }

    public int getposition(View v){
//        RestaurantViewHolder holder = (RestaurantViewHolder) v.getTag();
//        return holder.id;
        int jj = (int) v.getTag();
        return jj;
    }




  public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
      protected TextView vName;
      protected TextView vType;
      protected TextView vTitle;
      protected NetworkImageView img;
      protected ImageView blank,del;
      public View vv;


      RelativeLayout rl;
      int id;
      public RestaurantViewHolder(View v) {
          super(v);
          vv = (RelativeLayout) v.findViewById(R.id.bkgd);
          vName =  (TextView) v.findViewById(R.id.name);
          vType = (TextView)  v.findViewById(R.id.type);
          vTitle = (TextView) v.findViewById(R.id.title);
          img = (NetworkImageView) v.findViewById(R.id.thumbnail);
          rl = (RelativeLayout) v.findViewById(R.id.bkgd);
          blank = (ImageView) v.findViewById(R.id.imageView5);
          del = (ImageView) v.findViewById(R.id.imageView7);

      }
  }
}