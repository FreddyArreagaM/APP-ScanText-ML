package com.fnam.scantextml;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Bienvenida extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        int tiempo = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Establecemos que luego del tiempo de 3s se pase de esta actividad a Menu_principal
                startActivity(new Intent(Bienvenida.this, MenuPrincipal.class));
                finish();
            }
        }, tiempo);
    }
}