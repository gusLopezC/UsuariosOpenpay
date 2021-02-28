package com.guroh.jmascamargousuarios.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.guroh.jmascamargousuarios.Models.DetallePagos;
import com.guroh.jmascamargousuarios.R;

import java.text.DecimalFormat;
import java.util.List;

public class Adaptador_Pagos extends RecyclerView.Adapter<Adaptador_Pagos.ViewHolder>
        implements View.OnClickListener {
    private View.OnClickListener listener;
    DecimalFormat Formato = new DecimalFormat("###,##0.00");

    @Override
    public int getItemCount() {
        return pagosLista.size();
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
        private TextView caja, referencia, fecha, monto;

        public ViewHolder(View itemView){
            super(itemView);
            referencia  = itemView.findViewById(R.id.txtReferencia_ListadoPagos);
            fecha  = itemView.findViewById(R.id.txtFechaPago_ListadoPagos);
            monto = itemView.findViewById(R.id.txtMonto_ListadoPagos);
        }
    }

    public List<DetallePagos> pagosLista;

    public Adaptador_Pagos(List<DetallePagos> pagoLista) {
        this.pagosLista = pagoLista;
    }

    public Adaptador_Pagos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listado_pagos,parent,false);
        Adaptador_Pagos.ViewHolder viewHolder = new Adaptador_Pagos.ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;

    }

    public void onBindViewHolder(Adaptador_Pagos.ViewHolder holder, int position){
        holder.referencia.setText(pagosLista.get(position).getReferencia());
        holder.fecha.setText(pagosLista.get(position).getFecha());
        holder.monto.setText(pagosLista.get(position).getMonto());
        double wPago = Double.valueOf(pagosLista.get(position).getMonto());
        holder.monto.setText(Formato.format(wPago));
    }
}