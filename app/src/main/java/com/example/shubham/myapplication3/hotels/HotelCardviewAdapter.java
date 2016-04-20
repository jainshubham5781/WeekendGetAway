package com.example.shubham.myapplication3.hotels;

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

import java.util.ArrayList;
import java.util.List;

public class HotelCardviewAdapter extends RecyclerView.Adapter<HotelCardviewAdapter.HotelViewHolder> {

    private List<Hotel> hotelList;
    private Activity activity;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public HotelCardviewAdapter(MainHotels mainHotels, List<Hotel> hotelList) {
            this.hotelList = hotelList;
            this.activity = mainHotels;
    }

//    public HotelCardviewAdapter(RecentlyViewedHotels recentlyViewedHotels, List<Hotel> hotelList) {
//        this.hotelList = hotelList;
//    }


    @Override
    public int getItemCount() {
          return hotelList.size();
    }

    @Override
    public void onBindViewHolder(HotelViewHolder hotelViewHolder, int i) {
        Hotel ci = hotelList.get(i);
        hotelViewHolder.vName.setText(ci.getName());
        hotelViewHolder.vTitle.setText(ci.getRegion() + "-" + ci.getArea());
        hotelViewHolder.img.setImageUrl(ci.getImageUrl(), imageLoader);
        hotelViewHolder.id= ci.getId();
        int mid = hotelViewHolder.id;
        hotelViewHolder.vv.setTag(i);
        if(checkFavorite(mid)){
            hotelViewHolder.blank.setImageResource(R.drawable.imgthing);
            hotelViewHolder.blank.setTag("red");
        }
        else{
            hotelViewHolder.blank.setImageResource(R.drawable.hheart);
            hotelViewHolder.blank.setTag("grey");
        }
    }

   @Override
   public HotelViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.hotel_main_hotels_cardview, viewGroup, false);

        return new HotelViewHolder(itemView);
   }

    public boolean checkFavorite(int mid) {
        boolean check = false;
        int id = mid;
        String  sid = String.valueOf(id);
        ArrayList<String> favourites = new ArrayList<String>();
        favourites.addAll(((MainHotels) this.activity).getList());
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




  public static class HotelViewHolder extends RecyclerView.ViewHolder {
      protected TextView vName;
      protected TextView vTitle;
      protected NetworkImageView img;
      protected ImageView blank;
      public View vv;


      RelativeLayout rl;
      int id;
      public HotelViewHolder(View v) {
          super(v);
          vv = (RelativeLayout) v.findViewById(R.id.bkgd);
          vName =  (TextView) v.findViewById(R.id.name);
          vTitle = (TextView) v.findViewById(R.id.title);
          img = (NetworkImageView) v.findViewById(R.id.thumbnail);
          rl = (RelativeLayout) v.findViewById(R.id.bkgd);
          blank = (ImageView) v.findViewById(R.id.imageView5);

      }
  }
}