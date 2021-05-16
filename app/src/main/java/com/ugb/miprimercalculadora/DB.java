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
    static String nombre_bd = "DB_usuario";
    static String tblusu = "CREATE TABLE tblusuario(idusuario integer primary key autoincrement, nombre text, dui text, telefono text, profecion text, correo text, contraseña text)";
    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombre_bd, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblusu);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor agregar_usuario(String accion, String[] datos){
        Cursor datocursor = null;
        SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();

        switch (accion){
            case "nuevo":
                sqLiteDatabaseW.execSQL("INSERT INTO tblusuario(nombre, dui, telefono, profecion, correo, contraseña) VALUES ('"+datos[0]+"','"+datos[1]+"','"+datos[2]+"','"+datos[3]+"','"+datos[4]+"', '"+datos[5]+"')");
                break;
        }

        return datocursor; }

    public Cursor consultar_usuario(String accion, String dui, String contraseña){
        Cursor datocursor = null;
        SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();

        switch (accion){
            case "consultar":
                datocursor = sqLiteDatabaseR.rawQuery("SELECT * FROM tblusuario WHERE dui = "+dui+" AND contraseña ='"+contraseña+"'",null);
                break;
        }

        return datocursor; }
}
