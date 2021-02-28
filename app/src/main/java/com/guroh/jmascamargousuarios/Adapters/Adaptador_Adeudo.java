package com.guroh.jmascamargousuarios.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.guroh.jmascamargousuarios.Models.DetalleAdeudo;
import com.guroh.jmascamargousuarios.R;

import java.text.DecimalFormat;
import java.util.List;

public class Adaptador_Adeudo extends RecyclerView.Adapter<Adaptador_Adeudo.ViewHolder> {

    private View.OnClickListener listener;
    DecimalFormat Formato = new DecimalFormat("###,##0.00");
    @Override
    public int getItemCount() {
        return adeudoLista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView concepto, descripcionconcepto, montoadeudo;

        public ViewHolder(View itemView){
            super(itemView);
            concepto  = itemView.findViewById(R.id.txtClaveConcepto);
            descripcionconcepto  = itemView.findViewById(R.id.txtConcepto);
            montoadeudo = itemView.findViewById(R.id.txtAdeudo);
        }
    }

    public List<DetalleAdeudo> adeudoLista;

    public Adaptador_Adeudo(List<DetalleAdeudo> adeudoLista) {
        this.adeudoLista = adeudoLista;
    }

    public Adaptador_Adeudo.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listado_adeudo,parent,false);
        Adaptador_Adeudo.ViewHolder viewHolder = new Adaptador_Adeudo.ViewHolder(view);
        return viewHolder;

    }

    public void onBindViewHolder(Adaptador_Adeudo.ViewHolder holder, int position){
        holder.concepto.setText(adeudoLista.get(position).getConceptoAdeudo());
        holder.descripcionconcepto.setText(adeudoLista.get(position).getDescripcionConcepto());
        //holder.montoadeudo.setText(adeudoLista.get(position).getMontoAdeudo());
        double wAdeudo = Double.valueOf(adeudoLista.get(position).getMontoAdeudo());
        holder.montoadeudo.setText(Formato.format(wAdeudo));
        //holder.consumo.setText(adeudoLista.get(position).getIvaAdeudo());
        //holder.consumo.setText(adeudoLista.get(position).getSubtotalAdeudo());
        //holder.consumo.setText(adeudoLista.get(position).getTotalAdeudo());

    }
}