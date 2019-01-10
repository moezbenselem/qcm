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
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by Moez on 25/10/2017.
 */

public class HistoryFragment extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    String id_ens;
    Bundle bundle;

    private ArrayList<String> ids,Titles,Resultats,nomEns,Dates;
    SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.history_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        getView().findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        recyclerView =
                (RecyclerView) getView().findViewById(R.id.recycler_view2);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ids = new ArrayList<String>();
        Titles = new ArrayList<String>();
        Resultats = new ArrayList<String>();
        nomEns = new ArrayList<String>();
        Dates = new ArrayList<String>();
        bundle = getArguments();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = "http://qcmtest.6te.net/qcm/liste_hist.php?user="+sharedPref.getString("idUser","");
        if (sharedPref.getString("Type","type").equalsIgnoreCase("0")){
            url = "http://qcmtest.6te.net/qcm/liste_hist_ens.php?ens="+sharedPref.getString("idUser","");
        }
        else
            url = "http://qcmtest.6te.net/qcm/liste_hist.php?user="+sharedPref.getString("idUser","");
        System.out.println(url);
        Ion.with(getActivity())
                .load(url)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        System.out.println("ION Consult History ");

                        if (e == null) {
                            System.out.println("result ens =====" + result);
                            System.out.println("result ens size =====" + result.size());
                            if (result.size() == 0) {
                                System.out.println("result ens size =====" + result.size());
                                getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                Toast.makeText(getContext(),"Aucune résultat trouvée !",Toast.LENGTH_LONG).show();
                            } else {



                                for (int i = 0; i < result.size(); i++) {
                                    JsonObject obj = (JsonObject) result.get(i);
                                    //Log.e("name :", "" + obj.get("name"));

                                    String nom_pre = obj.get("nom").toString().replaceAll("\"", "") + " "
                                                    + obj.get("prenom").toString().replaceAll("\"", "");
                                    String res = obj.get("result").toString().replaceAll("\"", "");
                                    String title = obj.get("titre").toString().replaceAll("\"", "");
                                    String d = obj.get("t").toString().replaceAll("\"", "");

                                    nomEns.add(nom_pre);
                                    Resultats.add(res);
                                    Titles.add(title);
                                    Dates.add(d);


                                }
                                adapter = new RecyclerHist(getContext(),Titles,Dates,nomEns,Resultats);
                                recyclerView.setAdapter(adapter);
                                getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                //System.out.println("lesss nommmssss");
                                //System.out.println(noms);

                            }
                        }
                        else {
                            getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            Toast.makeText(getContext(),"Aucune résultat trouvée !",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                });

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Historique");

    }

}
