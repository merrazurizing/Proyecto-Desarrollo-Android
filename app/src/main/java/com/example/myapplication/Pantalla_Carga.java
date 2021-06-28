package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


import io.realm.Realm;
import io.realm.RealmConfiguration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static java.lang.Thread.sleep;

public class Pantalla_Carga extends AppCompatActivity {
    private SharedPreferences prefs;

    public static final String URL_BASE = "http://abascur.cl/android/misnotasapp/";
    public static final String ACESS_ID="18808222";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Realm mRealm;
        setUpRealmConfig();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        prefs = getSharedPreferences("Preference", Context.MODE_PRIVATE);

        String name = getSharedNombre();

        try{Thread.sleep(2000);}
        catch(InterruptedException e){System.out.println(e);}


        if(name != ""){

            sendLoging();
        }else{
            sendMainActivity();
        }
    }



    private void sendMainActivity(){
        Intent intent = new Intent(Pantalla_Carga.this, Registro.class);

        startActivity(intent);
    }
    private void sendLoging(){
        /* envez de mandar a loging , tiene que mandar a la pagina de la lista*/
        Intent intent = new Intent(Pantalla_Carga.this, Login.class);
        /* agregar una accion al usuario de que logeo*/
        startActivity(intent);
    }

    private String getSharedNombre(){
        return prefs.getString("nombre","");
    }
    private int getSharedIngresos(){
        return prefs.getInt("ingresos", 0);
    }

    private void setUpRealmConfig() {

        // Se inicializa realm
        Realm.init(this.getApplicationContext());

        // Configuración por defecto en realm
        RealmConfiguration config = new RealmConfiguration.
                Builder().
                deleteRealmIfMigrationNeeded().
                build();
        Realm.setDefaultConfiguration(config);

    }



}