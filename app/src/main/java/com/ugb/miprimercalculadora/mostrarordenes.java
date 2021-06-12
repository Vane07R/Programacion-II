package com.ugb.miprimercalculadora;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class mostrarordenes extends AppCompatActivity {
    public static  final String nombre="nombre";
    public static  final String duii="duii";
    public static  final String telefono="telefono";
    public static  final String correo="correo";
    public static  final String contra="contra";


    TextView nombre1, dui1, telefotol,profecion1,correo1,contra1;


    DB miconexion;
    Button agregar;
    ListView ltsmenu;
    TextView temp;
    ImageView imgfoto;
    Cursor datosmenucursor = null;
    ArrayList<menu> menuArrayList=new ArrayList<menu>();
    ArrayList<menu> menuArrayListCopy=new ArrayList<menu>();
    menu mismenus ;
    detectarInternet di;
    JSONArray jsonArrayDatosmenu;
    JSONObject jsonObjectDatosmenu;
    String urldefoto;
    String idlocal, accion = "nuevo", rev;
    utilidades u;
    int position = 0;

    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    TextView mostr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostarmenu);



        nombre1 = findViewById(R.id.nombre);
        nombre1.setText(getIntent().getStringExtra("nombre"));

        agregar = findViewById(R.id.btnagregarr);
        agregar.setOnClickListener(v->{
            Intent i = new Intent(getApplicationContext(), agregarplatillos.class);
            startActivity(i);
        });
        di = new detectarInternet(getApplicationContext());

      //  mostrarDatos();

        buscarProductos();

obtenerDatos();
    }



    private void Agregar(String accion) {
        Bundle parametros = new Bundle();
        parametros.putString("accion", accion);
        Intent i = new Intent(getApplicationContext(), agregarplatillos.class);
        i.putExtras(parametros);
        startActivity(i);
    }

    private void Eliminar(){
        try {
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(mostrarordenes.this);
            confirmacion.setTitle("Esta seguro de eliminar?");

                jsonObjectDatosmenu = jsonArrayDatosmenu.getJSONObject(position).getJSONObject("value");
                confirmacion.setMessage(jsonObjectDatosmenu.getString("nombremenu"));


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
                    datosmenucursor = miconexion.eliminar("eliminar", datosmenucursor.getString(0));
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

    private void Modificar(String accion) {
        if (di.hayConexionInternet()) {
            try {
                Bundle parametros = new Bundle();
                parametros.putString("accion", accion);

                if (jsonArrayDatosmenu.length() > 0) {
                    parametros.putString("datos", jsonArrayDatosmenu.getJSONObject(position).toString());
                }

                Intent i = new Intent(getApplicationContext(), agregarplatillos.class);
                i.putExtras(parametros);
                startActivity(i);

            } catch (Exception e) {
                mensajes(e.getMessage());
            }
        } else {

            try {
                Bundle parametros = new Bundle();
                parametros.putString("accion", accion);
                jsonObjectDatosmenu = new JSONObject();
                JSONObject jsonValueObject = new JSONObject();
                jsonArrayDatosmenu = new JSONArray();

                jsonObjectDatosmenu.put("_id", datosmenucursor.getString(0));
                jsonObjectDatosmenu.put("_rev", datosmenucursor.getString(0));
                jsonObjectDatosmenu.put("nombremenu", datosmenucursor.getString(1));
                jsonObjectDatosmenu.put("descripcionmenu", datosmenucursor.getString(2));
                jsonObjectDatosmenu.put("espera", datosmenucursor.getString(3));
                jsonObjectDatosmenu.put("precio", datosmenucursor.getString(4));
                jsonObjectDatosmenu.put("mesa", datosmenucursor.getString(5));
                jsonObjectDatosmenu.put("bebida", datosmenucursor.getString(6));
                jsonObjectDatosmenu.put("postre", datosmenucursor.getString(7));
                jsonObjectDatosmenu.put("urlPhoto", datosmenucursor.getString(8));
                jsonValueObject.put("value", jsonObjectDatosmenu);

                jsonArrayDatosmenu.put(jsonValueObject);

                if (jsonArrayDatosmenu.length() > 0) {
                    parametros.putString("datos", jsonArrayDatosmenu.getJSONObject(position).toString());
                }

                Intent i = new Intent(getApplicationContext(), agregarplatillos.class);
                i.putExtras(parametros);
                startActivity(i);

            } catch (Exception e) {
                mensajes(e.getMessage());
            }

        }
    }

    private void buscarProductos() {
        TextView tempVal = findViewById(R.id.txtbuscar);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                menuArrayList.clear();
                if (tempVal.getText().toString().length()<1){
                    menuArrayList.addAll(menuArrayListCopy);
                } else{
                    for (menu PB : menuArrayListCopy){
                        String nombremenu = PB.getNombremenu();
                        String descripcionmenu = PB.getDescipcionmenu();
                        String mesa = PB.getMesa();
                        String precio = PB.getPrecio();
                        String buscando = tempVal.getText().toString().trim().toLowerCase();
                        if(nombremenu.toLowerCase().contains(buscando) ||
                                descripcionmenu.toLowerCase().contains(buscando) ||
                                mesa.toLowerCase().contains(buscando) ||
                                precio.toLowerCase().contains(buscando)){
                            menuArrayList.add(PB);
                        }
                    }
                }
                adactadorImagenes adactadorImagenes = new adactadorImagenes(getApplicationContext(), menuArrayList);
                ltsmenu.setAdapter(adactadorImagenes);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }




    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    private void obtenerDatosOffLine() {
        try {
            miconexion = new DB(getApplicationContext(), "", null, 1);
            datosmenucursor = miconexion.consultar_menu("Consultar1", null);
            if( datosmenucursor.moveToFirst() ){
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
            conexionserver conexionconServer = new conexionserver();
            String resp = conexionconServer.execute(u.url_consulta, "GET").get();
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
        } else {
            mensajes("Mostrando datos locales");
        }
      //1   1  obtenerDatosOffLine();
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
                        mismenus = new menu(
                                jsonObject.getString("_id"),
                                jsonObject.getString("_rev"),
                                jsonObject.getString("nombremenu"),
                                jsonObject.getString("descripcionmenu"),
                                jsonObject.getString("espera"),
                                jsonObject.getString("precio"),
                                jsonObject.getString("mesa"),
                                jsonObject.getString("bebida"),
                                jsonObject.getString("postre"),
                                jsonObject.getString("urlPhoto")
                        );
                        //        datosmenucursor.getString(9));
                        menuArrayList.add(mismenus);
                    }}
            }


            adactadorImagenes adactadorImagenes = new adactadorImagenes(getApplicationContext(), menuArrayList);
            ltsmenu.setAdapter(adactadorImagenes);
            registerForContextMenu(ltsmenu);
            menuArrayListCopy.addAll(menuArrayList);

        }catch (Exception e){
            mensajes(e.getMessage());
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_ordenes, menu);
        try {

                AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
                position = adapterContextMenuInfo.position;
                menu.setHeaderTitle(jsonArrayDatosmenu.getJSONObject(position).getJSONObject("value").getString("nombremenu"));

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
                case R.id.mnxVerOrden:
                    //   ver("datos");
                    break;
            }
        }catch (Exception ex){
            mensajes(ex.getMessage());
        }
        return super.onContextItemSelected(item);
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
