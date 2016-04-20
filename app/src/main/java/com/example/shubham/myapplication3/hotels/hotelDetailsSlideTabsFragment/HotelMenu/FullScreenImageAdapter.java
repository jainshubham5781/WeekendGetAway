package com.example.shubham.myapplication3.hotels.hotelDetailsSlideTabsFragment.HotelMenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.shubham.myapplication3.R;
import com.example.shubham.myapplication3.app.AppController;


import java.util.ArrayList;

public class FullScreenImageAdapter extends PagerAdapter {
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private ArrayList<String> _imagePaths;
	private LayoutInflater inflater;
    private ImageView i1,i2,i3,i4,i5;

	// constructor
	public FullScreenImageAdapter(HotelMenuFragment activity,
                                  ArrayList<String> imagePaths) {
		//this._activity = activity;
		this._imagePaths = imagePaths;
	}

	@Override
	public int getCount() {
		return this._imagePaths.size();
	}

	@Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
	
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        NetworkImageView imgDisplay;
        inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);
        i1= (ImageView) viewLayout.findViewById(R.id.imageView9);
        i2= (ImageView) viewLayout.findViewById(R.id.imageView6);
        i3= (ImageView) viewLayout.findViewById(R.id.imageView4);
        i4= (ImageView) viewLayout.findViewById(R.id.imageView8);
        i5= (ImageView) viewLayout.findViewById(R.id.imageView10);

        imgDisplay = (NetworkImageView) viewLayout.findViewById(R.id.imgDisplay);
        String mm = _imagePaths.get(position);
        imgDisplay.setTag(position);
        if(position == 0)
            i1.setImageResource(R.drawable.abc_btn_radio_to_on_mtrl_015);
        if(position == 1)
            i2.setImageResource(R.drawable.abc_btn_radio_to_on_mtrl_015);
        if(position == 2)
            i3.setImageResource(R.drawable.abc_btn_radio_to_on_mtrl_015);
        if(position == 3)
            i4.setImageResource(R.drawable.abc_btn_radio_to_on_mtrl_015);
        if(position == 4)
            i5.setImageResource(R.drawable.abc_btn_radio_to_on_mtrl_015);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        imgDisplay.setImageUrl(mm,imageLoader);
        ((ViewPager) container).addView(viewLayout);
        return viewLayout;
	}
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }

}
