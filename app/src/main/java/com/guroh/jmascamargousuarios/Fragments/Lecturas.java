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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.guroh.jmascamargousuarios.Adapters.Adaptador_Lecturas;
import com.guroh.jmascamargousuarios.Clases.CustomProgress;
import com.guroh.jmascamargousuarios.Models.DetalleLecturas;
import com.guroh.jmascamargousuarios.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Lecturas extends Fragment {
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    CustomProgress Procesando = CustomProgress.getInstance();
    String wServidorWeb;
    String wURL_CargarLecturas;

    String wIdUsuario;

    private RecyclerView recyclerViewLecturas;

    private Adaptador_Lecturas adaptadorLecturas;
    List<DetalleLecturas> lecturasListado;

    public Lecturas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cargarConfiguracion();

        wIdUsuario = getArguments().getString("pIdUsuario");
        View view = inflater.inflate(R.layout.fragment_lecturas, container, false);
        recyclerViewLecturas = view.findViewById(R.id.recyclerViewLecturas);
        recyclerViewLecturas.setHasFixedSize(true);
        recyclerViewLecturas.setLayoutManager(new LinearLayoutManager(getContext()));
        lecturasListado = new ArrayList<>();

        wURL_CargarLecturas = wServidorWeb + "usuario_lecturas.php?IDUSUARIO="+wIdUsuario;
        new lecturasUsuario().execute();

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_lecturas, container, false);
        return view;
    }

    public void cargarConfiguracion() {
        SharedPreferences prefs = getActivity().getSharedPreferences("Principal", Context.MODE_PRIVATE);
        wServidorWeb = prefs.getString("ServidorWeb", "");
    }

    class lecturasUsuario extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Procesando.showProgress(getContext(), "Espere un Momento...", true);
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
