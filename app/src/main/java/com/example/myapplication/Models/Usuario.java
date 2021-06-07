package com.example.myapplication.Models;

import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Usuario extends RealmObject {

    @PrimaryKey
    private int id;
    private String nombre;
    private String run;
    private String password;

    public Usuario() {
    }

    public Usuario(String nombre, String run, String password) {
        Random rand = new Random();
        this.id=rand.nextInt(1000);
        this.nombre = nombre;
        this.run = run;
        this.password = password;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getRun() {
        return run;
    }
    public void setRun(String run) {
        this.run = run;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
