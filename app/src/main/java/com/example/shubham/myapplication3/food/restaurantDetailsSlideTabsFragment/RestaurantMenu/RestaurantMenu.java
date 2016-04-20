package com.example.shubham.myapplication3.food.restaurantDetailsSlideTabsFragment.RestaurantMenu;

public class RestaurantMenu {
      protected String title;
      protected int amount;
           public RestaurantMenu(){
      }

      public RestaurantMenu(String title, int amount){
            this.title=title;
            this.amount=amount;
      }

      public String getTitle(){return title;}
      public  void setTitle(String title){ this.title=title; }

      public int getAmount(){ return amount; }
      public void setAmount(int amount){ this.amount = amount;}

}
