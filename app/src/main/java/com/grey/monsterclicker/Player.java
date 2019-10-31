package com.grey.monsterclicker;

public class Player implements Comparable<Player>{
    private String name;
    private String score;

    Player(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public int compareTo(Player o) {
        if(Integer.parseInt(score) > Integer.parseInt(o.getScore())){
            return -1;
        }
        else{
            return 1;
        }
    }
}
