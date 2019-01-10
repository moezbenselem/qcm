package com.qcm.moez.qcm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class InscriptionAct extends AppCompatActivity {


    EditText etNom , etPre , etMdp , etLogin;
    RadioGroup rg;
    RadioButton rbAdmin , rbEns , rbEt;
    Button btValid , btAnnul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        etNom = (EditText) findViewById(R.id.etNom);
        etPre = (EditText) findViewById(R.id.etPre);
        etLogin = (EditText) findViewById(R.id.etLogin);
        etMdp = (EditText) findViewById(R.id.etPsw);

        rg = (RadioGroup) findViewById(R.id.rg);

        rbAdmin = (RadioButton) findViewById(R.id.rbAdmin);
        rbEns = (RadioButton) findViewById(R.id.rbEns);
        rbEt = (RadioButton) findViewById(R.id.rbEt);

        btValid = (Button) findViewById(R.id.btSauv);
        btAnnul = (Button) findViewById(R.id.btAnnuler);


        btValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String nom = etNom.getText().toString();
                String pre = etPre.getText().toString();
                String login = etLogin.getText().toString();
                String mdp = etMdp.getText().toString();

                if(nom.length()!=0 || pre.length() !=0 || login.length() !=0 || mdp.length()!=0)
                {

                    int selected = rg.getCheckedRadioButtonId();

                    int type = -1 ;

                    if(rbAdmin.isChecked())
                        type =3;
                    else if(rbEns.isChecked())
                        type =0;
                    else if(rbEt.isChecked())
                        type =1;

                    if(type!=-1){
                        System.out.println("http://qcmtest.6te.net/qcm/insert_user.php?id="+login+"&nom="+nom+"&pre="+pre+"&psw="+sha1(mdp)+"&type="+type);
                        Ion.with(getApplicationContext())
                                .load("http://qcmtest.6te.net/qcm/insert_user.php?id="+login+"&nom="+nom+"&pre="+pre+"&psw="+sha1(mdp)+"&type="+type)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        if (e == null) {
                                            String r = result.get("success").toString().replaceAll("\"", "");
                                            if (r.equalsIgnoreCase("true")) {
                                                Toast.makeText(getApplicationContext(), "EFFECTUER !\nVotre compte est en cours de validation !", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(InscriptionAct.this,LoginActivity.class);
                                                startActivity(intent);
                                                InscriptionAct.this.finish();
                                            } else
                                            {
                                                Toast.makeText(getApplicationContext(), "ECHOUEE ! Erreur Serveur !", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                        else {
                                            e.printStackTrace();
                                        }
                                    }
                                });


                    }
                    else
                        Toast.makeText(getApplicationContext(),"Vous devez choisir le TYPE !",Toast.LENGTH_LONG).show();



                }
                else {
                    Toast.makeText(getApplicationContext(),"Vous devez remplir tous les champs !",Toast.LENGTH_LONG).show();
                }


            }
        });


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
