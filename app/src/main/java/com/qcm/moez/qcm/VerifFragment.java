package com.qcm.moez.qcm;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by Moez on 10/11/2017.
 */

public class VerifFragment extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    String id_ens;
    Bundle bundle;
    Button btsauv;

    ArrayList<String> ids,noms,prenoms,psw,type,etat;
    SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.verif_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        getView().findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        recyclerView =
                (RecyclerView) getView().findViewById(R.id.recycler_view2);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        bundle = getArguments();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = "http://qcmtest.6te.net/qcm/liste_ens_login.php";
        System.out.println(url);
        ids = new ArrayList<String>();
        noms = new ArrayList<String>();
        prenoms = new ArrayList<String>();

        psw = new ArrayList<String>();
        type = new ArrayList<String>();
        etat = new ArrayList<String>();
        btsauv = (Button)getView().findViewById(R.id.btEnregist);

        Ion.with(this)
                .load("http://qcmtest.6te.net/qcm/liste_ens_login.php?db=qcm&user="+sharedPref.getString("idUser",""))
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

                                    String idR = obj.get("id").toString().replaceAll("\"", "");
                                    String nomR = obj.get("nom").toString().replaceAll("\"", "");
                                    String prenomR = obj.get("prenom").toString().replaceAll("\"", "");
                                    String loginR = obj.get("id").toString().replaceAll("\"", "");
                                    String mdpR = obj.get("psw").toString().replaceAll("\"", "");
                                    String etatR = obj.get("etat").toString().replaceAll("\"", "");
                                    String typeR = obj.get("type").toString().replaceAll("\"", "");

                                    ids.add(idR);
                                    noms.add(nomR +" "+ prenomR);
                                    prenoms.add(prenomR);
                                    psw.add(mdpR);
                                    etat.add(etatR);
                                    type.add(typeR);

                                }
                                adapter = new RecyclerVerif(getContext(),ids,noms,type,etat,btsauv);
                                recyclerView.setAdapter(adapter);
                                getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                                System.out.println("lesss nommmssss");
                                //System.out.println(noms);

                            }
                        } else
                        {
                            e.printStackTrace();

                        }
                    }
                });




        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Vérification");

    }

}
