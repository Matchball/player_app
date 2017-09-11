package com.lenovo.matchpointf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.category;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.lenovo.matchpointf.R.id.checkBox;

/**
 * activity to display league table
 */
public class LeagueTableActivity extends AppCompatActivity {
    private DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference playertdata = rootref.child("playertdata");
    private String selectedCategory,selectedCity;
    private String pcat,leagueCity;

    /**
     * to store data for each row of table
     */
    private ArrayList<LeagueTableData> leagueArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_table);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
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

        ArrayAdapter<String> catadapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,categories);
        catadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(catadapter);
        //spinner.setOnItemSelectedListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(LeagueTableActivity.this, "Cat: "+selectedCategory, Toast.LENGTH_SHORT).show();
                perfomQuery();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /*List<String> categories = new ArrayList<String>();
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
        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                ((TextView)findViewById(R.id.textView29)).setText(item);
                selectedCategory=item;
                Toast.makeText(parent.getContext(), "Category Selected: " + item, Toast.LENGTH_LONG).show();
                perfomQuery();
            }*/



       // });

        // Spinner Drop down elements

        // Creating adapter for spinner
       /* ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0);
        selectedCategory="BU11";
        ((TextView)findViewById(R.id.textView29)).setText("BU11");*/

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);//drop down for selecting cities
        final ArrayList<String> cities=new ArrayList<String>();
        cities.add("DELHI");
        cities.add("MUMBAI");
        cities.add("KOLKATA");
        cities.add("CHENNAI");

        ArrayAdapter<String> dataadapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,cities);
        dataadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataadapter);
        //spinner2.setOnItemSelectedListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCity=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(LeagueTableActivity.this, "Cat: "+selectedCity, Toast.LENGTH_SHORT).show();
                perfomQuery();
               /* if(selectedCategory.equalsIgnoreCase("Delhi"))
                {

                }
                else{
                    Toast.makeText(LeagueTableActivity.this, "--Select city--", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
       /* List<String> cities = new ArrayList<String>();
        cities.add("DELHI");
        cities.add("MUMBAI");
        cities.add("KOLKATA");
        cities.add("CHENNAI");
        // Spinner click listener
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "City Selected: " + item, Toast.LENGTH_LONG).show();
                selectedCity=item;
                perfomQuery();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        // Spinner Drop down elements

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner2.setAdapter(dataAdapter2);
        spinner2.setSelection(0);
        selectedCity="DELHI";*/
    }

    /**
     * performs the necessry update of league table according to selected specifications
     */
    public void perfomQuery()
    {
        leagueArray=new ArrayList<LeagueTableData>();//stores data for each row to be displayed in league table
        Query query1 = playertdata.orderByChild("rank");
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count1=0,count2=0;
                leagueArray.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    pcat=(String) messageSnapshot.child("playercategory").getValue();
                    Toast.makeText(LeagueTableActivity.this, "catagory: "+pcat, Toast.LENGTH_LONG).show();


                    if(pcat == selectedCategory) {
                        Toast.makeText(LeagueTableActivity.this, "equals cat", Toast.LENGTH_LONG).show();
                        String pname = (String) messageSnapshot.child("playerid").getValue();
                        String city = (String) messageSnapshot.child("city").getValue();
                        ++count1;
                        long nrank=count1;
                        long points=(Long) messageSnapshot.child("totalPoints").getValue();
                        double avgPoints=(double)Math.round(-1*(Double)messageSnapshot.child("averagePoints").getValue()*10.0)/10.0;

                        leagueCity=(String) messageSnapshot.child("playercategory").getValue();
                        Toast.makeText(LeagueTableActivity.this, "city: "+leagueCity, Toast.LENGTH_LONG).show();
                        if(leagueCity == selectedCity) {
                            Toast.makeText(LeagueTableActivity.this, "equals selCity", Toast.LENGTH_LONG).show();
                            ++count2;
                            long rank = count2;
                            LeagueTableData temp = new LeagueTableData(nrank, rank, pname, city, points, avgPoints);
                            leagueArray.add(temp);
                        }
                    }


                }
                Toast.makeText(LeagueTableActivity.this, "Query Completed ", Toast.LENGTH_LONG).show();
                modifyTable();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

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
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
        cleanTable(tableLayout);
        for (int i = 0; i <leagueArray.size(); i++) {
            LeagueTableData temp=leagueArray.get(i);
            TableRow row= new TableRow(this);
            final float scale = getResources().getDisplayMetrics().density;
            int dps=40;
            int pixels = (int) (dps * scale + 0.5f);
            TableLayout.LayoutParams tableRowParams=new TableLayout.LayoutParams
                    (TableLayout.LayoutParams.MATCH_PARENT,pixels);
            row.setLayoutParams(tableRowParams);
            TextView nrankt = new TextView(this);nrankt.setText(String.valueOf(temp.nRank));nrankt.setLayoutParams(new TableRow.LayoutParams(0));nrankt.setGravity(Gravity.CENTER);
            TextView rankt = new TextView(this);rankt.setText(String.valueOf(temp.rank));rankt.setLayoutParams(new TableRow.LayoutParams(1));rankt.setGravity(Gravity.CENTER);
            TextView pnamet=new TextView(this);pnamet.setText(String.valueOf(temp.pname));pnamet.setLayoutParams(new TableRow.LayoutParams(2));pnamet.setGravity(Gravity.CENTER);
            TextView cityt=new TextView(this);cityt.setText(String.valueOf(temp.city));cityt.setLayoutParams(new TableRow.LayoutParams(3));cityt.setGravity(Gravity.CENTER);
            TextView pointst=new TextView(this);pointst.setText(String.valueOf(temp.points));pointst.setLayoutParams(new TableRow.LayoutParams(4));pointst.setGravity(Gravity.CENTER);
            TextView averagePointst=new TextView(this);averagePointst.setText(String.valueOf(temp.averagePoints));averagePointst.setLayoutParams(new TableRow.LayoutParams(5));averagePointst.setGravity(Gravity.CENTER);
            row.addView(nrankt);
            row.addView(rankt);
            row.addView(pnamet);
            row.addView(cityt);
            row.addView(pointst);
            row.addView(averagePointst);
            tableLayout.addView(row,i+1);
        }
    }
}
