package com.guroh.jmascamargousuarios.Models;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.guroh.jmascamargousuarios.Adapters.Adaptador_Adeudo;
import com.guroh.jmascamargousuarios.Adapters.Adaptador_Lecturas;
import com.guroh.jmascamargousuarios.Adapters.Adaptador_Pagos;
import com.guroh.jmascamargousuarios.Adapters.Adaptador_Recibo;
import com.guroh.jmascamargousuarios.Clases.CustomProgress;
import com.guroh.jmascamargousuarios.Fragments.Adeudo;
import com.guroh.jmascamargousuarios.Fragments.Lecturas;
import com.guroh.jmascamargousuarios.Fragments.Pagos;
import com.guroh.jmascamargousuarios.Fragments.Reportes;
import com.guroh.jmascamargousuarios.R;
import com.guroh.jmascamargousuarios.webview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetalleCuenta extends AppCompatActivity {
    CustomProgress Procesando = CustomProgress.getInstance();
    RequestQueue requestQueue;
    String wURL_CargarUsuario, wURL_CargarAdeudo, wURL_CargarLecturas, wURL_CargarPagos, wURL_CargarRecibo;
    String wClaveRegistro, wNombreRegistro;
    String wIdUsuario;
    String wCaja, wReferencia;
    double wTotalPago;
    TextView etIdUsuario, etCuenta, etNombre, etDireccion, etMedidor, etTarifa;
    TextView etDescuentoSocial, etSubtotal, etIVA, etTotal;
    TextView txtTotalPago;
    FrameLayout fmAdeudos, fmAdeudos2, fmPagos, fmLecturas;
    Button btnAceptar;

    double wDescuentoSocial, wSubtotal, wIVA, wTotal;
    private RecyclerView recyclerViewLecturas;
    private RecyclerView rvDetallePago;

    private Adaptador_Lecturas adaptadorLecturas;
    List<DetalleLecturas> lecturasListado;

    private Adaptador_Adeudo adaptadorAdeudo;
    List<DetalleAdeudo> adeudoListado;

    private Adaptador_Pagos adaptadorPagos;
    List<DetallePagos> pagosListado;

    private Adaptador_Recibo adaptadorRecibo;
    List<DetalleRecibo> recibosListado;

    DecimalFormat Formato = new DecimalFormat("###,##0.00");

    BottomNavigationView nvMenu;

    Fragment Adeudo, Pagos, Lecturas, Reportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cuenta);

        CargarConfiguracion();

        Bundle datos = this.getIntent().getExtras();
        wIdUsuario = datos.getString("wIdUsuario");
        //wIdUsuario = "6017";
        lecturasListado = new ArrayList<>();
        adeudoListado = new ArrayList<>();
        pagosListado = new ArrayList<>();


        recibosListado = new ArrayList<>();


        etIdUsuario = findViewById(R.id.etIdUsuario);
        etCuenta    = findViewById(R.id.etCuenta);
        etTarifa    = findViewById(R.id.etTarifa);
        etNombre    = findViewById(R.id.etNombre);
        etDireccion = findViewById(R.id.etDireccion);
        etMedidor   = findViewById(R.id.etMedidor);


        wURL_CargarUsuario  = "http://www.jmascamargo.com.mx/app/" + "datos_usuario.php?IDUSUARIO="+wIdUsuario;
        wURL_CargarAdeudo   = "http://www.jmascamargo.com.mx/app/" + "usuario_adeudo.php?IDUSUARIO="+wIdUsuario;
        wURL_CargarLecturas = "http://www.jmascamargo.com.mx/app/" + "usuario_lecturas.php?IDUSUARIO="+wIdUsuario;
        wURL_CargarPagos    = "http://www.jmascamargo.com.mx/app/" + "usuario_pagos.php?IDUSUARIO="+wIdUsuario;
        wURL_CargarRecibo   = "http://www.jmascamargo.com.mx/app/" + "usuario_detalle_pago.php?CAJA="+wCaja+"&REFERENCIA="+wReferencia;
        new datosUsuario().execute();

        nvMenu = findViewById(R.id.nvMenu);
        nvMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()== R.id.principal){
                    finish();
                }
                if (menuItem.getItemId()== R.id.adeudo){
                    Bundle args = new Bundle();
                    args.putString("pIdUsuario", wIdUsuario);
                    Adeudo = new Adeudo();
                    Adeudo.setArguments(args);
                    showSelectedFragment(Adeudo);
                }
                if (menuItem.getItemId()== R.id.pagos){
                    Bundle args = new Bundle();
                    args.putString("pIdUsuario", wIdUsuario);
                    Pagos = new Pagos();
                    Pagos.setArguments(args);
                    showSelectedFragment(Pagos);
                }
                if (menuItem.getItemId()== R.id.lecturas){
                    Bundle args = new Bundle();
                    args.putString("pIdUsuario", wIdUsuario);
                    Lecturas = new Lecturas();
                    Lecturas.setArguments(args);
                    showSelectedFragment(Lecturas);
                }
                if (menuItem.getItemId()== R.id.reporte){
                    Bundle args = new Bundle();
                    args.putString("pIdUsuario", wIdUsuario);
                    Reportes = new Reportes();
                    Reportes.setArguments(args);
                    showSelectedFragment(Reportes);
                }
                return true;
            }
        });
        //pagosListado.clear();
        //new pagosUsuario().execute();
        //wURL_CargarRecibo = "http://www.jmascamargo.com.mx/app/" + "usuario_detalle_pago.php?CAJA=2&REFERENCIA=276409";
        //Toast.makeText(getApplicationContext(),""+wURL_CargarRecibo,Toast.LENGTH_SHORT).show();
        //new reciboUsuario().execute();
    }

    public void CargarConfiguracion() {
        SharedPreferences prefs = getSharedPreferences("Principal", Context.MODE_PRIVATE);
        wClaveRegistro   = prefs.getString("ClaveRegistro", "");
        wNombreRegistro  = prefs.getString("NombreRegistro", "");
    }

    private void showSelectedFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void Adeudo (View view){
        Toast.makeText(this,""+wURL_CargarAdeudo,Toast.LENGTH_SHORT).show();
        fmAdeudos.setVisibility(View.VISIBLE);
        fmAdeudos2.setVisibility(View.VISIBLE);
        fmPagos.setVisibility(View.INVISIBLE);
        fmLecturas.setVisibility(View.INVISIBLE);
        adeudoListado.clear();
        //new adeudoUsuario().execute();
    }

    public void Lecturas (View view){
        fmAdeudos.setVisibility(View.INVISIBLE);
        fmAdeudos2.setVisibility(View.INVISIBLE);
        fmPagos.setVisibility(View.INVISIBLE);
        fmLecturas.setVisibility(View.VISIBLE);
        lecturasListado.clear();
        new lecturasUsuario().execute();
    }

    public void Pagos (View view){
        fmAdeudos.setVisibility(View.INVISIBLE);
        fmAdeudos2.setVisibility(View.INVISIBLE);
        fmPagos.setVisibility(View.VISIBLE);
        fmLecturas.setVisibility(View.INVISIBLE);
        pagosListado.clear();
        new pagosUsuario().execute();
    }

    public void Pagar (View view){
        Intent intent = new Intent(this, webview.class);
        intent.putExtra("wCuenta", etCuenta.getText().toString());
        startActivity(intent);
    }

    class datosUsuario extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Procesando.showProgress(DetalleCuenta.this, "Espere un Momento...", false);
        }
        protected String doInBackground(String... args) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(wURL_CargarUsuario, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    JSONObject jsonObject = null;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            etIdUsuario.setText(jsonObject.getString("IDUSUARIO"));
                            etCuenta.setText(jsonObject.getString("CLAV_US"));
                            etNombre.setText(jsonObject.getString("NOMB_US"));
                            etDireccion.setText(jsonObject.getString("DIRE_US"));
                            etTarifa.setText(jsonObject.getString("TARI_US"));
                            etMedidor.setText(jsonObject.getString("MEDI_US"));
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Error al Procesar",Toast.LENGTH_SHORT).show();
                        }
                    }
                    Procesando.hideProgress();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Procesando.hideProgress();
                    Toast.makeText(getApplicationContext(),"Error en la Conexion",Toast.LENGTH_SHORT).show();
                }
            }
            );
            requestQueue = Volley.newRequestQueue(DetalleCuenta.this);
            requestQueue.add(jsonArrayRequest);
            return null;
        }
        protected void onPostExecute(String file_url) {
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });
        }
    }

    class lecturasUsuario extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            Procesando.showProgress(DetalleCuenta.this, "Espere un Momento...", false);
        }
        protected String doInBackground(String... args) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, wURL_CargarLecturas,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject lecturas = array.getJSONObject(i);
                                    lecturasListado.add(new DetalleLecturas(
                                            lecturas.getString("PERI_LE"),
                                            lecturas.getString("LANT_LE"),
                                            lecturas.getString("LACT_LE"),
                                            lecturas.getString("CONS_LE")
                                    ));
                                }
                                adaptadorLecturas = new Adaptador_Lecturas(lecturasListado);
                                recyclerViewLecturas.setAdapter(adaptadorLecturas);
                                Procesando.hideProgress();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Procesando.hideProgress();
                    Toast.makeText(getApplicationContext(),"Error al Conectar",Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(DetalleCuenta.this).add(stringRequest);
            return null;
        }
        protected void onPostExecute(String file_url) {

        }
    }

