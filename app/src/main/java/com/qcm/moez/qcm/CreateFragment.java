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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
    EditText etNbr , etNbrQ ,etTitre , etDesc ,etQuest,et;
    int nbr =0 , idQcm , idQuest , num_rep = 0;;
    private ArrayList<String> ids,Titles,Resultats,nomEns,Dates;
    SharedPreferences sharedPref;
    int indice=0;
    SharedPreferences.Editor editor;
    RelativeLayout rl;


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

        rl = (RelativeLayout)getView().findViewById(R.id.rl_QCM);
        btSuiv = (Button)getView().findViewById(R.id.btSuivCreation);
        btGenerer = (Button)getView().findViewById(R.id.btGenerer);
        listView = (ListView)getView().findViewById(R.id.listView);
        listView.setItemsCanFocus(true);
        etNbr = (EditText)getView().findViewById(R.id.etNbr);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPref.edit();

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

                        desc = etDesc.getText().toString();

                        nbr = Integer.parseInt(etNbrQ.getText().toString());

                        id_ens = sharedPref.getString("idUser", "");
                        rl.setVisibility(View.GONE);
                    }
                    catch (Exception exep){
                        exep.printStackTrace();
                    }

                    addQCM();
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
                            id_ens = sharedPref.getString("idUser", "");
                            num_rep = 0;
                            for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                                View vi = listView.getChildAt(i);
                                EditText et = (EditText) vi.findViewById(R.id.etSugg);
                                CheckBox ch = (CheckBox) vi.findViewById(R.id.checkSugg);
                                if (ch.isChecked())
                                    num_rep++;
                            }


                            System.out.println("http://qcmtest.6te.net/qcm/insert_quest.php?quest=\"" + quest + "\"&id_exam=\"" + idQcm + "\"&num_rep=\"" + num_rep + "\"");
                            addQuest();

                            for (int i = 0; i < listView.getAdapter().getCount(); i++) {

                                View vi = listView.getChildAt(i);
                                et = (EditText) vi.findViewById(R.id.etSugg);
                                CheckBox ch = (CheckBox) vi.findViewById(R.id.checkSugg);
                                if (ch.isChecked()) {
                                    //System.out.println(et.getText().toString());
                                    System.out.println("http://qcmtest.6te.net/qcm/insert_prop.php?id_quest=" + idQuest + "&id_exam=" + idQcm + "&type=" + 1 + "&rep=" + et.getText().toString());
                                    addPropo("1");
                                } else {

                                    System.out.println("http://qcmtest.6te.net/qcm/insert_prop.php?id_quest=" + idQuest + "&id_exam=" + idQcm + "&type=" + 0 + "&rep=" + et.getText().toString());
                                    addPropo("0");
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

    public void addQCM(){

        try {
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://qcmtest.6te.net/qcm/insert_qcm.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.indexOf("/div>") != -1)
                                response = response.substring(response.lastIndexOf("/div>") + 5);

                            System.out.println("result === \n" + response);

                            try {

                                JSONObject result = new JSONObject(response);
                                String r = result.get("last_id").toString().replaceAll("\"", "");
                                idQcm = Integer.parseInt(r);

                                editor.putInt("idq",idQcm);
                                editor.apply();
                                System.out.println("IDQCM == "+idQcm);

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
                    params.put("titre",titre);
                    params.put("desc",desc);
                    params.put("id_ens",id_ens);
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


    public void addQuest(){

        try {
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://qcmtest.6te.net/qcm/insert_quest.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.indexOf("/div>") != -1)
                                response = response.substring(response.lastIndexOf("/div>") + 5);

                            System.out.println("result === \n" + response);

                            try {

                                JSONObject result = new JSONObject(response);
                                String r = result.get("last_id").toString().replaceAll("\"", "");
                                idQuest = Integer.parseInt(r);

                                editor.putInt("idquset", idQuest+1);
                                editor.apply();
                                System.out.println("IDQUEST == " + idQuest);

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
                    params.put("quest",quest);
                    params.put("num_rep",num_rep+"");
                    params.put("id_exam",idQcm+"");
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


    public void addPropo(final String t){

        try {
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://qcmtest.6te.net/qcm/insert_prop.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.indexOf("/div>") != -1)
                                response = response.substring(response.lastIndexOf("/div>") + 5);

                            System.out.println("result === \n" + response);

                            try {

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

                    idQuest = sharedPref.getInt("idquset",0);
                    params.put("id_quest",idQuest+"");
                    params.put("type",t);
                    params.put("rep",et.getText().toString());
                    params.put("id_exam",idQcm+"");
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


}

