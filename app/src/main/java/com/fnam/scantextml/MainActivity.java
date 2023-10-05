package com.fnam.scantextml;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button btnReconocerTexto;
    private ImageView imagen;
    private TextView textoReconocido;

    private Uri uri = null;

    private ProgressDialog progressDialog;

    private TextRecognizer textRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnReconocerTexto = findViewById(R.id.btnReconocerTexto);
        imagen = findViewById(R.id.imagen);
        textoReconocido = findViewById(R.id.textoReconocido);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        btnReconocerTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri == null){
                    Toast.makeText(MainActivity.this, "Seleccione una imagen", Toast.LENGTH_SHORT).show();
                }else{
                    reconocerTextoImagen();
                }
            }
        });
    }

    private void reconocerTextoImagen() {
        progressDialog.show();
        progressDialog.setMessage("Preparando imagen");

        try {
            InputImage inputImage = InputImage.fromFilePath(this, uri);
            progressDialog.setMessage("Reconociendo texto");
            Task<Text> textTask = textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            progressDialog.dismiss();
                            String texto = text.getText();
                            textoReconocido.setText(texto);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "No se pudo reconocer el texto debido a "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Error al preparar la imagen: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    //Metodo para abrir la Galeria
    private void openGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galeriaARL.launch(intent);
    }

    //Metodo para abrir la camara y tomar la foto
    private void openCamara(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Titulo");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripcion");

        uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        camaraARL.launch(intent);
    }


    //Metodo pra obtener imagen en Galeria
    public ActivityResultLauncher<Intent> galeriaARL = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>(){
            @Override
            public void onActivityResult(ActivityResult result) {
                //Validamos si la imagen fue recibida correctamente
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData(); //Obtenemos el valor
                    uri = data.getData();
                    imagen.setImageURI(uri); //Guardamos el uri dentro de la imagen
                    textoReconocido.setText("");
                }else{
                    Toast.makeText(MainActivity.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        }
    );

    //Metodo para obtener imagen de la camara
    public ActivityResultLauncher<Intent> camaraARL = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        imagen.setImageURI(uri);
                        textoReconocido.setText("");
                    }else{
                        Toast.makeText(MainActivity.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    //Metodo para implementar el menu dentro de la actividad
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mi_menu, menu); //Obtenemos y agregamos el menu dentro de la accion
        return super.onCreateOptionsMenu(menu);
    }

    //Metodo para gestionar el control de opciones del menu
    // y opcion seleccionada
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_abrir_galeria){
            openGaleria();
        }else{
            if(item.getItemId() == R.id.menu_abrir_camara){
                openCamara();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}