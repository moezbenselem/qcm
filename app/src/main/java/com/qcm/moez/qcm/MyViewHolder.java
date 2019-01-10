package com.qcm.moez.qcm;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

public class MyViewHolder extends RecyclerView.ViewHolder {

    public int currentItem;
    public ImageView itemImage;
    public TextView itemTitle;
    public TextView itemDetail;
    static Context context;

    public MyViewHolder(View itemView , final Context context, final ArrayList<String> listQcm) {
        super(itemView);
        this.context = context;
        itemImage = (ImageView)itemView.findViewById(R.id.item_image);
        itemTitle = (TextView)itemView.findViewById(R.id.item_title);
        itemDetail =
                (TextView)itemView.findViewById(R.id.item_detail);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int position = getAdapterPosition();
                //Toast.makeText(context,"MyViewHolder",Toast.LENGTH_SHORT).show();



                    try {
                        Bundle bundle = new Bundle();

                        bundle.putInt("id_exam",Integer.parseInt(listQcm.get(position)));
                        System.out.println("qqqqqqqqqqqqq ==  "+listQcm.get(position));
                        //Toast.makeText(context,"ID QCM == "+listQcm.get(position),Toast.LENGTH_LONG).show();


                        Fragment fragment = new QuestFragment();
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = ((MainActivity) MyViewHolder.context).getSupportFragmentManager();
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