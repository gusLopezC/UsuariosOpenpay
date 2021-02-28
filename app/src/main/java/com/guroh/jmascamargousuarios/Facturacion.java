package com.guroh.jmascamargousuarios;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class Facturacion extends Activity {
    private WebView browser;
    private ProgressBar progressBar;
    private Button btIr;
    private EditText etPagina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion);
        final Bundle datos = this.getIntent().getExtras();
        final String wCuenta = datos.getString("wCuenta");
        // Definimos el webView
        browser = (WebView) findViewById(R.id.wbPagina);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);


        browser.getSettings().setJavaScriptEnabled(true);

        //Habilitamos los botones de Zoom
        browser.getSettings().setBuiltInZoomControls(true);

        browser.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        browser.getSettings().setDomStorageEnabled(true);

        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setDomStorageEnabled(true);
        browser.loadUrl("https://facturas.jmascamargo.com.mx:84");
        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                final String js = "javascript:" + "document.getElementById('txtCuenta').value = '" + wCuenta + "';" + "document.getElementById('idAsunto').click();";
                if (Build.VERSION.SDK_INT >= 19) {
                    view.evaluateJavascript(js, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {

                        }
                    });
                } else {
                    view.loadUrl(js);
                }
            }
        });

        //browser.loadUrl("https://pagos.jmascamargo.com.mx:82 javascript:var txtCuenta =document.getElementById('txtCuenta').value='"+"123"+"';");
        //Cargamos la web
        //browser.loadUrl(etPagina.getText().toString());
        //browser.loadUrl("https://pagos.jmascamargo.com.mx:82");
        //Sincronizamos la barra de progreso de la web

        //browser.loadUrl("javascript: {" + "document.getElementById('txtCuenta').value = '"+"uname" +"';" + "var frms = document.getElementsByName('https://pagos.jmascamargo.com.mx:82');" + "frms[0].submit(); };");

        browser.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                Facturacion.this.setProgress(progress * 1000);

                progressBar.incrementProgressBy(progress);

                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        /**
        btIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browser.getSettings().setJavaScriptEnabled(true);

                //Habilitamos los botones de Zoom
                browser.getSettings().setBuiltInZoomControls(true);

                browser.setWebViewClient(new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });

                // Cargamos la web
                browser.loadUrl(etPagina.getText().toString());

                //Sincronizamos la barra de progreso de la web


                browser.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int progress) {
                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.VISIBLE);
                        Facturacion.this.setProgress(progress * 1000);

                        progressBar.incrementProgressBy(progress);

                        if (progress == 100) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
         */
        //Habilitamos JavaScript




    }

    @Override
    public void onBackPressed()
    {
        if (browser.canGoBack())
        {
            browser.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }
}
