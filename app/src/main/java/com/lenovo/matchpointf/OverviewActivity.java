package com.lenovo.matchpointf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static android.R.attr.category;

/**
 * to select the functionality from player personal data or league table or tournament list
 */
public class OverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
    }

    public void goToTournamentCalendar(View v) {
        String category=getIntent().getStringExtra("CATEGORY");
        String playerid=getIntent().getStringExtra("PLAYER_ID");
        Intent intent=new Intent(this,Screen3.class);
        intent.putExtra("PLAYER_ID", playerid);
        intent.putExtra("CATEGORY",category);
        startActivity(intent);
    }

    public void getLeagueTable(View v) {
        Intent intent=new Intent(this,LeagueTableActivity.class);
        startActivity(intent);
    }

    public void getPersonalData(View v) {
        String category=getIntent().getStringExtra("CATEGORY");
        String playerId=getIntent().getStringExtra("PLAYER_ID");
        Intent intent=new Intent(this,PlayerPersonalActivity.class);
        intent.putExtra("PLAYER_ID", playerId);
        intent.putExtra("CATEGORY",category);
        startActivity(intent);
    }
}
