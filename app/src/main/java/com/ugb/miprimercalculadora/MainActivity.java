package com.ugb.miprimercalculadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.ScatteringByteChannel;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btn;
    DB miBD;
    ListView ltsProductos;
    Cursor datosProdutoCursor = null;
    ArrayList<productos>productosArrayList= new ArrayList<productos>();
    ArrayList<productos>productosArrayListCopy= new ArrayList<productos>();
    productos mis_productos;
    utilidades u;
    JSONArray jsonArrayDatosProducto;
    JSONObject jsonObjectDatosProducto;
    detectarInternet di;
    int position=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        di= new detectarInternet(getApplicationContext());
        btn=findViewById(R.id.btnAgregarProducto);
        btn.setOnClickListener(v->{
           agregarProductos("nuevo");
        });
        
        obtenerDatosProducto();
        buscarProductos();

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_productos,menu);
        try {

            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
           position=adapterContextMenuInfo.position;
            menu.setHeaderTitle(jsonArrayDatosProducto.getJSONObject(position).getString("nombre"));
        }catch (Exception e){
            mostrarMsgToast(e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {
       switch (item.getItemId()){
           case R.id.mnxAgregar:
               agregarProductos("nuevo");
               break;
           case R.id.mnxModificar:
               agregarProductos("modificar");
               break;
           case R.id.mnxEliminar:
               eliminarProducto();
               break;
       }
    }catch (Exception ex){
        mostrarMsgToast(ex.getMessage());
    }
       return super.onContextItemSelected(item);
    }

    private void eliminarProducto() {
        try {
        AlertDialog.Builder confirmar =new AlertDialog.Builder(MainActivity.this);
        confirmar.setTitle(datosProdutoCursor.getString(1));
        confirmar.setMessage("Esta seguro de eliminar el registro");
        confirmar.setPositiveButton("Si",  (dialog, which)-> {
            miBD = new DB(getApplicationContext(), "", null, 1);
            datosProdutoCursor = miBD.administracion_productos("eliminar", new String[]{datosProdutoCursor.getString(0)});
            obtenerDatosProducto();
            mostrarMsgToast("Registro Eliminado con exito...");
            dialog.dismiss();
        });
        confirmar.setNegativeButton("no",(dialog, which)-> {
            mostrarMsgToast("Eliminacion cancelada por el usuario");
            dialog.dismiss();
        });
        confirmar.create().show();
    }catch (Exception ex){
        mostrarMsgToast(ex.getMessage());
         }
    }

    private void buscarProductos(){
        TextView tempVal =findViewById(R.id.txtBuscarProductos);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    productosArrayList.clear();
                    if(tempVal.getText().toString().trim().length()<1 ){
                        productosArrayList.addAll(productosArrayListCopy);
                    }else{
                        for(productos am: productosArrayListCopy){
                            String nombre = am.getNombre();
                            String descripcion =am.getDescripcion();
                            String codigo =am.getCodigo();
                            String advertencias=am.getAdvertencias();
                            String precio=am.getPrecio();

                            String buscando = tempVal.getText().toString().trim().toLowerCase();

                            if(nombre.toLowerCase().trim().contains(buscando) ||
                                    descripcion.trim().toLowerCase().contains(buscando)||
                                    codigo.trim().contains(buscando)||
                                    advertencias.trim().toLowerCase().contains(buscando)||
                                    precio.trim().contains(buscando)
                            ){
                                final boolean add = productosArrayList.add(am);
                            }
                        }
                    }
                    adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(),productosArrayList);
                    ltsProductos.setAdapter(adaptadorImagenes);

                }catch (Exception e){
                    mostrarMsgToast(e.getMessage());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void agregarProductos(String accion){
        try {

            Bundle parametrosProductos= new Bundle();
            parametrosProductos.putString("accion",accion);
            parametrosProductos.putString("datos",jsonArrayDatosProducto.getJSONObject(position).toString());

            Intent agregarProducto = new Intent(getApplicationContext(), AgregarProducto.class);
            agregarProducto.putExtras(parametrosProductos);
            startActivity(agregarProducto);
        }catch (Exception e){
            mostrarMsgToast(e.getMessage());
        }
    }
    private void obtenerDatosProductosOffline(){
        try {
            miBD = new DB(getApplicationContext(), "", null, 1);
            datosProdutoCursor = miBD.administracion_productos("consultar", null);
            if (datosProdutoCursor.moveToFirst()) {//si hay productos que mostrar
                jsonObjectDatosProducto = new JSONObject();
                JSONObject jsonValueObject = new JSONObject();
                do {
                    jsonObjectDatosProducto.put("_id", datosProdutoCursor.getString(0));//idProducto
                    jsonObjectDatosProducto.put("_rev", datosProdutoCursor.getString(0));//rev
                    jsonObjectDatosProducto.put("nombre", datosProdutoCursor.getString(1));//nombre
                    jsonObjectDatosProducto.put("Descripcion", datosProdutoCursor.getString(2));//Descripcion
                    jsonObjectDatosProducto.put("codigo", datosProdutoCursor.getString(3));//codigo
                    jsonObjectDatosProducto.put("Advertencias", datosProdutoCursor.getString(4));//Advertencias
                    jsonObjectDatosProducto.put("precio", datosProdutoCursor.getString(5));//precio
                    jsonObjectDatosProducto.put("UrlImag", datosProdutoCursor.getString(6));//urlPhoto
                    jsonValueObject.put("value", jsonObjectDatosProducto);
                    jsonArrayDatosProducto.put(jsonValueObject);

                } while (datosProdutoCursor.moveToNext());
                mostarDatosProducto();
            } else {
                mostrarMsgToast("No hay datos de productos que mostrar, por favor agregue nuevos productos...");
                agregarProductos("nuevo");
            }
        }catch (Exception e){
            mostrarMsgToast(e.getMessage());
        }
    }
    private void obtenerDatosProductoOnLine(){
        try {
            ConexionServer conexionServer = new ConexionServer();

            String resp = conexionServer.execute(u.url_consulta, "GET").get();

            jsonObjectDatosProducto = new JSONObject(resp);
            jsonArrayDatosProducto = jsonObjectDatosProducto.getJSONArray("rows");
            mostarDatosProducto();
        }catch (Exception ex){
            mostrarMsgToast(ex.getMessage());
        }
    }

    private void obtenerDatosProducto(){
        //si tengo internet obtener datos amigos online, sino, obtener datos amigos offline
        if(di.hayConexionInternet()) {
            mostrarMsgToast("Hay internet, mostrando datos de la nube");
            obtenerDatosProductoOnLine();
        } else {
            jsonArrayDatosProducto = new JSONArray();
            mostrarMsgToast("NO hay internet, mostrando datos local");
            obtenerDatosProductosOffline();
        }

    }



    private void mostarDatosProducto(){
        try {
            if (jsonArrayDatosProducto.length()>0){
                ltsProductos = findViewById(R.id.ltsAgregarProductos);
                productosArrayList.clear();
                productosArrayListCopy.clear();

                JSONObject jsonObject;
                for (int i=0; i<jsonArrayDatosProducto.length();i++){
                    jsonObject = jsonArrayDatosProducto.getJSONObject(i).getJSONObject("value");

                    mis_productos = new productos(
                            jsonObject.getString("_id"),
                            jsonObject.getString("_rev"),
                            jsonObject.getString("nombre"),
                            jsonObject.getString("Descripcion"),
                            jsonObject.getString("codigo"),
                            jsonObject.getString("Advertencias"),
                            jsonObject.getString("precio"),
                            jsonObject.getString("urlPhoto")
                    );
                    productosArrayList.add(mis_productos);
                }
                adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(),productosArrayList);
                ltsProductos.setAdapter(adaptadorImagenes);

                registerForContextMenu(ltsProductos);

                productosArrayListCopy.addAll(productosArrayList);
            }else
                mostrarMsgToast("No hay registro que mostar");
            agregarProductos("nuevo");

        }catch (Exception e){
            mostrarMsgToast(e.getMessage());
        }
    }

    private void mostrarMsgToast(String mgs){
        Toast.makeText(getApplicationContext(),mgs,Toast.LENGTH_LONG).show();
    }


    private class ConexionServer extends AsyncTask<String, String, String> {
        HttpURLConnection urlConnection;

        protected void onPosExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... parametros) {
            StringBuilder result = new StringBuilder();
            try {
                String uri = parametros [0];
                String metodo = parametros[1];
                URL url = new URL(uri);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod(metodo);

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String linea;
                while ((linea=bufferedReader.readLine())!=null){
                    result.append(linea);

                }
            }catch (Exception e){
                //
            }
            return result.toString();
        }
    }
}

    class   productos{
    String idProducto;
    String rev;
    String nombre;
    String Descripcion;
    String codigo;
    String Advertencias;
    String precio;
    String UrlImag;

    public productos(String idProducto,String rev, String nombre, String Descripcion, String codigo, String Advertencias, String precio, String UrlImag) {
        this.idProducto = idProducto;
        this.rev = rev;
        this.nombre = nombre;
        this.Descripcion = Descripcion;
        this.codigo = codigo;
        this.Advertencias = Advertencias;
        this.precio = precio;
        this.UrlImag = UrlImag;
    }

        public String getRev() {
            return rev;
        }

        public void setRev(String rev) {
            this.rev = rev;
        }

        public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAdvertencias() {
        return Advertencias;
    }

    public void setAdvertencias(String advertencias) {
        this.Advertencias = Advertencias;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getUrlImag() {
        return UrlImag;
    }

    public void setUrlImg(String urlImg) {
        this.UrlImag = UrlImag;
    }
}