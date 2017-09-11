package com.lenovo.matchpointf;

/**
 * Created by atharva vyas on 10-07-2017.
 * class contains data necessary for displaying on league table activity
 */

public class LeagueTableData {
    public long nRank;
    public long rank;
    public String pname;
    public String city;
    public long points;
    public double averagePoints;
    public LeagueTableData()
    {

    }
    public LeagueTableData(long nrank,long rank,String pname,String city,long points,double avgPoints)
    {
        this.nRank=nrank;
        this.rank=rank;
        this.pname=pname;
        this.city=city;
        this.points=points;
        this.averagePoints=avgPoints;
    }
}
