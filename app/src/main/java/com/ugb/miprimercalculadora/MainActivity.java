package com.ugb.miprimercalculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

private Tarifa tarifa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tarifa=new Tarifa();
        this.tarifa.inicilizarIntervalos();
    }
    public void calcular (View view){
        EditText txtnum1 = (EditText) findViewById(R.id.txtnum1);
TextView lblrespuesta = (TextView) findViewById(R.id.lblrespuesta);
        double metrosCubicos = 0;
        try {
            metrosCubicos = Double.parseDouble(txtnum1.getText().toString());
        }catch(Exception e){
            Toast.makeText(this.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG);
        }
        String totalAPagar =String.valueOf(this.tarifa.calcularTotalAPagar(metrosCubicos));
        lblrespuesta.setText(totalAPagar);
    }

}