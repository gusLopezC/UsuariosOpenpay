package com.guroh.jmascamargousuarios.Pantallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.guroh.jmascamargousuarios.Clases.CustomProgress;
import com.guroh.jmascamargousuarios.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Acceso extends AppCompatActivity {
    CustomProgress Procesando = CustomProgress.getInstance();
    RequestQueue requestQueue;

    String wURL_Login, wURL_VerificaUsuario, wURL_ActualizaToken, wURL_AltaUsuario;
    EditText etUsuarioRegistro, etClaveRegistro;
    String wUSUA_RE, wClaveRegistro, wNombreRegistro;
    CheckBox chMantener;

    EditText etCorreo, etTelefono, etContrasena;
    TextView txtVersion;
    String wAgendar;
    String wCorreo, wTelefono, wNombre, wApellido, wDireccion, wContrasena, wConfirmarContrasena;
    String wCorreoR, wTelefonoR, wNombreR, wApellidoR, wDireccionR, wContrasenaR, wConfirmarContrasenaR;
    String wTokenCliente, wTokenClienteBD, wExiste;
    String wServidorWeb, wMantenerSesion, wMantenerTipoUsuario, wClaveCiudad, wNombreCiudad;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        CargarConfiguracion();

        mAuth = FirebaseAuth.getInstance();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                if (wTokenCliente.equals("")){
                    //finish();
                }
            }
        }, 1000);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        chMantener = findViewById(R.id.chMantener);

        wServidorWeb = "http://www.jmascamargo.com.mx/app/";

        if (wMantenerSesion.equals("1")){
            startActivity(new Intent(Acceso.this, Cuentas.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }else{

        }
    }

    public void CargarConfiguracion() {
        SharedPreferences prefs = getSharedPreferences("Principal", Context.MODE_PRIVATE);
        wServidorWeb    = prefs.getString("ServidorWeb", "");
        wTokenCliente   = prefs.getString("Token", "");
        wMantenerSesion = prefs.getString("MantenerSesion", "");
    }

    public void Aceptar (View view){
        wCorreo = etCorreo.getText().toString().trim();
        wContrasena = etContrasena.getText().toString().trim();
        if (!wCorreo.isEmpty() || !wContrasena.isEmpty()) {
            Acceder(wCorreo, wContrasena);
        }
    }

    private void Acceder (String correo, String contrasena) {
        Procesando.showProgress(Acceso.this, "Verificando Usuario...", true);
        mAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            wURL_Login = wServidorWeb + "login.php?CORREO=" + wCorreo;
                            new loginUsuario().execute();
                        } else {
                            Procesando.hideProgress();
                            String wMensaje = "Usuario o Contraseña Incorrectos";
                            int wIcono = R.drawable.ic_admiracion;
                            VentanaNotificacion(wIcono, wMensaje);
                            getApplicationContext();
                            Vibrator Vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            Vibrar.vibrate(300);
                            Vibrar.vibrate(0);
                            Vibrar.vibrate(300);
                            etCorreo.requestFocus();
                        }
                    }
                });
    }

    private void Login() {
        if (!wNombreRegistro.equals("")){
            SharedPreferences preferencias=getSharedPreferences("Principal", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferencias.edit();
            editor.putString("ClaveRegistro", wClaveRegistro);
            editor.putString("NombreRegistro", wNombreRegistro);
            editor.putString("ServidorWeb", wServidorWeb);
            editor.putString("MantenerSesion", wMantenerSesion);
            editor.commit();
            finish();
            Intent intent = new Intent(this, Cuentas.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        }
    }

    public void RestableceContrasena (View view) {
        wCorreo = etCorreo.getText().toString();
        if (!wCorreo.isEmpty()){
            Procesando.showProgress(Acceso.this, "Espere un Momento...", true);
            getEnviarCorreo();
        } else {
            Toast.makeText(getApplicationContext(),"Debe teclear su correo",Toast.LENGTH_SHORT).show();
        }
    }

    private void getEnviarCorreo() {
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(wCorreo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Procesando.hideProgress();
                    Toast.makeText(getApplicationContext(),"Verifica tu correo para reestablecer tu contraseña",Toast.LENGTH_SHORT).show();
                } else {
                    Procesando.hideProgress();
                    Toast.makeText(getApplicationContext(),"Error al enviar el Correo, Verifícalo.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Registrar (View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.ventana_registro, null);
        builder.setView(textEntryView);
        final AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnCrearUsuario;
        btnCrearUsuario = textEntryView.findViewById(R.id.btnCrearUsuario_VentanaRegistro);

        final TextView etCorreoR, etTelefonoR, etNombreR, etApellidoR, etDireccionR, etContrasenaR, etConfirmarContrasenaR;
        etCorreoR     = textEntryView.findViewById(R.id.etCorreo_VentanaRegistro);
        etTelefonoR   = textEntryView.findViewById(R.id.etTelefono_VentanaRegistro);
        etNombreR     = textEntryView.findViewById(R.id.etNombre_VentanaRegistro);
        etContrasenaR = textEntryView.findViewById(R.id.etContrasena_VentanaRegistro);
        etConfirmarContrasenaR = textEntryView.findViewById(R.id.etConfirmarContrasena_VentanaRegistro);

        btnCrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wCorreoR     = etCorreoR.getText().toString().trim();
                wTelefonoR   = etTelefonoR.getText().toString();
                wNombreR     = etNombreR.getText().toString().trim();
                wContrasenaR = etContrasenaR.getText().toString();
                wConfirmarContrasenaR = etConfirmarContrasenaR.getText().toString();
                etCorreoR.setError(null);
                if ("".equals(wCorreoR)) {
                    etCorreoR.setError("Captura un correo electrónico");
                    etCorreoR.requestFocus();
                    return;
                }else {
                    if (isEmail(wCorreoR) == false){
                        Toast.makeText(Acceso.this, "Capture una direccion de correo válida", Toast.LENGTH_SHORT).show();
                        etCorreoR.requestFocus();
                    } else {
                        if ("".equals(wTelefonoR)) {
                            etTelefonoR.setError("Captura un número de teléfono");
                            etTelefonoR.requestFocus();
                            return;
                        } else {
                            if ("".equals(wNombreR)) {
                                etNombreR.setError("Captura el Nombre");
                                etNombreR.requestFocus();
                                return;
                            } else {
                                if ("".equals(wContrasenaR)) {
                                    etContrasenaR.setError("Captura una Contraseña");
                                    etContrasenaR.requestFocus();
                                    return;
                                } else {
                                    if (isNumeric(wContrasenaR) == false){
                                        Toast.makeText(Acceso.this, "La contraseña debe contener por lo menos un número", Toast.LENGTH_SHORT).show();
                                        etContrasenaR.requestFocus();
                                    } else {
                                        if ("".equals(wConfirmarContrasenaR)) {
                                            etConfirmarContrasenaR.setError("Confirma la Contraseña");
                                            etConfirmarContrasenaR.requestFocus();
                                            return;
                                        } else {
                                            wCorreoR = etCorreoR.getText().toString().trim();
                                            wNombreR = etNombreR.getText().toString().trim();
                                            wContrasenaR = etContrasenaR.getText().toString().trim();
                                            wConfirmarContrasenaR = etConfirmarContrasenaR.getText().toString().trim();
                                            if (!wContrasenaR.equals(wConfirmarContrasenaR)) {
                                                String wMensaje = "Las Contraseñas no coinciden";
                                                int wIcono = R.drawable.ic_admiracion;
                                                VentanaNotificacion(wIcono, wMensaje);
                                            } else {
                                                if (etContrasenaR.length() < 6 || etContrasenaR.length() > 8) {
                                                    Toast.makeText(getApplicationContext(),"La contraseña debe contener mínimo 6 caracteres y 8 como máximo.",Toast.LENGTH_LONG).show();
                                                } else {
                                                    wURL_VerificaUsuario = wServidorWeb + "usuario_verificar.php?TIPO=1&CORREO=" + wCorreoR;
                                                    new verificaUsuario().execute();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public boolean isEmail(String cadena) {
        boolean resultado;
        if (Patterns.EMAIL_ADDRESS.matcher(cadena).matches()) {
            resultado = true;
        } else {
            resultado = false;
        }
        return resultado;
    }

    public boolean isNumeric(String cadena) {
        boolean resultado = false;
        for (int i = 0; i < cadena.length(); i++) {
            if (Character.isDigit(cadena.charAt(i))) {
                resultado = true;
            }
        }
        return resultado;
    }

    class verificaUsuario extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... params) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(wURL_VerificaUsuario, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getJSONObject(0);
                        wExiste = jsonObject.getString("EXISTE");
                        if (wExiste.equals("1")){
                            Toast.makeText(Acceso.this, "Correo Electrónico ya registrado",Toast.LENGTH_SHORT).show();
                            Vibrator Vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            Vibrar.vibrate(300);
                            Vibrar.vibrate(0);
                            Vibrar.vibrate(300);
                            Procesando.hideProgress();
                        }else{
                            CrearUsuario();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),"Error al Procesar",Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"" + error,Toast.LENGTH_SHORT).show();
                }
            }
            );
            requestQueue = Volley.newRequestQueue(Acceso.this);
            requestQueue.add(jsonArrayRequest);
            return null;
        }
        protected void onPostExecute(String file_url) {
            Procesando.hideProgress();
        }
    }

    public void CrearUsuario () {
        mAuth.createUserWithEmailAndPassword(wCorreoR, wContrasenaR)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            wURL_AltaUsuario = wServidorWeb + "usuario_registro.php";
                            new altaUsuario().execute();
                        } else {
                            Toast.makeText(getApplicationContext(),"Error al crear la cuenta..",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public class altaUsuario extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Procesando.showProgress(Acceso.this, "Cargando Imagenes...", true);
        }
        protected String doInBackground(String... args) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, wURL_AltaUsuario, new Response.Listener<String>() {
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(),"Cuenta Creada Correctamente",Toast.LENGTH_SHORT).show();
                    //finish();
                    Procesando.hideProgress();
                    RestartActivity();
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Procesando.hideProgress();
                    Toast.makeText(Acceso.this,""+error, Toast.LENGTH_SHORT).show();
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("CORREO", wCorreoR);
                    parametros.put("TELEFONO", wTelefonoR);
                    parametros.put("NOMBRE", wNombreR);
                    parametros.put("CONTRASENA", wContrasenaR);
                    //parametros.put("TOKE_PA", wToken);
                    return parametros;
                }
            };
            requestQueue = Volley.newRequestQueue(Acceso.this);
            requestQueue.add(stringRequest);
            return null;
        }
        protected void onPostExecute(String file_url) {
        }
    }

    public void RestartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    class loginUsuario extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... params) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(wURL_Login, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    wClaveRegistro = "";
                    wNombreRegistro = "";
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getJSONObject(0);
                        wClaveRegistro = jsonObject.getString ("CLAV_RE");
                        wNombreRegistro = jsonObject.getString ("NOMB_AC");
                        wTokenClienteBD   = jsonObject.getString("TOKE_AC");
                        Procesando.hideProgress();
                        if (!wTokenClienteBD.equals(wTokenCliente)){
                            wURL_ActualizaToken = wServidorWeb + "/token_actualizar.php";
                            new actualizarToken().execute();
                        }else{
                            if (chMantener.isChecked()){
                                wMantenerSesion = "1";
                            }else{
                                wMantenerSesion = "0";
                            }
                            GuardarConfiguracion();
                            startActivity(new Intent(Acceso.this, Cuentas.class));
                            finish();
                        }
                    } catch (JSONException e) {
                        Procesando.hideProgress();
                        Toast.makeText(getApplicationContext(),"Error al Procesar",Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Procesando.hideProgress();
                    String wMensaje = "Cuenta Inexistente";
                    int wIcono = R.drawable.ic_admiracion;
                    VentanaNotificacion(wIcono, wMensaje);
                }
            }
            );
            requestQueue = Volley.newRequestQueue(Acceso.this);
            requestQueue.add(jsonArrayRequest);
            return null;
        }
        protected void onPostExecute(String file_url) {

        }
    }

    public class actualizarToken extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            //Procesando.showProgress(Acceso.this, "Actualizando Token...", true);
        }
        protected String doInBackground(String... args) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, wURL_ActualizaToken, new Response.Listener<String>() {
                public void onResponse(String response) {
                    Procesando.hideProgress();
                    if (chMantener.isChecked()){
                        wMantenerSesion = "1";
                    }else{
                        wMantenerSesion = "0";
                    }
                    GuardarConfiguracion();
                    startActivity(new Intent(Acceso.this, Cuentas.class));
                    finish();
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Procesando.hideProgress();
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("VIENE", "CLIENTE");
                    parametros.put("CORREO", wCorreo);
                    parametros.put("TOKEN", wTokenCliente);
                    return parametros;
                }
            };
            requestQueue = Volley.newRequestQueue(Acceso.this);
            requestQueue.add(stringRequest);
            return null;
        }
        protected void onPostExecute(String file_url) {

        }
    }

    public void GuardarConfiguracion() {
        SharedPreferences prefs = getSharedPreferences("Principal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ServidorWeb", wServidorWeb);
        editor.putString("Correo", wCorreo);
        editor.putString("MantenerSesion", wMantenerSesion);
        editor.putString("ClaveRegistro", wClaveRegistro);
        editor.putString("NombreRegistro", wNombreRegistro);
        editor.putString("TokenCliente", wTokenCliente);
        editor.commit();
    }

    public void VentanaNotificacion(int wIcono, String wMensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Acceso.this);
        LayoutInflater factory = LayoutInflater.from(Acceso.this);
        final View textEntryView = factory.inflate(R.layout.ventana_notificacion,null);
        builder.setView(textEntryView);

        TextView txtMensaje;
        ImageView imgIcono;

        builder.setCancelable(true);
        final AlertDialog alert = builder.create();

        txtMensaje = textEntryView.findViewById(R.id.txtMensaje_VentanaNotificacion);
        txtMensaje.setText(wMensaje);
        imgIcono = textEntryView.findViewById(R.id.imgIcono_VentanaNotificacion);
        imgIcono.setImageResource(wIcono);

        Button btnAceptar, btnCancelar;
        btnAceptar = textEntryView.findViewById(R.id.btnAceptar_VentanaNotificacion);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        alert.show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}