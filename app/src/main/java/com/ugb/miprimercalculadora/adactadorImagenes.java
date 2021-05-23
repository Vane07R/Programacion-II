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

public class adactadorImagenes extends BaseAdapter {
    Context context;
    ArrayList<menu> datosmenuArrayList;
    LayoutInflater layoutInflater;
    menu mismenu;

    public adactadorImagenes(Context context, ArrayList<menu> datosmenuArrayList) {
        this.context = context;
        this.datosmenuArrayList = datosmenuArrayList;
    }

    @Override
    public int getCount() {
        return datosmenuArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return datosmenuArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View encuadre = layoutInflater.inflate(R.layout.listview_imagenes, parent, false);
        TextView temp = encuadre.findViewById(R.id.lblPlatillo);
        ImageView img = encuadre.findViewById(R.id.ImgPhoto);
        try{
            mismenu = datosmenuArrayList.get(position);
            temp.setText(mismenu.getNombre());

            temp = encuadre.findViewById(R.id.lblDuracion);
            temp.setText(mismenu.getEspera());

            temp = encuadre.findViewById(R.id.lblMesa);
            temp.setText(mismenu.getMesa());

            String urldefoto = mismenu.getUrlfoto();

            img.setImageURI(Uri.parse(urldefoto));

        }catch (Exception e){
        }
        return encuadre;
    }
}
