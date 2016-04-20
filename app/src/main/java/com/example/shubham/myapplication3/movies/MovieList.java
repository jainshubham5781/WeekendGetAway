package com.example.shubham.myapplication3.movies;

/**
 * Created by Shubham on 27-09-2015.
 */
public class MovieList {
        private  String title, imageUrl, description, genre,type, cast, director,videoUrl;
        private double rating;
        private int m_id;
        private String location;
    public MovieList(){
    }

    public MovieList(String title, String imageUrl, String location, double rating, int m_id, String description, String genre, String type, String cast, String director,String videoUrl){
        this.title=title;
        this.imageUrl=imageUrl;
        this.location=location;
        this.rating=rating;
        this.description=description;
        this.genre=genre;
        this.cast=cast;
        this.type=type;
        this.director=director;
        this.m_id=m_id;
        this.videoUrl=videoUrl;
    }

    public String getTitle(){return title;}
    public  void setTitle(String title){ this.title=title; }

    public String getImageUrl(){ return imageUrl; }
    public void setImageUrl(String imageUrl){ this.imageUrl=imageUrl; }


    public String getVideoUrl(){ return videoUrl; }
    public void setVideoUrl(String videoUrl){ this.videoUrl=videoUrl; }

    public void setLocation(String location){ this.location=location; }
    public String getLocation(){ return location; }

    public double getRating(){ return rating; }
    public void setRating(double rating){this.rating = rating;}

    public void setDescription(String description){ this.description=description; }
    public String getDescription(){ return description; }

    public String getCast(){return cast;}
    public  void setCast(String cast){ this.cast=cast; }

    public String getDirector(){return director;}
    public  void setDirector(String director){ this.director=director; }

    public String getType(){return type;}
    public  void setType(String type){ this.type=type; }

    public String getGenre(){return genre;}
    public  void setGenre(String genre){ this.genre=genre; }

    public int getMid(){ return m_id; }
    public void setMid(int m_id){this.m_id = m_id;}

}
