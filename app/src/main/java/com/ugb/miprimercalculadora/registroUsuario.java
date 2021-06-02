package com.ugb.miprimercalculadora;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class registroUsuario extends AppCompatActivity {

    FloatingActionButton btnregresar;
    String accion = "nuevo";
    Button btnagregar;
    DB miconexion;
    TextView temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registroempleado);

        miconexion = new DB(getApplicationContext(),"",null,1);
        btnregresar = findViewById(R.id.btnregresar);
        btnagregar = findViewById(R.id.btnguardar);

        btnregresar.setOnClickListener(v -> {
            regresarmainactivity();
        });

        btnagregar.setOnClickListener(v -> {
            agregar();
        });
    }

    private void agregar() {
        try {
            temp = findViewById(R.id.txtnombre);
            String nombre = temp.getText().toString();

            temp = findViewById(R.id.txtdui);
            String dui = temp.getText().toString();

            temp = findViewById(R.id.txttelefono);
            String telefono = temp.getText().toString();

            temp = findViewById(R.id.txtcorreo);
            String correo = temp.getText().toString();

            temp = findViewById(R.id.txtcontra);
            String pass = temp.getText().toString();

            String[] datos = {nombre, dui, telefono, correo, pass};
            miconexion.agregar_usuario(accion, datos);

            mensajes("Registro guardado con exito.");
            regresarmainactivity();
        }catch (Exception w){
            mensajes(w.getMessage());
        }
    }
    private void regresarmainactivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

}
