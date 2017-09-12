package com.example.shree.player_nav_drawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CenterInfo extends AppCompatActivity {


    private FirebaseDatabase matchPointDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference rootref = matchPointDatabase.getReference();
    private DatabaseReference playerpdata = rootref.child("playerpdata");
    private DatabaseReference playertdata = rootref.child("playertdata");
    private DatabaseReference tdata = rootref.child("tournamentdata");
    private DatabaseReference center_data = rootref.child("Center");
    Spinner spinner, spinner2;
    String center3, cityselected, cent, cityentered;
    // private Date  joiningdate;
    private DatePicker joiningdatecal;
    String joiningdate2;
    EditText cityad;
    private Integer vacancydec;
    private String center5, tcity, slotdate, access1;
    Intent intent;
    Long vac_dec;
    Date date;
    Date b = new Date();

Button next;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centerinfo);


        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<String> cityarray = new ArrayList<String>();
        cityarray.add("Select the city");
        cityarray.add("Delhi");
        cityarray.add("DELHI");
        cityarray.add("Mumbai");
        cityarray.add("Nagpur");
        cityarray.add("Pune");

        ArrayAdapter<String> cityadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinneritem, cityarray);
        cityadapter.setDropDownViewResource(R.layout.dropdwon_item);

        spinner.setAdapter(cityadapter);
        //spinner.setOnItemSelectedListener(this);



        spinner2 = (Spinner) findViewById(R.id.spinner2);           //Center selection
        final ArrayList<String> centerarray = new ArrayList<String>();
        centerarray.add("Select the Center");


        ArrayAdapter<String> centeradapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinneritem, centerarray);
        centeradapter.setDropDownViewResource(R.layout.dropdwon_item);

        spinner2.setAdapter(centeradapter);
        //spinner2.setOnItemSelectedListener(this);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityselected = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(CenterInfo.this, "City for tourna " + cityselected, Toast.LENGTH_SHORT).show();
                if (cityselected.equalsIgnoreCase("Delhi")) {/*//Toast.makeText(CenterInfo.this, "----", Toast.LENGTH_SHORT).show();
                    centerarray.add("Gurgaon complex");
                    centerarray.add("Gurgaon complex");
                    centerarray.add("nehru stadium");*/

                    Query query1 = tdata.orderByChild("city").equalTo(cityselected);
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

                } else if (cityselected.equalsIgnoreCase("DELHI")) {/*//Toast.makeText(CenterInfo.this, "----", Toast.LENGTH_SHORT).show();
                    centerarray.add("Gurgaon complex");
                    centerarray.add("Gurgaon complex");
                    centerarray.add("nehru stadium");*/

                    Query query1 = tdata.orderByChild("city").equalTo(cityselected);
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                tcity = (String) messageSnapshot.child("city").getValue();
                                center3 = (String) messageSnapshot.child("venue").getValue();
                                Toast.makeText(CenterInfo.this, "centers:" + center3, Toast.LENGTH_LONG).show();
                                centerarray.add(center3);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else if (cityselected.equalsIgnoreCase("Pune")) {
                    Query query1 = tdata.orderByChild("city").equalTo(cityselected);
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                tcity = (String) messageSnapshot.child("city").getValue();
                                center3 = (String) messageSnapshot.child("venue").getValue();
                                Toast.makeText(CenterInfo.this, "centers:" + center3, Toast.LENGTH_LONG).show();
                                centerarray.add(center3);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else if (cityselected.equalsIgnoreCase("Mumbai")) {
                    Query query1 = tdata.orderByChild("city").equalTo(cityselected);
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                tcity = (String) messageSnapshot.child("city").getValue();
                                center3 = (String) messageSnapshot.child("venue").getValue();
                                Toast.makeText(CenterInfo.this, "centers:" + center3, Toast.LENGTH_LONG).show();
                                centerarray.add(center3);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else if (cityselected.equalsIgnoreCase("Nagpur")) {
                    Toast.makeText(CenterInfo.this, "--", Toast.LENGTH_SHORT).show();

                } else {
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
                cent = (String) adapterView.getItemAtPosition(i).toString();
                Toast.makeText(CenterInfo.this, "center sel: " + cent, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        next=(Button) findViewById(R.id.next_page);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

    }


    public void next() {
        final String city_next = cityselected;
        cityad = (EditText) findViewById(R.id.citytext);
        cityentered = cityad.getText().toString();
        Toast.makeText(CenterInfo.this, "cityentered: " + cityentered, Toast.LENGTH_SHORT).show();

        /*slot_avail = new GregorianCalendar(slot.getYear(), slot.getMonth()+1, slot.getDayOfMonth()).getTime();
        *//*slot_avil2 = (String)slot.getDayOfMonth()+(String) slot.getMonth()+1+(String) slot.getYear();*//*
        int day = slot.getDayOfMonth();
        int month = slot.getMonth()+1;
        int year = slot.getYear();*/
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Toast.makeText(CenterInfo.this, "date: " +  dateFormat.format(b), Toast.LENGTH_SHORT).show();


        joiningdatecal = (DatePicker) findViewById(R.id.joiningcalendar);
        int day = joiningdatecal.getDayOfMonth();
        int month = joiningdatecal.getMonth() + 1;
        int year = joiningdatecal.getYear();
        joiningdate2 = Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
        center_data.child(cent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long v = (Long) dataSnapshot.child("vacancy").getValue();
                access1 = (String) dataSnapshot.child("accesstype").getValue();
                slotdate = (String) dataSnapshot.child("Availableslot").getValue();
                Toast.makeText(CenterInfo.this, "v::" + v, Toast.LENGTH_LONG).show();
                vac_dec = v;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
                    date = formatter.parse(slotdate);
                    Toast.makeText(CenterInfo.this, "slot dt::" + dateFormat1.format(date), Toast.LENGTH_LONG).show();
                    if (b.compareTo(date) > 0) {
                        Toast.makeText(CenterInfo.this, "b is after date", Toast.LENGTH_LONG).show();

                    } else if (b.compareTo(date) < 0) {
                        Toast.makeText(CenterInfo.this, "b is before date", Toast.LENGTH_LONG).show();
                    } else if (b.compareTo(date) == 0) {
                        Toast.makeText(CenterInfo.this, "b=date", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CenterInfo.this, "Error!!!!", Toast.LENGTH_LONG).show();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if( ((b.compareTo(date)>0) || (b.compareTo(date)==0)) && vac_dec>0 ){
                if (access1.equals("Residents")) {
                    Toast.makeText(CenterInfo.this, "in res ", Toast.LENGTH_LONG).show();
                    Query query1 = tdata.orderByChild("city").equalTo(cityselected);
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {

                                tcity = (String) messageSnapshot.child("city").getValue();
                                center3 = (String) messageSnapshot.child("venue").getValue();
                          /* Toast.makeText(CenterInfo.this, " tcity = "+tcity, Toast.LENGTH_LONG).show();
                           Toast.makeText(CenterInfo.this,  "center3= "+center3, Toast.LENGTH_LONG).show();
                           Toast.makeText(CenterInfo.this,  "cityselected= "+cityselected, Toast.LENGTH_LONG).show();
                           Toast.makeText(CenterInfo.this,  "cityentered= "+cityentered, Toast.LENGTH_LONG).show();
                           Toast.makeText(CenterInfo.this, "cityentered = tcity = "+cityad.equals(tcity), Toast.LENGTH_LONG).show();
                           Toast.makeText(CenterInfo.this, "cityentered = cityselected = "+cityad.equals(cityselected), Toast.LENGTH_LONG).show();
                           Toast.makeText(CenterInfo.this, "tornamnt ", Toast.LENGTH_LONG).show();*/
                                if (cityentered.equals(tcity) && cityentered.equals(cityselected)) {
                                    // Toast.makeText(CenterInfo.this, "Resident hai", Toast.LENGTH_LONG).show();
                                    // Toast.makeText(CenterInfo.this,  "joiningdate2= "+joiningdate2, Toast.LENGTH_LONG).show();
                                   // if (joiningdate2.equals(slotdate)) {
                                        Toast.makeText(CenterInfo.this, "available slot", Toast.LENGTH_LONG).show();

                                        intent = new Intent(CenterInfo.this, Signup.class);


                                        intent.putExtra("C1", city_next);
                                        intent.putExtra("C2", cent);
                                        intent.putExtra("C3", joiningdate2);
                                        intent.putExtra("C4", vac_dec);

                                        Toast.makeText(CenterInfo.this, "getextra:" + city_next + cent + joiningdate2 + vac_dec, Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    //} else {
                                     //   Toast.makeText(CenterInfo.this, "Slot is not available.. ", Toast.LENGTH_LONG).show();
                                   // }

                                } else {
                                    Toast.makeText(CenterInfo.this, "tournament is not available for Non Residents ", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else if (access1.equals("Open all")) {

                    Toast.makeText(CenterInfo.this, "in open ", Toast.LENGTH_LONG).show();


                    // Toast.makeText(CenterInfo.this, "Resident hai", Toast.LENGTH_LONG).show();
                    // Toast.makeText(CenterInfo.this,  "joiningdate2= "+joiningdate2, Toast.LENGTH_LONG).show();
                    //if (joiningdate2.equals(slotdate)) {
                        Toast.makeText(CenterInfo.this, "available slot", Toast.LENGTH_LONG).show();

                        intent = new Intent(CenterInfo.this, Signup.class);

                        intent.putExtra("C1", city_next);
                        intent.putExtra("C2", cent);
                        intent.putExtra("C3", joiningdate2);
                        intent.putExtra("C4", v);
                        startActivity(intent);
                        Toast.makeText(CenterInfo.this, "getextra:" + city_next + cent + joiningdate2 + vac_dec, Toast.LENGTH_LONG).show();
                   // } else {
                    //    Toast.makeText(CenterInfo.this, "Slot is not available.. ", Toast.LENGTH_LONG).show();
                    //}


                } else {
                    Toast.makeText(CenterInfo.this, "tournament is not available.. ", Toast.LENGTH_LONG).show();
                }
            }
                else if (b.compareTo(date) < 0) {
                    Toast.makeText(CenterInfo.this, "Sorry!! Can not register before starting date!!!", Toast.LENGTH_LONG).show();}
                else{
                    Toast.makeText(CenterInfo.this, "SORRY!! NO VACANCY..CAN NOT REGISTRATION..!! ", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

   /* public void getdate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
        String strDate = "Current Date : " + mdformat.format(calendar.getTime());
        try {
            Date start = new SimpleDateFormat("yyyy/MM/dd ", Locale.ENGLISH).parse(slotdate);
            Date end = new SimpleDateFormat("yyyy/MM/dd ", Locale.ENGLISH).parse(strDate);
           // var day = value.Date;
            Toast.makeText(CenterInfo.this, "dates: " + start + end, Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }*/

}
