package com.ugb.miprimercalculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn=findViewById(R.id.btnAgregarProducto);
        btn.setOnClickListener( V -> {
            new AgregarProducto();
        });
        Intent AgregarProducto = new Intent(getApplicationContext(), AgregarProducto.class);
        startActivity(AgregarProducto);
    }
}