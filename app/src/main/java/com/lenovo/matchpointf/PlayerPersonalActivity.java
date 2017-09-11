package com.lenovo.matchpointf;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import static android.R.attr.category;
import static android.R.attr.y;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.media.CamcorderProfile.get;

/**
 * @author atharva vyas
 * activity displaying personal data of user
 */
public class PlayerPersonalActivity extends AppCompatActivity {
    private DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference playerpdata = rootref.child("playerpdata");
    private DatabaseReference playertdata = rootref.child("playertdata");
    private DatabaseReference tournamentmsdata = rootref.child("tournamentmsdata");
    /**
     * needed to extract information of rank the players had when the recent tournament data was played
     */
    private DatabaseReference monthlydata=rootref.child("monthlyRankList");
    private String playerid;
    /**
     * contains the list of recently played tournaments
     */
    private ArrayList<RecentTournamentData> tlist;
    /**
     * points alloted as per ranks achieved in tournaments
     */
    private int[] pointarray = {270, 180, 105, 60, 32, 16, 8};
    /**
     * pop up window to display perfomance of player in recently played tournament
     */
    private PopupWindow mPopupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_personal);
        final TextView namet=(TextView)findViewById(R.id.textView36);
        final TextView categoryt=(TextView)findViewById(R.id.textView37);
        final TextView cityt=(TextView)findViewById(R.id.textView38);
        final TextView nrankt=(TextView)findViewById(R.id.textView39);
        final TextView winlosst=(TextView)findViewById(R.id.textView40);
        final TextView tpointst=(TextView)findViewById(R.id.textView41);
        final TextView cbrankt=(TextView)findViewById(R.id.textView45);
        final TextView cwinlosst=(TextView)findViewById(R.id.textView46);
        final TextView avgpointst=(TextView)findViewById(R.id.textView47);
        tlist=new ArrayList<RecentTournamentData>();
        playerid=getIntent().getStringExtra("PLAYER_ID");
        Query query = playertdata.orderByChild("playerid").equalTo(playerid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {

                    String category=(String) messageSnapshot.child("playercategory").getValue();
                    Long careerWin=(Long) messageSnapshot.child("careerWin").getValue();
                    Long careerLoss=(Long) messageSnapshot.child("careerLoss").getValue();
                    String city=(String) messageSnapshot.child("city").getValue();
                    Long yearWon=(Long) messageSnapshot.child("yearlyMatchesWon").getValue();
                    Long yearLoss=(Long) messageSnapshot.child("yearlyMatchesLost").getValue();
                    Long totalPoints=(Long) messageSnapshot.child("totalPoints").getValue();
                    Long nrank=(Long) messageSnapshot.child("rank").getValue();
                    double avgPoints=-(Double)messageSnapshot.child("averagePoints").getValue();
                    String categoryu;
                    if(category.charAt(0)=='B')
                        categoryu="Boys Under ";
                    else
                        categoryu="Girls Under ";
                    categoryu+=category.substring(2);
                    namet.setText(playerid);
                    categoryt.setText(categoryu);
                    cityt.setText(city);
                    cbrankt.setText("-");
                    nrankt.setText(String.valueOf(nrank));
                    winlosst.setText(yearWon+"/"+yearLoss);
                    cwinlosst.setText(careerWin+"/"+careerLoss);
                    tpointst.setText(String.valueOf(totalPoints));
                    avgpointst.setText(String.valueOf(avgPoints));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        findLastFiveTournaments();
    }

    /**
     * finds last five tournaments played by the user
     */
    public void findLastFiveTournaments()
    {
        Query query = tournamentmsdata.orderByChild("time");//checks for all the recent tournaments and if the user particpated in or not
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    GenericTypeIndicator<ArrayList<String>> gen = new GenericTypeIndicator<ArrayList<String>>() {
                    };
                    ArrayList<String> enrolled = (ArrayList<String>) messageSnapshot.child("enrolled").getValue(gen);
                    if(enrolled.contains(playerid)) {
                        String tname = (String) messageSnapshot.child("tournamentName").getValue();
                        Long date = (Long) messageSnapshot.child("entryDate").child("date").getValue();
                        Long month = (Long) messageSnapshot.child("entryDate").child("month").getValue();
                        Long year = (Long) messageSnapshot.child("entryDate").child("year").getValue();
                        long rounds=(Long)messageSnapshot.child("rounds").getValue();
                        GenericTypeIndicator<ArrayList<TournamentMatch>> gen3 = new GenericTypeIndicator<ArrayList<TournamentMatch>>() {
                        };
                        ArrayList<TournamentMatch> tmatches = (ArrayList<TournamentMatch>) messageSnapshot.child("matchlist").getValue(gen3);
                        GenericTypeIndicator<Map<String,Integer>> gen2 = new GenericTypeIndicator<Map<String,Integer>>() {
                        };
                        Map<String,Integer> pointstable = (Map<String,Integer>) messageSnapshot.child("pointallotment").getValue(gen2);
                        RecentTournamentData temp;
                        for(int i=0;i<pointstable.size();++i) {
                            if(pointstable.get(playerid)==pointarray[i]) {
                                String pos="";
                                if(i==0)
                                {
                                    pos="1";
                                }
                                else if(i==1)
                                {
                                    pos="2";
                                }
                                else
                                {
                                    pos=((int)Math.pow(2,i-1)+1)+"/"+(int)Math.pow(2,i);
                                }
                                temp = new RecentTournamentData(tname, date + "/" + month + "/" + (year + 1900), pointstable.get(playerid), pos,tmatches,rounds,month,year);
                                if(tlist.size()<5)
                                    tlist.add(temp);
                            }
                        }

                    }

                }
                updateTable();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * updates the table with the racent tournaments data obtained
     */
    public void updateTable()
    {
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.recentTournamentTable);
        for (int i = 0; i <tlist.size(); i++) {
            RecentTournamentData temp=tlist.get(i);
            long year=temp.year;
            final long month=temp.month;
            final TableRow row= new TableRow(this);
            final float scale = getResources().getDisplayMetrics().density;
            int dps=40;
            final long rounds=temp.rounds;
            int pixels = (int) (dps * scale + 0.5f);
            TableLayout.LayoutParams tableRowParams=new TableLayout.LayoutParams
                    (TableLayout.LayoutParams.MATCH_PARENT,pixels);
            row.setLayoutParams(tableRowParams);
            TextView datet = new TextView(this);datet.setText(String.valueOf(temp.edate));datet.setLayoutParams(new TableRow.LayoutParams(0));datet.setGravity(Gravity.CENTER);
            TextView tnamet = new TextView(this);tnamet.setText(String.valueOf(temp.tname));tnamet.setLayoutParams(new TableRow.LayoutParams(1));tnamet.setGravity(Gravity.CENTER);
            TextView pointst=new TextView(this);pointst.setText(String.valueOf(temp.position));pointst.setLayoutParams(new TableRow.LayoutParams(2));pointst.setGravity(Gravity.CENTER);
            TextView positiont=new TextView(this);positiont.setText(String.valueOf(temp.points));positiont.setLayoutParams(new TableRow.LayoutParams(3));positiont.setGravity(Gravity.CENTER);
            final ArrayList<TournamentMatch> tmatches=temp.tournamentMatches;
            row.addView(datet);
            row.addView(tnamet);
            row.addView(pointst);
            row.addView(positiont);
            Query query = monthlydata.orderByChild("year").equalTo(year);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        if((Long) messageSnapshot.child("month").getValue()==month) {
                            GenericTypeIndicator<Map<String,MonthlyRankData>> gen4 = new GenericTypeIndicator<Map<String, MonthlyRankData>>() {
                            };
                            final Map<String,MonthlyRankData> rankmap = (Map<String, MonthlyRankData>) messageSnapshot.child("rankmap").getValue(gen4);
                            row.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v1) {
                                    rowFunction(v1,tmatches,rounds,rankmap);
                                }
                            });

                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            tableLayout.addView(row,i+1);
        }
    }

    /**
     * function to display performance of player in the specific tournament ,triggered by clicking on row
     * @param v
     * @param matchlist
     * @param rounds
     * @param rankmp hash map containing ranks of the month in which tournament was played
     */
    public void rowFunction(View v,ArrayList<TournamentMatch> matchlist,long rounds,Map<String,MonthlyRankData> rankmp)
    {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.custom_layout, null);

        mPopupWindow = new PopupWindow(
                customView,
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        );
        mPopupWindow.setFocusable(true);
        mPopupWindow.update();
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }
        final TableLayout tlayout = (TableLayout) customView.findViewById(R.id.customTable);
        int count=1;
        for (int i = 0; i <matchlist.size(); i++) {
            TournamentMatch tmatch =matchlist.get(i);
            if(tmatch.player1.equals(playerid) || tmatch.player2.equals(playerid)) {
                TableRow row = new TableRow(this);
                final float scale = getResources().getDisplayMetrics().density;
                int dps = 40;
                int pixels = (int) (dps * scale + 0.5f);
                TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams//table layout inside pop up window
                        (TableLayout.LayoutParams.MATCH_PARENT, pixels);
                row.setLayoutParams(tableRowParams);
                TextView roundt = new TextView(this);
                if((tmatch.matchid)/100==rounds)
                roundt.setText("finals");
                else if((tmatch.matchid)/100==rounds-1)
                {
                    roundt.setText("semi finals");
                }
                else if((tmatch.matchid)/100==rounds-2)
                {
                    roundt.setText("quarter finals");
                }
                else
                {
                    roundt.setText("qualifiers");
                }
                roundt.setLayoutParams(new TableRow.LayoutParams(0));
                roundt.setGravity(Gravity.CENTER);
                TextView player1t = new TextView(this);
                MonthlyRankData mtemp1=(MonthlyRankData)rankmp.get(tmatch.player1);
                player1t.setText(String.valueOf(tmatch.player1)+"("+mtemp1.rank+")");
                player1t.setLayoutParams(new TableRow.LayoutParams(1));
                player1t.setGravity(Gravity.CENTER);
                TextView player2t = new TextView(this);
                MonthlyRankData mtemp2=(MonthlyRankData)rankmp.get(tmatch.player2);
                player2t.setText(String.valueOf(tmatch.player2)+"("+mtemp2.rank+")");
                player2t.setLayoutParams(new TableRow.LayoutParams(2));
                player2t.setGravity(Gravity.CENTER);
                TextView scoret = new TextView(this);
                scoret.setText(String.valueOf(tmatch.matchScores));
                scoret.setLayoutParams(new TableRow.LayoutParams(3));
                scoret.setGravity(Gravity.CENTER);
                row.addView(roundt);
                row.addView(player1t);
                row.addView(player2t);
                row.addView(scoret);
                tlayout.addView(row, count);
                ++count;
            }
        }
        // Get a reference for the custom view cancel button
        Button closeButton = (Button) customView.findViewById(R.id.cancelbutton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAtLocation((android.support.constraint.ConstraintLayout) findViewById(R.id.playerpersonallayout), Gravity.CENTER, 0, 0);
    }
}
