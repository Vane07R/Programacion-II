package com.ugb.miprimercalculadora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

public class agregarpeliculas extends AppCompatActivity {

    FloatingActionButton btnregresar;
    ImageView imgfotodepelicula1;
    VideoView vipelicula ;
    Intent tomarfotointent;
    String urlfoto, urlvideo;
    String idpelicula,idlocal, accion = "nuevo", rev;
    Button btnagregar, btncargarvideo;
    DB miconexion;
    TextView temp;
    utilidades miUrl;
    String urifoto, urivideo;
    detectarInternet di;
    
    private static final int RPQ= 100;
    private static final int RIG= 101;
    private static final int RVD= 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarpelicula);


        miconexion = new DB(getApplicationContext(),"",null,1);

        btnregresar = findViewById(R.id.btnatras);
        btncargarvideo = findViewById(R.id.btncargarvideo);
        imgfotodepelicula1 = findViewById(R.id.imgfotopelicula1);

        vipelicula = findViewById(R.id.vipelicula);
        btnagregar = findViewById(R.id.btnguardarpelicula);

        btnregresar.setOnClickListener(v -> {
            regresarmainactivity();
        });

        imgfotodepelicula1.setOnClickListener(v -> {
            abrirgaleriaimagen();
        });


        btncargarvideo.setOnClickListener(v -> {
            abrirgaleriavideo();
        });

        btnagregar.setOnClickListener(v -> {
            agregarproducto();


        });

        permisos();
        mostrardatospelicula();

        MediaController mediaController = new MediaController(this);
        vipelicula.setMediaController(mediaController);
        mediaController.setAnchorView(vipelicula);

    }

    private void permisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(agregarpeliculas.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            }else {
                ActivityCompat.requestPermissions(agregarpeliculas.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},RPQ);
            }
        }else {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent dataimagen) {

        if (resultCode == Activity.RESULT_OK && dataimagen != null) {

            if (requestCode == RIG) {
                Uri photo = dataimagen.getData();
                imgfotodepelicula1.setImageURI(photo);
                urlfoto = photo.toString();
            }else if (requestCode == RVD){
                Uri video = dataimagen.getData();
                vipelicula.setVideoURI(video);
                urlvideo = video.toString();
            }

        }

        super.onActivityResult(requestCode, resultCode, dataimagen);
    }

    private void abrirgaleriaimagen(){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT );
        i.setType("image/*");
        startActivityForResult(i, RIG);
    }

    private void abrirgaleriavideo(){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT );
        i.setType("video/*");
        startActivityForResult(i, RVD);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode== RPQ){
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            }else{
                mensajes("Otorgar permisos");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void regresarmainactivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    private void agregarproducto() {
        try {
            temp = findViewById(R.id.txtTitulo);
            String titulo = temp.getText().toString();

            temp = findViewById(R.id.txtsinopsis);
            String sinopsis = temp.getText().toString();

            temp = findViewById(R.id.txtduracion);
            String duracion = temp.getText().toString();

            temp = findViewById(R.id.txtprecio);
            String precio = temp.getText().toString();


            JSONObject datospeliculas = new JSONObject();
            if(accion.equals("modificar") && idpelicula.length()>0 && rev.length()>0 ){
                datospeliculas.put("_id",idpelicula);
                datospeliculas.put("_rev",rev);
            }

            datospeliculas.put("titulo",titulo);
            datospeliculas.put("sinopsis",sinopsis);
            datospeliculas.put("duracion",duracion);
            datospeliculas.put("precio",precio);
            datospeliculas.put("urlfoto",urlfoto);
            datospeliculas.put("urltrailer",urlvideo);

            String[] datos = {idlocal, titulo, sinopsis, duracion, precio, urifoto, urlvideo };

            di = new detectarInternet(getApplicationContext());
            if (di.hayConexionInternet()) {
                subirdatos guardarpelicula = new subirdatos(getApplicationContext());
                String resp = guardarpelicula.execute(datospeliculas.toString()).get();
            }

            miconexion.administracion_peliculas(accion, datos);
            mensajes("Registro guardado con exito.");

            regresarmainactivity();

        }catch (Exception w){
            mensajes(w.getMessage());
        }
    }

    private void mostrardatospelicula() {

        try {
            Bundle recibirparametros = getIntent().getExtras();
            accion = recibirparametros.getString("accion");
            idlocal = recibirparametros.getString("idlocal");

            if(accion.equals("modificar")){
                JSONObject datos = new JSONObject(recibirparametros.getString("datos")).getJSONObject("value");

                idpelicula = datos.getString("_id");

                rev = datos.getString("_rev");

                temp = findViewById(R.id.txtTitulo);
                temp.setText(datos.getString("titulo"));

                temp = findViewById(R.id.txtsinopsis);
                temp.setText(datos.getString("sinopsis"));

                temp = findViewById(R.id.txtduracion);
                temp.setText(datos.getString("duracion"));

                temp = findViewById(R.id.txtprecio);
                temp.setText(datos.getString("precio"));

                urlfoto =  datos.getString("urlfoto");

                urlvideo =  datos.getString("urltrailer");

                imgfotodepelicula1.setImageURI(Uri.parse(urifoto));
                vipelicula.setVideoURI(Uri.parse(urivideo));

            }
        }catch (Exception ex){
            mensajes(ex.getMessage());
        }
    }
    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }
}
