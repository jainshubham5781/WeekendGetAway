package com.example.shubham.myapplication3.food;

public class Restaurant {
      protected String name,title,imageUrl,type,region,area;
      protected int area_id,region_id,id;
      protected Double rating;
      public Restaurant(){
      }

      public Restaurant(String title,String name, String imageUrl, Double rating ,String type,String region,String area,int area_id,int region_id,int id ){
            this.title=title;
            this.imageUrl=imageUrl;
            this.type=type;
            this.name=name;
            this.region=region;
            this.area=area;
            this.area_id=area_id;
            this.region_id=region_id;
            this.id=id;
            this.rating=rating;
      }

      public String getTitle(){return title;}
      public  void setTitle(String title){ this.title=title; }

      public String getName(){return name;}
      public  void setName(String name){ this.name=name; }

      public String getImageUrl(){ return imageUrl; }
      public void setImageUrl(String imageUrl){ this.imageUrl=imageUrl; }

      public String getType(){ return type; }
      public void setType(String type){ this.type = type;}

      public String getRegion(){ return region; }
      public void setRegion(String region){ this.region = region;}

      public String getArea(){ return area; }
      public void setArea(String area){ this.area = area;}

      public int getAreaId(){ return area_id; }
      public void setAreaId(int area_id){ this.area_id = area_id;}

      public int getRegionId(){ return region_id; }
      public void setRegionId(int region_id){ this.region_id = region_id;}

      public int getId(){ return id; }
      public void setId(int id){ this.id = id;}

      public double getRating(){ return rating; }
      public void setRating(double rating){ this.rating = rating;}

}
