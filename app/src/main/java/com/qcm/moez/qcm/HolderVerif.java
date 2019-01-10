package com.qcm.moez.qcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Moez on 10/11/2017.
 */

public class HolderVerif  extends RecyclerView.ViewHolder {

    public int currentItem;
    public ImageView itemImage;
    public TextView itemNomPre;
    public Switch itemEtat;
    public TextView itemType;
    public TextView itemId;
    CardView cardView;
    static Context context;
    SharedPreferences sharedPref;

    public HolderVerif(final View itemView) {
        super(itemView);
        this.context = context;
        itemImage = (ImageView)itemView.findViewById(R.id.item_image);
        itemNomPre = (TextView)itemView.findViewById(R.id.item_nompre);
        itemType = (TextView)itemView.findViewById(R.id.item_type);
        itemEtat = (Switch) itemView.findViewById(R.id.switchEtat);
        itemId = (TextView)itemView.findViewById(R.id.item_login);
        cardView = (CardView)itemView.findViewById(R.id.card_viewUser);

        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int position = getAdapterPosition();
                System.out.println("POSITION ===== "+position);

                //Snackbar.make(v,ids.get(position),
                //      Snackbar.LENGTH_LONG)
                //    .setAction("Action", null).show();

            }
        });*/


    }
}