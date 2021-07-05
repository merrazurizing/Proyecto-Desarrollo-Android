package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Toast;


import io.realm.Realm;
import io.realm.RealmConfiguration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import static java.lang.Thread.sleep;

public class Pantalla_Carga extends AppCompatActivity {
    private SharedPreferences prefs;

    public static final String URL_BASE = "https://abascur.cl/android/misnotasapp/";
    public static final String ACESS_ID="18808222";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Realm mRealm;
        setUpRealmConfig();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        prefs = getSharedPreferences("Preference", Context.MODE_PRIVATE);

        String name = getSharedNombre();

        System.out.println("RUT"+getSharedRut());
        if(!getSharedRut().equals("")){
            validarUsuario(getSharedRut());
        }
        else{
            sendMainActivity();
        }

    }



    private void sendMainActivity(){
        Intent intent = new Intent(Pantalla_Carga.this, Registro.class);

        startActivity(intent);
    }
    private void sendLoging(){
        /* envez de mandar a loging , tiene que mandar a la pagina de la lista*/

        Intent intent = new Intent(Pantalla_Carga.this, Lista_Notas_Usuario.class);
        /* agregar una accion al usuario de que logeo*/

        Bundle b =new Bundle();

        b.putString("nombre",getSharedNombre());
        b.putString("run",getSharedRut());
        b.putString("contrasena", getSharedPassword());
        b.putBoolean("primer_login",true);

        intent.putExtras(b);

        startActivity(intent);
    }

    private String getSharedNombre(){
        return prefs.getString("nombre","");
    }

    private String getSharedRut(){
        return prefs.getString("run","");
    }
    private String getSharedPassword(){
        return prefs.getString("contrasena","");
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


    private void validarUsuario(final String run) {
        System.out.println("ASDASDASDASDASDASDASDASDADASDASD");
        System.out.println("Run:"+run);
        System.out.println("Contrasena "+ getSharedPassword());
        Map<String, String> params = new HashMap<String, String>();
        params.put("rutUsuario", run);
        params.put("idAcceso",ACESS_ID);


        // Toast.makeText(getApplicationContext(), params.toString() , Toast.LENGTH_SHORT).show();
        String URL = URL_BASE+"GetUsuario";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonReque = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Log.d("JSONPost", response.toString());
                        try {
                            String status = response.getString("status");
                            JSONObject mensaje = response.getJSONObject("mensaje");

                            if (status.equals("success")) {

                                if(getSharedRut().equals(mensaje.getString("rutUsuario")) && getSharedPassword().equals(mensaje.getString("contrasenaUsuario"))){
                                    System.out.println("RUT RESPONSE:" + mensaje.getString("rutUsuario"));
                                    sendLoging();
                                }else{
                                    sendMainActivity();
                                }
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


}