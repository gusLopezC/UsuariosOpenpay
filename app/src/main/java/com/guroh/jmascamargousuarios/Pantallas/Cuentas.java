package com.guroh.jmascamargousuarios.Pantallas;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.guroh.jmascamargousuarios.Adapters.Adaptador_Cuentas;
import com.guroh.jmascamargousuarios.Clases.CustomProgress;
import com.guroh.jmascamargousuarios.Models.DetalleCuenta;
import com.guroh.jmascamargousuarios.Models.DetalleCuentas;
import com.guroh.jmascamargousuarios.R;
import com.tooltip.Tooltip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cuentas extends AppCompatActivity {
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    CustomProgress Procesando = CustomProgress.getInstance();

    RequestQueue requestQueue;
    String wServidorWeb;
    String wClaveRegistro, wNombreRegistro;
    String wURL_UsuarioCuentas, wURL_CargarUsuario, wURL_ValidarCodigo, wURL_GrabarCuenta, wURL_EliminarCuenta;
    String wIdUsuario, wCuenta, wCodigo, wValido, wAlias, wSaldo;
    String pCLAV_RE, pIDUSUARIO, pALIAS;
    Button btnConsultar, btnValidar, btnAgregar, btnCancelar, btnAceptar;
    TextView etIdUsuario, etCuenta, etNombre, etDireccion, etCodigo, etAlias;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewCuentas;
    private Adaptador_Cuentas adaptadorCuentas;
    List<DetalleCuentas> cuentasListado;
    int iwIdUsuario;

    int wIdUsuarioL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuentas);
        FloatingActionButton fab = findViewById(R.id.fab);

        CargarConfiguracion();

        recyclerViewCuentas = findViewById(R.id.recyclerViewCuentas);
        recyclerViewCuentas.setHasFixedSize(true);
        recyclerViewCuentas.setLayoutManager(new LinearLayoutManager(this));
        cuentasListado = new ArrayList<>();

        swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cuentasListado.clear();
                wURL_UsuarioCuentas  = wServidorWeb + "usuario_cuentas.php?CLAV_RE="+wClaveRegistro;
                new cuentasUsuario().execute();
                adaptadorCuentas.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        wURL_UsuarioCuentas  = wServidorWeb + "usuario_cuentas.php?CLAV_RE="+wClaveRegistro;
        new cuentasUsuario().execute();


