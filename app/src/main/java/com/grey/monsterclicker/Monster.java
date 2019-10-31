package com.grey.monsterclicker;

import android.widget.ImageView;

public class Monster {
    private long health;
    private long dmg;
    private int id;
    private String name;
    private int img;
    private int lives;

    public Monster(int id, long health, int lives, int img) {
        this.health = health;
        this.dmg = health;
        this.lives = lives;
        this.img = img;
        this.id = id;

    }

    public long getDmg() {
        return dmg;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void decLives() {
        if(this.lives!=0){
            this.lives--;
            this.dmg = health;
        }
    }

    public long getHealth() {
        return this.dmg;
    }

    public void setHealth(long health) {
        this.dmg = health;
    }

    public void hitHealth(long dmg) {
        this.dmg -= dmg;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public boolean isLive(){
        if(this.dmg <= 0 && this.lives <= 0)
            return false;
        return true;
    }
}
