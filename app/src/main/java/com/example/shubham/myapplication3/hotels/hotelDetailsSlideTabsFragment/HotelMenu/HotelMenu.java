package com.example.shubham.myapplication3.hotels.hotelDetailsSlideTabsFragment.HotelMenu;

public class HotelMenu {
      protected String title;
      protected int amount;
           public HotelMenu(){
      }

      public HotelMenu(String title, int amount){
            this.title=title;
            this.amount=amount;
      }

      public String getTitle(){return title;}
      public  void setTitle(String title){ this.title=title; }

      public int getAmount(){ return amount; }
      public void setAmount(int amount){ this.amount = amount;}

}
