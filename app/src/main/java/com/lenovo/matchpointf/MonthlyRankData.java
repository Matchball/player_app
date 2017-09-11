package com.lenovo.matchpointf;

/**
 * Created by atharva on 14-07-2017.
 */

public class MonthlyRankData {
    public Long rank;
    public String playercategory;
    public String city;

    public MonthlyRankData()
    {

    }

    public MonthlyRankData(long r,String p,String c){
        this.rank=r;
        this.playercategory=p;
        this.city=c;
    }
}
