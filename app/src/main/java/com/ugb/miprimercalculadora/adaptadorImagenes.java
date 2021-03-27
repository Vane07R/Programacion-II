package com.ugb.miprimercalculadora;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class adaptadorImagenes extends BaseAdapter{
    Context context;
    ArrayList<productos>datosProductoArraylist;
    LayoutInflater layoutInflater;
    productos misProductos;

    public adaptadorImagenes(Context context, ArrayList<productos>datosProductoArraylist) {
        this.context = context;
        this.datosProductoArraylist = datosProductoArraylist;
    }
    @Override
    public int getCount() {
        return datosProductoArraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return datosProductoArraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(datosProductoArraylist.get(position).idProducto);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
       View itemView = layoutInflater.inflate(R.layout.listview_imagenes,parent,false);
        TextView tempVal = itemView.findViewById(R.id.lblTitulo);
        ImageView imgViewView = itemView.findViewById(R.id.ImgPhoto);
try {  misProductos = datosProductoArraylist.get(position);
    tempVal.setText(misProductos.getNombre());
    Bitmap imagenBitmap = BitmapFactory.decodeFile(misProductos.getUrlImag());
    imgViewView.setImageBitmap(imagenBitmap);

}catch (Exception e){
}
        return itemView;
    }
}
