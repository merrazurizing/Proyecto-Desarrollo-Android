package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Accion_Usuario;
import com.example.myapplication.Models.Nota_Usuario;
import com.example.myapplication.R;


import java.util.ArrayList;
import java.util.List;

public class AccioneListAdapter extends RecyclerView.Adapter<AccioneListAdapter.ViewHolder> {


    List<Nota_Usuario> ShowList;
    Context context;
    int position;
    private OnItemClickListener itemClickListener;


    public AccioneListAdapter(List<Nota_Usuario> showList, OnItemClickListener itemClickListener) {
        ShowList = showList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public AccioneListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_accion,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        context = parent.getContext();
        position = viewType;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccioneListAdapter.ViewHolder viewHolder, final int i) {
        /*se pasa a cada uno de los elementos el evento listener junto con el respectivo elemento*/

        viewHolder.bind(ShowList.get(i), itemClickListener,i);
        position = i;
    }

    @Override
    public int getItemCount() {
        return ShowList.size();
    }
    /*Se remplaza el listado*/
    public void updateList(ArrayList<Nota_Usuario> data) {
        ShowList = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textId, textAccion;
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);

            textId = itemView.findViewById(R.id.textView_id);
            textAccion = itemView.findViewById(R.id.textView_accion);

            cv = itemView.findViewById(R.id.cardViewItem);


        }

        public void bind(final Nota_Usuario notaUsuario, final OnItemClickListener listener,final int i){

            //textId.setText(context.getString(R.string.nombre_apellido, Utilidades.primeraMayuscula(alumno.getNombre()), Utilidades.primeraMayuscula(alumno.getApellido())));
            //textRut.setText("" + Utilidades.formatearRut(alumno.getRut()));


            textId.setText(String.valueOf(i+1));
            textAccion.setText(notaUsuario.getNombre());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(notaUsuario,getAdapterPosition());
                }
            });

        }
    }
    public interface OnItemClickListener {
        void OnItemClick(Nota_Usuario notaUsuario, int position);
    }
}

