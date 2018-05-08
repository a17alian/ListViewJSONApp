package com.example.brom.listviewjsonapp;

/**
 * Created by aliceanglesjo on 2018-04-20.
 */

public class Mountain {
    private String name;
    private String location;
    private int height;


    // Constructor
    public Mountain (String inName, String inLocation, int inHeight){
        name = inName;
        location = inLocation;
        height = inHeight;

    }

    public Mountain (String inName){
        name = inName;
        location = "";
        height = -1;
    }




    public String locationInfo(){ return location;}
    public String heightInfo(){ return Integer.toString(height);}


    public String info(){
        String str = name;
        str+= " is located in ";
        str+= location;
        str+= " and has a height of ";
        str+= Integer.toString(height);
        str+="m. ";

        return str;
    }

    @Override
    public String toString() {
        return name;
    }

}
