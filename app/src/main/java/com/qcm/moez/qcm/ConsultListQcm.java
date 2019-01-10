package com.qcm.moez.qcm;

import android.os.Bundle;
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

public class ConsultListQcm extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    String id_ens;
    Bundle bundle;

    private ArrayList<String> ids,QcmTitle,QcmDesc,idEns;


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
        QcmDesc = new ArrayList<String>();
        QcmTitle = new ArrayList<String>();

        bundle = getArguments();
        id_ens=bundle.getString("id_ens");
        System.out.println("http://qcmtest.6te.net/qcm/liste_exam_by_ens.php?db=qcm&ens="+id_ens);
        Ion.with(getActivity())
                .load("http://qcmtest.6te.net/qcm/liste_exam_by_ens.php?db=qcm&ens="+id_ens)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        System.out.println("ION COnsultListQcm");

                        if (e == null) {
                            System.out.println("result ens =====" + result);
                            System.out.println("result ens size =====" + result.size());
                            if (result.size() == 0) {
                                System.out.println("result ens size =====" + result.size());
                            } else {



                                for (int i = 0; i < result.size(); i++) {
                                    JsonObject obj = (JsonObject) result.get(i);
                                    //Log.e("name :", "" + obj.get("name"));

                                    String id = obj.get("id_exam").toString().replaceAll("\"", "");
                                    String desc = obj.get("discription").toString().replaceAll("\"", "");
                                    String title = obj.get("titre_exam").toString().replaceAll("\"", "");

                                    ids.add(id);
                                    QcmDesc.add(desc);
                                    QcmTitle.add(title);

                                }
                                adapter = new RecyclerAdapterQcm(getContext(),ids,QcmTitle,QcmDesc,ids);
                                recyclerView.setAdapter(adapter);
                                getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                System.out.println("lesss nommmssss");
                                //System.out.println(noms);

                            }
                        }
                        else {
                            getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            Toast.makeText(getContext(),"Aucun QCM trouvée pour ce Enseignant !",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                });


        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("QCM de "+bundle.getString("nom_ens")+" "+bundle.getString("pre_ens"));


    }







}
