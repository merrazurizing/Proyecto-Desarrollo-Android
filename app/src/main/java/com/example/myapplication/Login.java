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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class Login extends AppCompatActivity {


    private EditText edit2,edit3;
    private Button btn2,btn3;
    private SharedPreferences prefs;
    private ArrayList<Usuario> lisUsuario =new ArrayList<>();
    private Realm mRealm;
    public static final String URL_BASE = "https://abascur.cl/android/misnotasapp/";
    public static final String ACESS_ID="18808222";
    public String response_rut;
    public String response_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loging);
        prefs = getSharedPreferences("Preference", Context.MODE_PRIVATE);

        edit2=findViewById(R.id.edit_contrasena);
        edit3=findViewById(R.id.edit_run);
        btn2=findViewById(R.id.btn2);
        btn3=findViewById(R.id.btn3);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearShared();
                sendMainActivity();
            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String contrasena=edit2.getText().toString();
                String run=edit3.getText().toString();


                if(isValidForm()){
                    /* Sacar pass de realm y no del shared , lo mismo con el rut */

                    mRealm = Realm.getDefaultInstance();
                    Usuario usuario = new Usuario();
                    usuario=mRealm.where(Usuario.class).equalTo("run",run).findFirst();

                        if(usuario.getRun().equals(run) && usuario.getPassword().equals(contrasena)){
                            Accion_Usuario accion = new Accion_Usuario(run,"Login");

                            mRealm.beginTransaction();
                            mRealm.insertOrUpdate(accion);
                            mRealm.commitTransaction();

                            sendSecondActivity(usuario.getNombre(),usuario.getRun(),usuario.getPassword());
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Error de contraseña o usuario",Toast.LENGTH_LONG).show();
                        }

                }else{
                    Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_LONG).show();

                }
            }
        });

    }


    private boolean isValidForm(){
        boolean r=false;
        if(TextUtils.isEmpty(edit3.getText())){
            edit3.setError("El RUN es obligatorio");
        }else if(TextUtils.isEmpty(edit2.getText())){
            edit2.setError("La contraseña es obligatoria");
        }else{
            r=true;
        }
        return r;
    }

    private void sendSecondActivity(String nombre,String run,String password){
        Intent intent = new Intent(Login.this, Acciones_Usuario.class);
        Bundle b =new Bundle();

        b.putString("nombre",nombre);
        b.putString("run",run);
        b.putBoolean("primer_login",true);
        b.putString("password" ,password);
        intent.putExtras(b);
        startActivity(intent);
    }
    private void sendMainActivity(){
        Intent intent = new Intent(Login.this, Registro.class);
        startActivity(intent);
    }
    private void clearShared(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putString("nombre","");
        editor.putString("contrasena","");
        editor.putString("run","");
        editor.apply();
    }


    private String getSharedNombre(){
        return prefs.getString("nombre","");
    }
    private int getSharedIngresos(){return prefs.getInt("ingresos", 0); }
    private String getSharedContrasena(){ return prefs.getString("contrasena", "");}
    private String getSharedRun(){ return prefs.getString("run", "");}
}