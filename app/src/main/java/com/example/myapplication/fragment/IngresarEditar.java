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


public class IngresarEditar extends DialogFragment {

    private EditText nombre,descripcion;
    private Button buttonGuardar;

    private DialogInterface.OnDismissListener onDismissListener;

    private AccioneListAdapter adapters;
    private ArrayList<Nota_Usuario> list;

    private Realm mRealm;


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
        /*
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"OK",Toast.LENGTH_LONG).show();
                Nota_Usuario nora = new Nota_Usuario(editRut.getText().toString(),editNombre.getText().toString(),editApellido.getText().toString(),false);

                mRealm.beginTransaction();
                mRealm.insertOrUpdate(nota);
                mRealm.commitTransaction();

                list=new ArrayList(mRealm.where(Alumno.class).findAll());
                adapters.updateList(list);
                adapters.notifyDataSetChanged();
                dismiss();
            }
        });

        */

        return v;
    }
}