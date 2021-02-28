package com.guroh.jmascamargousuarios.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.guroh.jmascamargousuarios.Models.DetalleLecturas;
import com.guroh.jmascamargousuarios.R;

import java.util.List;

public class Adaptador_Lecturas extends RecyclerView.Adapter<Adaptador_Lecturas.ViewHolder> {

    private View.OnClickListener listener;

    @Override
    public int getItemCount() {
        return lecturasLista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView periodo, lanterior, lactual, consumo;

        public ViewHolder(View itemView){
            super(itemView);
            periodo  = itemView.findViewById(R.id.txtPeriodo);
            lanterior  = itemView.findViewById(R.id.txtLecturaAnterior);
            lactual = itemView.findViewById(R.id.txtLecturaActual);
            consumo = itemView.findViewById(R.id.txtConsumo);
        }
    }

    public List<DetalleLecturas> lecturasLista;

    public Adaptador_Lecturas(List<DetalleLecturas> lecturaLista) {
        this.lecturasLista = lecturaLista;
    }

    public Adaptador_Lecturas.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listado_lecturas,parent,false);
        Adaptador_Lecturas.ViewHolder viewHolder = new Adaptador_Lecturas.ViewHolder(view);
        return viewHolder;

    }

    public void onBindViewHolder(Adaptador_Lecturas.ViewHolder holder, int position){
        holder.periodo.setText(lecturasLista.get(position).getPeriodo());
        holder.lanterior.setText(lecturasLista.get(position).getLanterior());
        holder.lactual.setText(lecturasLista.get(position).getLactual());
        holder.consumo.setText(lecturasLista.get(position).getConsumo());
    }
}