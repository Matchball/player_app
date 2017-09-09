package com.example.shree.player_nav_drawer;

import java.util.ArrayList;

/**
 * Created by atharva vyas on 12-07-2017.
 * contains data needed to display in recent tournaments table
 */

public class RecentTournamentData {
    public String tname;
    public String edate;
    public long month;
    public long year;
    public Integer points;
    public String position;
    public Long rounds;
    /**
     * for storing different matches played in one of the recent tournment matches
     */
    public ArrayList<TournamentMatch> tournamentMatches;
    public RecentTournamentData()
    {

    }
    public RecentTournamentData(String tname, String edate, Integer points, String position, ArrayList<TournamentMatch> tmatches, Long rounds, long month, long year)
    {
        this.tname=tname;
        this.edate=edate;
        this.points=points;
        this.position=position;
        this.tournamentMatches=tmatches;
        this.rounds=rounds;
        this.month=month;
        this.year=year;
    }
}
