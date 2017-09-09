package com.example.shree.player_nav_drawer;

import java.util.ArrayList;

/**
 * Created by atharva vyas on 12-06-2017.
 * tournament data needed to display on player personal activity
 */

public class PlayerTournamentData {
    public String playerid;
    public int totalPoints;
    public ArrayList<IndividualMatchPoint> toppoints;
    public int topPointscount;
    public double averagePoints;
    public int yearlyMatchesWon;
    public int yearlyMatchesLost;
    public int careerWin;
    public int careerLoss;
    public int rank;
    public String playercategory;
    public String city;
    public PlayerTournamentData(String pid,int tp,double avgp,int ymw,int yml,int cw,int cl,int r,String pc,ArrayList<IndividualMatchPoint>topp,int toppc,String city)
    {
        this.playerid=pid;
        this.totalPoints=tp;
        this.averagePoints=avgp;
        this.yearlyMatchesWon=ymw;
        this.yearlyMatchesLost=yml;
        this.careerWin=cw;
        this.careerLoss=cl;
        this.rank=r;
        this.playercategory=pc;
        this.toppoints=topp;
        this.topPointscount=toppc;
        this.city=city;
    }
    public PlayerTournamentData()
    {

    }
}
