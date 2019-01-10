package com.qcm.moez.qcm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by Moez on 10/11/2017.
 */

public class RecyclerVerif extends RecyclerView.Adapter {

    private Context context;
    HolderVerif viewHolder;
    private ArrayList<String> Ids,Noms,Type,Etat;
    public static ArrayList<User> changedUsers;
    Button Bt;
    private int image = R.drawable.logo_quest;
    ArrayList<CompoundButton> listBt;
    Class fragmentClass = null;
    Fragment fragment = null;


    public RecyclerVerif(Context context, ArrayList<String> ids, ArrayList<String> noms, ArrayList<String> type, ArrayList<String> etat, Button bt) {
        this.context = context;
        Ids = ids;
        Noms = noms;
        Type = type;
        Etat = etat;
        Bt = bt;
        changedUsers = new ArrayList<User>();
        listBt= new ArrayList<CompoundButton>();
    }

    @Override
    public HolderVerif onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_user, viewGroup, false);
        viewHolder = new HolderVerif(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aaviewHolder, final int i) {
        //System.out.println(Title);


        viewHolder.itemId.setText(Ids.get(i));
        viewHolder.itemNomPre.setText(Noms.get(i));

        if(Type.get(i).equalsIgnoreCase("0"))
        {
            viewHolder.itemType.setText("Enseignant");
            viewHolder.itemImage.setImageResource(R.drawable.logo_ens);
        }
        else if(Type.get(i).equalsIgnoreCase("1"))
        {
            viewHolder.itemType.setText("Etudiant");
            viewHolder.itemImage.setImageResource(R.drawable.etudiant);
        }
        else if(Type.get(i).equalsIgnoreCase("3"))
        {
            viewHolder.itemType.setText("Administrateur");
            viewHolder.itemImage.setImageResource(R.drawable.logoadmin);
        }


        final int e ;
        if(Etat.get(i).equalsIgnoreCase("0"))
        {
            viewHolder.itemEtat.setText("Non Vérifier");
            viewHolder.itemEtat.setChecked(false);
            e=0;
            viewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.orange));

        }else
        {
            viewHolder.itemEtat.setText("Vérifier");
            viewHolder.itemEtat.setChecked(true);
            e=1;
            viewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green));
        }


        listBt.add(viewHolder.itemEtat);


        viewHolder.itemEtat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Bt.setEnabled(true);

                int current = listBt.indexOf(buttonView);
                ArrayList<String> idsFromChanged = new ArrayList<String>();
                int newEtat;
                if(Etat.get(current).equalsIgnoreCase("1"))
                    newEtat=0;
                else newEtat=1;
                try {
                    User u = new User(Ids.get(current),Noms.get(current),newEtat+"");
                    System.out.println("new user ::");
                    System.out.println(u.toString());

                    if(changedUsers.isEmpty())
                        changedUsers.add(u);
                    else
                    {

                        for (int i = 0;i<changedUsers.size();i++)
                        {
                            idsFromChanged.add(changedUsers.get(i).id);
                        }

                        if(idsFromChanged.contains(u.id)==false)
                            changedUsers.add(u);
                        else
                            changedUsers.remove(idsFromChanged.indexOf(u.id));
                    }


            }catch (Exception exp){
                    exp.printStackTrace();
                }
            }



        });

        Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int z =0;z<changedUsers.size();z++){

                    System.out.println(changedUsers.get(z).toString());
                    Ion.with(context)
                            .load("http://qcmtest.6te.net/qcm/update_user.php?id="+changedUsers.get(z).id+"&etat="+changedUsers.get(z).etat)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (e == null) {
                                        String r = result.get("success").toString().replaceAll("\"", "");
                                        if (r.equalsIgnoreCase("true")) {
                                            Toast.makeText(context, "EFFECTUER !", Toast.LENGTH_LONG).show();

                                        } else
                                        {
                                            Toast.makeText(context, "ECHOUEE ! Erreur Serveur !", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    else {
                                        e.printStackTrace();
                                    }
                                }
                            });

                }
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        try {
                            fragmentClass = VerifFragment.class;
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();


                    }
                };
                Handler handler = new Handler();
                handler.postDelayed(runnable, 3000);


            }
        });


    }

    @Override
    public int getItemCount() {
        return Ids.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
