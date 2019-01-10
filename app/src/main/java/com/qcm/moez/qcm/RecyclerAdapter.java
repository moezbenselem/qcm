package com.qcm.moez.qcm;

/**
 * Created by Moez on 25/10/2017.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter {

    private static Context context;

    private ArrayList<String> ids;
    private ArrayList<String> noms;
    private ArrayList<String> prenoms;
    MyViewHolder2 viewHolder;
    private int image = R.drawable.logo_ens;



    public RecyclerAdapter(ArrayList<String> ids , ArrayList<String>prenoms , ArrayList<String>noms , Context context) {


        this.ids = ids;
        this.prenoms = prenoms;
        this.noms = noms;
        this.context= context;
        System.out.println(ids);
        System.out.println(noms);
        System.out.println(prenoms);



    }



    @Override
    public MyViewHolder2 onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        viewHolder = new MyViewHolder2(v,context,ids,prenoms,noms);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder ahviewHolder, int i) {
        System.out.println(noms);


        viewHolder.itemTitle.setText(noms.get(i));
        viewHolder.itemDetail.setText(prenoms.get(i));
        viewHolder.itemImage.setImageResource(image);
    }


    @Override
    public int getItemCount() {
        return ids.size();
    }
}