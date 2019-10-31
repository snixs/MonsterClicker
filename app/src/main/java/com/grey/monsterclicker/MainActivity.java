package com.grey.monsterclicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView start;
    TextView load;
    TextView options;
    TextView scores;
    String name;
    long lvl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (TextView) findViewById(R.id.start);
        load = (TextView) findViewById(R.id.load);
       // options = (TextView) findViewById(R.id.options);
        scores = (TextView) findViewById(R.id.options);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.apply();
                Intent newGameIntent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(newGameIntent);
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newGameIntent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(newGameIntent);
            }
        });

//        options.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent newGameIntent = new Intent(MainActivity.this, OptionsActivity.class);
//                startActivity(newGameIntent);
//            }
//        });
        scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                lvl = sharedPref.getLong("lvl", 1);

                final EditText userNameGet = new EditText(MainActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("see scores")
                        .setMessage("see scores")
                        .setView(userNameGet)
                        .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!userNameGet.getText().toString().matches(""))
                                {
                                  name = String.valueOf(userNameGet.getText());
                                    Intent newGameIntent = new Intent(MainActivity.this, OptionsActivity.class);
                                    Intent scoresIntent = new Intent(MainActivity.this, ScoresActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", name);
                                    bundle.putString("score", Long.toString(lvl));
                                    scoresIntent.putExtras(bundle);
                                    scoresIntent.addFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(scoresIntent);
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create();
                dialog.show();

            }
        });
    }
}
