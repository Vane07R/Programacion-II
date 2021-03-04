package com.ugb.miprimercalculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
    TabHost tbhConversores;
    RelativeLayout contenidoView;



    private Tarifa tarifa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tarifa=new Tarifa();
        this.tarifa.inicilizarIntervalos();

        contenidoView = findViewById(R.id.vista);
        tbhConversores = findViewById(R.id.tabs);
        tbhConversores.setup();

        tbhConversores.addTab(tbhConversores.newTabSpec("Agua").setContent(R.id.Agua).setIndicator("Agua"));
        tbhConversores.addTab(tbhConversores.newTabSpec("Area").setContent(R.id.tabArea).setIndicator("Area"));

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

class conversores {

    Double[][] conversor = {
            {1.0,0.1323,0.111111,0.092903,0.00014774656489, 0.000013188960818,0.0000092903}
    };

    public double covertir(int option, int de, int a, double cantidad) {
        return conversor[option][a] / conversor[option][de] * cantidad;
    }

}