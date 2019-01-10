package com.qcm.moez.qcm;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Moez on 27/10/2017.
 */

public class RecyclerAdapterQcm extends RecyclerView.Adapter {

    private Context context;
    MyViewHolder viewHolder;
    private ArrayList<String> ids,QcmTitle,QcmDesc,idEns;

    private int image = R.drawable.logo_quest;

    public RecyclerAdapterQcm(Context context, ArrayList<String> ids, ArrayList<String> qcmTitle, ArrayList<String> qcmDesc, ArrayList<String> idEns) {
        this.context = context;
        this.ids = ids;
        QcmTitle = qcmTitle;
        QcmDesc = qcmDesc;
        this.idEns = idEns;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        viewHolder = new MyViewHolder(v,context,ids);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aaviewHolder, int i) {
        System.out.println(QcmTitle);


        viewHolder.itemTitle.setText(QcmTitle.get(i));
        viewHolder.itemDetail.setText(QcmDesc.get(i));
        viewHolder.itemImage.setImageResource(image);
    }

    @Override
    public int getItemCount() {
        return ids.size();
    }
}
