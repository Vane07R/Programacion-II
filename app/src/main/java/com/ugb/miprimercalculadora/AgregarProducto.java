package com.ugb.miprimercalculadora;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgregarProducto extends AppCompatActivity {
    FloatingActionButton btnAtras;
    ImageView imgFotoProducto;
    Intent tomarFotoIntent;
    String urlCompletaImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        btnAtras = findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(v->{
            mostrarVistaPrincipal();

        });

        imgFotoProducto=findViewById(R.id.imgFotoProducto);
        imgFotoProducto.setOnClickListener(v ->{
            tomarFotoProducto();
        });
    }
    private void mostrarVistaPrincipal(){
        Intent iprincipal = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(iprincipal);
    }

    private void tomarFotoProducto(){
        tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (tomarFotoIntent.resolveActivity(getPackageManager())!=null){
            File photoProducto = null;
                            try {
                                photoProducto = crearImagenProducto();
                            }catch (Exception e){
                                mostrarMsgToast(e.getMessage());
                            }
            if( photoProducto!=null ){
                try{
                    Uri uriphotoProducto = FileProvider.getUriForFile(AgregarProducto.this, "com.ugb.miprimercalculadora.fileprovider",photoProducto);
                    tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriphotoProducto);
                    startActivityForResult(tomarFotoIntent,1);
                }catch (Exception e){
                    mostrarMsgToast(e.getMessage());
                }
            } else {
                mostrarMsgToast("No fue posible tomar la foto");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode==1 && requestCode==RESULT_OK){
                Bitmap imageBitmap = BitmapFactory.decodeFile(urlCompletaImg);
                imgFotoProducto.setImageBitmap(imageBitmap);
            }
        }catch (Exception e){
            mostrarMsgToast(e.getMessage());
        }
    }

    private File crearImagenProducto() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreImagen = "imagen_"+ timeStamp +"_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if( dirAlmacenamiento.exists()==false ) {
            dirAlmacenamiento.mkdirs();
        }
        File imgage = File.createTempFile(nombreImagen,".jpg", dirAlmacenamiento);
        urlCompletaImg = imgage.getAbsolutePath();
        return imgage;
    }

    private void mostrarMsgToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}