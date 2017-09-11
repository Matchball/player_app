package com.lenovo.matchpointf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.Scanner;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Activity for displaying Tournament data
 * @author atharva vyas
 */
public class TournamentDataActivity extends AppCompatActivity {
    private DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference Tdata = rootref.child("tournamentdata");//database reference to connect to tournament database
    private TextView organizerT;
    private CheckBox cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_data);
        TextView dateT=(TextView)findViewById(R.id.textView12);
        TextView nameT=(TextView)findViewById(R.id.textView13);
        organizerT=(TextView)findViewById(R.id.textView16);
        TextView cityT=(TextView)findViewById(R.id.textView18);
        TextView venueT=(TextView)findViewById(R.id.textView20);
        TextView categoryT=(TextView)findViewById(R.id.textView25);
        TextView drawSizeT=(TextView)findViewById(R.id.textView26);
        TextView pointsT=(TextView)findViewById(R.id.textView27);
        TextView additionalInfoT=(TextView)findViewById(R.id.textView28);
        dateT.setText(getIntent().getStringExtra("DATE"));
        nameT.setText(getIntent().getStringExtra("TNAME"));
        organizerT.setText(getIntent().getStringExtra("ORGANIZER"));
        cityT.setText(getIntent().getStringExtra("CITY"));
        venueT.setText(getIntent().getStringExtra("VENUE"));
        drawSizeT.setText(getIntent().getStringExtra("DRAWSIZE"));
        categoryT.setText(getIntent().getStringExtra("CATSTRING"));
        cb=(CheckBox)findViewById(R.id.checkBox);
        if(organizerT.getText().equals("external"))
        {
            cb.setVisibility(View.INVISIBLE);
        }
        pointsT.setText("270");
        additionalInfoT.setText("-");
        Toast.makeText(TournamentDataActivity.this, "-------------", Toast.LENGTH_LONG).show();
    }

    /**
     * This method enrolls the user of app in tournament if not already enrolled
     * @param v the view from which registration is done
     */
     public void registerNow(View v)
    {
        if(organizerT.getText().equals("external")|| cb.isChecked())
        {
            String tname=getIntent().getStringExtra("TNAME");
            Toast.makeText(TournamentDataActivity.this, "city:: "+tname, Toast.LENGTH_LONG).show();
            final String cat=getIntent().getStringExtra("CATEGORY");
            final String pid = getIntent().getStringExtra("PLAYER_ID");
            Query query = Tdata.orderByChild("tournamentName").equalTo(tname);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        GenericTypeIndicator<ArrayList<MyClass>> gen = new GenericTypeIndicator<ArrayList<MyClass>>() {
                        };
                        ArrayList<MyClass> enrolled = (ArrayList<MyClass>) messageSnapshot.child("enrolled").getValue(gen);//enrolled list will contain elements having both playerid and category
                        boolean b=false;
                        for(int i=0;i<enrolled.size();++i)
                        {
                            if((enrolled.get(i).playername).equals(pid))
                            {
                                b=true;
                            }
                        }
                        if (b) {
                            Toast.makeText(TournamentDataActivity.this, "U are already enrolled", Toast.LENGTH_LONG).show();
                        } else {
                            enrolled.add(new MyClass(pid,cat));
                            String key;
                            key = messageSnapshot.getKey();
                            Tdata.child(key).child("enrolled").setValue(enrolled);
                            Toast.makeText(TournamentDataActivity.this, "U have been enrolled", Toast.LENGTH_LONG).show();
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        else
        {
            Toast.makeText(TournamentDataActivity.this, "Agree to terms and conditions first", Toast.LENGTH_LONG).show();
        }
    }
}
