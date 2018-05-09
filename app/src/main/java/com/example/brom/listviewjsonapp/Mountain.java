package com.example.brom.listviewjsonapp;

/**
 * Created by aliceanglesjo on 2018-04-20.
 */

public class Mountain {
    private String name;
    private String location;
    private String imgUrl;
    private int height;


    // Constructor
    public Mountain (String inName, String inLocation, int inHeight, String inImg){
        name = inName;
        location = inLocation;
        height = inHeight;
        imgUrl = inImg;

    }


    public String locationInfo(){ return location;}
    public String heightInfo(){ return Integer.toString(height);}

    public String imgInfo(){
        return imgUrl;
    }


    public String info(){
        String str = name;
        str+= " is located in ";
        str+= location + '\n';
        str+= "It has a height of ";
        str+= Integer.toString(height);
        str+="m. "  + '\n';

        return str;
    }

    @Override
    public String toString() {
        return name;
    }

}
