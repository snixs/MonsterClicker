package com.grey.monsterclicker;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    AnimationDrawable ad;
    ObjectAnimator animator;
    GameView gameView;

    //Monsters
    ArrayList<Monster> monsters;
    Monster currentMonster;
    int monsterIndex;

    //thread
    Thread mThread;
    Thread mHelperthread;

    // Values
    long dot;
    long dpc;
    long dps;
    long shopDpc;
    long shopDps;


    // Views
    TextView dotsView;
    TextView dpsAndDpcView;
    ImageView dotBtn;

    Button shopDpcBtn;
    Button shopDpsBtn;
  //  volatile boolean run = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initialize();
        loadPref();

    }

    @Override
    protected void onStart() {
        super.onStart();
        gameThread();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mThread.interrupt();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mThread == null) {
            gameThread();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mThread.interrupt();
        mThread=null;

    }

    public void gameThread(){

        mThread = new Thread(){
            public void run(){
                try{
                    while(!isInterrupted()){
                        Thread.sleep(1000);

                        GameActivity.this.runOnUiThread(new Runnable(){
                            public void run() {
                                dot += dps;
                                dotsView.setText(dot + " Dots");
                                if(!animator.isPaused())
                                    isHit(dps);
                                Log.e("Health", "Lives count: " + currentMonster.getLives());
                                Log.e("Health", "Dmg count: " + currentMonster.getDmg());
                                savePref("dot", dot);
                                savePref("monsterLife", currentMonster.getHealth());
                            }

                        });

                    }

                }catch(InterruptedException e){
                    e.printStackTrace();
                }

            }

        };

        mThread.start();
    }

    public void initialize(){

        //monsters set init
        monsters = new ArrayList<>();
        monsters.add(new Monster(1,15,8, R.drawable.zombie));
        monsters.add(new Monster(3,25,7, R.drawable.spider));
        monsters.add(new Monster(2,35,6, R.drawable.imp));
        monsters.add(new Monster(4,45,6, R.drawable.demon));
        monsters.add(new Monster(5,25,5, R.drawable.zombie));
        monsters.add(new Monster(6,35,5, R.drawable.spider));
        monsters.add(new Monster(7,45,4, R.drawable.imp));
        monsters.add(new Monster(8,55,4, R.drawable.demon));
        monsters.add(new Monster(9,150,1, R.drawable.demon));
        monsters.add(new Monster(10,150,2, R.drawable.demon));

        //var init
        monsterIndex = 0;
        dot = 0; // dot points
        dpc = 1; //dmg per click
        dps = 0; //dmg per sec
        shopDps = 250; //+dps shop cost
        shopDpc = 15; //+dpc shop cost

        // Views init
        dotsView = (TextView) findViewById(R.id.dotsView);
        shopDpcBtn = (Button) findViewById(R.id.shopDpc);
        shopDpsBtn = (Button) findViewById(R.id.shopDps);
        dpsAndDpcView = (TextView) findViewById(R.id.dpsAndDpcView);

        dotBtn = (ImageView) findViewById(R.id.dotBtn);
        dotBtn.setImageDrawable(null);
        ViewTreeObserver viewTreeObserver = dotBtn.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    spawnMonster();
                    animate();
                    dotBtn.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    public void loadPref(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        dot = sharedPref.getLong("dot", 0);

        dpc = sharedPref.getLong("dpc", 1);

        dps = sharedPref.getLong("dps", 0);

        shopDps = sharedPref.getLong("shopDps", 250);

        shopDpc = sharedPref.getLong("shopDpc", 15);

        dotsView.setText(dot + " Dots");
        shopDpcBtn.setText("Dpc: +2 | " + shopDpc + "D");
        dpsAndDpcView.setText(dps + " Dps | " + dpc + "Dpc");
        shopDpsBtn.setText("Dps: +1 | " + shopDps + "D");

        long monsterLife = sharedPref.getLong("monsterLife", 0);
        Log.e("Health", "monsterLife is: " + monsterLife);
      //  currentMonster.setHealth(monsterLife);

    }

    public void savePref(String key, long value){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Editor editor = sharedPref.edit();
        editor.putLong(key, value);
        editor.apply();

    }


    private void animate() {
        animator = new ObjectAnimator();
        animator = ObjectAnimator.ofFloat(dotBtn,"y", 170);
        animator.setDuration(600);
        animator.setRepeatCount(Animation.INFINITE);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.start();
    }

    public void dotBtn(View v){
        isHit(dpc);
        dot += dpc;
        dotsView.setText(dot + " Dots");
        savePref("dot", dot);
    }

    public void shopDpc(View v){
        if(dot >= shopDpc){
            dot -= shopDpc;
            dpc += 2;

            shopDpc *= 1.2;

            shopDpcBtn.setText("Dpc: +2 | " + shopDpc + "D");
            dpsAndDpcView.setText(dps + "Dps | " + dpc + "Dpc");
            dotsView.setText(dot + " Dots");

            savePref("shopDpc", shopDpc);
            savePref("dpc", dpc);
            savePref("dot", dot);

        }else{
            Toast.makeText(this, "You need more dots!", Toast.LENGTH_SHORT).show();

        }

    }

    public void shopDps(View v){
        if(dot >= shopDps){
            dot -= shopDps;
            dps += 1;

            shopDps *= 1.5;

            shopDpsBtn.setText("Dps: +1 | " + shopDps + "D");
            dpsAndDpcView.setText(dps + " Dps | " + dpc + "Dpc");
            dotsView.setText(dot + " Dots");

            savePref("shopDps", shopDps);
            savePref("dps", dps);
            savePref("dot", dot);

        }else{
            Toast.makeText(this, "You need more dots!", Toast.LENGTH_SHORT).show();

        }

    }

    public void isHit(long dmg){


        if(currentMonster.getHealth()<=0){

            Log.e("Life", "getHealth Life is: " + currentMonster.getHealth());
            Anim anima = new Anim((AnimationDrawable) getResources().getDrawable(
                    R.drawable.death_fade)) {
                @Override
                public void onAnimationFinish() {
                    currentMonster.decLives();

                    if (!currentMonster.isLive()) {
                        if(monsterIndex >= monsters.size()-1)
                        {
                            endGame();

                        }else{
                            monsterIndex++;
                            spawnMonster();
                            savePref("lvl", monsterIndex+1);
                            animator.start();
                        }
                    }
                    else {
                        dotBtn.setImageResource(currentMonster.getImg());
                        animator.start();
                    }
                    dotBtn.setEnabled(true);
                }

                @Override
                public void onAnimationStart() {
                    animator.pause();
                }
            };
            dotBtn.setImageDrawable(anima);
            dotBtn.setEnabled(false);
            anima.start();
        }
        else{
            currentMonster.hitHealth(dmg);
        }

    }


    public void spawnMonster(){
        currentMonster = monsters.get(monsterIndex);
        dotBtn.setImageResource(currentMonster.getImg());

    }

    private void endGame() {
        Log.e("Game", "IN END GAME");
        dotBtn.setImageResource(R.drawable.demon_f4);
        animator.end();
        mThread.interrupt();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("You Beat The Game");
        alert.setCancelable(false);
        alert.setMessage("You scored " + dot + " points!");
        alert.setPositiveButton("Nice!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent endIntent = new Intent(GameActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putString("name", mPlayerName);
//                bundle.putString("score", Integer.toString(mScore));
                endIntent.putExtras(bundle);
                endIntent.addFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(endIntent);
                finish();
            }
        });
        alert.show();

    }
}
