package com.example.shubham.myapplication3.movieDetailsSlideTabsFragment.movieReadReview;

/**
 * Created by Shubham on 09-11-2015.
 */
public class MovieReviewList {


    private  String id,name,comment;
    private double star;
    public MovieReviewList(){
    }

    public MovieReviewList(String id,String name,String comment, double star){
        this.id=id;
        this.name=name;
        this.comment=comment;
        this.star=star;
    }

    public String getId(){return id;}
    public  void setId(String id){ this.id=id; }

    public void setName(String name){ this.name=name; }
    public String getName(){ return name; }

    public double getStar(){ return star; }
    public void setStar(double star){
        this.star = star;
    }

    public void setComment(String comment){ this.comment=comment; }
    public String getComment(){ return comment; }
}