/*
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // remove item from adapter

                Toast.makeText(getApplicationContext(),"DERECHA",Toast.LENGTH_SHORT).show();
            }


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                // move item in `fromPos` to `toPos` in adapter.
                return true;// true if moved, false otherwise
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewCuentas);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback2 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // remove item from adapter
                wIdUsuarioL = viewHolder.getAdapterPosition();
                EliminarCuenta();
                Toast.makeText(getApplicationContext(),"IZQUIERDA",Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"" + wIdUsuarioL,Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                //wReferencia = pagosListado.get(recyclerViewPagos.getChildAdapterPosition(view)).getReferencia();
                //iwIdUsuario = cuentas.get(recyclerView.getChildAdapterPosition(view)).get
                // move item in `fromPos` to `toPos` in adapter.
                return true;// true if moved, false otherwise
            }
        };
        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(simpleItemTouchCallback2);
        itemTouchHelper2.attachToRecyclerView(recyclerViewCuentas);

 */
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.cerrar_sesion) {
            CerrarSesion();
        }
        return true ;
    }

    public void CargarConfiguracion() {
        SharedPreferences prefs = getSharedPreferences("Principal", Context.MODE_PRIVATE);
        wServidorWeb = prefs.getString("ServidorWeb", "");
        wClaveRegistro = prefs.getString("ClaveRegistro", "");
        wNombreRegistro = prefs.getString("NombreRegistro", "");
    }

    public void AgregarCuenta (View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(Cuentas.this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.ventana_registro_cuenta, null);
        builder.setView(textEntryView);
        etIdUsuario = textEntryView.findViewById(R.id.etIdUsuario);
        etIdUsuario.requestFocus();
        etCuenta = textEntryView.findViewById(R.id.etCuenta);
        etNombre = textEntryView.findViewById(R.id.etNombre);
        etDireccion = textEntryView.findViewById(R.id.etDireccion);
        etCodigo = textEntryView.findViewById(R.id.etCodigo);
        etAlias = textEntryView.findViewById(R.id.etAlias);
        btnConsultar = textEntryView.findViewById(R.id.btnConsultar);

        etCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = (TextView) v;
                final Tooltip tooltip = new Tooltip.Builder(txt)
                        .setText("El Código de Verificación se encuentra impreso en el Recibo de Servicio")
                        .setTextColor(Color.parseColor("#ffffff"))
                        .setGravity(Gravity.TOP)
                        .setCornerRadius(8f)
                        .setDismissOnClick(true)
                        .setArrowHeight(18f)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        tooltip.dismiss();
                    }
                },5000);
            }
        });

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wIdUsuario = etIdUsuario.getText().toString();
                wCuenta = etIdUsuario.getText().toString();
                etIdUsuario.setError(null);
                if ("".equals(wIdUsuario)) {
                    etIdUsuario.setError("Id de Usuario");
                    etIdUsuario.requestFocus();
                    return;
                }else {
                    wURL_CargarUsuario = wServidorWeb + "datos_usuario.php?IDUSUARIO="+wIdUsuario;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etIdUsuario.getWindowToken(), 0);
                    new datosUsuario().execute(wIdUsuario, wCuenta);
                }
            }
        });

        btnValidar = textEntryView.findViewById(R.id.btnValidar);
        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wIdUsuario = etIdUsuario.getText().toString();
                wCodigo = etCodigo.getText().toString();
                etIdUsuario.setError(null);
                if ("".equals(wCodigo)) {
                    etCodigo.setError("Captura un Código de Verificaión");
                    etCodigo.requestFocus();
                    return;
                }else {
                    wURL_ValidarCodigo = wServidorWeb + "validar_codigo.php?IDUSUARIO=" + wIdUsuario + "&CODIGO=" + wCodigo;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etCodigo.getWindowToken(), 0);
                    new validarCodigo().execute();
                }
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnAgregar = textEntryView.findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wAlias = etAlias.getText().toString().trim();
                etAlias.setError(null);
                if ("".equals(wAlias)) {
                    etAlias.setError("Captura un Alias para la cuenta");
                    etAlias.requestFocus();
                    return;
                }else {
                    wURL_GrabarCuenta = wServidorWeb + "usuario_grabar.php";
                    new agregarCuenta().execute();
                    dialog.dismiss();
                }
            }
        });
        btnCancelar = textEntryView.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    class cuentasUsuario extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Procesando.showProgress(Cuentas.this, "Espere un Momento...", false);
        }
        protected String doInBackground(String... strings) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, wURL_UsuarioCuentas,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject cuentas = array.getJSONObject(i);
                                    cuentasListado.add(new DetalleCuentas(
                                            cuentas.getString("IDUSUARIO"),
                                            cuentas.getString("CLAV_US"),
                                            cuentas.getString("NOMB_US"),
                                            cuentas.getString("DIRE_US"),
                                            cuentas.getString("ALIAS"),
                                            cuentas.getString("SALD_US")
                                    ));
                                }
                                adaptadorCuentas = new Adaptador_Cuentas(cuentasListado);
                                adaptadorCuentas.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View view) {
                                            wIdUsuario = cuentasListado.get(recyclerViewCuentas.getChildAdapterPosition(view)).getIdusuario();
                                            wSaldo     = cuentasListado.get(recyclerViewCuentas.getChildAdapterPosition(view)).getSaldo();
                                            //EliminarCuenta();
                                            Bundle extras = new Bundle();
                                            extras.putString("IdUsuario",wIdUsuario);
                                            extras.putString("Saldo",wSaldo);
                                            Intent intent = new Intent(Cuentas.this, AddCardActivity.class);
                                            intent.putExtras(extras);
                                            startActivity(intent);
                                            return false;
                                        }
                                });
                                adaptadorCuentas.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(final View view) {
                                        String wIdUsuario = cuentasListado.get(recyclerViewCuentas.getChildAdapterPosition(view)).getIdusuario();
                                        Intent intent = new Intent(Cuentas.this, DetalleCuenta.class);
                                        intent.putExtra("wIdUsuario", wIdUsuario);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                                    }


                                });
                                recyclerViewCuentas.setAdapter(adaptadorCuentas);
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
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            Volley.newRequestQueue(Cuentas.this).add(stringRequest);

            return null;
        }
        protected void onPostExecute(String s) {
        }
    }

    class datosUsuario extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Procesando.showProgress(Cuentas.this, "Espere un Momento...", false);
        }
        protected String doInBackground(String... args) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(wURL_CargarUsuario, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getJSONObject(0);
                        etCuenta.setText(jsonObject.getString("CLAV_US"));
                        etNombre.setText(jsonObject.getString("NOMB_US"));
                        etDireccion.setText(jsonObject.getString("DIRE_US"));
                        etCodigo.requestFocus();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),"Error al Procesar",Toast.LENGTH_SHORT).show();
                    }
                    Procesando.hideProgress();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Procesando.hideProgress();
                    AlertaError ();
                    //Toast.makeText(getApplicationContext(),"Error en la Conexion",Toast.LENGTH_SHORT).show();
                }
            }
            );
            requestQueue = Volley.newRequestQueue(Cuentas.this);
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

    class validarCodigo extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Procesando.showProgress(Cuentas.this, "Espere un Momento...", false);
        }
        protected String doInBackground(String... args) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(wURL_ValidarCodigo, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getJSONObject(0);
                        wValido = jsonObject.getString("VALIDO");
                        if (wValido.equals("0")){
                            ValidacionNegativa();
                        }else{
                            ValidacionPositiva();
                        }
                    } catch (JSONException e) {
                        //Toast.makeText(getApplicationContext(),"Error al Procesar",Toast.LENGTH_SHORT).show();
                        AlertaError ();
                    }
                    Procesando.hideProgress();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Procesando.hideProgress();
                    AlertaError ();
                    //Toast.makeText(getApplicationContext(),"Error en la Conexion",Toast.LENGTH_SHORT).show();
                }
            }
            );
            requestQueue = Volley.newRequestQueue(Cuentas.this);
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

    public void ValidacionPositiva (){
        AlertDialog.Builder builder = new AlertDialog.Builder(Cuentas.this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.ventana_validacion_positiva, null);
        builder.setView(textEntryView);
        //etAlias = textEntryView.findViewById(R.id.etAlias);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnAceptar = textEntryView.findViewById(R.id.btnAceptar_VentanaValidacionPositiva);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                etAlias.setEnabled(true);
                etAlias.requestFocus();
                btnAgregar.setEnabled(true);
            }
        });
    }

    public void ValidacionNegativa (){
        AlertDialog.Builder builder = new AlertDialog.Builder(Cuentas.this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.ventana_validacion_negativa, null);
        builder.setView(textEntryView);
        //etAlias = textEntryView.findViewById(R.id.etAlias);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnAceptar = textEntryView.findViewById(R.id.btnAceptar_VentanaValidacionNegativa);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                etCodigo.requestFocus();
            }
        });
    }

    public void EliminarCuenta(){
        wURL_EliminarCuenta  = wServidorWeb + "usuario_eliminar.php";
        AlertDialog.Builder builder = new AlertDialog.Builder(Cuentas.this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.ventana_eliminar_cuenta, null);
        builder.setView(textEntryView);
        //etAlias = textEntryView.findViewById(R.id.etAlias);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnAceptar = textEntryView.findViewById(R.id.btnAceptar_VentanaEliminarCuenta);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new eliminarCuenta().execute();
                dialog.dismiss();
            }
        });

        btnCancelar = textEntryView.findViewById(R.id.btnCancelar_VentanaEliminarCuenta);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    class eliminarCuenta extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Procesando.showProgress(Cuentas.this, "Espere un Momento...", false);
        }
        protected String doInBackground(String... args) {
            //pCLAV_RE = wClaveRegistro;
            pIDUSUARIO = wIdUsuario;
            //pALIAS = wAlias;


            StringRequest stringRequest = new StringRequest(Request.Method.POST, wURL_EliminarCuenta, new Response.Listener<String>(){
                public void onResponse(String response){
                    Procesando.hideProgress();
                    cuentasListado.clear();
                    new cuentasUsuario().execute();
                    //Toast.makeText(getApplicationContext(),"Operacion Exitosa",Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener(){
                public void onErrorResponse(VolleyError error){
                    Procesando.hideProgress();
                    AlertaError ();
                }
            }){
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<String, String>();
                    //parametros.put("CLAV_RE", pCLAV_RE);
                    parametros.put("IDUSUARIO", pIDUSUARIO);
                    //parametros.put("ALIASS", pALIAS);
                    return parametros;
                }
            };
            requestQueue = Volley.newRequestQueue(Cuentas.this);
            requestQueue.add(stringRequest);
            Procesando.hideProgress();
            return null;
        }
        protected void onPostExecute(String file_url) {

        }
    }

    class agregarCuenta extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Procesando.showProgress(Cuentas.this, "Espere un Momento...", false);
        }
        protected String doInBackground(String... args) {
            pCLAV_RE = wClaveRegistro;
            pIDUSUARIO = wIdUsuario;
            pALIAS = wAlias;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, wURL_GrabarCuenta, new Response.Listener<String>(){
                public void onResponse(String response){
                    Procesando.hideProgress();
                    cuentasListado.clear();
                    new cuentasUsuario().execute();
                    //Toast.makeText(getApplicationContext(),"Operacion Exitosa",Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener(){
                public void onErrorResponse(VolleyError error){
                    Procesando.hideProgress();
                    AlertaError ();
                }
            }){
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("CLAV_RE", pCLAV_RE);
                    parametros.put("IDUSUARIO", pIDUSUARIO);
                    parametros.put("ALIASS", pALIAS);
                    return parametros;
                }
            };
            requestQueue = Volley.newRequestQueue(Cuentas.this);
            requestQueue.add(stringRequest);
            Procesando.hideProgress();
            return null;
        }
        protected void onPostExecute(String file_url) {

        }
    }

    public void CerrarSesion() {
        SharedPreferences prefs = getSharedPreferences("Principal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("MantenerSesion", "0");
        editor.commit();
        finish();
        Intent intent = new Intent(this, Entrada.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cuentas.this);
        alertDialog.setTitle("");
        alertDialog.setMessage("Confirma que desea salir ?");
        Vibrator Vibrar = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        Vibrar.vibrate(300);
        Vibrar.vibrate(0);
        Vibrar.vibrate(300);
        alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    public void AlertaError () {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
        alertDialog.setTitle("");
        alertDialog.setMessage("Ocurrio un error al conectar con el servidor, por lo que tu solicitud no puede ser procesada." + "\n" + "\n" + "Lamentamos los inconvenientes.");
        alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }
}