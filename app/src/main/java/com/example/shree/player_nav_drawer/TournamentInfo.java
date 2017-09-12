package com.example.shree.player_nav_drawer;

/**
 * Created by atharva vyas on 05-07-2017.
 * contains attributes to represent tournament data while displaying the tournaments in which a player can enroll
 */

public class TournamentInfo {
    public String date;
    public String registrationDeadline;
    public String tname;
    public String organizer;
    public String city;
    public String venue;
    public String category;
    public String categoryString;
    public long drawsize;

    public TournamentInfo(String dt,String rd,String tname,String org,String city,String venue,String cat,long drawsize,String catstring)
    {
        this.date=dt;
        this.registrationDeadline=rd;
        this.tname=tname;
        this.organizer=org;
        this.city=city;
        this.venue=venue;
        this.category=cat;
        this.drawsize=drawsize;
        this.categoryString=catstring;
    }
}
