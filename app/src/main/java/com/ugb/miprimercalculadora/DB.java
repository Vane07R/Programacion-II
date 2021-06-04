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
    static String tblusu = "CREATE TABLE tblusuario(idusuario integer primary key autoincrement, nombre text, dui text, telefono text, correo text, contra text)";
    static String tblmenu ="CREATE TABLE tblmenu(idmenu integer primary key autoincrement, nombremenu text , descripcionmenu text, espera text ,precio text,mesa text, bebida text, postre text,urlfoto text )";
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
        Cursor datocursor = null;
        SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();

        switch (accion){
            case "Consultar1":
                datocursor = sqLiteDatabaseR.rawQuery("select * from tblmenu",null);
                break;
            case "nuevo1":
                sqLiteDatabaseW.execSQL("INSERT INTO tblmenu(nombremenu, descripcionmenu, espera,precio ,mesa , bebida , postre,urlfoto) VALUES ('"+datos[1]+"','"+datos[2]+"','"+datos[3]+"','"+datos[4]+"','"+datos[5]+"','"+datos[6]+"','"+datos[7]+"','"+datos[8]+"')");
                break;
            case "modificar":
                sqLiteDatabaseW.execSQL("update tblmenu set nombremenu='"+datos[1]+"',descripcionmenu='"+datos[2]+"',espera='"+datos[3]+"',precio='"+datos[4]+"',mesa='"+datos[5]+"',bebida='"+datos[6]+"',postre='"+datos[7]+"',urlfoto='"+datos[8]+"' where idmenu='"+datos[0]+"'");
                break;
        }

        return datocursor; }

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

    public Cursor eliminar(String accion,String idd){
        Cursor datocursor = null;
        SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();

        switch (accion){

            case "eliminar":
                sqLiteDatabaseW.execSQL("DELETE FROM tblmenu WHERE idmenu='"+ idd+"'");
                break;
        }
        return datocursor; }
}

