package com.fnam.scantextml;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuPrincipal extends AppCompatActivity {

    //Declaracion de button para controlar la vista
    Button btnreconocer, btnAcercaDe, btnSalir, btnEntendido;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //Seteamos los respectivos objetos de la vista a los buttons declarados
        btnreconocer = findViewById(R.id.btnReconocer);
        btnAcercaDe = findViewById(R.id.btnAcercade);
        btnSalir = findViewById(R.id.btnSalir);
        dialog = new Dialog(this);
        btnreconocer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
            }
        });

        btnAcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showDialog(){
        Button btnEntendido;

        dialog.setContentView(R.layout.acerca_de); //Abrir el layout
        btnEntendido = dialog.findViewById(R.id.btnEntendido);

        btnEntendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(false); //Este metodo permite que sin importa dar touch alrededor no se va a cerrar
    }
}