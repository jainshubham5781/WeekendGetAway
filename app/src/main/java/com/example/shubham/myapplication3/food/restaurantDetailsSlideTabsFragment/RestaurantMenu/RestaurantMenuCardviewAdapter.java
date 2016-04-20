package com.example.shubham.myapplication3.food.restaurantDetailsSlideTabsFragment.RestaurantMenu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;


import java.util.List;

public class RestaurantMenuCardviewAdapter extends RecyclerView.Adapter<RestaurantMenuCardviewAdapter.RestaurantViewHolder> {

    private List<RestaurantMenu> restaurantMenuList;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public RestaurantMenuCardviewAdapter(RestaurantMenuFragment restaurantMenuFragment, List<RestaurantMenu> restaurantMenuList) {
            this.restaurantMenuList = restaurantMenuList;
    }

    @Override
    public int getItemCount() {
          return restaurantMenuList.size();
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder restaurantViewHolder, int i) {
        RestaurantMenu ci = restaurantMenuList.get(i);
        restaurantViewHolder.vName.setText(ci.getTitle());
        restaurantViewHolder.vAmount.setText(String.valueOf(ci.getAmount()));
//        restaurantViewHolder.vTitle.setText(ci.getRegion() + "-" + ci.getArea());
//        restaurantViewHolder.img.setImageUrl(ci.getImageUrl(), imageLoader);

    }

   @Override
   public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.restaurant_menu_cardview, viewGroup, false);

        return new RestaurantViewHolder(itemView);
   }

  public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
      protected TextView vName;
      protected TextView vAmount;
//      protected TextView vTitle;
//      protected NetworkImageView img;
//      RelativeLayout rl;
      public RestaurantViewHolder(View v) {
          super(v);
          vName =  (TextView) v.findViewById(R.id.textView2);
          vAmount = (TextView)  v.findViewById(R.id.textView7);
//          vTitle = (TextView) v.findViewById(R.id.title);
//          img = (NetworkImageView) v.findViewById(R.id.thumbnail);
//          rl = (RelativeLayout) v.findViewById(R.id.bkgd);
      }
  }
}