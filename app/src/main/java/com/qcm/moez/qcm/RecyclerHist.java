package com.qcm.moez.qcm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Moez on 07/11/2017.
 */

public class RecyclerHist extends RecyclerView.Adapter {

    private Context context;
    HolderHistory viewHolder;
    private ArrayList<String> Title,Dates,nomEns,Resultats;

    private int image = R.drawable.logo_quest;

    public RecyclerHist(Context context,ArrayList<String> Title, ArrayList<String> dates, ArrayList<String> idEns,ArrayList<String> resultats) {
        this.context = context;

        this.nomEns = idEns;
        this.Title = Title;
        this.Resultats = resultats;
        this.Dates = dates;


    }

    @Override
    public HolderHistory onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_result_hist, viewGroup, false);
        viewHolder = new HolderHistory(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aaviewHolder, int i) {
        System.out.println(Title);


        viewHolder.itemTitle.setText(Title.get(i));
        viewHolder.itemResult.setText(Resultats.get(i));
        viewHolder.itemEns.setText(nomEns.get(i));
        viewHolder.itemDate.setText(Dates.get(i));
        viewHolder.itemImage.setImageResource(image);
    }

    @Override
    public int getItemCount() {
        return Resultats.size();
    }
}
