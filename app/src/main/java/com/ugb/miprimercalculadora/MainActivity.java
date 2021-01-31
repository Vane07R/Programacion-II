package com.ugb.miprimercalculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void calcular (View view){
        TextView tempVal1 = (TextView)findViewById(R.id.txtnum1);
        double num1 = Double.parseDouble(tempVal1.getText().toString());

        tempVal1 = (TextView)findViewById(R.id.txtnum2);
        double num2 = Double.parseDouble(tempVal1.getText().toString());


        double respuesta = 1;
        RadioButton optOperacionesBasicas = findViewById(R.id.optSuma);
        if (optOperacionesBasicas.isChecked() ){
            respuesta = num1 + num2;
        }

        optOperacionesBasicas = findViewById(R.id.optResta);
        if (optOperacionesBasicas.isChecked() ){
            respuesta = num1 - num2;
        }

        optOperacionesBasicas = findViewById(R.id.optMultiplicacion);
        if (optOperacionesBasicas.isChecked() ){
            respuesta = num1 * num2;
        }

        optOperacionesBasicas = findViewById(R.id.optDivision);
        if (optOperacionesBasicas.isChecked() ){
            respuesta = num1 / num2;
        }

        optOperacionesBasicas = findViewById(R.id.optFactorial);
            // i++ -> i=i+1 -> i+=1
            if (optOperacionesBasicas.isChecked ()) {
                for (int i=1; i<=num1; i++){
                    respuesta *=i;
                }


            }
        optOperacionesBasicas = findViewById(R.id.optPorcentaje);
        if (optOperacionesBasicas.isChecked() ){
            respuesta = ((num1 / num2) *100);
        }

        optOperacionesBasicas = findViewById(R.id.optExponenciacion);
        if (optOperacionesBasicas.isChecked() ){
            respuesta =  Math.pow(num1,num2);
        }

        optOperacionesBasicas = findViewById(R.id.optRaiz);
        if (optOperacionesBasicas.isChecked() ){
            respuesta =  Math.pow(num1 , 1/num2);
        }

        optOperacionesBasicas = findViewById(R.id.optModulo);
        if (optOperacionesBasicas.isChecked() ){
            respuesta = num1 % num2;
        }

        optOperacionesBasicas = findViewById(R.id.optMayor2);
        if (optOperacionesBasicas.isChecked()){
            if (num1>=num2){
                respuesta=num1;
            }
            else respuesta=num2;

        }

        tempVal1 = findViewById(R.id.lblrespuesta);
        tempVal1.setText("Respuesta:"+respuesta);

    }

}