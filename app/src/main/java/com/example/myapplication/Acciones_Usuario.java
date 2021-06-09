package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Models.Accion_Usuario;
import com.example.myapplication.Models.Usuario;

import java.util.ArrayList;

import io.realm.Realm;

public class Acciones_Usuario extends AppCompatActivity {

    private TextView text1_second,text2_second,text3_second;
    private Button btn_atras_second;
    private SharedPreferences prefs;
    private Realm mRealm;
    private boolean primer_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mRealm = Realm.getDefaultInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        prefs = getSharedPreferences("Preference", Context.MODE_PRIVATE);

        text1_second =findViewById(R.id.text1_second);
        text2_second =findViewById(R.id.text2_second);
        text3_second =findViewById(R.id.text3_second);

        btn_atras_second=findViewById(R.id.btn_atras_second);

        Bundle bundle = this.getIntent().getExtras();

        String nombre= bundle.getString("nombre");
        String run = bundle.getString("run");
        primer_login=bundle.getBoolean("primer_login");
        saveShared(nombre,run);

        text1_second.setText(nombre);
        text2_second.setText(run);

        ArrayList<Accion_Usuario> lista_acciones=new ArrayList<>();
        ArrayList<Accion_Usuario> lista_acciones_reverse=new ArrayList<>();
        Usuario usuario = new Usuario();
        lista_acciones=new ArrayList(mRealm.where(Accion_Usuario.class).equalTo("run",run).findAll());
        String lista_mostrar="";
        for (int i =0 ; i<= lista_acciones.size()-1  ;i++){
            lista_mostrar+=(i+1)+"--"+lista_acciones.get(i).getRun()+"--"+lista_acciones.get(i).getAccion()+"\n";
        }

        text3_second.setText(lista_mostrar);
        btn_atras_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mRealm = Realm.getDefaultInstance();

                    Accion_Usuario accion = new Accion_Usuario(run,"Cerrar Sesion");
                    mRealm.beginTransaction();
                    mRealm.insertOrUpdate(accion);
                    mRealm.commitTransaction();

                    Intent intent =new Intent(Acciones_Usuario.this, Login.class);
                    clearShared();
                    startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        // call the superclass method first
        super.onStop();
        mRealm = Realm.getDefaultInstance();

        Accion_Usuario accion = new Accion_Usuario(text2_second.getText().toString(),"Cerro aplicacion");
        mRealm.beginTransaction();
        mRealm.insertOrUpdate(accion);
        mRealm.commitTransaction();

    }

    @Override
    public void onResume() {
        super.onResume();
        mRealm = Realm.getDefaultInstance();
        if(primer_login){
            primer_login=false;
        }else{
            Accion_Usuario accion = new Accion_Usuario(text2_second.getText().toString(),"Volvio a la aplicacion");
            mRealm.beginTransaction();
            mRealm.insertOrUpdate(accion);
            mRealm.commitTransaction();

        }





        ArrayList<Accion_Usuario> lista_acciones=new ArrayList<>();
        lista_acciones=new ArrayList(mRealm.where(Accion_Usuario.class).equalTo("run",text2_second.getText().toString()).findAll());
        String lista_mostrar="";
        for (int i =0 ; i<= lista_acciones.size()-1  ;i++){
            lista_mostrar+=(i+1)+"--"+lista_acciones.get(i).getRun()+"--"+lista_acciones.get(i).getAccion()+"\n";
        }
        text3_second.setText(lista_mostrar);
    }



    private void clearShared(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putString("nombre","");
        editor.putString("contrasena","");
        editor.putString("run","");
        editor.apply();
    }

    private  void saveShared(String nombre,String run){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nombre",nombre);
        editor.putString("run",run);
        editor.apply();
    }


}