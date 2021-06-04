package com.ugb.miprimercalculadora;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

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
}

