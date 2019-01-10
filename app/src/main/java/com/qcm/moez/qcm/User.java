package com.qcm.moez.qcm;

/**
 * Created by Moez on 10/11/2017.
 */

public class User {

    String id , nom_pre , etat , type ;

    public User(String id, String nom_pre, String etat) {
        this.id = id;
        this.nom_pre = nom_pre;
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", nom_pre='" + nom_pre + '\'' +
                ", etat='" + etat + '\'' +
                '}';
    }
}
