package com.grey.monsterclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class ScoresActivity extends AppCompatActivity {

    DataBaseHelper dbHelper;
    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        dbHelper = new DataBaseHelper(this, "scoresDB",1);
        mListView = (ListView) findViewById(R.id.scoresList);


        Bundle bundle = getIntent().getExtras();
        String playerName = bundle.getString("name");
        String playerScore =  bundle.getString("score");

        Player newPlayer = new Player(playerName, playerScore);
        dbHelper.insertData(newPlayer);
        ArrayList<Player> playerListTest = dbHelper.getAllPlayers();


        ScoreListAdapter adapter = new ScoreListAdapter(this, R.layout.rows, playerListTest);
        mListView.setAdapter(adapter);
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Quit?");
        builder.setMessage("Do you want to play again?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Bundle bundle = getIntent().getExtras();
                String playerName = bundle.getString("name");
                Intent newPlayerIntent = new Intent(ScoresActivity.this, MainActivity.class);
                newPlayerIntent.putExtra("name", playerName);
                newPlayerIntent.addFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(newPlayerIntent);
                finish();
            }
        });
        builder.setNegativeButton("Nah", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ScoresActivity.super.onBackPressed();
                finish();
            }
        });
        builder.show();
    }
}
