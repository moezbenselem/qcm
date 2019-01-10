package com.qcm.moez.qcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moez on 10/11/2017.
 */

public class CreateFragment extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String id_ens,titre , desc , quest;
    Bundle bundle;
    ListView listView;
    Button btSuiv,btGenerer;
    List<RowItem> rowItems;
    CustumLvAdapter adapter;
    EditText etNbr , etNbrQ ,etTitre , etDesc ,etQuest;
    int nbr =0 , idQcm , idQuest;
    private ArrayList<String> ids,Titles,Resultats,nomEns,Dates;
    SharedPreferences sharedPref;
    int indice=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        ids = new ArrayList<String>();
        Titles = new ArrayList<String>();
        Resultats = new ArrayList<String>();
        nomEns = new ArrayList<String>();
        Dates = new ArrayList<String>();
        bundle = getArguments();
        final SharedPreferences.Editor editor;
        final RelativeLayout rl = (RelativeLayout)getView().findViewById(R.id.rl_QCM);
        btSuiv = (Button)getView().findViewById(R.id.btSuivCreation);
        btGenerer = (Button)getView().findViewById(R.id.btGenerer);
        listView = (ListView)getView().findViewById(R.id.listView);
        listView.setItemsCanFocus(true);
        etNbr = (EditText)getView().findViewById(R.id.etNbr);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());


        btGenerer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int n = Integer.parseInt(etNbr.getText().toString());

                    rowItems = new ArrayList<RowItem>();
                    for (int i = 0; i < n; i++) {

                        RowItem item = new RowItem();
                        rowItems.add(item);


                    }

                    adapter = new CustumLvAdapter(getContext(),
                            R.layout.listview_sugg, rowItems);
                    //adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        System.out.println("TAAAAAAGGGG ==== "+btSuiv.getTag().toString());
        editor = sharedPref.edit();
        btSuiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btSuiv.getTag().toString().equalsIgnoreCase("creation"))
                {
                    try {
                        etTitre = (EditText) getView().findViewById(R.id.etTitre);

                        etDesc = (EditText) getView().findViewById(R.id.etDesc);
                        etNbrQ = (EditText) getView().findViewById(R.id.etnbrq);

                        titre = etTitre.getText().toString();
                        titre = titre.replaceAll(" ", "(");
                        desc = etDesc.getText().toString();
                        desc = desc.replaceAll(" ", "(");
                        nbr = Integer.parseInt(etNbrQ.getText().toString());

                        id_ens = sharedPref.getString("idUser", "");
                        rl.setVisibility(View.GONE);
                        System.out.println("http://qcmtest.6te.net/qcm/insert_qcm.php?titre=\"" + titre + "\"&desc=\"" + desc + "\"&id_ens=\"" + id_ens+"\"");
                    }
                    catch (Exception exep){
                        exep.printStackTrace();
                    }
                    Ion.with(getContext())
                            .load("http://qcmtest.6te.net/qcm/insert_qcm.php?titre=\"" + titre + "\"&desc=\"" + desc + "\"&id_ens=\"" + id_ens+"\"")
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (e == null) {
                                        String r = result.get("last_id").toString().replaceAll("\"", "");
                                        idQcm = Integer.parseInt(r);

                                        editor.putInt("idq",idQcm);
                                        editor.apply();
                                        System.out.println("IDQCM == "+idQcm);
                                    }
                                    else {
                                        e.printStackTrace();
                                    }
                                }
                            });

                    btSuiv.setTag("suiv");

                }
                else {
                    System.out.println("indice avant if === "+indice);

                    int nombre = Integer.parseInt(etNbrQ.getText().toString());
                    System.out.println("nombre  === "+nombre);
                    if (indice < nombre) {
                        try {
                            indice++;
                            System.out.println("indice dans if === "+indice);
                            etQuest = (EditText) getView().findViewById(R.id.etQuestion);
                            quest = etQuest.getText().toString();
                            quest = quest.replaceAll(" ", "(");
                            id_ens = sharedPref.getString("idUser", "");
                            int num_rep = 0;
                            for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                                View vi = listView.getChildAt(i);
                                EditText et = (EditText) vi.findViewById(R.id.etSugg);
                                CheckBox ch = (CheckBox) vi.findViewById(R.id.checkSugg);
                                if (ch.isChecked())
                                    num_rep++;
                            }


                            System.out.println("http://qcmtest.6te.net/qcm/insert_quest.php?quest=\"" + quest + "\"&id_exam=\"" + idQcm + "\"&num_rep=\"" + num_rep + "\"");
                            Ion.with(getContext())
                                    .load("http://qcmtest.6te.net/qcm/insert_quest.php?quest=\"" + quest + "\"&id_exam=" + idQcm + "&num_rep=" + num_rep + "")
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result) {
                                            if (e == null) {
                                                String r = result.get("last_id").toString().replaceAll("\"", "");
                                                idQuest = Integer.parseInt(r);

                                                editor.putInt("idquset", idQuest);
                                                editor.apply();
                                                System.out.println("IDQUEST == " + idQuest);
                                            } else {
                                                e.printStackTrace();
                                            }
                                        }
                                    });


                            for (int i = 0; i < listView.getAdapter().getCount(); i++) {

                                View vi = listView.getChildAt(i);
                                EditText et = (EditText) vi.findViewById(R.id.etSugg);
                                CheckBox ch = (CheckBox) vi.findViewById(R.id.checkSugg);
                                if (ch.isChecked()) {
                                    //System.out.println(et.getText().toString());
                                    System.out.println("http://qcmtest.6te.net/qcm/insert_prop.php?id_quest=" + idQuest + "&id_exam=" + idQcm + "&type=" + 1 + "&rep=" + et.getText().toString());
                                    Ion.with(getContext())
                                            .load("http://qcmtest.6te.net/qcm/insert_prop.php?id_quest=" + idQuest + "&id_exam=" + idQcm + "&type=" + 1 + "&rep=" + et.getText().toString())
                                            .asJsonObject()
                                            .setCallback(new FutureCallback<JsonObject>() {
                                                @Override
                                                public void onCompleted(Exception e, JsonObject result) {
                                                    if (e == null) {
                                                        System.out.println("effectuee !!!!");
                                                    } else {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                } else {

                                    System.out.println("http://qcmtest.6te.net/qcm/insert_prop.php?id_quest=" + idQuest + "&id_exam=" + idQcm + "&type=" + 0 + "&rep=" + et.getText().toString());
                                    Ion.with(getContext())
                                            .load("http://qcmtest.6te.net/qcm/insert_prop.php?id_quest=" + idQuest + "&id_exam=" + idQcm + "&type=" + 0 + "&rep=" + et.getText().toString())
                                            .asJsonObject()
                                            .setCallback(new FutureCallback<JsonObject>() {
                                                @Override
                                                public void onCompleted(Exception e, JsonObject result) {
                                                    if (e == null) {
                                                        System.out.println("effectuee !!!!");
                                                    } else {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                }

                                //FragmentTransaction ft = getFragmentManager().beginTransaction();
                                //ft.detach(CreateFragment.this).attach(CreateFragment.class.newInstance()).commit();


                                    etQuest.setText("");
                                    etNbr.setText("");



                            }
                            adapter = null;
                            //adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (indice == nombre)
                    {
                        Toast.makeText(getContext(),"C'est tout !",Toast.LENGTH_SHORT).show();
                        try {
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_main, CreateFragment.class.newInstance(),"findThisFragment")
                                    .addToBackStack(null)
                                    .commit();
                        }catch (Exception efd)
                        {
                            efd.printStackTrace();
                        }

                    }
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Creation du QCM");

    }

}
