package com.guroh.jmascamargousuarios.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.guroh.jmascamargousuarios.Adapters.Adaptador_Pagos;
import com.guroh.jmascamargousuarios.Adapters.Adaptador_Recibo;
import com.guroh.jmascamargousuarios.Clases.CustomProgress;
import com.guroh.jmascamargousuarios.Models.DetallePagos;
import com.guroh.jmascamargousuarios.Models.DetalleRecibo;
import com.guroh.jmascamargousuarios.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Pagos extends Fragment {
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    CustomProgress Procesando = CustomProgress.getInstance();
    String wServidorWeb;

    private RecyclerView recyclerViewPagos;
    private RecyclerView rvDetallePago;

    private Adaptador_Pagos adaptadorPagos;
    List<DetallePagos> pagosListado;

    private Adaptador_Recibo adaptadorRecibo;
    List<DetalleRecibo> recibosListado;

    String wURL_CargarPagos, wURL_CargarRecibo;
    String wIdUsuario, wCaja, wReferencia;;
    TextView txtTotalPago;

    double wTotalPago;

    Button btnAceptar;

    DecimalFormat Formato = new DecimalFormat("###,##0.00");

    public Pagos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cargarConfiguracion();

        wIdUsuario = getArguments().getString("pIdUsuario");
        View view = inflater.inflate(R.layout.fragment_pagos, container, false);

        recyclerViewPagos = view.findViewById(R.id.recyclerViewPagos);
        recyclerViewPagos.setHasFixedSize(true);
        recyclerViewPagos.setLayoutManager(new LinearLayoutManager(getContext()));

        pagosListado = new ArrayList<>();
        recibosListado = new ArrayList<>();
        wURL_CargarPagos    = wServidorWeb + "usuario_pagos.php?IDUSUARIO="+wIdUsuario;
        new pagosUsuario().execute();
        // Inflate the layout for t;his fragment
        return view;
        //return inflater.inflate(R.layout.fragment_pagos, container, false);
    }

    public void cargarConfiguracion() {
        SharedPreferences prefs = getActivity().getSharedPreferences("Principal", Context.MODE_PRIVATE);
        wServidorWeb = prefs.getString("ServidorWeb", "");
    }

    class pagosUsuario extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            CustomProgress Procesando = CustomProgress.getInstance();
            Procesando.showProgress(getContext(), "Espere un Momento...", true);
        }
        protected String doInBackground(String... args) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, wURL_CargarPagos,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //wSubtotal = 0.00;
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject pagos = array.getJSONObject(i);
                                    pagosListado.add(new DetallePagos(
                                            pagos.getString("CAJA_PA"),
                                            pagos.getString("REFE_PA"),
                                            pagos.getString("FECH_PA"),
                                            pagos.getString("MONT_PA")
                                    ));
                                }
                                adaptadorPagos = new Adaptador_Pagos(pagosListado);
                                adaptadorPagos.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(final View view) {
                                        wReferencia = pagosListado.get(recyclerViewPagos.getChildAdapterPosition(view)).getReferencia();
                                        wCaja = pagosListado.get(recyclerViewPagos.getChildAdapterPosition(view)).getCaja();
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        LayoutInflater factory = LayoutInflater.from(getContext());
                                        final View textEntryView = factory.inflate(R.layout.ventana_detallepago, null);
                                        txtTotalPago = textEntryView.findViewById(R.id.txtTotalPago);
                                        txtTotalPago.setText(Formato.format(wTotalPago));
                                        adaptadorRecibo = new Adaptador_Recibo(recibosListado);
                                        rvDetallePago = textEntryView.findViewById(R.id.rvDetallePago);
                                        builder.setView(textEntryView);
                                        wURL_CargarRecibo = wServidorWeb + "usuario_detalle_pago.php?CAJA="+wCaja+"&REFERENCIA="+wReferencia;
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                new reciboUsuario().execute();
                                            }
                                        }, 10);
                                        final AlertDialog dialog = builder.create();
                                        dialog.show();
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        btnAceptar = textEntryView.findViewById(R.id.btnAceptar_VentanaDetallePago);
                                        btnAceptar.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                });
                                recyclerViewPagos.setAdapter(adaptadorPagos);
                                Procesando.hideProgress();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                AlertaError ();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Procesando.hideProgress();
                    AlertaError ();
                    //Toast.makeText(getContext(),"Error al Conectar",Toast.LENGTH_SHORT).show();
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            Volley.newRequestQueue(getContext()).add(stringRequest);
            return null;
        }
        protected void onPostExecute(String file_url) {

        }
    }

    class reciboUsuario extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            CustomProgress Procesando = CustomProgress.getInstance();
            Procesando.showProgress(getContext(), "Espere un Momento...", true);
        }
        protected String doInBackground(String... args) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, wURL_CargarRecibo,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                wTotalPago = 0.00;
                                recibosListado.clear();
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject pago = array.getJSONObject(i);
                                    recibosListado.add(new DetalleRecibo(
                                            pago.getString("CONC_PA"),
                                            pago.getString("DECO_PA"),
                                            pago.getString("MONT_PA")
                                    ));
                                    wTotalPago = wTotalPago + pago.getDouble("MONT_PA");
                                }

                                //txtTotalPago = textEntryView.findViewById(R.id.txtTotalPago);
                                txtTotalPago.setText(Formato.format(wTotalPago));
                                adaptadorRecibo = new Adaptador_Recibo(recibosListado);
                                //rvDetallePago = textEntryView.findViewById(R.id.rvDetallePago);
                                rvDetallePago.setHasFixedSize(true);
                                rvDetallePago.setLayoutManager(new LinearLayoutManager(getContext()));
                                rvDetallePago.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                                rvDetallePago.setAdapter(adaptadorRecibo);

                                Procesando.hideProgress();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Procesando.hideProgress();
                    AlertaError ();
                    //Toast.makeText(getContext(),"Error al Conectar",Toast.LENGTH_SHORT).show();
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            Volley.newRequestQueue(getContext()).add(stringRequest);
            return null;
        }
        protected void onPostExecute(String file_url) {

        }
    }

    public void AlertaError () {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("");
        alertDialog.setMessage("Ocurrio un error al conectar con el servidor, por lo que tu solicitud no puede ser procesada." + "\n" + "\n" + "Lamentamos los inconvenientes.");
        alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }
}
