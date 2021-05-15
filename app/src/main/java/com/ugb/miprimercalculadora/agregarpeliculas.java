package com.ugb.miprimercalculadora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

//Allison Vanessa Rodriguez Sosa
//Flor Mabel Contreras Rodriguez
//Roger Alberto Chávez Zelaya
//Elmer Antonio Angel Reyes

public class agregarpeliculas extends AppCompatActivity {

    FloatingActionButton btnregresar;
    ImageView imgfotodepelicula, imgfotodepelicula2;
    VideoView vipelicula ;
    Intent tomarfotointent;
    String urlfoto,urlfoto2, urlvideo;
    String idpelicula,idlocal, accion = "nuevo", rev;
    Button btnagregar, btncargarvideo;
    DB miconexion;
    TextView temp;
    utilidades miUrl;
    String urifoto, urifoto2, urivideo;
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
        imgfotodepelicula = findViewById(R.id.imgfotopelicula1);


        vipelicula = findViewById(R.id.vipelicula);
        btnagregar = findViewById(R.id.btnguardarpelicula);

        btnregresar.setOnClickListener(v -> {
            regresarmainactivity();
        });

        imgfotodepelicula.setOnClickListener(v -> {
            abrirgaleriaimagen();
        });

        //  imgfotodepelicula2.setOnClickListener(v -> {
        //    abrirgaleriaimagen();
        // });


        btncargarvideo.setOnClickListener(v -> {
            abrirgaleriavideo();
        });

        btnagregar.setOnClickListener(v -> {
            agregarproducto();


        });

        permisos();
        mostrardatospelicula();
        controles();

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

            String[] datos = {idlocal, titulo, sinopsis, duracion, precio, urlfoto, urlvideo };

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

                imgfotodepelicula.setImageURI(Uri.parse(urlfoto));
                vipelicula.setVideoURI(Uri.parse(urlvideo));

            }
        }catch (Exception ex){
            mensajes(ex.getMessage());
        }
    }

    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();

    }

    private void controles(){
        MediaController mediaController = new MediaController(this);
        vipelicula.setMediaController(mediaController);
        mediaController.setAnchorView(vipelicula);
    }

    public static String getRealUrl(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            else if (isMediaDocument(uri))  {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent dataimagen) {
        if (resultCode == Activity.RESULT_OK && dataimagen != null) {
            if (requestCode == RIG) {
                Uri photo = dataimagen.getData();
                imgfotodepelicula.setImageURI(photo);

                urlfoto = getRealUrl(this,photo);


            }else if (requestCode == RVD){
                Uri video = dataimagen.getData();
                vipelicula.setVideoURI(video);

                urlvideo = getRealUrl(this,video);
            }
        }
        super.onActivityResult(requestCode, resultCode, dataimagen);
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}