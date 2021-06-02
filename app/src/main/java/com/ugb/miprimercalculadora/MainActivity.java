package com.ugb.miprimercalculadora;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


//Allison Vanessa Rodriguez Sosa
//Flor Mabel Contreras Rodriguez
//Roger Alberto Chávez Zelaya
//Elmer Antonio Angel Reyes

public class MainActivity extends AppCompatActivity {
    ArrayList<menu> menuArrayList=new ArrayList<menu>();
    ArrayList<menu> menuArrayListCopy=new ArrayList<menu>();
    menu mismenud;
    JSONArray jsonArrayDatosmenu;
    JSONObject jsonObjectDatosmenu;
    FloatingActionButton btnadd;
    Button login, registro;
    TextView temp;
    DB miconexion;
    ListView ltsmenu;
    Cursor datosusuariocursor = null;
    detectarInternet di;
    utilidades u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

        }catch (Exception e){
            mostrarMsgToast(e.getMessage());
        }

        login = findViewById(R.id.btniniciar);
        registro = findViewById(R.id.btnregistrar);

        login.setOnClickListener(v->{
            logi();
        });

        registro.setOnClickListener(v->{
            Intent i = new Intent(getApplicationContext(), registroUsuario.class);
            startActivity(i);
        });
        btnadd = findViewById(R.id.btnagregar);
        btnadd.setOnClickListener(v->{
            Agregar("nuevo");
        });

        obtenerDatos();
    }



    private void mostrarMsgToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    private void logi() {
        try {
            temp = findViewById(R.id.txtuss);
            String dui = temp.getText().toString();

            temp = findViewById(R.id.txtcontra);
            String contra = temp.getText().toString();

            miconexion = new DB(getApplicationContext(), "", null, 1);
            datosusuariocursor = miconexion.consultar_usuario("consultar", dui, contra);
            if( datosusuariocursor.moveToFirst() ) {

                String nombre = datosusuariocursor.getString(1);
                String duii = datosusuariocursor.getString(2);
                String telefono = datosusuariocursor.getString(3);
                String correo = datosusuariocursor.getString(4);
                String contraa = datosusuariocursor.getString(5);


                mensajes("Bienvenido " + nombre);
                Intent i = new Intent(MainActivity.this, mostrarordenes.class);
                i.putExtra(mostrarordenes.nombre, nombre);
                i.putExtra(mostrarordenes.duii,duii);
                i.putExtra(mostrarordenes.telefono, telefono);
                i.putExtra(mostrarordenes.correo, correo);
                i.putExtra(mostrarordenes.contra,contra);
                startActivity(i);
            }
        }catch (Exception e){
            mensajes("No se encontro el usuario");
        }
    }

    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    private void Agregar(String accion) {
        Bundle parametros = new Bundle();
        parametros.putString("accion", accion);
        Intent i = new Intent(getApplicationContext(), agregarplatillos.class);
        i.putExtras(parametros);
        startActivity(i);

    }


   private void obtenerDatosOffLine() {
     try {
         miconexion = new DB(getApplicationContext(), "", null, 1);
        datosusuariocursor = miconexion.administracion_peliculas("Consultar", null);
       if( datospeliculasdcursor.moveToFirst() ){
         mostrarDatos();
     } else {
                mensajes("No hay datos registrados");
            }
       }catch (Exception e){
            mensajes(e.getMessage());
        }

    }

    private void obtenerDatosOnLine() {
        try {
            conexionserver conexionserver = new conexionserver();
            String resp = conexionserver.execute(u.url_consulta, "GET").get();
            jsonObjectDatosmenu=new JSONObject(resp);
            jsonArrayDatosmenu = jsonObjectDatosmenu.getJSONArray("rows");
            mostrarDatos();
        }catch (Exception ex){
            mensajes(ex.getMessage());
        }

    }
    private void obtenerDatos() {
        if(di.hayConexionInternet()) {
            mensajes("Mostrando datos desde la nube");
            obtenerDatosOnLine();
           // obtenerDatosOffLine();
        } else {
            mensajes("Mostrando datos locales");
           // obtenerDatosOffLine();
        }

    }

    private void mostrarDatos() {
        try{
            ltsmenu = findViewById(R.id.list);
            menuArrayList.clear();
            menuArrayListCopy.clear();
            JSONObject jsonObject;
            if(di.hayConexionInternet()) {
                if(jsonArrayDatosmenu.length()>0) {
                    for (int i = 0; i < jsonArrayDatosmenu.length(); i++) {
                        jsonObject = jsonArrayDatosmenu.getJSONObject(i).getJSONObject("value");
                        mismenud= new menu(
                                jsonObject.getString("_id"),
                                jsonObject.getString("_rev"),
                                jsonObject.getString("nombremenu"),
                                jsonObject.getString("descripcionmenu"),
                                jsonObject.getString("espera"),
                                jsonObject.getString("precio"),
                                jsonObject.getString("mesa"),
                                jsonObject.getString("bebida"),
                                jsonObject.getString("postre"),
                                jsonObject.getString("urlfoto"),
                                jsonObject.getString("urltrailer")
                        );
                        menuArrayList.add(mismenud);
                    }}
            } else {
                do{
                    mismenud = new menu(
                            datosusuariocursor.getString(0),
                            datosusuariocursor.getString(1),
                            datosusuariocursor.getString(1),
                            datosusuariocursor.getString(2),
                            datosusuariocursor.getString(3),
                            datosusuariocursor.getString(4),
                            datosusuariocursor.getString(5),
                            datosusuariocursor.getString(6),
                            datosusuariocursor.getString(7),
                            datosusuariocursor.getString(8),
                            datosusuariocursor.getString(6)
                    );
                    menuArrayList.add(mismenud);
                }while(datosusuariocursor.moveToNext());
            }
            adactadorImagenes adaptadorImagenes = new adactadorImagenes(getApplicationContext(), menuArrayList);
            ltsmenu.setAdapter(adaptadorImagenes);
            registerForContextMenu(ltsmenu);
            menuArrayListCopy.addAll(menuArrayList);

        }catch (Exception e){
            mensajes(e.getMessage());
        }
    }

    private class conexionserver extends AsyncTask<String, String, String> {
        HttpURLConnection urlConnection;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... parametros) {
            StringBuilder result = new StringBuilder();
            try{
                String uri = parametros[0];
                String metodo = parametros[1];
                URL url = new URL(uri);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod(metodo);

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String linea;
                while( (linea=bufferedReader.readLine())!=null ){
                    result.append(linea);
                }
            }catch (Exception e){
                Log.i("GET", e.getMessage());
            }
            return result.toString();
        }
    }
}


