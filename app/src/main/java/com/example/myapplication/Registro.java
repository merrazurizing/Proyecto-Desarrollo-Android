package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Models.Accion_Usuario;
import com.example.myapplication.Models.Usuario;

import io.realm.Realm;

import static android.app.ProgressDialog.show;

public class Registro extends AppCompatActivity {

    private TextView text1;
    private EditText edit1,edit2,edit3;
    private Button btn1;
    private SharedPreferences prefs;
    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1=findViewById(R.id.text1);
        edit1=findViewById(R.id.edit1);
        edit2=findViewById(R.id.edit2);
        edit3=findViewById(R.id.edit3);
        btn1=findViewById(R.id.btn1);

        prefs = getSharedPreferences("Preference", Context.MODE_PRIVATE);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre=edit1.getText().toString();
                String password=edit2.getText().toString();
                String run=edit3.getText().toString();

                //text1.setText(nombre);



                if(isValidForm()){
                    if(estaRegistrado(run)){
                        Toast.makeText(getApplicationContext(),"Usuario ya esta registrado",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_LONG).show();
                        saveShared(nombre,run,password);
                        guardarEnRealm(nombre,run,password);
                        sendSecondActivity(nombre,password);
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void guardarEnRealm(String nombre, String run, String password) {
        mRealm = Realm.getDefaultInstance();

        Usuario usuario = new Usuario(nombre,run,password);
        Accion_Usuario accion = new Accion_Usuario(run,"Registro");

        mRealm.beginTransaction();
        mRealm.insertOrUpdate(usuario);
        mRealm.insertOrUpdate(accion);
        mRealm.commitTransaction();
    }

    private boolean estaRegistrado(String run){
        mRealm = Realm.getDefaultInstance();
        Usuario usuario = new Usuario();
        usuario =mRealm.where(Usuario.class).equalTo("run",run).findFirst();
        return usuario != null ? true : false;

    }


    @Override
    protected void onStart() {
        super.onStart();
        //text1.setText("Nombre guardado:"+getSharedNombre());
    }

    private boolean isValidForm(){
        boolean r=false;
        if(TextUtils.isEmpty(edit1.getText())){
            edit1.setError("El nombre es obligatorio");
        }else if(TextUtils.isEmpty(edit2.getText())){
            edit2.setError("La contraseña es obligatorio");
        }else{
            r=true;
        }

        return r;
    }

    private void sendSecondActivity(String nombre,String run){
        Intent intent = new Intent(Registro.this, Acciones_Usuario.class);
        Bundle b =new Bundle();

        b.putString("nombre",nombre);
        b.putString("run",run);
        b.putBoolean("primer_login",true);

        intent.putExtras(b);
        startActivity(intent);
    }

    private  void saveShared(String nombre,String contrasena,String run){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nombre",nombre);
        editor.putInt("ingresos",1);
        editor.putString("contrasena",contrasena);
        editor.putString("run",run);
        editor.apply();
    }



    private String getSharedNombre(){
        return prefs.getString("nombre","");
    }
    private int getSharedIngresos(){
        return prefs.getInt("ingresos", 0);
    }
}