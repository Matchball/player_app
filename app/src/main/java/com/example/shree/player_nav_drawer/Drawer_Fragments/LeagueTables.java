package com.example.shree.player_nav_drawer.Drawer_Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shree.player_nav_drawer.LeagueTableData;
import com.example.shree.player_nav_drawer.R;
import com.google.android.gms.wearable.ChannelApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LeagueTables.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LeagueTables#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeagueTables extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference playerpdata=rootref.child("playerid");
    private DatabaseReference playertdata = rootref.child("playertdata");
    private DatabaseReference Tdata=rootref.child("tournamentdata");
    private String selectedCategory,selectedCity,selectedmy;
    private String pcat,leagueCity,rank;
    private ArrayList<LeagueTableData> leagueArray=new ArrayList<LeagueTableData>();//stores data for each row to be displayed in league table
    // TODO: Rename and change types of parameters
    private String mParam1;

    long nrank,points,count1=0,count2=0,rank1;
    double avgPoints;
    String pname,city,tournamentMY;
    private String mParam2;
    TableLayout tableLayout;
    TableRow row;
    LeagueTableData temp;
    Spinner spinner,spinner2,spinner3;
    Button button;
    private Date startingDate;
    public DateFormat datef;
    public String startingDateString;
    private long year,dayofmonth,month;
    TextView nrankt,rankt,pnamet,cityt,averagePointst,pointst;
    private OnFragmentInteractionListener mListener;

    public LeagueTables() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeagueTables.
     */
    // TODO: Rename and change types and number of parameters
    public static LeagueTables newInstance(String param1, String param2) {
        LeagueTables fragment = new LeagueTables();
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =inflater.inflate(R.layout.fragment_league_tables,container,false);
       // ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        ///TableView
        tableLayout = (TableLayout) v.findViewById(R.id.table);
        /*row= new TableRow(getContext());

        final float scale = getResources().getDisplayMetrics().density;
        int dps=40;
        int pixels = (int) (dps * scale + 0.5f);

        TableLayout.LayoutParams tableRowParams=new TableLayout.LayoutParams
                (TableLayout.LayoutParams.MATCH_PARENT,pixels);
        row.setLayoutParams(tableRowParams);
        nrankt = new TextView(getContext());
        nrankt.setLayoutParams(new TableRow.LayoutParams(0));
        nrankt.setGravity(Gravity.CENTER);
        String grey="#eeeeee";
        nrankt.setTextColor(Color.parseColor(grey));




        rankt = new TextView(getContext());
        rankt.setLayoutParams(new TableRow.LayoutParams(1));
        rankt.setGravity(Gravity.CENTER);
        rankt.setTextColor(Color.parseColor(grey));

        pnamet=new TextView(getContext());
        pnamet.setLayoutParams(new TableRow.LayoutParams(2));
        pnamet.setGravity(Gravity.CENTER);
        pnamet.setTextColor(Color.parseColor(grey));

        cityt=new TextView(getContext());
        cityt.setLayoutParams(new TableRow.LayoutParams(3));
        cityt.setGravity(Gravity.CENTER);
        cityt.setTextColor(Color.parseColor(grey));

        averagePointst=new TextView(getContext());
        averagePointst.setLayoutParams(new TableRow.LayoutParams(5));
        averagePointst.setGravity(Gravity.CENTER);
        averagePointst.setTextColor(Color.parseColor(grey));

        pointst=new TextView(getContext());
        pointst.setLayoutParams(new TableRow.LayoutParams(4));
        pointst.setGravity(Gravity.CENTER);
        pointst.setTextColor(Color.parseColor(grey));
*/


    spinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayList<String> categories=new ArrayList<String>();

        categories.add("BU11");
        categories.add("GU11");
        categories.add("BU13");
        categories.add("GU13");
        categories.add("BU15");
        categories.add("GU15");
        categories.add("BU17");
        categories.add("GU17");
        categories.add("BU19");
        categories.add("GU19");

        ArrayAdapter<String> catadapter=new ArrayAdapter<String>(getContext(),R.layout.spinneritem,categories);
        catadapter.setDropDownViewResource(R.layout.dropdwon_item);

        spinner.setAdapter(catadapter);
        //spinner.setOnItemSelectedListener(this);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getContext(), "Cat: "+selectedCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    spinner2 = (Spinner) v.findViewById(R.id.spinner2);//drop down for selecting cities
        ArrayList<String> cities=new ArrayList<String>();

        cities.add("DELHI");
        cities.add("MUMBAI");
        cities.add("KOLKATA");
        cities.add("CHENNAI");

        ArrayAdapter<String> dataadapter=new ArrayAdapter<String>(getContext(),R.layout.spinneritem,cities);          // you don't display last item. It is used as hint.

        dataadapter.setDropDownViewResource(R.layout.dropdwon_item);
        spinner2.setAdapter(dataadapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCity=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getContext(), "City: "+selectedCity, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner3= (Spinner) v.findViewById(R.id.spinner3);//drop down for selecting cities
        ArrayList<String> mmyy=new ArrayList<String>();

        mmyy.add("08/2017");
        mmyy.add("07/2018");
        mmyy.add("06/2017");
        mmyy.add("09/2017");

        ArrayAdapter<String> m_yadapter=new ArrayAdapter<String>(getContext(),R.layout.spinneritem,mmyy);          // you don't display last item. It is used as hint.

        m_yadapter.setDropDownViewResource(R.layout.dropdwon_item);
        spinner3.setAdapter(m_yadapter);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 selectedmy = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getContext(), "mmyy: "+selectedmy, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        button=(Button) v.findViewById(R.id.perform);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performquery();
            }
        });



        return v;
    }


    public void performquery()
    {if(!selectedCategory.equals(null) && !selectedCity.equals(null) && !selectedmy.equals(null)){

     //   Toast.makeText(getContext(), "cat selected"+selectedCategory, Toast.LENGTH_LONG).show();
       Query query1=playertdata.orderByChild("playercategory").equalTo(selectedCategory);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                leagueArray.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    leagueCity=(String) messageSnapshot.child("city").getValue();
                 rank1=(Long) messageSnapshot.child("rank").getValue();
                   //Toast.makeText(getContext(), "rank: city: "+leagueCity+" "+rank1, Toast.LENGTH_LONG).show();
                  nrank=count1;
                 points=(Long) messageSnapshot.child("totalPoints").getValue();
                 avgPoints=(double)Math.round(-1*(Double)messageSnapshot.child("averagePoints").getValue()*10.0)/10.0;

                    pname = (String) messageSnapshot.child("playerid").getValue();  //playername
                    city = (String) messageSnapshot.child("city").getValue();       //playercity
                       /* Query query2=Tdata.orderByChild("city").equalTo(selectedCity);
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                    String Lc=(String) dataSnapshot.child("city").getValue();
                                    Toast.makeText(getContext(), "city:selcity " +Lc+" "+selectedCity , Toast.LENGTH_LONG).show();
                                    ++count2;

                                    year = (long) messageSnapshot.child("startingDate").child("year").getValue();
                                    month = (long) messageSnapshot.child("startingDate").child("month").getValue();
                                    dayofmonth = (long) messageSnapshot.child("startingDate").child("date").getValue();
                                    startingDate = new GregorianCalendar((int) year + 1900, (int) month, (int) dayofmonth).getTime();
                                    datef = new SimpleDateFormat("MM/yyyy", Locale.US);//since we need to display date in readable format
                                    startingDateString = datef.format(startingDate);
                                    String startD = startingDateString.toString();
                                    Toast.makeText(getContext(), "Satrt_Date: " + startD, Toast.LENGTH_LONG).show();
                                   if (startD.equals(selectedmy)) {
                                        Toast.makeText(getContext(), "date matched"+startD+" "+selectedmy, Toast.LENGTH_LONG).show();

                                        long rank = count2;
                                        //  Toast.makeText(getContext(), "values::"+nrank+" "+rank+" "+pname+" "+city+" "+points+" "+avgPoints, Toast.LENGTH_LONG).show();
                                        temp = new LeagueTableData(nrank, rank, pname, city, points, avgPoints);
                                        // Toast.makeText(getContext(), "temp: "+temp, Toast.LENGTH_LONG).show();
                                        leagueArray.add(temp);
                                   }
                                }

                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });*/

                    //    Toast.makeText(getContext(), "equals selCity", Toast.LENGTH_LONG).show();

                    if(leagueCity.equalsIgnoreCase(selectedCity)) {
                    //    Toast.makeText(getContext(), "equals selCity", Toast.LENGTH_LONG).show();
                    ++count2;
                    long rank = count2;

                    Toast.makeText(getContext(), "values::"+nrank+" "+rank+" "+pname+" "+city+" "+points+" "+avgPoints, Toast.LENGTH_LONG).show();
                    temp = new LeagueTableData(nrank, rank, pname, city, points, avgPoints);
                    // Toast.makeText(getContext(), "temp: "+temp, Toast.LENGTH_LONG).show();
                    leagueArray.add(temp);


                }
            }
                Toast.makeText(getContext(), "Query Completed ", Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), "C1 C2: "+count1+" "+count2, Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), "Arraysize:"+leagueArray.size(), Toast.LENGTH_LONG).show();
                modifyTable();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }else{Toast.makeText(getContext(), "cat and city "+selectedCategory+" "+selectedCity, Toast.LENGTH_LONG).show();

    }
    }

    /**
     * to clean table after before udating table else the rows will apppend to the previous ones
     * @param table table to which all the player data is added
     */
    public void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }

    /**
     * adds the data from league array to table on screen
     */
    public void modifyTable()
    {
        cleanTable(tableLayout);
        String grey="#eeeeee";

       /*
           nrankt.setText("11");
            rankt.setText("22");
            pnamet.setText("xyz");
            cityt.setText("xyx");
            pointst.setText("33");
            averagePointst.setText("44");

            row.addView(nrankt);
            row.addView(rankt);
            row.addView(pnamet);
            row.addView(cityt);
            row.addView(pointst);
            row.addView(averagePointst);
            tableLayout.addView(row);*/

        cleanTable(tableLayout);
        for (int i = 0; i <leagueArray.size(); i++) {
            LeagueTableData temp=leagueArray.get(i);
            TableRow row= new TableRow(getContext());
            final float scale = getResources().getDisplayMetrics().density;
            int dps=40;
            int pixels = (int) (dps * scale + 0.5f);
            TableLayout.LayoutParams tableRowParams=new TableLayout.LayoutParams
                    (TableLayout.LayoutParams.MATCH_PARENT,pixels);
            row.setLayoutParams(tableRowParams);
            TextView nrankt = new TextView(getContext());nrankt.setText(String.valueOf(temp.nRank)); nrankt.setTextColor(Color.parseColor(grey));nrankt.setLayoutParams(new TableRow.LayoutParams(0));nrankt.setGravity(Gravity.CENTER);
            TextView rankt = new TextView(getContext());rankt.setText(String.valueOf(temp.rank)); rankt.setTextColor(Color.parseColor(grey));rankt.setLayoutParams(new TableRow.LayoutParams(1));rankt.setGravity(Gravity.CENTER);
            TextView pnamet=new TextView(getContext());pnamet.setText(String.valueOf(temp.pname)); pnamet.setTextColor(Color.parseColor(grey));pnamet.setGravity(Gravity.CENTER);
            TextView cityt=new TextView(getContext());cityt.setText(String.valueOf(temp.city)); cityt.setTextColor(Color.parseColor(grey));cityt.setGravity(Gravity.CENTER);
            TextView pointst=new TextView(getContext());pointst.setText(String.valueOf(temp.points));pointst.setTextColor(Color.parseColor(grey));pointst.setGravity(Gravity.CENTER);
            TextView averagePointst=new TextView(getContext());averagePointst.setText(String.valueOf(temp.averagePoints)); averagePointst.setTextColor(Color.parseColor(grey));averagePointst.setGravity(Gravity.CENTER);
            row.addView(nrankt);
            row.addView(rankt);
            row.addView(pnamet);
            row.addView(cityt);
            row.addView(pointst);
            row.addView(averagePointst);
            tableLayout.addView(row,i+1);
        }
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
