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
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Moez on 27/10/2017.
 */

public class MyViewHolder2 extends RecyclerView.ViewHolder {

    public int currentItem;
    public ImageView itemImage;
    public TextView itemTitle;
    public TextView itemDetail;
    static Context context;
    SharedPreferences sharedPref;

    public MyViewHolder2(View itemView , final Context context , final ArrayList<String> listEns,final ArrayList<String> listprenoms ,final ArrayList<String> listnoms) {
        super(itemView);
        this.context = context;
        itemImage = (ImageView)itemView.findViewById(R.id.item_image);
        itemTitle = (TextView)itemView.findViewById(R.id.item_title);
        itemDetail =
                (TextView)itemView.findViewById(R.id.item_detail);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int position = getAdapterPosition();
                System.out.println("POSITION ===== "+position);
                //Toast.makeText(context,"MyViewHolder2",Toast.LENGTH_SHORT).show();


                    try {
                        System.out.println("list ensssss ids");
                        System.out.println(listEns);
                        Bundle bundle = new Bundle();
                        bundle.putString("id_ens",listEns.get(position));
                        bundle.putString("nom_ens",listnoms.get(position));
                        bundle.putString("pre_ens",listprenoms.get(position));

                        sharedPref = PreferenceManager.getDefaultSharedPreferences((MainActivity) MyViewHolder2.context);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("idEns",listEns.get(position) );
                        editor.apply();


                        Fragment fragment = new ConsultListQcm();
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = ((MainActivity) MyViewHolder2.context).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                //Snackbar.make(v,ids.get(position),
                  //      Snackbar.LENGTH_LONG)
                    //    .setAction("Action", null).show();

            }
        });
    }
}