package com.example.myapplication.Models;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Accion_Usuario extends RealmObject {

    @PrimaryKey
    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private String run;
    private String accion;


    public Accion_Usuario() {
    }

    public Accion_Usuario(String run, String accion) {
        this.id=count.incrementAndGet();
        this.run = run;
        this.accion = accion;
    }




    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public int getId() {
        return id;
    }
}
