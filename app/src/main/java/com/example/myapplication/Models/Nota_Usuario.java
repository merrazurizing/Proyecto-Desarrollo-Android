package com.example.myapplication.Models;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.RealmObject;

public class Nota_Usuario extends RealmObject {
    private String id;
    private String nombre;
    private String descripcion;
    private String fecha_creacion;
    private String fecha_update;
    private String fecha_insert;
    private String run_usuario;
    private boolean sendBD=false;

    public Nota_Usuario() {
    }

    public Nota_Usuario(String id, String nombre, String descripcion, String fecha_creacion, String fecha_update, String fecha_insert, String run_usuario) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
        this.fecha_update = fecha_update;
        this.fecha_insert = fecha_insert;
        this.run_usuario = run_usuario;
    }

    public boolean isSendBD() {
        return sendBD;
    }

    public void setSendBD(boolean sendBD) {
        this.sendBD = sendBD;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFecha_creacion() {
        return fecha_creacion;
    }
    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
    public String getFecha_update() {
        return fecha_update;
    }
    public void setFecha_update(String fecha_update) {
        this.fecha_update = fecha_update;
    }
    public String getFecha_insert() {
        return fecha_insert;
    }
    public void setFecha_insert(String fecha_insert) {
        this.fecha_insert = fecha_insert;
    }
    public String getRun_usuario() {
        return run_usuario;
    }
    public void setRun_usuario(String run_usuario) {
        this.run_usuario = run_usuario;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
