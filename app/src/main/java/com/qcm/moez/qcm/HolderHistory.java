package com.qcm.moez.qcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Moez on 07/11/2017.
 */

public class HolderHistory extends RecyclerView.ViewHolder {

    public int currentItem;
    public ImageView itemImage;
    public TextView itemTitle;
    public TextView itemResult;
    public TextView itemDate;
    public TextView itemEns;
    static Context context;
    SharedPreferences sharedPref;

    public HolderHistory(View itemView) {
        super(itemView);
        this.context = context;
        itemImage = (ImageView)itemView.findViewById(R.id.item_image);
        itemTitle = (TextView)itemView.findViewById(R.id.item_title);
        itemResult = (TextView)itemView.findViewById(R.id.item_result);
        itemDate = (TextView)itemView.findViewById(R.id.item_date);
        itemEns = (TextView)itemView.findViewById(R.id.item_ens);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int position = getAdapterPosition();
                System.out.println("POSITION ===== "+position);

                //Snackbar.make(v,ids.get(position),
                //      Snackbar.LENGTH_LONG)
                //    .setAction("Action", null).show();

            }
        });
    }
}