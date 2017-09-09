package com.example.shree.player_nav_drawer;

/**
 * Created by lenovo on 13-07-2017.
 */

public class TournamentMatch {
    public String player1;
    public String player2;
    public String matchScores;
    public String winner;
    public String matchType;
    public Integer matchid;
    public TournamentMatch()
    {

    }
    public TournamentMatch(String p1,String p2,String ms,String winner,String mt,int mid)
    {
        this.player1=p1;
        this.player2=p2;
        this.matchScores=ms;
        this.winner=winner;
        this.matchType=mt;
        this.matchid=mid;
    }
}
