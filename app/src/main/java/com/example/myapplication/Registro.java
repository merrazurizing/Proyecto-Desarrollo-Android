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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Models.Accion_Usuario;
import com.example.myapplication.Models.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



import io.realm.Realm;
import io.realm.RealmResults;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import static android.app.ProgressDialog.show;

public class Registro extends AppCompatActivity {

    private TextView text1;
    private EditText edit1,edit2,edit3;
    private Button btn1,btn_ingreso;
    private SharedPreferences prefs;
    Realm mRealm;

    public static final String URL_BASE = "https://abascur.cl/android/misnotasapp/";
    public static final String ACESS_ID="18808222";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1=findViewById(R.id.text1);
        edit1=findViewById(R.id.edit1);
        edit2=findViewById(R.id.edit2);
        edit3=findViewById(R.id.edit3);
        btn1=findViewById(R.id.btn1);
        btn_ingreso= findViewById(R.id.btn_login);

        prefs = getSharedPreferences("Preference", Context.MODE_PRIVATE);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre=edit1.getText().toString();
                String password=edit2.getText().toString();
                String run=edit3.getText().toString();

                //text1.setText(nombre);

                if(Utilidades.verificaConexion(getApplication())){
                    if(isValidForm()){
                        if(estaRegistrado(run)){
                            Toast.makeText(getApplicationContext(),"Usuario ya esta registrado",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_LONG).show();
                            //saveShared(nombre,password,run);
                            guardarEnRealm(nombre,run,password);
                            sendSecondActivity(nombre,run,password);
                        }

                    }else{
                        Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_LONG).show();

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Esta acción necesita conexión a internet, comprueba tu conexión.", Toast.LENGTH_SHORT).show();

                }


            }
        });
        btn_ingreso.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                sendLoginActivity();
            }

        });
    }

    private void guardarEnRealm(String nombre, String run, String password) {
        mRealm = Realm.getDefaultInstance();

        Usuario usuario = new Usuario(nombre,run,password,false);
        Accion_Usuario accion = new Accion_Usuario(run,"Registro");

        mRealm.beginTransaction();
        mRealm.insertOrUpdate(usuario);
        mRealm.insertOrUpdate(accion);
        mRealm.commitTransaction();
        SyncbdRemote();
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

    private void sendSecondActivity(String nombre,String run,String contrasena){
        Intent intent = new Intent(Registro.this, Acciones_Usuario.class);
        Bundle b =new Bundle();

        b.putString("nombre",nombre);
        b.putString("run",run);
        b.putString("contrasena", contrasena);
        b.putBoolean("primer_login",true);

        intent.putExtras(b);
        startActivity(intent);
    }

    private void sendLoginActivity(){
        Intent intent = new Intent(Registro.this, Login.class);
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

    private void SyncbdRemote(){
        RealmResults<Usuario> ListadoNoSync=mRealm.where(Usuario.class).equalTo("sendBD",false).findAll();
        if(ListadoNoSync.size()>0){
            for(int i=0;i<ListadoNoSync.size();i++){
                String nombre = ListadoNoSync.get(i).getNombre();
                String rut = ListadoNoSync.get(i).getRun();
                String password = ListadoNoSync.get(i).getPassword();

                InsertOrUpdate(rut,nombre,password);
            }
        }
    }

    private void InsertOrUpdate(final String rut,final String nombre,final String password){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        Map<String, String> params = new HashMap<String, String>();

        params.put("rutUsuario", String.valueOf(rut));
        params.put("nombreUsuario", String.valueOf(nombre));
        params.put("contrasenaUsuario", String.valueOf(password));
        params.put("fechaHoraCreacion",dtf.format(now));

        params.put("idAcceso",ACESS_ID);
        Toast.makeText(getApplicationContext(), params.toString() , Toast.LENGTH_SHORT).show();

        String URL = URL_BASE+"InsertOrUpdateUsuario";
        //Toast.makeText(getApplicationContext(), URL , Toast.LENGTH_SHORT).show();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        System.out.println("URL "+URL);

        JsonObjectRequest jsonReque = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Log.d("JSONPost", response.toString());
                        try {
                            String status = response.getString("status");
                            String mensaje = response.getString("mensaje");
                            if (status.equals("success")) {
                                // Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                                /*alumnosAppV2: Se actualiza en realm el estado*/
                                UpdateEnviado(rut);
                            }
                        } catch (JSONException e) {
                            System.out.println("CAtch");
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

    private void  UpdateEnviado(String rut){
        mRealm.beginTransaction();
        Usuario usuario = mRealm.where(Usuario.class).equalTo("run",rut).findFirst();
        assert usuario!=null;
        usuario.setSendBD(true);
        mRealm.commitTransaction();
    }

    

    private String getSharedNombre(){
        return prefs.getString("nombre","");
    }
    private int getSharedIngresos(){
        return prefs.getInt("ingresos", 0);
    }
}