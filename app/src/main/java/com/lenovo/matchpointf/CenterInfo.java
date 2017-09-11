package com.lenovo.matchpointf;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

public class CenterInfo extends AppCompatActivity {

    private FirebaseDatabase matchPointDatabase = FirebaseDatabase.getInstance()
            ;
    private DatabaseReference rootref = matchPointDatabase.getReference();

    private DatabaseReference tdata=rootref.child("tournamentdata");

    private DatabaseReference centerandslot=rootref.child("Center");

    private Spinner spinner,spinner2;

    private String center3,cityselected,cent,cityentered;

    private DatePicker joiningdatecal;

    private String joiningdate2;

    private EditText cityad;

    private Integer vacancydec;

    private String center5,tcity,slotdate,access1;

    private Intent intent;

    private String vac_dec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centerinfo);

        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<String> cityarray=new ArrayList<String>();
        cityarray.add("Select the city");
        cityarray.add("Delhi");
        cityarray.add("Mumbai");
        cityarray.add("Nagpur");
        cityarray.add("Pune");

        ArrayAdapter<String> cityadapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,cityarray);
        cityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(cityadapter);
        //spinner.setOnItemSelectedListener(this);
        spinner2 = (Spinner) findViewById(R.id.spinner2);           //Center selection
        final ArrayList<String> centerarray=new ArrayList<String>();
        centerarray.add("Select the Center");


        ArrayAdapter<String> centeradapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,centerarray);
        centeradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(centeradapter);
        //spinner2.setOnItemSelectedListener(this);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityselected=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(CenterInfo.this, "City for tourna "+cityselected, Toast.LENGTH_SHORT).show();
                if(cityselected.equalsIgnoreCase("Delhi"))
                {/*//Toast.makeText(CenterInfo.this, "----", Toast.LENGTH_SHORT).show();
                    centerarray.add("Gurgaon complex");
                    centerarray.add("Gurgaon complex");
                    centerarray.add("nehru stadium");*/

                    Query query1=tdata.orderByChild("city").equalTo(cityselected);
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                tcity = (String) messageSnapshot.child("city").getValue();
                                center3 = (String) messageSnapshot.child("venue").getValue();
                                //Toast.makeText(CenterInfo.this, "centers:"+center3, Toast.LENGTH_LONG).show();
                                centerarray.add(center3);


                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                else if(cityselected.equalsIgnoreCase("Pune"))
                {Query query1=tdata.orderByChild("city").equalTo(cityselected);
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                tcity = (String) messageSnapshot.child("city").getValue();
                                center3 = (String) messageSnapshot.child("venue").getValue();
                                Toast.makeText(CenterInfo.this, "centers:"+center3, Toast.LENGTH_LONG).show();
                                centerarray.add(center3);
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else if(cityselected.equalsIgnoreCase("Mumbai"))
                {Query query1=tdata.orderByChild("city").equalTo(cityselected);
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                tcity = (String) messageSnapshot.child("city").getValue();
                                center3 = (String) messageSnapshot.child("venue").getValue();
                                Toast.makeText(CenterInfo.this, "centers:"+center3, Toast.LENGTH_LONG).show();
                                centerarray.add(center3);
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else if(cityselected.equalsIgnoreCase("Nagpur"))
                {Toast.makeText(CenterInfo.this, "--", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(CenterInfo.this, "--Select city--", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //SPINNER 2

      spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             cent=(String) adapterView.getItemAtPosition(i).toString();
              Toast.makeText(CenterInfo.this, "center sel: "+cent, Toast.LENGTH_LONG).show();
          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {}
      });


    }
    public void next(View v) {
        final String city_next=cityselected;
        cityentered=cityad.getText().toString();
        Toast.makeText(CenterInfo.this, "cityentered: "+cityentered, Toast.LENGTH_SHORT).show();

        int day=joiningdatecal.getDayOfMonth();
        int month=joiningdatecal.getMonth()+1;
        int year=joiningdatecal.getYear();
        joiningdate2 =  Integer.toString(day)+"/"+Integer.toString(month)+"/"+Integer.toString(year);

        Query query5=centerandslot.orderByChild("CenterName").equalTo(cent);
        //Toast.makeText(CenterInfo.this, "query "+query5, Toast.LENGTH_LONG).show();
        query5.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Toast.makeText(CenterInfo.this, "--------- "+dataSnapshot, Toast.LENGTH_LONG).show();

                // if(dataSnapshot.getChildrenCount()>1)
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    access1=(String) messageSnapshot.child("accesstype").getValue();
                    slotdate=(String) messageSnapshot.child("Availableslot").getValue();
                    final long v=(Long) messageSnapshot.child("vacancy").getValue();
                    Toast.makeText(CenterInfo.this, "v::"+v, Toast.LENGTH_LONG).show();

                    //Toast.makeText(CenterInfo.this, "counter--: "+v, Toast.LENGTH_LONG).show();
                    if(access1.equals("Residents"))
                    {
                        Toast.makeText(CenterInfo.this, "in res ", Toast.LENGTH_LONG).show();
                        Query query1=tdata.orderByChild("city").equalTo(cityselected);
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (final DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {

                                    tcity = (String) messageSnapshot.child("city").getValue();
                                    center3 = (String) messageSnapshot.child("venue").getValue();
                                    /*Toast.makeText(CenterInfo.this, " tcity = "+tcity, Toast.LENGTH_LONG).show();
                                    Toast.makeText(CenterInfo.this,  "center3= "+center3, Toast.LENGTH_LONG).show();
                                    Toast.makeText(CenterInfo.this,  "cityselected= "+cityselected, Toast.LENGTH_LONG).show();
                                    Toast.makeText(CenterInfo.this,  "cityentered= "+cityentered, Toast.LENGTH_LONG).show();
                                    Toast.makeText(CenterInfo.this, "cityentered = tcity = "+cityad.equals(tcity), Toast.LENGTH_LONG).show();
                                    Toast.makeText(CenterInfo.this, "cityentered = cityselected = "+cityad.equals(cityselected), Toast.LENGTH_LONG).show();
                                    Toast.makeText(CenterInfo.this, "tornamnt ", Toast.LENGTH_LONG).show();*/
                                    if (cityentered.equals(tcity) && cityentered.equals(cityselected)) {
                                       // Toast.makeText(CenterInfo.this, "Resident hai", Toast.LENGTH_LONG).show();
                                       // Toast.makeText(CenterInfo.this,  "joiningdate2= "+joiningdate2, Toast.LENGTH_LONG).show();
                                        if(joiningdate2.equals(slotdate))
                                        {
                                            Toast.makeText(CenterInfo.this, "available slot", Toast.LENGTH_LONG).show();

                                            intent = new Intent(CenterInfo.this, SignInActivity.class);


                                            intent.putExtra("C1",city_next);
                                            intent.putExtra("C2",cent);
                                            intent.putExtra("C3",joiningdate2);
                                            intent.putExtra("C4",v);
                                            startActivity(intent);
                                        }
                                        else{
                                            Toast.makeText(CenterInfo.this, "Slot is not available.. ", Toast.LENGTH_LONG).show();
                                        }

                                    }

                                    else{Toast.makeText(CenterInfo.this, "tournament is not available for Non Residents ", Toast.LENGTH_LONG).show();}
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    else if(access1.equals("Open all") )
                    {

                        Toast.makeText(CenterInfo.this, "in open ", Toast.LENGTH_LONG).show();


                        // Toast.makeText(CenterInfo.this, "Resident hai", Toast.LENGTH_LONG).show();
                        // Toast.makeText(CenterInfo.this,  "joiningdate2= "+joiningdate2, Toast.LENGTH_LONG).show();
                        if(joiningdate2.equals(slotdate))
                        {
                            Toast.makeText(CenterInfo.this, "available slot", Toast.LENGTH_LONG).show();
                            intent = new Intent(CenterInfo.this, SignInActivity.class);
                            intent = new Intent(CenterInfo.this, SignInActivity.class);

                            intent.putExtra("C1",city_next);
                            intent.putExtra("C2",cent);
                            intent.putExtra("C3",joiningdate2);
                            intent.putExtra("C4",v);
                            startActivity(intent);
                            /*v=v-1;  //vacancy counter decrements
                            messageSnapshot.getRef().getParent().child("vacancy").setValue(v);
                            Toast.makeText(CenterInfo.this, "counter: "+v, Toast.LENGTH_LONG).show();
*/
                        }
                        else{
                            Toast.makeText(CenterInfo.this, "Slot is not available.. ", Toast.LENGTH_LONG).show();
                        }


                    }
                    else{Toast.makeText(CenterInfo.this, "tournament is not available.. ", Toast.LENGTH_LONG).show();}
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }
/*
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        //Toast.makeText(CenterInfo.this, "inside listener", Toast.LENGTH_LONG).show();
        String cityselected=adapterView.getItemAtPosition(i).toString();

        Toast.makeText(CenterInfo.this, "City selected- "+cityselected, Toast.LENGTH_LONG).show();
        Spinner sp=(Spinner) adapterView;
        if(sp.getId()==R.id.spinner)
        {
            Toast.makeText(CenterInfo.this, "inside listener 1", Toast.LENGTH_LONG).show();
        Query query1=tdata.orderByChild("city").equalTo(cityselected);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    tcity = (String) messageSnapshot.child("city").getValue();
                    center3 = (String) messageSnapshot.child("venue").getValue();
                    Toast.makeText(CenterInfo.this, "centers:"+center3, Toast.LENGTH_LONG).show();

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }
        else if(sp.getId()==R.id.spinner2)
        {Toast.makeText(CenterInfo.this, "inside listener 2", Toast.LENGTH_LONG).show();

            Query query1=tdata.orderByChild("city").equalTo(cityselected);
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (final DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        tcity = (String) messageSnapshot.child("city").getValue();
                        center3 = (String) messageSnapshot.child("venue").getValue();
                        Toast.makeText(CenterInfo.this, "centers:"+center3, Toast.LENGTH_LONG).show();

                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(CenterInfo.this, "Plz select the city!", Toast.LENGTH_LONG).show();
    }

*/


}
