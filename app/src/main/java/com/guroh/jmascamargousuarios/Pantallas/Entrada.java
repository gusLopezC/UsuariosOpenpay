package com.guroh.jmascamargousuarios.Pantallas;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.guroh.jmascamargousuarios.R;

public class Entrada extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 2000;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);

        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                    Intent startIntent = new Intent(Entrada.this, Acceso.class);
                    startActivity(startIntent, ActivityOptions.makeSceneTransitionAnimation(Entrada.this).toBundle());
                    finishAfterTransition();
            }
        },SPLASH_SCREEN_DELAY);
    }
}