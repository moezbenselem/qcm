package com.qcm.moez.qcm;

/**
 * Created by Moez on 02/11/2017.
 */

public class Proposition {

    int id;
    int id_quest;
    int id_exam;
    int type;
    String propo;

    public Proposition(int id, int id_quest, int id_exam, int type, String propo) {
        this.id = id;
        this.id_quest = id_quest;
        this.id_exam = id_exam;
        this.type = type;
        this.propo = propo;
    }
}
