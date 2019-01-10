package com.qcm.moez.qcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Moez on 12/12/2017.
 */

public class ConfigFragment extends Fragment {

    EditText etOldPsw ,etNewPsw,etNewPsw2;
    String idUser,oldPsw;
    Button save,valide;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        return inflater.inflate(R.layout.config_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        oldPsw = sharedPref.getString("Password", "");
        idUser = sharedPref.getString("idUser",idUser);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>5)
                    save.setEnabled(true);
                else
                    save.setEnabled(false);
            }
        };


        etOldPsw = (EditText) getView().findViewById(R.id.password);
        etNewPsw = (EditText) getView().findViewById(R.id.newpassword);
        etNewPsw2 = (EditText) getView().findViewById(R.id.newpassword2);
        valide = (Button) getView().findViewById(R.id.btConfirmerPSW);
        save = (Button) getView().findViewById(R.id.sauvegarde);
        etNewPsw2.setVisibility(View.INVISIBLE);
        etNewPsw.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        save.setEnabled(false);

        etNewPsw2.addTextChangedListener(textWatcher);
        etNewPsw.addTextChangedListener(textWatcher);

        valide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etOldPsw.getText().toString();
                if (oldPsw.equalsIgnoreCase(sha1(password))) {
                    etOldPsw.setVisibility(View.INVISIBLE);
                    valide.setVisibility(View.INVISIBLE);
                    etNewPsw.setVisibility(View.VISIBLE);
                    etNewPsw2.setVisibility(View.VISIBLE);
                    save.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Verifier votre mot de passe !", Toast.LENGTH_SHORT).show();
                    System.out.println("mdp : " + oldPsw);
                    System.out.println("entree mdp : " + password);

                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpassword =etNewPsw.getText().toString();
                String newpassword2=etNewPsw2.getText().toString();

                if (newpassword.equals(newpassword2))
                {
                    System.out.println("http://qcmtest.6te.net/qcm/editProfile.php?old="+oldPsw+"&psw="+sha1(newpassword)+"&idUser="+idUser);
                    Ion.with(getContext())
                            .load("http://qcmtest.6te.net/qcm/editProfile.php?old="+oldPsw+"&psw="+sha1(newpassword)+"&idUser="+idUser)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (e == null) {
                                        String r = result.get("success").toString().replaceAll("\"", "");
                                        if (r.equalsIgnoreCase("true")) {
                                            Toast.makeText(getContext(), "Mis a jour avec Succée !", Toast.LENGTH_SHORT).show();
                                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putBoolean("connected", false);
                                            editor.putString("Nom", null);
                                            editor.putString("Prenom", null);
                                            editor.putString("Password", null);
                                            editor.putString("idUser", null);
                                            editor.apply();
                                            Intent intent = new Intent(getContext(),LoginActivity.class);
                                            startActivity(intent);


                                        } else
                                            Toast.makeText(getContext(), "Mis a jour ECHOUEE !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else
                {
                    Toast.makeText(getContext(), "verifier les champs !", Toast.LENGTH_SHORT).show();
                }
            }
        });



        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Configuration du Profile");


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
