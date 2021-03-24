package com.ugb.miprimercalculadora;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Switch;

import androidx.annotation.Nullable;


public class DB extends SQLiteOpenHelper {
    static String nombreDB = "db_Tienda";
    static String tblProductos = "CREATE TABLE tblproductos (idproductos integer primary key autoincrement, nombre text, descripcion text, codigo text, advertencias text, precio text, urlPhoto text)";

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombreDB, factory, version); //CREATE DATABASE db_Tienda;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(tblProductos);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ///No, por que es para migrar o actualizar a una nueva version...
    }

    public Cursor administracion_productos(String accion, String[] datos) {
        Cursor datosCursor = null;
        SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();
        switch (accion) {
            case "consultar":
                datosCursor = sqLiteDatabaseR.rawQuery("Select * from tblproductos order by nombre", null);
                break;

            case "nuevo":
                sqLiteDatabaseW.execSQL("INSERT INTO tblproductos(nombre, descripcion, codigo, advertencias, precio, urlPhoto) VALUES ('"+datos[1]+"','"+datos[2]+"','"+datos[3]+"','"+datos[4]+"','"+datos[5]+"','"+datos[6]+"')");
                 break;
            case "modificar":
                sqLiteDatabaseW.execSQL("UPDATE tblproductos SET nombre='"+datos[1]+"',descripcion='"+datos[2]+"',codigo='"+datos[3]+"',Advertencia='"+datos[4]+"',precio='"+datos[5]+"',urlPhoto='"+datos[6]+"' WHERE idProduto='"+datos[0]+"'");
                break;
        }
        return datosCursor;

    }
}
