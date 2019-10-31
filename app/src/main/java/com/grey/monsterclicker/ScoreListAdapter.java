package com.grey.monsterclicker;

import android.app.Activity;
import android.app.Person;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ScoreListAdapter extends ArrayAdapter<Player> {
    private Context mContext;
    private int mResource;


    public ScoreListAdapter(Context context, int resource, ArrayList<Player> scoresList) {
        super(context, resource, scoresList);
        this.mContext = context;
        this.mResource = resource;
    }

    static class Viewholder{
        TextView playerName;
        TextView score;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the items information
        String name =getItem(position).getName();
        String score = getItem(position).getScore();

        //create the player with the information
        Player player = new Player(name, score);

        //create the viewholder object
        Viewholder holder;

        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource,parent,false);
            holder = new Viewholder();
            holder.playerName =  (TextView) convertView.findViewById(R.id.player_name_score);
            holder.score = (TextView) convertView.findViewById(R.id.player_score);

            convertView.setTag(holder);

        }
        else{
            holder = (Viewholder) convertView.getTag();
        }


        holder.playerName.setText(player.getName());
        holder.score.setText(player.getScore());

        return convertView;
    }
}
