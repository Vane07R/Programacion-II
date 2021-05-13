package com.ugb.miprimercalculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistarActivity extends AppCompatActivity implements View.OnClickListener {
EditText us,pas,nom,ap;
Button reg,can;
daoUsuario dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);
        us=(EditText)findViewById(R.id.Ruser);
        pas=(EditText)findViewById(R.id.Rcontraseña);
        nom= (EditText) findViewById(R.id.Rnombre);
        ap= (EditText) findViewById(R.id.Rapellidos);
        reg= (Button) findViewById(R.id.btnRRegistrar);
        can= (Button) findViewById(R.id.btnRCancelar);
        reg.setOnClickListener(this);
        can.setOnClickListener(this);
        dao=new daoUsuario(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRRegistrar:
                Usuario u=new Usuario();
                u.setUsuario(us.getText().toString());
                u.setPassword(pas.getText().toString());
                u.setNombre(nom.getText().toString());
                u.setApellidos(ap.getText().toString());
                if(!u.isNull()){
                    Toast.makeText(this,"Error:Campos vacios",Toast.LENGTH_LONG).show();
                }else if(dao.insertUsuario(u)){
                    Toast.makeText(this,"Registro guardado con exito",Toast.LENGTH_LONG).show();
                    Intent i2=new Intent(RegistarActivity.this,MainActivity.class);
                    startActivity(i2);
                    finish();

                }else{
                    Toast.makeText(this,"Este usuario ya se encuentra registrado",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btnRCancelar:
                Intent i=new Intent(RegistarActivity.this,MainActivity.class);
                startActivity(i);
                finish();
                break;



        }

        }


}