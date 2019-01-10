package com.qcm.moez.qcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Moez on 02/11/2017.
 */

public class QuestFragment extends Fragment {

    String quest;
    int exam;
    Bundle bundle;
    int resultat=0;
    int n=0;
    SharedPreferences sharedPref;
    private ArrayList<String> ids,QcmTitle,QcmDesc,idEns;
    static ArrayList<Question> listeQuest;
    static ArrayList<Proposition> listeProp;
    LinearLayout linear,linear2;
    int i =0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        return inflater.inflate(R.layout.consult_quest,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //getView().findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);


        ids = new ArrayList<String>();
        QcmDesc = new ArrayList<String>();
        QcmTitle = new ArrayList<String>();

        listeQuest = new ArrayList<Question>();
        listeProp = new ArrayList<Proposition>();

        bundle = getArguments();
        exam=bundle.getInt("id_exam");
        quest=bundle.getString("id_quest");
        final Button btSuiv = (Button)getView().findViewById(R.id.btSuivCreation);
        linear = (LinearLayout) getView().findViewById(R.id.layoutRep);
        linear.setOrientation(LinearLayout.VERTICAL);

        linear2 = (LinearLayout) getView().findViewById(R.id.mainLayout);
        linear2.setOrientation(LinearLayout.VERTICAL);

        tratitement(i);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        n = sharedPref.getInt("nbr",100);
        System.out.println("TAILLLEEEEE lbarraa +++++++++++ "+n);
        btSuiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btSuiv.getText().toString().equalsIgnoreCase("Terminer !"))
                {

                        String r = resultat+"/"+n;
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy|HH:mm:ss");
                    String date = df.format(Calendar.getInstance().getTime());
                    System.out.println("http://qcmtest.6te.net/qcm/insert_result.php?result=" + r + "&id_ens=" + sharedPref.getString("idEns","") + "&id_user=" + sharedPref.getString("idUser","") + "&id_exam=" + exam+"&date="+date+"");

                    Ion.with(getActivity())
                            .load("http://qcmtest.6te.net/qcm/insert_result.php?result=" + r + "&id_ens=" + sharedPref.getString("idEns","") + "&id_user=" + sharedPref.getString("idUser","") + "&id_exam=" + exam+"&date="+date+"")
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (e == null) {
                                        String r = result.get("success").toString().replaceAll("\"", "");
                                        if (r.equalsIgnoreCase("true")) {
                                            Toast.makeText(getActivity(), "EFFECTUER !", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getActivity(),MainActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        } else
                                        {
                                            Toast.makeText(getActivity(), "ECHOUEE !", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    else {
                                        e.printStackTrace();
                                    }
                                }
                            });



                }else {
                    if (i < n-1) {
                        try {
/*

                        boolean test = true;
                        for (int k = 0; k < linear.getChildCount(); k++) {
                            CheckBox c = (CheckBox) linear.getChildAt(k);
                            if (c.isChecked()) {
                                if (Integer.parseInt(c.getTag().toString()) != 1) {
                                    test = false;
                                }

                            }

                            if (test)
                                resultat++;
                        }
*/
                            int k = 0;
                            boolean test = true;
                            while (test && k < linear.getChildCount()) {
                                CheckBox c = (CheckBox) linear.getChildAt(k);
                                k++;
                                if (c.isChecked()) {
                                    test = Integer.parseInt(c.getTag().toString()) == 1;
                                }
                            }

                            if (test == true)
                                resultat++;

                            i++;
                            tratitement(i);

                        } catch (Exception ec) {
                            ec.printStackTrace();
                        }
                    } else {
                        int k = 0;
                        boolean test = true;
                        while (test && k < linear.getChildCount()) {
                            CheckBox c = (CheckBox) linear.getChildAt(k);
                            k++;
                            if (c.isChecked()) {
                                test = Integer.parseInt(c.getTag().toString()) == 1;
                            }
                        }

                        if (test == true)
                            resultat++;

                        i++;
                        TextView tv = (TextView) linear2.findViewById(R.id.tvQuest);
                        tv.setText("C'est tout !\nVous avez réponder correctement à " + resultat + " question du " + n);
                        View lay = linear2.findViewById(R.id.layoutRep);
                        linear2.removeView(lay);
                        btSuiv.setText("Terminer !");

                    }
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Répendez au Questions");


    }


public void tratitement(int x){
    System.out.println("resultaaaaaaaatttt ====== "+resultat);
    System.out.println("http://qcmtest.6te.net/qcm/liste_quest_qcm.php?exam="+exam);
    Ion.with(getActivity())
            .load("http://qcmtest.6te.net/qcm/liste_quest_qcm.php?exam="+exam)
            .asJsonArray()
            .setCallback(new FutureCallback<JsonArray>() {
                @Override
                public void onCompleted(Exception e, JsonArray result) {
                    System.out.println("ION Liste Quest");

                    try
                    {
                        if (e == null) {
                            System.out.println("result quest =====" + result);
                            System.out.println("result quest size =====" + result.size());
                            if (result.size() == 0) {
                                System.out.println("result equest size =====" + result.size());
                            } else {

                                for (int i = 0; i < result.size(); i++) {
                                    JsonObject obj = (JsonObject) result.get(i);

                                    int id = Integer.parseInt(obj.get("id_quest").toString().replaceAll("\"", ""));
                                    int id_exam = Integer.parseInt(obj.get("id_exam").toString().replaceAll("\"", ""));
                                    int num_rep = Integer.parseInt(obj.get("num_rep").toString().replaceAll("\"", ""));;
                                    String quest = obj.get("quest").toString().replaceAll("\"", "");;

                                    Question q = new Question(id,id_exam,num_rep,quest);
                                    listeQuest.add(q);

                                }
                                //getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                System.out.println("lesss quest");
                                System.out.println(listeQuest);

                                if(i==0) {
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putInt("nbr", listeQuest.size());
                                    editor.apply();
                                    System.out.println("TAILLLEEEEE lde5l +++++++++++ "+n);
                                }
                                TextView tvQuest = (TextView) getView().findViewById(R.id.tvQuest);
                                tvQuest.setText(listeQuest.get(i).quest);

                                listeProp.clear();
                                System.out.println("http://qcmtest.6te.net/qcm/liste_prop_quest.php?exam="+exam+"&quest="+listeQuest.get(i).id);
                                Ion.with(getActivity())
                                        .load("http://qcmtest.6te.net/qcm/liste_prop_quest.php?exam="+exam+"&quest="+listeQuest.get(i).id)
                                        .asJsonArray()
                                        .setCallback(new FutureCallback<JsonArray>() {
                                            @Override
                                            public void onCompleted(Exception e, JsonArray result) {
                                                System.out.println("ION Liste Rep");

                                                if (e == null) {
                                                    System.out.println("result quest =====" + result);
                                                    System.out.println("result quest size =====" + result.size());
                                                    if (result.size() == 0) {
                                                        System.out.println("result equest size =====" + result.size());
                                                    } else {

                                                        for (int i = 0; i < result.size(); i++) {
                                                            JsonObject obj = (JsonObject) result.get(i);

                                                            int id = Integer.parseInt(obj.get("id_rep").toString().replaceAll("\"", ""));
                                                            int id_quest = Integer.parseInt(obj.get("id_quest").toString().replaceAll("\"", ""));
                                                            int id_exam = Integer.parseInt(obj.get("id_exam").toString().replaceAll("\"", ""));
                                                            int type = Integer.parseInt(obj.get("type").toString().replaceAll("\"", ""));;
                                                            String propo = obj.get("reponse").toString().replaceAll("\"", "");

                                                            Proposition p = new Proposition(id,id_quest,id_exam,type,propo);
                                                            listeProp.add(p);

                                                        }
                                                        getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                                        System.out.println("lesss repp");
                                                        System.out.println(listeProp);


                                                        linear.removeAllViewsInLayout();
                                                        RecyclerView.LayoutParams lparams = new RecyclerView.LayoutParams(
                                                                RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);

                                                        for(int x =0; x<listeProp.size();x++) {
                                                            CheckBox checkBox = new CheckBox(getContext());

                                                            checkBox.setLayoutParams(lparams);
                                                            checkBox.setText(listeProp.get(x).propo);
                                                            checkBox.setId(listeProp.get(x).id);
                                                            checkBox.setTag(listeProp.get(x).type);
                                                            linear.addView(checkBox);

                                                        }




                                                    }
                                                }
                                                else {
                                                    getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                                    //Toast.makeText(getContext(),"Aucun QCM trouvée pour ce Enseignant !",Toast.LENGTH_LONG).show();
                                                    e.printStackTrace();
                                                }
                                            }
                                        });


                            }
                        }
                        else {
                            getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            Toast.makeText(getContext(),"Aucun QCM trouvée pour ce Enseignant !",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }catch (Exception except){
                        except.printStackTrace();
                    }
                }
            });



}




}
