package com.qcm.moez.qcm;

/**
 * Created by Moez on 10/11/2017.
 */

public class Account {

    String id , nom ,prenom , psw ,etat , type ;

    public Account() {
    }

    public Account(String id, String nom, String prenom, String psw, String etat, String type) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.psw = psw;
        this.etat = etat;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
