package com.guroh.jmascamargousuarios.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.guroh.jmascamargousuarios.Adapters.Adaptador_Reportes;
import com.guroh.jmascamargousuarios.Clases.CustomProgress;
import com.guroh.jmascamargousuarios.Models.DetalleReportes;
import com.guroh.jmascamargousuarios.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reportes extends Fragment {
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    CustomProgress Procesando = CustomProgress.getInstance();
    String wServidorWeb;
    String wURL_CargarReportes, wURL_CrearReporte;

    String wIdUsuario, wIdReporte;
    String wDetalleReporte, wRespuestaReporte;
    TextView txtDetalleReporte, txtRespuestaReporte;
    EditText etDetalleReporte;

    private RecyclerView recyclerViewReportes;

    private Adaptador_Reportes adaptadorReportes;
    List<DetalleReportes> reportesListado;

    Button btnAceptar, btnEnviarReporte, btnCancelar;

    public Reportes() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cargarConfiguracion();

        wIdUsuario = getArguments().getString("pIdUsuario");
        View view = inflater.inflate(R.layout.fragment_reportes, container, false);
        recyclerViewReportes = view.findViewById(R.id.recyclerViewReportes_Reportes);
        recyclerViewReportes.setHasFixedSize(true);
        recyclerViewReportes.setLayoutManager(new LinearLayoutManager(getContext()));
        reportesListado = new ArrayList<>();

        wURL_CargarReportes = wServidorWeb + "usuario_reportes.php?IDUSUARIO="+wIdUsuario;
        new reportesUsuario().execute();

        btnEnviarReporte = view.findViewById(R.id.btnCrearReporte_Reportes);
        btnEnviarReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearReporte();
            }
        });

        return view;
    }

    public void cargarConfiguracion() {
        SharedPreferences prefs = getActivity().getSharedPreferences("Principal", Context.MODE_PRIVATE);
        wServidorWeb = prefs.getString("ServidorWeb", "");
    }

    class reportesUsuario extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Procesando.showProgress(getContext(), "Espere un Momento...", true);
        }
        protected String doInBackground(String... args) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, wURL_CargarReportes,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject reportes = array.getJSONObject(i);
                                    reportesListado.add(new DetalleReportes(
                                            reportes.getString("IDREPORTE"),
                                            reportes.getString("DETA_RE"),
                                            reportes.getString("FECH_RE"),
                                            reportes.getString("RESP_RE"),
                                            reportes.getString("FERE_RE"),
                                            reportes.getString("ESTA_RE")
                                    ));
                                }
                                adaptadorReportes = new Adaptador_Reportes(reportesListado);
                                adaptadorReportes.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(final View view) {
                                        wIdReporte = reportesListado.get(recyclerViewReportes.getChildAdapterPosition(view)).getIdReporte();
                                        wDetalleReporte = reportesListado.get(recyclerViewReportes.getChildAdapterPosition(view)).getDetalleReporte();
                                        wRespuestaReporte = reportesListado.get(recyclerViewReportes.getChildAdapterPosition(view)).getRespuestaReporte();

                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        LayoutInflater factory = LayoutInflater.from(getContext());
                                        final View textEntryView = factory.inflate(R.layout.ventana_detalle_reporte, null);
                                        txtDetalleReporte = textEntryView.findViewById(R.id.txtDetalleReporte_DetalleReporte);
                                        txtRespuestaReporte = textEntryView.findViewById(R.id.txtRespuestaReporte_DetalleReporte);
                                        builder.setView(textEntryView);
                                       // wURL_CargarReporte = wServidorWeb + "usuario_detalle_pago.php?CAJA="+wCaja+"&REFERENCIA="+wReferencia;
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                txtDetalleReporte.setText(wDetalleReporte);
                                                txtRespuestaReporte.setText(wRespuestaReporte);
                                                //new detalleReporte().execute();
                                            }
                                        }, 10);
                                        final AlertDialog dialog = builder.create();
                                        dialog.show();
                                        btnAceptar = textEntryView.findViewById(R.id.btnAceptar_VentanaDetalleReporte);
                                        btnAceptar.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                });
                                recyclerViewReportes.setAdapter(adaptadorReportes);
                                Procesando.hideProgress();
                            } catch (JSONException e) {
                                Procesando.hideProgress();
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

    public void CrearReporte() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View textEntryView = factory.inflate(R.layout.ventana_crear_reporte, null);
        etDetalleReporte = textEntryView.findViewById(R.id.etDetalleReporte_VentanaCrearReporte);
        builder.setView(textEntryView);
        wURL_CrearReporte = wServidorWeb + "usuario_crear_reporte.php?IDUSUARIO="+wIdUsuario+"&DETALLE="+wDetalleReporte;
        final Handler handler = new Handler();
        final AlertDialog dialog = builder.create();
        dialog.show();
        btnAceptar = textEntryView.findViewById(R.id.btnEnviarReporte_VentanaDetalleReporte);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                wDetalleReporte = etDetalleReporte.getText().toString();
                //Toast.makeText(getContext(),"Reporte " + wDetalleReporte,Toast.LENGTH_SHORT).show();
                new crearReporte().execute();
            }
        });
        btnCancelar = textEntryView.findViewById(R.id.btnCancelar_VentanaDetalleReporte);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public class crearReporte extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Procesando.showProgress(getContext(), "Espere un Momento...", true);
        }
        protected String doInBackground(String... args) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, wURL_CrearReporte, new Response.Listener<String>() {
                public void onResponse(String response) {
                    Procesando.hideProgress();
                    ReporteCreado();
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),"Error : " + error,Toast.LENGTH_SHORT).show();
                    Procesando.hideProgress();
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("IDUSUARIO", wIdUsuario);
                    parametros.put("DETALLE", wDetalleReporte);

                    return parametros;
                }
            };
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

    public void ReporteCreado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View textEntryView = factory.inflate(R.layout.ventana_reporte_creado, null);
        builder.setView(textEntryView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        btnAceptar = textEntryView.findViewById(R.id.btnAceptar_VentanaReporteCreado);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                reportesListado.clear();
                new reportesUsuario().execute();
            }
        });
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