package com.ugb.miprimercalculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btn;
    DB miBD;
    ListView ltsProductos;
    Cursor datosProdutoCursor = null;
    ArrayList<productos>productosArrayList= new ArrayList<productos>();
    productos mis_productos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn=findViewById(R.id.btnAgregarProducto);
        btn.setOnClickListener( V -> {
            new AgregarProducto();
        });
        obtenerDatosProducto();

        Intent AgregarProducto = new Intent(getApplicationContext(), AgregarProducto.class);
        startActivity(AgregarProducto);
    }
    private void obtenerDatosProducto(){
        miBD = new DB(getApplicationContext(),"",null,1);
        datosProdutoCursor = miBD.administracion_productos("consultar",null);
        if( datosProdutoCursor.moveToFirst() ){//si hay productos que mostrar
            mostarDatosProducto();
        } else {//sino que llame para agregar nuevos productos...
            mostrarMsgToast("No hay datos de productos que mostrar, por favor agregue nuevos productos...");
           new AgregarProducto();
        }

    }
    private void mostarDatosProducto(){
        ltsProductos = findViewById(R.id.ltsAgregarProductos);
        productosArrayList.clear();
        do {
            mis_productos = new productos(
                    datosProdutoCursor.getString(0),//idProducto
                    datosProdutoCursor.getString(1),//nombre
                    datosProdutoCursor.getString(2),//Descripcion
                    datosProdutoCursor.getString(3),//codigo
                    datosProdutoCursor.getString(4),//precio
                    datosProdutoCursor.getString(5),//Advertencias
                    datosProdutoCursor.getString(6) //urlPhoto
            );

            productosArrayList.add(mis_productos);
        }while (datosProdutoCursor.moveToNext());
adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(),productosArrayList);
ltsProductos.setAdapter(adaptadorImagenes);
registerForContextMenu(ltsProductos);
    }
    private void mostrarMsgToast(String mgs){
        Toast.makeText(getApplicationContext(),mgs,Toast.LENGTH_LONG).show();
    }
}
class productos{
    String idProducto;
    String nombre;
    String Descripcion;
    String codigo;
    String Advertencias;
    String precio;
    String UrlImag;

    public productos(String idProducto, String nombre, String Descripcion, String codigo, String Advertencias, String precio, String UrlImag) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.Descripcion = Descripcion;
        this.codigo = codigo;
        this.Advertencias = Advertencias;
        this.precio = precio;
        this.UrlImag = UrlImag;
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