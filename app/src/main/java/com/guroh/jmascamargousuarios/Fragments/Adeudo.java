package com.guroh.jmascamargousuarios.Fragments;


import android.app.AlertDialog;
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
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.guroh.jmascamargousuarios.Adapters.Adaptador_Adeudo;
import com.guroh.jmascamargousuarios.Clases.CustomProgress;
import com.guroh.jmascamargousuarios.Models.DetalleAdeudo;
import com.guroh.jmascamargousuarios.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Adeudo extends Fragment {
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    CustomProgress Procesando = CustomProgress.getInstance();
    String wServidorWeb;
    String wURL_CargarAdeudo;
    String wIdUsuario;
    double wDescuentoSocial, wSubtotal, wIVA, wTotal;
    TextView etDescuentoSocial, etSubtotal, etIVA, etTotal;
    private RecyclerView recyclerViewAdeudo;
    private Adaptador_Adeudo adaptadorAdeudo;
    List<DetalleAdeudo> adeudoListado;

    DecimalFormat Formato = new DecimalFormat("###,##0.00");

    public Adeudo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CargarConfiguracion();

        wIdUsuario = getArguments().getString("pIdUsuario");
        View view = inflater.inflate(R.layout.fragment_adeudo, container, false);
        recyclerViewAdeudo = view.findViewById(R.id.recyclerViewAdeudo);
        recyclerViewAdeudo.setHasFixedSize(true);
        recyclerViewAdeudo.setLayoutManager(new LinearLayoutManager(getContext()));

        adeudoListado = new ArrayList<>();

        etDescuentoSocial = view.findViewById(R.id.etDescuentoSocial);
        etSubtotal        = view.findViewById(R.id.etSubtotal);
        etIVA             = view.findViewById(R.id.etIVA);
        etTotal           = view.findViewById(R.id.etTotal);

        wURL_CargarAdeudo   = wServidorWeb + "usuario_adeudo.php?IDUSUARIO="+wIdUsuario;
        new adeudoUsuario().execute();
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_adeudo, container, false);
        return view;
    }

    public void CargarConfiguracion() {
        SharedPreferences prefs = getActivity().getSharedPreferences("Principal", Context.MODE_PRIVATE);
        wServidorWeb = prefs.getString("ServidorWeb", "");
    }

    class adeudoUsuario extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Procesando.showProgress(getContext(), "Espere un Momento...", true);
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
                                    JSONObject adeudo = array.getJSONObject(i);
                                    adeudoListado.add(new DetalleAdeudo(
                                            adeudo.getString("CONC_AD"),
                                            adeudo.getString("DECO_AD"),
                                            adeudo.getString("MONT_AD"),
                                            adeudo.getString("SUBT_AD"),
                                            adeudo.getString("IVA_CC"),
                                            adeudo.getString("TOTA_AD")
                                    ));

                                    if (adeudo.getDouble("MONT_AD") > 0.00){
                                        wSubtotal = wSubtotal + adeudo.getDouble("MONT_AD");
                                    }else{
                                        wDescuentoSocial = wDescuentoSocial + adeudo.getDouble("MONT_AD");
                                    }
                                    wIVA = wIVA + adeudo.getDouble("IVA_CC");
                                }
                                etDescuentoSocial.setText(Formato.format(wDescuentoSocial));
                                etSubtotal.setText(Formato.format(wSubtotal));
                                wTotal = (wSubtotal + wDescuentoSocial) + wIVA;
                                etIVA.setText((Formato.format(wIVA)));
                                etTotal.setText(Formato.format(wTotal));
                                adaptadorAdeudo = new Adaptador_Adeudo(adeudoListado);
                                recyclerViewAdeudo.setAdapter(adaptadorAdeudo);
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
