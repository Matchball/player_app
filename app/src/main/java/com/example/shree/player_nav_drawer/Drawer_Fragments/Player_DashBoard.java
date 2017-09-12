package com.example.shree.player_nav_drawer.Drawer_Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shree.player_nav_drawer.MonthlyRankData;
import com.example.shree.player_nav_drawer.R;
import com.example.shree.player_nav_drawer.RecentTournamentData;
import com.example.shree.player_nav_drawer.TournamentMatch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Player_DashBoard.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Player_DashBoard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Player_DashBoard extends Fragment {

    private DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference playerpdata = rootref.child("playerpdata");
    private DatabaseReference playertdata = rootref.child("playertdata");
    private DatabaseReference tournamentmsdata = rootref.child("tournamentmsdata");
    /**
     * needed to extract information of rank the players had when the recent tournament data was played
     */
    private DatabaseReference monthlydata=rootref.child("monthlyRankList");
    private String playerid,category;
            private String category1,city,categoryu;
            private Long careerWin,careerLoss,yearWon,yearLoss,totalPoints,nrank;
            private double avgPoints;
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
    TableLayout tableLayout;
    private PopupWindow mPopupWindow;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView namet,categoryt,cityt,winlosst,nrankt,cbrankt,cwinlosst,avgpointst,tpointst;


    private OnFragmentInteractionListener mListener;

    public Player_DashBoard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Player_DashBoard.
     */
    // TODO: Rename and change types and number of parameters
    public static Player_DashBoard newInstance(String param1, String param2) {
        Player_DashBoard fragment = new Player_DashBoard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Bundle b1=getArguments();
       String [] data= b1.getStringArray("putData");
        Toast.makeText(getContext(), "putdata_dash : "+data[0]+" "+data[1], Toast.LENGTH_SHORT).show();
        playerid=data[0];
        //Toast.makeText(getContext(), "getextras: "+playerid+category, Toast.LENGTH_SHORT).show();
        category=data[1];
        //Toast.makeText(getContext(), "getextras_dash: "+playerid+category, Toast.LENGTH_SHORT).show();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_player__dash_board,container,false);

        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        // Inflate the layout for this fragment

          namet=(TextView) v.findViewById(R.id.textView36);
        //Toast.makeText(getContext(), "namet: "+namet.toString(), Toast.LENGTH_SHORT).show();
        categoryt=(TextView) v.findViewById(R.id.textView37);
         cityt=(TextView) v.findViewById(R.id.textView38);
         nrankt=(TextView) v.findViewById(R.id.textView39);
         winlosst=(TextView) v.findViewById(R.id.textView40);
         tpointst=(TextView) v.findViewById(R.id.textView41);
         cbrankt=(TextView) v.findViewById(R.id.textView45);
        cwinlosst=(TextView) v.findViewById(R.id.textView46);
        avgpointst=(TextView) v.findViewById(R.id.textView47);
        tlist=new ArrayList<RecentTournamentData>();

        tableLayout = (TableLayout) v.findViewById(R.id.recentTournamentTable);

       // Toast.makeText(getApplicationContext(), "get"+playerid, Toast.LENGTH_SHORT).show();
        Query query = playertdata.orderByChild("playerid").equalTo(playerid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                   Toast.makeText(getContext(), "in pt data", Toast.LENGTH_SHORT).show();
                     category1=(String) messageSnapshot.child("playercategory").getValue();
                    careerWin=(Long) messageSnapshot.child("careerWin").getValue();
                    careerLoss=(Long) messageSnapshot.child("careerLoss").getValue();
                    city=(String) messageSnapshot.child("city").getValue();
                    yearWon=(Long) messageSnapshot.child("yearlyMatchesWon").getValue();
                    yearLoss=(Long) messageSnapshot.child("yearlyMatchesLost").getValue();
                     totalPoints=(Long) messageSnapshot.child("totalPoints").getValue();
                     nrank=(Long) messageSnapshot.child("rank").getValue();
                    avgPoints=-(Double)messageSnapshot.child("averagePoints").getValue();

                    if(category1.charAt(0)=='B')
                        categoryu="Boys Under ";
                    else
                        categoryu="Girls Under ";
                    Toast.makeText(getContext(), "in pt data:catagryu: "+categoryu+" "+category1+careerLoss+nrank+avgPoints, Toast.LENGTH_SHORT).show();
                    categoryu+=category1.substring(2);
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



        return v;
    }

    private void findLastFiveTournaments() {

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

        for (int i = 0; i <tlist.size(); i++) {
            RecentTournamentData temp=tlist.get(i);
            long year=temp.year;
            final long month=temp.month;
            final TableRow row= new TableRow(getContext());
            final float scale = getResources().getDisplayMetrics().density;
            int dps=40;
            final long rounds=temp.rounds;
            int pixels = (int) (dps * scale + 0.5f);
            TableLayout.LayoutParams tableRowParams=new TableLayout.LayoutParams
                    (TableLayout.LayoutParams.MATCH_PARENT,pixels);
            row.setLayoutParams(tableRowParams);
            TextView datet = new TextView(getContext());datet.setText(String.valueOf(temp.edate));datet.setLayoutParams(new TableRow.LayoutParams(0));datet.setGravity(Gravity.CENTER);
            TextView tnamet = new TextView(getContext());tnamet.setText(String.valueOf(temp.tname));tnamet.setLayoutParams(new TableRow.LayoutParams(1));tnamet.setGravity(Gravity.CENTER);
            TextView pointst=new TextView(getContext());pointst.setText(String.valueOf(temp.position));pointst.setLayoutParams(new TableRow.LayoutParams(2));pointst.setGravity(Gravity.CENTER);
            TextView positiont=new TextView(getContext());positiont.setText(String.valueOf(temp.points));positiont.setLayoutParams(new TableRow.LayoutParams(3));positiont.setGravity(Gravity.CENTER);
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
                TableRow row = new TableRow(getContext());
                final float scale = getResources().getDisplayMetrics().density;
                int dps = 40;
                int pixels = (int) (dps * scale + 0.5f);
                TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams//table layout inside pop up window
                        (TableLayout.LayoutParams.MATCH_PARENT, pixels);
                row.setLayoutParams(tableRowParams);
                TextView roundt = new TextView(getContext());
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
                TextView player1t = new TextView(getContext());
                MonthlyRankData mtemp1=(MonthlyRankData)rankmp.get(tmatch.player1);
                player1t.setText(String.valueOf(tmatch.player1)+"("+mtemp1.rank+")");
                player1t.setLayoutParams(new TableRow.LayoutParams(1));
                player1t.setGravity(Gravity.CENTER);
                TextView player2t = new TextView(getContext());
                MonthlyRankData mtemp2=(MonthlyRankData)rankmp.get(tmatch.player2);
                player2t.setText(String.valueOf(tmatch.player2)+"("+mtemp2.rank+")");
                player2t.setLayoutParams(new TableRow.LayoutParams(2));
                player2t.setGravity(Gravity.CENTER);
                TextView scoret = new TextView(getContext());
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
        mPopupWindow.showAtLocation((android.support.constraint.ConstraintLayout) v.findViewById(R.id.playerpersonallayout), Gravity.CENTER, 0, 0);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
