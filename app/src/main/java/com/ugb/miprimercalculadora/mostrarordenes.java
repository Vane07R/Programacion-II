package com.ugb.miprimercalculadora;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class mostrarordenes extends AppCompatActivity {
    public static  final String nombre="nombre";
    public static  final String duii="duii";
    public static  final String telefono="telefono";
    public static  final String correo="correo";
    public static  final String contra="contra";


    TextView nombre1, dui1, telefotol,profecion1,correo1,contra1;


    FloatingActionButton btnadd;
    DB miconexion;
    ListView ltsmenu;
    Cursor datosmenucursor = null;
    ArrayList<menu> menuArrayList=new ArrayList<menu>();
    ArrayList<menu> menuArrayListCopy=new ArrayList<menu>();
    menu mismenu;
    JSONArray jsonArrayDatosmenu;
    JSONObject jsonObjectDatosmenu;
    String idlocal;
    int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostarmenu);

        nombre1 = findViewById(R.id.nombre);

        nombre1.setText(getIntent().getStringExtra("nombre"));

    }

    private void Agregar(String accion) {
        Bundle parametros = new Bundle();
        parametros.putString("accion", accion);
        Intent i = new Intent(getApplicationContext(), agregarplatillos.class);
        i.putExtras(parametros);
        startActivity(i);
    }

    private void mostrarDatos() {
        try{
            ltsmenu = findViewById(R.id.list);
            menuArrayList.clear();
            menuArrayListCopy.clear();
            JSONObject jsonObject;


            adactadorImagenes adactadorImagenes = new adactadorImagenes(getApplicationContext(), menuArrayList);
            ltsmenu.setAdapter(adactadorImagenes);
            registerForContextMenu(ltsmenu);
            menuArrayListCopy.addAll(menuArrayList);

        }catch (Exception e){
            mensajes(e.getMessage());
        }
    }
    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_ordenes, menu);
        try {

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
                    //  Modificar ("modificar");
                    break;
                case R.id.mnxEliminar:
                    //  Eliminar();
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
}