/*
    class adeudoUsuario extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Procesando = new ProgressDialog(DetalleCuenta.this);
            Procesando.setMessage("Actualizando Adeudo, Espere...");
            Procesando.setIndeterminate(false);
            Procesando.setCancelable(false);
            Procesando.show();
            //Procesando.setContentView(R.layout.cargador);
        }
        protected String doInBackground(String... args) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, wURL_CargarAdeudo,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                wSubtotal = 0.00;
                                wIVA = 0.00;
                                wSubtotal = 0.00;
                                wDescuentoSocial = 0.00;
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject Adeudo = array.getJSONObject(i);
                                    adeudoListado.add(new DetalleAdeudo(
                                            Adeudo.getString("CONC_AD"),
                                            Adeudo.getString("DECO_AD"),
                                            Adeudo.getString("MONT_AD"),
                                            Adeudo.getString("SUBT_AD"),
                                            Adeudo.getString("IVA_CC"),
                                            Adeudo.getString("TOTA_AD")
                                    ));

                                    if (Adeudo.getDouble("MONT_AD") > 0.00){
                                        wSubtotal = wSubtotal + Adeudo.getDouble("MONT_AD");
                                    }else{
                                        wDescuentoSocial = wDescuentoSocial + Adeudo.getDouble("MONT_AD");
                                    }
                                    wIVA = wIVA + Adeudo.getDouble("IVA_CC");
                                }
                                etDescuentoSocial.setText(Formato.format(wDescuentoSocial));
                                etSubtotal.setText(Formato.format(wSubtotal));
                                wTotal = (wSubtotal + wDescuentoSocial) + wIVA;
                                etIVA.setText((Formato.format(wIVA)));
                                etTotal.setText(Formato.format(wTotal));
                                adaptadorAdeudo = new Adaptador_Adeudo(adeudoListado);
                                recyclerViewLecturas.setAdapter(adaptadorAdeudo);
                                Procesando.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Procesando.dismiss();
                    Toast.makeText(getApplicationContext(),"Error al Conectar",Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(DetalleCuenta.this).add(stringRequest);
            return null;
        }
        protected void onPostExecute(String file_url) {

        }
    }
*/
    class pagosUsuario extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            Procesando.showProgress(DetalleCuenta.this, "Espere un Momento...", false);
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
                                        wReferencia = pagosListado.get(recyclerViewLecturas.getChildAdapterPosition(view)).getReferencia();
                                        wCaja = pagosListado.get(recyclerViewLecturas.getChildAdapterPosition(view)).getCaja();
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(DetalleCuenta.this);
                                        LayoutInflater factory = LayoutInflater.from(getApplicationContext());
                                        final View textEntryView = factory.inflate(R.layout.ventana_detallepago, null);
                                        txtTotalPago = textEntryView.findViewById(R.id.txtTotalPago);
                                        txtTotalPago.setText(Formato.format(wTotalPago));
                                        adaptadorRecibo = new Adaptador_Recibo(recibosListado);
                                        rvDetallePago = textEntryView.findViewById(R.id.rvDetallePago);

                                        builder.setView(textEntryView);
                                        wURL_CargarRecibo = "http://www.jmascamargo.com.mx/app/" + "usuario_detalle_pago.php?CAJA="+wCaja+"&REFERENCIA="+wReferencia;
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                new reciboUsuario().execute();
                                            }
                                        }, 10);
                                        final AlertDialog dialog = builder.create();
                                        dialog.show();
                                        btnAceptar = textEntryView.findViewById(R.id.btnAceptar_VentanaDetallePago);
                                        btnAceptar.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                });
                                recyclerViewLecturas.setAdapter(adaptadorPagos);
                                Procesando.hideProgress();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Procesando.hideProgress();
                    Toast.makeText(getApplicationContext(),"Error al Conectar",Toast.LENGTH_SHORT).show();
                }
            });
            Procesando.hideProgress();
            Volley.newRequestQueue(DetalleCuenta.this).add(stringRequest);
            return null;
        }
        protected void onPostExecute(String file_url) {

        }
    }

    class reciboUsuario extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Procesando.showProgress(DetalleCuenta.this, "Espere un Momento...", false);
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
                                rvDetallePago.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                rvDetallePago.addItemDecoration(new DividerItemDecoration(DetalleCuenta.this, DividerItemDecoration.VERTICAL));
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
                    Toast.makeText(getApplicationContext(),"Error al Conectar",Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(DetalleCuenta.this).add(stringRequest);
            return null;
        }
        protected void onPostExecute(String file_url) {

        }
    }
}
