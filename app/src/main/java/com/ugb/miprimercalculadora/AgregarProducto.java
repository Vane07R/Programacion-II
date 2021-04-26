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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgregarProducto extends AppCompatActivity {
    FloatingActionButton btnAtras;
    ImageView imgFotoProducto;
    Intent tomarFotoIntent;
    String urlCompletaImg, idProducto,rev, accion="nuevo";
    Button btn;
    DB miBD;
    TextView tempVal;
    utilidades miURL;
    detectarInternet di;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        miBD = new DB(getApplicationContext(),"",null,1);
        btnAtras = findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(v-> {
            mostrarVistaPrincipal();
        });
        imgFotoProducto=findViewById(R.id.imgFotoProducto);
        imgFotoProducto.setOnClickListener(v ->{
            tomarFotoProducto();
        });
        btn = findViewById(R.id.btnGuardarProducto);
        btn.setOnClickListener(v ->{
            try {
                tempVal = findViewById(R.id.txtNombre);
                String nombre = tempVal.getText().toString();

                tempVal = findViewById(R.id.txtTipoDeProducto);
                String descripcion = tempVal.getText().toString();

                tempVal = findViewById(R.id.txtCodigo);
                String codigo = tempVal.getText().toString();

                tempVal = findViewById(R.id.txtAdvertencias);
                String advertencias = tempVal.getText().toString();

                tempVal = findViewById(R.id.txtAPrecio);
                String precio = tempVal.getText().toString();

                JSONObject datosTienda =new JSONObject();
                if (accion.equals("modificar")&& idProducto.length()>0 && rev.length()>0){

                  datosTienda.put("_id",idProducto);
                  datosTienda.put("_rev",rev);
                }
                datosTienda.put("nombre",nombre);
                datosTienda.put("descripcion",descripcion);
                datosTienda.put("codigo",codigo);
                datosTienda.put("advertencias",advertencias);
                datosTienda.put("precio",precio);
                datosTienda.put("urlPhoto",urlCompletaImg);
                String[] datos = {idProducto, nombre, descripcion, codigo, advertencias, precio, urlCompletaImg};

                di = new detectarInternet(getApplicationContext());
                if (di.hayConexionInternet()) {
                    enviarDatosTienda objGuardarProducto = new enviarDatosTienda(getApplicationContext());
                    String resp = objGuardarProducto.execute(datosTienda.toString()).get();
                }
                miBD.administracion_productos(accion, datos);
                mostrarMsgToast("Producto guardado con exito.");

                mostrarVistaPrincipal();
            }catch (Exception e){
                mostrarMsgToast(e.getMessage());
            }
        });
        mostraDatosProducto();
    }
    private void mostraDatosProducto() {
        try {
            Bundle recibirParametros=getIntent().getExtras();
            accion=recibirParametros.getString("accion");
            if (accion.equals("modificar")){
                JSONObject datos = new JSONObject(recibirParametros.getString("datos")).getJSONObject("value");

                idProducto=datos.getString("_id");
                rev=datos.getString("_rev");

                tempVal=findViewById(R.id.txtNombre);
                tempVal.setText(datos.getString("nombre"));

                tempVal=findViewById(R.id.txtTipoDeProducto);
                tempVal.setText(datos.getString("descripcion"));

                tempVal=findViewById(R.id.txtCodigo);
                tempVal.setText(datos.getString("codigo"));

                tempVal=findViewById(R.id.txtAdvertencias);
                tempVal.setText(datos.getString("advertencias"));

                tempVal=findViewById(R.id.txtAPrecio);
                tempVal.setText(datos.getString("precio"));

                urlCompletaImg=datos.getString("urlPhoto");
                Bitmap bitmap = BitmapFactory.decodeFile((urlCompletaImg));
                imgFotoProducto.setImageBitmap(bitmap);
            }
        }catch (Exception e){
            mostrarMsgToast(e.getMessage());
        }
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
            if (requestCode==1 && resultCode==RESULT_OK){
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