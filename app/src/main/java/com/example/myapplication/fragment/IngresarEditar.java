package com.example.myapplication.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Lista_Notas_Usuario;
import com.example.myapplication.Models.Nota_Usuario;
import com.example.myapplication.R;
import com.example.myapplication.adapters.AccioneListAdapter;

import java.text.Normalizer;
import java.util.ArrayList;

import io.realm.Realm;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


public class IngresarEditar extends DialogFragment {

    private EditText nombre,descripcion;
    private Button buttonGuardar;

    private DialogInterface.OnDismissListener onDismissListener;

    private AccioneListAdapter adapters;
    private ArrayList<Nota_Usuario> list;
    private Nota_Usuario nota = null;

    private Realm mRealm;
    private String rut;


    public IngresarEditar() {
        // Required empty public constructor
    }


    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener){
        this.onDismissListener=onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        if(onDismissListener!=null){
            onDismissListener.onDismiss(dialog);
        }
    }

    public IngresarEditar setAdepter(AccioneListAdapter adepter){
        this.adapters=adepter;
        return this;
    }

    public IngresarEditar setRun(String run){
        this.rut=run;
        return this;
    }

    public IngresarEditar setNota(Nota_Usuario nota){
        this.nota=nota;
        return this;
    }

    public IngresarEditar setArrayList(ArrayList<Nota_Usuario> list){
        this.list=list;
        return this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstance){
        View v=inflater.inflate(R.layout.fragment_ingresar_editar, container, false);

        mRealm = Realm.getDefaultInstance();
        nombre = v.findViewById(R.id.input_nombreNota);
        descripcion = v.findViewById(R.id.input_descripcionNota);
        buttonGuardar = v.findViewById(R.id.button_Fragment);
        if(nota==null){
            buttonGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),"Ingresar",Toast.LENGTH_LONG).show();

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    DateTimeFormatter id = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    LocalDateTime now = LocalDateTime.now();

                    Nota_Usuario nota = new Nota_Usuario(id.format(now), nombre.getText().toString(), descripcion.getText().toString(), dtf.format(now), dtf.format(now), dtf.format(now), rut);

                    mRealm.beginTransaction();
                    mRealm.insertOrUpdate(nota);
                    mRealm.commitTransaction();


                    list=new ArrayList(mRealm.where(Nota_Usuario.class).findAll());
                    adapters.updateList(list);
                    adapters.notifyDataSetChanged();
                    dismiss();
                }
            });
        }else {

            nombre.setEnabled(false);
            descripcion.setEnabled(false);

            nombre.setText(nota.getNombre());
            descripcion.setText(nota.getDescripcion());

            buttonGuardar.setText("Editar");

            buttonGuardar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    nombre.setEnabled(true);
                    descripcion.setEnabled(true);

                    buttonGuardar.setText("Guardar");

                    buttonGuardar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(),"Ingresar",Toast.LENGTH_LONG).show();

                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            DateTimeFormatter id = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                            LocalDateTime now = LocalDateTime.now();


                           /* Nota_Usuario notaAux = new Nota_Usuario(nota.getId(), nombre.getText().toString(), descripcion.getText().toString(), dtf.format(now), dtf.format(now), dtf.format(now), rut);
                            notaAux.setSendBD(false);
                            */


                            // ¿Sere yo?
                            nota.setNombre(nombre.getText().toString());
                            nota.setNombre(descripcion.getText().toString());
                            nota.setFecha_update(dtf.format(now));
                            nota.setSendBD(false);

                            mRealm.beginTransaction();
                            mRealm.insertOrUpdate(nota);
                            mRealm.commitTransaction();

                            list.clear();
                            list=new ArrayList(mRealm.where(Nota_Usuario.class).findAll());
                            for( Nota_Usuario nota : list){
                                System.out.println("nombre : " + nota.getNombre());
                                System.out.println("id : " + nota.getId());
                            }
                            adapters.updateList(list);
                            adapters.notifyDataSetChanged();

                            dismiss();
                        }
                    });
                }
            });

        }


        return v;
    }
}