package com.guroh.jmascamargousuarios.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.guroh.jmascamargousuarios.Models.DetalleRecibo;
import com.guroh.jmascamargousuarios.R;

import java.text.DecimalFormat;
import java.util.List;

public class Adaptador_Recibo extends RecyclerView.Adapter<Adaptador_Recibo.ViewHolder> {

    DecimalFormat Formato = new DecimalFormat("###,##0.00");
    @Override
    public int getItemCount() {
        return recibosLista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView concepto, descripcionconcepto, montopago;

        public ViewHolder(View itemView){
            super(itemView);
            concepto  = itemView.findViewById(R.id.txtClaveConcepto);
            descripcionconcepto  = itemView.findViewById(R.id.txtConcepto);
            montopago = itemView.findViewById(R.id.txtPago);
        }
    }

    public List<DetalleRecibo> recibosLista;

    public Adaptador_Recibo(List<DetalleRecibo> reciboLista) {
        this.recibosLista = reciboLista;
    }

    public Adaptador_Recibo.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listado_pago,parent,false);
        Adaptador_Recibo.ViewHolder viewHolder = new Adaptador_Recibo.ViewHolder(view);
        return viewHolder;

    }

    public void onBindViewHolder(Adaptador_Recibo.ViewHolder holder, int position){
        holder.concepto.setText(recibosLista.get(position).getConceptoPago());
        holder.descripcionconcepto.setText(recibosLista.get(position).getDescripcionConcepto());
        Double wMontoPago = Double.valueOf(recibosLista.get(position).getMontoPago());
        holder.montopago.setText(Formato.format(wMontoPago));
    }
}