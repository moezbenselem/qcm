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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {


    private ArrayList<String> ids,noms,prenoms,psw,type,etat;
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
                ids = new ArrayList<String>();
                noms = new ArrayList<String>();
                prenoms = new ArrayList<String>();

                psw = new ArrayList<String>();
                type = new ArrayList<String>();
                etat = new ArrayList<String>();

                Ion.with(this)
                        .load("http://qcmtest.6te.net/qcm/liste_ens_login.php?db=qcm")
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
                                            noms.add(nomR);
                                            prenoms.add(prenomR);
                                            psw.add(mdpR);
                                            etat.add(etatR);
                                            type.add(typeR);

                                        }
                                        System.out.println(psw);


                                        //findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                                        System.out.println("lesss nommmssss");
                                        //System.out.println(noms);

                                    }
                                } else
                                {
                                    e.printStackTrace();

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
                            }
                        });


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

                        String LOGIN = etLogin.getText().toString();
                        String PSW = etPsw.getText().toString();
                        String Sha1PSW = sha1(PSW);
                        System.out.println("encrypted ======= "+Sha1PSW);

                        if (ids.indexOf(LOGIN) != -1 && psw.indexOf(Sha1PSW) != -1) {
                            int pos = ids.indexOf(LOGIN);
                            if (LOGIN.equals(ids.get(pos))) {
                                if (Sha1PSW.equals(psw.get(pos))) {
                                    if (etat.get(pos).equals("0")) {
                                        Toast.makeText(getApplicationContext(), "Ce Compte est en cours de Vérification !", Toast.LENGTH_LONG).show();
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("id_user", ids.get(pos));
                                        intent.putExtra("nom", noms.get(pos));
                                        intent.putExtra("prenom", prenoms.get(pos));
                                        intent.putExtra("etat", etat.get(pos));
                                        intent.putExtra("type", type.get(pos));

                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putBoolean("connected", true);
                                        editor.putString("Nom", noms.get(pos));
                                        editor.putString("Prenom", prenoms.get(pos));
                                        editor.putString("Password", psw.get(pos));
                                        editor.putString("idUser", ids.get(pos));
                                        editor.putString("Type", type.get(pos));
                                        editor.apply();

                                        startActivity(intent);
                                        finish();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "Mot de Passe Incorrecte !", Toast.LENGTH_LONG).show();
                                }
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


}
