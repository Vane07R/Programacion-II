package com.ugb.miprimercalculadora;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class adaptadorImagenes  extends BaseAdapter {
    Context context;
    ArrayList<peliculasd> datospeliculasArrayList;
    LayoutInflater layoutInflater;
    peliculasd misPeliculas;

    public adaptadorImagenes(Context context, ArrayList<peliculasd> datospeliculasArrayList) {
        this.context = context;
        this.datospeliculasArrayList = datospeliculasArrayList;
    }

    @Override
    public int getCount() {
        return datospeliculasArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return datospeliculasArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View encuadre = layoutInflater.inflate(R.layout.listview_imagenes, parent, false);
        TextView temp = encuadre.findViewById(R.id.lblTitulo);
        ImageView img = encuadre.findViewById(R.id.ImgPhoto);
        try{
            misPeliculas = datospeliculasArrayList.get(position);
            temp.setText(misPeliculas.getTitulo());

            temp = encuadre.findViewById(R.id.lblDuracion);
            temp.setText("Duracion: " +misPeliculas.getDuracion() +" minutos ");

            temp = encuadre.findViewById(R.id.lblPrecio);
            temp.setText("$"+misPeliculas.getPrecio());

            String urldefoto = misPeliculas.getUrlfoto();
            img.setImageURI(Uri.parse(urldefoto));

        }catch (Exception e){
        }
        return encuadre;
    }
}
