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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    static String nombre_bd = "DB_usuario";
    static String tblusu = "CREATE TABLE tblusuario(idusuario integer primary key autoincrement, nombre text, dui text, telefono text, correo text, contra text)";
    static String tblmenu ="CREATE TABLE tblmenu(idmenu integer primary key autoincrement,idusuario text, nombremenu text , descripcionmenu text, espera text ,precio text,mesa text, bebida text, postre text,urlfoto text, urlvideo text )";
    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombre_bd, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblusu);
        db.execSQL(tblmenu);
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
                sqLiteDatabaseW.execSQL("INSERT INTO tblusuario(nombre, dui, telefono, correo, contra) VALUES ('"+datos[0]+"','"+datos[1]+"','"+datos[2]+"','"+datos[3]+"','"+datos[4]+"')");
                break;
        }

        return datocursor; }
    public Cursor consultar_menu(String accion, String[]datos){
        Cursor datocursor1 = null;
        SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();

        switch (accion){
            case "Consultar1":
                datocursor1 = sqLiteDatabaseR.rawQuery("select * from tblmenu",null);
                break;
        }

        return datocursor1; }

    public Cursor consultar_usuario(String accion, String dui, String contra){
        Cursor datocursor = null;
        SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();

        switch (accion){
            case "consultar":
                datocursor = sqLiteDatabaseR.rawQuery("SELECT * FROM tblusuario WHERE dui = "+dui+" AND contra ='"+contra+"'",null);
                break;
        }

        return datocursor; }
}

