package com.guroh.jmascamargousuarios.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.guroh.jmascamargousuarios.Models.DetalleReportes;
import com.guroh.jmascamargousuarios.R;

import java.util.List;

public class Adaptador_Reportes extends RecyclerView.Adapter<Adaptador_Reportes.ViewHolder>
        implements View.OnClickListener {
    private View.OnClickListener listener;

    @Override
    public int getItemCount() {
        return reportesLista.size();
    }

    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView fecha, detalleReporte;

        public ViewHolder(View itemView){
            super(itemView);
            fecha  = itemView.findViewById(R.id.txtFechaReporte_Reportes);
            detalleReporte = itemView.findViewById(R.id.txtDetalleReporte_Reportes);
        }
    }

    public List<DetalleReportes> reportesLista;

    public Adaptador_Reportes(List<DetalleReportes>  reporteLista) {
        this.reportesLista = reporteLista;
    }

    public Adaptador_Reportes.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listado_reportes,parent,false);
        Adaptador_Reportes.ViewHolder viewHolder = new Adaptador_Reportes.ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;

    }

    public void onBindViewHolder(Adaptador_Reportes.ViewHolder holder, int position){
        holder.fecha.setText(reportesLista.get(position).getFechaReporte());
        holder.detalleReporte.setText(reportesLista.get(position).getDetalleReporte());
    }
}