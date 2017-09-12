package com.example.shree.player_nav_drawer;

import java.util.Date;

/**
 * Created by atharva vyas on 12-06-2017.
 * contains information necessay to display on player personal activity
 */

public class PlayerPersonalData {
    public String playerid;
    public String password;
    public String name;
    public Date dob;
    public String address;
    public String addresscity;
    public String city;
    public String center;
    public String gender;
    public String joiningDate;
    public int age;
    public String playercategory;
    public PlayerPersonalData(String pid,String passwd,String n,Date d,String a,String ac,String cy,String cr,String g,String j,int age,String pc)
    {
        this.playerid=pid;
        this.password=passwd;
        this.name=n;
        this.dob=d;
        this.address=a;
        this.addresscity=ac;
        this.city=cy;
        this.center=cr;
        this.gender=g;
        this.joiningDate=j;
        this.age=age;
        this.playercategory=pc;
    }
    public PlayerPersonalData()
    {

    }
}
