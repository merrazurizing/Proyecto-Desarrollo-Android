package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Models.Accion_Usuario;
import com.example.myapplication.Models.Nota_Usuario;
import com.example.myapplication.Models.Usuario;
import com.example.myapplication.adapters.AccioneListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;

public class Lista_Notas_Usuario extends AppCompatActivity {

    private TextView text1_second,text2_second;
    private Button btn_atras_second;
    private SharedPreferences prefs;
    private Realm mRealm;
    private boolean primer_login;

    public static final String URL_BASE = "https://abascur.cl/android/misnotasapp/";
    public static final String ACESS_ID="18808222";

    private RecyclerView recyclerView;
    private FrameLayout frameLayout;

    private AccioneListAdapter adapter;

    private ArrayList<Nota_Usuario> lista_notas;

    private String nombre;
    private String rut;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mRealm = Realm.getDefaultInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        prefs = getSharedPreferences("Preference", Context.MODE_PRIVATE);

        text1_second =findViewById(R.id.text1_second);
        text2_second =findViewById(R.id.text2_second);


        btn_atras_second=findViewById(R.id.btn_atras_second);

        Bundle bundle = this.getIntent().getExtras();

        String nombre= bundle.getString("nombre");
        String run = bundle.getString("run");
        String contrasena = bundle.getString("contrasena");

        rut=run;
        primer_login=bundle.getBoolean("primer_login");
        saveShared(nombre,run,contrasena);

        lista_notas = new ArrayList<Nota_Usuario>();
        get_user_notes();

        text1_second.setText("Nombre: "+ nombre);
        text2_second.setText("RUN: "+ run);

        Usuario usuario = new Usuario();


        String lista_mostrar="";
        for (int i =0 ; i<= lista_notas.size()-1  ;i++){
            lista_mostrar+=(i+1)+"--"+lista_notas.get(i).getRun_usuario()+"--"+lista_notas.get(i).getDescripcion()+"\n";
        }
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new AccioneListAdapter(lista_notas, new AccioneListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Nota_Usuario notaUsuario, int position) {
                Toast.makeText(getApplicationContext(),"item:"+notaUsuario.getId(),Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //text3_second.setText(lista_mostrar);
        btn_atras_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mRealm = Realm.getDefaultInstance();

                    Accion_Usuario accion = new Accion_Usuario(run,"Cerrar Sesion");
                    mRealm.beginTransaction();
                    mRealm.insertOrUpdate(accion);
                    mRealm.commitTransaction();

                    Intent intent =new Intent(Lista_Notas_Usuario.this, Login.class);
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

            lista_notas=new ArrayList(mRealm.where(Accion_Usuario.class).equalTo("run",text2_second.getText().toString()).findAll());
            adapter.updateList(lista_notas);
            adapter.notifyDataSetChanged();

        }

        ArrayList<Accion_Usuario> lista_acciones=new ArrayList<>();
        lista_acciones=new ArrayList(mRealm.where(Accion_Usuario.class).equalTo("run",text2_second.getText().toString()).findAll());
        String lista_mostrar="";
        for (int i =0 ; i<= lista_acciones.size()-1  ;i++){
            lista_mostrar+=(i+1)+"--"+lista_acciones.get(i).getRun()+"--"+lista_acciones.get(i).getAccion()+"\n";
        }
        //text3_second.setText(lista_mostrar);
    }



    private void clearShared(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putString("nombre","");
        editor.putString("contrasena","");
        editor.putString("run","");
        editor.apply();
    }

    private  void saveShared(String nombre,String run, String contrasena){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nombre",nombre);
        editor.putString("run",run);
        editor.putString("contrasena",contrasena);
        System.out.println("run "+run);
        System.out.println("contrasena "+contrasena);
        editor.apply();
    }


    private void get_user_notes(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = URL_BASE + "GetAllNotas/18808222";
        JsonObjectRequest jsonReque = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("JSONPost", response.toString());
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();

                        try {
                            String status = response.getString("status");

                            JSONArray mensaje = response.getJSONArray("mensaje");
                            if (status.equals("success")) {
                                add_note_list_to_realm(mensaje);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error no se puedo realizar la consulta", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonReque);

    }

    public void add_note_list_to_realm(JSONArray mensaje){
        lista_notas.clear();
        mRealm.beginTransaction();
        mRealm.delete(Usuario.class);
        mRealm.commitTransaction();
        try {
            if (mensaje.length() > 0) {
                for (int i = 0; i < mensaje.length(); i++) {

                    JSONObject jsonObject = mensaje.getJSONObject(i);
                    String run = jsonObject.getString("rutUsuario");
                    System.out.println("Rut de API : " + run +"y Rut de Shared : " + rut);
                    if(rut.equals(run)){
                        String id = jsonObject.getString("idNota");
                        String nombre = jsonObject.getString("nombreNota");
                        String descripcion = jsonObject.getString("descripcionNota");
                        String fecha_creacion = jsonObject.getString("fechaHoraCreacion");
                        String fecha_update = jsonObject.getString("fechaHoraUpdate");
                        String fecha_insert = jsonObject.getString("fechaHoraInsert");
                        lista_notas.add(new Nota_Usuario(id ,nombre,descripcion,fecha_creacion,fecha_update,fecha_insert,run));
                    }
                }
            }
            mRealm.beginTransaction();
            //mRealm.copyToRealmOrUpdate(lista_notas);
            mRealm.commitTransaction();
            //mSwipeRefreshLayout.setRefreshing(false);
            adapter.updateList(lista_notas);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}