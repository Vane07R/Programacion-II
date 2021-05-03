package com.ugb.miprimercalculadora;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//Allison Vanessa Rodriguez Sosa
//Flor Mabel Contreras Rodriguez
//Roger Alberto Chávez Zelaya
//Elmer Antonio Angel Reyes

public class DB extends SQLiteOpenHelper {
    static String nombre_bd = "DB_Peliculas";
    static String tblpeliculas = "CREATE TABLE tblpeliculas (idpelicula integer primary key autoincrement, titulo text, sipnosis text, duracion text, precio text, urlfoto text, urlvideo text)";

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombre_bd, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblpeliculas);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor administracion_peliculas(String accion, String[] datos){
        Cursor datocursor = null;
        SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();

        switch (accion){
            case "Consultar":
                datocursor = sqLiteDatabaseR.rawQuery("select * from tblpeliculas",null);
                break;

            case "nuevo":
                sqLiteDatabaseW.execSQL("INSERT INTO tblpeliculas(titulo, sipnosis, duracion, precio, urlfoto, urlvideo) VALUES ('"+datos[1]+"','"+datos[2]+"','"+datos[3]+"','"+datos[4]+"','"+datos[5]+"','"+datos[6]+"')");
                break;
            case "modificar":
                sqLiteDatabaseW.execSQL("update tblpeliculas set titulo='"+datos[1]+"',sipnosis='"+datos[2]+"',duracion='"+datos[3]+"',precio='"+datos[4]+"',urlfoto='"+datos[5]+"',urlvideo='"+datos[6]+"' where idpelicula='"+datos[0]+"'");
                break;
            case "eliminar":
                sqLiteDatabaseW.execSQL("DELETE FROM tblpeliculas WHERE idpelicula='"+ datos[0]+"'");
                break;
        }

        return datocursor;
    }

    public Cursor eliminar(String accion,String idd){
        Cursor datocursor = null;
        SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();

        switch (accion){

            case "eliminar":
                sqLiteDatabaseW.execSQL("DELETE FROM tblpeliculas WHERE idpelicula='"+ idd+"'");
                break;
        }
        return datocursor; }
}
