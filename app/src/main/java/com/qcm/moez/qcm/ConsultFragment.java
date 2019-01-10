package com.qcm.moez.qcm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by Moez on 25/10/2017.
 */

public class ConsultFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    private ArrayList<String> ids;
    private ArrayList<String> noms;
    private ArrayList<String> prenoms;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        return inflater.inflate(R.layout.consult_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        getView().findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        recyclerView =
                (RecyclerView) getView().findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ids = new ArrayList<String>();
        noms = new ArrayList<String>();
        prenoms = new ArrayList<String>();
        String info="";
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPref.getString("Type", "").equalsIgnoreCase("0"))
            info = "?idEns="+sharedPref.getString("idUser", "");
        Ion.with(getActivity())
                .load("http://qcmtest.6te.net/qcm/liste_ens.php"+info)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        System.out.println("ION Consult liste ens");

                        if (e == null) {
                            System.out.println("result ens =====" + result);
                            System.out.println("result ens size =====" + result.size());
                            if (result.size() == 0) {
                                System.out.println("result ens size =====" + result.size());
                            } else {



                                for (int i = 0; i < result.size(); i++) {
                                    JsonObject obj = (JsonObject) result.get(i);
                                    //Log.e("name :", "" + obj.get("name"));

                                    String id = obj.get("id").toString().replaceAll("\"", "");
                                    String nom = obj.get("nom").toString().replaceAll("\"", "");
                                    String prenom = obj.get("prenom").toString().replaceAll("\"", "");

                                    ids.add(id);
                                    noms.add(nom);
                                    prenoms.add(prenom);

                                }
                                adapter = new RecyclerAdapter(ids,prenoms,noms,getContext());
                                recyclerView.setAdapter(adapter);
                                getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                System.out.println("lesss nommmssss");
                                //System.out.println(noms);

                            }
                        }
                        else
                            e.printStackTrace();
                    }
                });


        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Liste des Enseignants");


    }







}
