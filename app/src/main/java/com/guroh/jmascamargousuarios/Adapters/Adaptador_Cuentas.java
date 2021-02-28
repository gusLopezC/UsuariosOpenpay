package com.guroh.jmascamargousuarios.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.guroh.jmascamargousuarios.Models.DetalleCuentas;
import com.guroh.jmascamargousuarios.R;

import java.text.DecimalFormat;
import java.util.List;

public class Adaptador_Cuentas extends RecyclerView.Adapter<Adaptador_Cuentas.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    private View.OnClickListener listener;
    private View.OnLongClickListener listenerDouble;

    DecimalFormat Formato = new DecimalFormat("###,##0.00");
    Context context;
    @Override
    public int getItemCount() {
        return cuentasLista.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener listenerDouble){
        this.listenerDouble = listenerDouble;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public boolean onLongClick(View view) {
        if(listenerDouble!=null){
            listenerDouble.onLongClick(view);
        }
        return true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView idusuario, cuenta, nombre, direccion, alias, saldo;
        CardView cv;
        public ViewHolder(final View itemView){
            super(itemView);
            idusuario = itemView.findViewById(R.id.txtIdUsuario);
            cuenta    = itemView.findViewById(R.id.txtCuenta);
            nombre    = itemView.findViewById(R.id.txtNombre);
            direccion = itemView.findViewById(R.id.txtDireccion);
            alias     = itemView.findViewById(R.id.txtAlias);
            saldo     = itemView.findViewById(R.id.txtSaldo);


            //itemView.setOnCreateContextMenuListener(this);
            cv = itemView.findViewById(R.id.cardview);
            //recordName = (TextView)itemView.findViewById(R.id.tv_record);
            //visibleFile = (TextView)itemView.findViewById(R.id.visible_file);
            //date = (TextView)itemView.findViewById(R.id.date);
            //time = (TextView)itemView.findViewById(R.id.time);
            final String wCuenta = cuenta.getText().toString();
            /*
            btnExpand = itemView.findViewById(R.id.btn_expand);
            btnExpand.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(final View v) {
                    PopupMenu popup = new PopupMenu(btnExpand.getContext(), itemView);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.pagar:
                                    //Toast.makeText(v.getContext(),""+wCuenta,Toast.LENGTH_SHORT).show();
                                    //Pagar ();
                                    Intent intent = new Intent(v.getContext(), webview.class);
                                    intent.putExtra("wCuenta", "004");
                                    v.getContext().startActivity(intent);
                                    return true;
                                case R.id.facturar:
                                    //Pagar ();
                                    Intent intent2 = new Intent(v.getContext(), Facturacion.class);
                                    intent2.putExtra("wCuenta", "004");
                                    v.getContext().startActivity(intent2);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.inflate(R.menu.my_menu_item);
                    try {
                        Field mFieldPopup=popup.getClass().getDeclaredField("mPopup");
                        mFieldPopup.setAccessible(true);
                        MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popup);
                        mPopup.setForceShowIcon(true);
                    } catch (Exception e) {

                    }
                    popup.show();
                }
            });
*/

        }
    }

    private void Pagar() {

    }

    public List<DetalleCuentas> cuentasLista;

    public Adaptador_Cuentas(List<DetalleCuentas> cuentaLista) {
        this.cuentasLista = cuentaLista;
    }

    public Adaptador_Cuentas.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listado_cuentas,parent,false);
        Adaptador_Cuentas.ViewHolder viewHolder = new Adaptador_Cuentas.ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return viewHolder;
    }

    public void onBindViewHolder(Adaptador_Cuentas.ViewHolder holder, int position){
        holder.idusuario.setText(cuentasLista.get(position).getIdusuario());
        holder.cuenta.setText(cuentasLista.get(position).getCuenta());
        holder.nombre.setText(cuentasLista.get(position).getNombre());
        holder.direccion.setText(cuentasLista.get(position).getDireccion());
        holder.alias.setText(cuentasLista.get(position).getAlias());
        double wSaldo = Double.valueOf(cuentasLista.get(position).getSaldo());
        holder.saldo.setText("$ " + Formato.format(wSaldo));
    }
}