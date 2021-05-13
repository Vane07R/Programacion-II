package com.ugb.miprimercalculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InicioActivity extends AppCompatActivity implements View.OnClickListener {
Button btnEditar,btnEliminar,btnMostrar,btnSalir;
TextView nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        nombre=(TextView)findViewById(R.id.NombreUsuario);
        btnEditar=(Button)findViewById(R.id.btnEditar);
        btnEliminar=(Button)findViewById(R.id.btnEliminar);
        btnMostrar=(Button)findViewById(R.id.btnMostar);
        btnSalir=(Button)findViewById(R.id.btnSalir);
        btnEditar.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        btnMostrar.setOnClickListener(this);
        btnSalir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.btnEditar:
              Intent c=new Intent(InicioActivity.this,.class);
              startActivity(i2);
              finish();
              break;
          case R.id.btnEliminar:
              break;
          case R.id.btnMostar:
              Intent i2=new Intent(InicioActivity.this,Mostrar.class);
              startActivity(i2);
              finish();
              break;
          case R.id.btnSalir:
              Intent i2=new Intent(InicioActivity.this,MainActivity.class);
              startActivity(i2);
              finish();
              break;
      }
    }

}