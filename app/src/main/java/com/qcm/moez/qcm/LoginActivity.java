package com.qcm.moez.qcm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {



    ArrayList<Account> listUsers;
    EditText etLogin,etPsw;
    Button btCon,btInsc;
    SharedPreferences sharedPref;
    Boolean connected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

            connected = sharedPref.getBoolean("connected", false);

            if (connected) {

                Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            } else {

                listUsers = new ArrayList<>();

                getUsers();

                etLogin = (EditText) findViewById(R.id.etLogin);
                etPsw = (EditText) findViewById(R.id.etPsw);
                btCon = (Button) findViewById(R.id.btConnect);
                btInsc = (Button) findViewById(R.id.btInsc);

                btInsc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(LoginActivity.this,InscriptionAct.class);
                        startActivity(i);
                        finish();

                    }
                });

                btCon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        System.out.println("list users size main = "+listUsers.size());

                        String LOGIN = etLogin.getText().toString();
                        String PSW = etPsw.getText().toString();
                        String Sha1PSW = sha1(PSW);
                        System.out.println("encrypted ======= "+Sha1PSW);


                        Account account = findAccount(LOGIN);

                        if (account!= null && account.psw != null) {


                                if (Sha1PSW.equals(account.psw)) {
                                    if (account.etat.equals("0")) {
                                        Toast.makeText(getApplicationContext(), "Ce Compte est en cours de Vérification !", Toast.LENGTH_LONG).show();
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("id_user", account.id);
                                        intent.putExtra("nom", account.nom);
                                        intent.putExtra("prenom", account.prenom);
                                        intent.putExtra("etat", account.etat);
                                        intent.putExtra("type", account.type);

                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putBoolean("connected", true);
                                        editor.putString("Nom", account.nom);
                                        editor.putString("Prenom", account.prenom);
                                        editor.putString("Password", account.psw);
                                        editor.putString("idUser", account.id);
                                        editor.putString("Type", account.type);
                                        editor.apply();

                                        startActivity(intent);
                                        finish();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "Mot de Passe Incorrecte !", Toast.LENGTH_LONG).show();
                                }


                        } else {
                            Toast.makeText(getApplicationContext(), "Ce Compte n'existe pas !", Toast.LENGTH_LONG).show();
                        }


                    }
                });
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public String sha1(String s) {
        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();


            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(String.format("%02X", 0xFF & messageDigest[i]));
            return hexString.toString().toLowerCase();

        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }


    Account findAccount(String id){

        Account a = null;
        for(int i=0;i<listUsers.size();i++){
            if(id.equals(listUsers.get(i).id)){
                a = listUsers.get(i);
                break;
            }
        }
        return a;
    }


    private void getUsers() {
        try {
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://qcmtest.6te.net/qcm/liste_ens_login.php?db=qcm",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.indexOf("/div>") != -1)
                                response = response.substring(response.lastIndexOf("/div>") + 5);

                            System.out.println("result === \n" + response);

                            try {

                                System.out.println("Consult liste users");

                                JSONArray result = new JSONArray(response);



                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject obj = (JSONObject) result.get(i);
                                    //Log.e("name :", "" + obj.get("name"));

                                    String idR = obj.get("id").toString().replaceAll("\"", "");
                                    String nomR = obj.get("nom").toString().replaceAll("\"", "");
                                    String prenomR = obj.get("prenom").toString().replaceAll("\"", "");
                                    String loginR = obj.get("id").toString().replaceAll("\"", "");
                                    String mdpR = obj.get("psw").toString().replaceAll("\"", "");
                                    String etatR = obj.get("etat").toString().replaceAll("\"", "");
                                    String typeR = obj.get("type").toString().replaceAll("\"", "");

                                    Account user = new Account(idR,nomR,prenomR,mdpR,etatR,typeR);
                                    listUsers.add(user);

                                }

                                System.out.println("list users size = "+listUsers.size());


                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            new AlertDialog.Builder(LoginActivity.this).setTitle("Erreur")
                                    .setMessage("Une erreur serveur est survenue !\nVérifier votre connexion !").setPositiveButton("Réssayer", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    LoginActivity.this.recreate();

                                }
                            }).setNegativeButton("Quittez", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    LoginActivity.this.finish();
                                }
                            }).show();

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
                final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
                requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

                    @Override
                    public void onRequestFinished(Request<Object> request) {

                        System.out.println("request finished !");

                    }
                });
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }

    }


}
