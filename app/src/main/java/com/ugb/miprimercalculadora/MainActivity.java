package com.ugb.miprimercalculadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


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

    DB miconexion;
    ListView ltsmenu;
    Cursor datosusuariocursor = null;
    Cursor datomenucursor = null;
    ArrayList<menu> menuArrayList=new ArrayList<menu>();
    ArrayList<menu> menuArrayListCopy=new ArrayList<menu>();
    menu mismenud;
    JSONArray jsonArrayDatosmenu;
    JSONObject jsonObjectDatosmenu;
    utilidades u;
    String idlocal;
    detectarInternet di;
    Button login, registro;
    TextView temp;
    int position = 0;


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


        di = new detectarInternet(getApplicationContext());

        obtenerDatosOffLine();
        obtenerDatos();
        mostrarDatos();
        Eliminar();
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


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_ordenes, menu);
        try {
            if(di.hayConexionInternet()) {
                AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
                datomenucursor.moveToPosition(adapterContextMenuInfo.position);
                position = adapterContextMenuInfo.position;
                menu.setHeaderTitle(jsonArrayDatosmenu.getJSONObject(position).getJSONObject("value").getString("nombremenu"));
            } else {
                AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo)menuInfo;
                datomenucursor.moveToPosition(adapterContextMenuInfo.position);
                menu.setHeaderTitle(datomenucursor.getString(1));
            }
            idlocal = datomenucursor.getString(0);
        }catch (Exception e){
            mensajes(e.getMessage());
        }
    }
    @Override

    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.mnxAgregar:
                    Agregar("nuevo");
                    break;
                case R.id.mnxModificar:
                    Modificar ("modificar");
                    break;
                case R.id.mnxEliminar:
                    Eliminar();
                    break;
            }
        }catch (Exception ex){
            mensajes(ex.getMessage());
        }
        return super.onContextItemSelected(item);
    }

    private void Eliminar(){
        try {
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(MainActivity.this);
            confirmacion.setTitle("Esta seguro de eliminar?");
            if (di.hayConexionInternet())
            {
                jsonObjectDatosmenu = jsonArrayDatosmenu.getJSONObject(position).getJSONObject("value");
                confirmacion.setMessage(jsonObjectDatosmenu.getString("nombremenu"));
            }else {
                confirmacion.setMessage(datomenucursor.getString(1));
            }

            confirmacion.setPositiveButton("Si", (dialog, which) -> {

                try {
                    if(di.hayConexionInternet()){
                        conexionserver objEliminar = new conexionserver();
                        String resp =  objEliminar.execute(u.url_mto +
                                jsonObjectDatosmenu.getString("_id")+ "?rev="+
                                jsonObjectDatosmenu.getString("_rev"), "DELETE"
                        ).get();

                        JSONObject jsonRespEliminar = new JSONObject(resp);
                        if(jsonRespEliminar.getBoolean("ok")){
                            jsonArrayDatosmenu.remove(position);
                            mostrarDatos();
                        }
                    }

                    miconexion = new DB(getApplicationContext(), "", null, 1);
                    datomenucursor = miconexion.eliminar("eliminar", datomenucursor.getString(0));
                    obtenerDatos();
                    mensajes("Menu eliminado");
                    dialog.dismiss();
                }catch (Exception e){
                }
            });
            confirmacion.setNegativeButton("No", (dialog, which) -> {
                mensajes("Eliminacion detendia");
                dialog.dismiss();
            });
            confirmacion.create().show();
        } catch (Exception ex){
            mensajes(ex.getMessage());
        }
    }

    private void Modificar(String accion){
        Bundle parametros = new Bundle();
        parametros.putString("accion", accion);
        parametros.putString("idlocal", idlocal);
        jsonObjectDatosmenu = new JSONObject();
        JSONObject jsonValueObject = new JSONObject();
        if(di.hayConexionInternet())
        {
            try {
                if(jsonArrayDatosmenu.length()>0){
                    parametros.putString("datos", jsonArrayDatosmenu.getJSONObject(position).toString() );
                }
            }catch (Exception e){
                mensajes(e.getMessage());
            }
        }else{
            try {
                jsonArrayDatosmenu = new JSONArray();
                jsonObjectDatosmenu.put("idmenu", datomenucursor.getString(0));
                jsonObjectDatosmenu.put("rev", datomenucursor.getString(0));
                jsonObjectDatosmenu.put("nombremenu",datomenucursor.getString(1));
                jsonObjectDatosmenu.put("descipcionmenu", datomenucursor.getString(2));
                jsonObjectDatosmenu.put("espera", datomenucursor.getString(3));
                jsonObjectDatosmenu.put("precio", datomenucursor.getString(4));
                jsonObjectDatosmenu.put("mesa", datomenucursor.getString(5));
                jsonObjectDatosmenu.put("bebida", datomenucursor.getString(6));
                jsonObjectDatosmenu.put("postre", datomenucursor.getString(7));
                jsonObjectDatosmenu.put("urlfoto", datomenucursor.getString(8));
                jsonObjectDatosmenu.put("urltrailer", datomenucursor.getString(9));
                jsonValueObject.put("value", jsonObjectDatosmenu);
                jsonArrayDatosmenu.put(jsonValueObject);
                if(jsonArrayDatosmenu.length()>0){
                    parametros.putString("datos", jsonArrayDatosmenu.getJSONObject(position).toString() );
                }

            }catch (Exception e){
                mensajes(e.getMessage());
            }
        }
        Intent i = new Intent(getApplicationContext(), agregarplatillos.class);
        i.putExtras(parametros);
        startActivity(i);
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
        datomenucursor = miconexion.consultar_menu("Consultar1", null);
       if( datomenucursor.moveToFirst() ){
         mostrarDatos();
     } else {
                mensajes("No hay menus registrados");
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
                                jsonObject.getString("idmenu"),
                                jsonObject.getString("rev"),
                                jsonObject.getString("nombremenu"),
                                jsonObject.getString("descripcionmenu"),
                                jsonObject.getString("espera"),
                                jsonObject.getString("precio"),
                                jsonObject.getString("mesa"),
                                jsonObject.getString("bebida"),
                                jsonObject.getString("postre"),
                               jsonObject.getString("urlfoto")

                        );
                        menuArrayList.add(mismenud);
                    }}
            } else {
                do{
                    mismenud = new menu(
                            datomenucursor.getString(0),
                            datomenucursor.getString(1),
                            datomenucursor.getString(1),
                            datomenucursor.getString(2),
                            datomenucursor.getString(3),
                            datomenucursor.getString(4),
                            datomenucursor.getString(5),
                            datomenucursor.getString(6),
                            datomenucursor.getString(7),
                            datomenucursor.getString(8)
                    );
                    menuArrayList.add(mismenud);
                }while(datomenucursor.moveToNext());
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

