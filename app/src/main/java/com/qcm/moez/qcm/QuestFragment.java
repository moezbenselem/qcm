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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Moez on 02/11/2017.
 */

public class QuestFragment extends Fragment {

    String quest;
    int exam;
    Bundle bundle;
    int resultat = 0;
    int n = 0;
    SharedPreferences sharedPref;
    private ArrayList<String> ids, QcmTitle, QcmDesc, idEns;
    static ArrayList<Question> listeQuest;
    static ArrayList<Proposition> listeProp;
    LinearLayout linear, linear2;
    int i = 0;
    TextView tvQuest;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.consult_quest, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //getView().findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

        tvQuest = (TextView) getView().findViewById(R.id.tvQuest);
        ids = new ArrayList<String>();
        QcmDesc = new ArrayList<String>();
        QcmTitle = new ArrayList<String>();

        listeQuest = new ArrayList<Question>();
        listeProp = new ArrayList<Proposition>();

        bundle = getArguments();
        exam = bundle.getInt("id_exam");
        quest = bundle.getString("id_quest");
        final Button btSuiv = (Button) getView().findViewById(R.id.btSuivCreation);
        linear = (LinearLayout) getView().findViewById(R.id.layoutRep);
        linear.setOrientation(LinearLayout.VERTICAL);

        linear2 = (LinearLayout) getView().findViewById(R.id.mainLayout);
        linear2.setOrientation(LinearLayout.VERTICAL);

        getQuests();
        //showQuest(i);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        n = sharedPref.getInt("nbr", 100);
        System.out.println("TAILLLEEEEE lbarraa +++++++++++ " + n);
        btSuiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btSuiv.getText().toString().equalsIgnoreCase("Terminer !")) {

                    saveResult();

                } else {
                    if (i < n - 1) {
                        try {
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
                            showQuest(i);

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
                        //saveResult();
                        linear2.findViewById(R.id.labelques).setVisibility(View.GONE);
                        linear2.findViewById(R.id.labelRep).setVisibility(View.GONE);
                        btSuiv.setText("Terminer !");

                    }
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Répendez au Questions");


    }

    public void saveResult(){

        try {
            String r = resultat + "/" + n;
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy|HH:mm:ss");
            String date = df.format(Calendar.getInstance().getTime());
            final StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://qcmtest.6te.net/qcm/insert_result.php?result=" + r + "&id_ens=" + sharedPref.getString("idEns", "") + "&id_user=" + sharedPref.getString("idUser", "") + "&id_exam=" + exam + "&date=" + date + "",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.indexOf("/div>") != -1)
                                response = response.substring(response.lastIndexOf("/div>") + 5);

                            System.out.println("result === \n" + response);

                            try {

                                JSONObject result = new JSONObject(response);
                                String r = result.get("success").toString().replaceAll("\"", "");
                                if (r.equalsIgnoreCase("true")) {
                                    Toast.makeText(getActivity(), "EFFECTUER !", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), "ECHOUEE !", Toast.LENGTH_SHORT).show();

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new Hashtable<>();

                    return params;
                }
            };

            {
                int socketTimeout = 30000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }


    }

    public void getQuests() {

        try {
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://qcmtest.6te.net/qcm/liste_quest_qcm.php?exam=" + exam,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.indexOf("/div>") != -1)
                                response = response.substring(response.lastIndexOf("/div>") + 5);

                            System.out.println("result === \n" + response);

                            try {

                                JSONArray result = new JSONArray(response);

                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject obj = (JSONObject) result.get(i);
                                    //Log.e("name :", "" + obj.get("name"));

                                    int id = Integer.parseInt(obj.get("id_quest").toString().replaceAll("\"", ""));
                                    int id_exam = Integer.parseInt(obj.get("id_exam").toString().replaceAll("\"", ""));
                                    int num_rep = Integer.parseInt(obj.get("num_rep").toString().replaceAll("\"", ""));

                                    String quest = obj.get("quest").toString().replaceAll("\"", "");


                                    Question q = new Question(id, id_exam, num_rep, quest);
                                    listeQuest.add(q);
                                    showQuest(0);
                                    listeProp.clear();

                                }

                                System.out.println("les questions");
                                System.out.println(listeQuest);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new Hashtable<>();

                    return params;
                }
            };

            {
                int socketTimeout = 30000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }


    }


    public void getPropo(int id_quest) {
        getView().findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://qcmtest.6te.net/qcm/liste_prop_quest.php?exam=" + exam + "&quest=" + id_quest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.indexOf("/div>") != -1)
                            response = response.substring(response.lastIndexOf("/div>") + 5);

                        System.out.println("result === \n" + response);

                        try {

                            JSONArray result = new JSONArray(response);
                            listeProp.clear();
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject obj = (JSONObject) result.get(i);

                                int id = Integer.parseInt(obj.get("id_rep").toString().replaceAll("\"", ""));
                                int id_quest = Integer.parseInt(obj.get("id_quest").toString().replaceAll("\"", ""));
                                int id_exam = Integer.parseInt(obj.get("id_exam").toString().replaceAll("\"", ""));
                                int type = Integer.parseInt(obj.get("type").toString().replaceAll("\"", ""));

                                String propo = obj.get("reponse").toString().replaceAll("\"", "");

                                Proposition p = new Proposition(id, id_quest, id_exam, type, propo);
                                listeProp.add(p);

                            }

                            linear.removeAllViewsInLayout();
                            RecyclerView.LayoutParams lparams = new RecyclerView.LayoutParams(
                                    RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);

                            for (int x = 0; x < listeProp.size(); x++) {
                                CheckBox checkBox = new CheckBox(getContext());

                                checkBox.setLayoutParams(lparams);
                                checkBox.setText(listeProp.get(x).propo);
                                checkBox.setId(listeProp.get(x).id);
                                checkBox.setTag(listeProp.get(x).type);
                                linear.addView(checkBox);

                            }
                            System.out.println("les propositions");
                            System.out.println(listeProp);
                            getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
                        getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<>();

                return params;
            }
        };

        {
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }

    }



    public void showQuest(int index) {

        try {

            System.out.println("size list quest from showQuest = "+listeQuest.size());
            Question q = listeQuest.get(index);
            tvQuest.setText(q.quest);
            getPropo(q.id);
        }catch (Exception e){
            e.printStackTrace();
        }



    }

}
