package com.example.calendarioescolar.Modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

public class AsignaturasBD extends SQLiteOpenHelper {

    Context contexto;


    public AsignaturasBD(Context contexto) {
        super(contexto, "asignaturas", null, 1);
        this.contexto = contexto;
    }


    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("CREATE TABLE asignatura (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
    }


    public String elemento(int id) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT nombre FROM asignatura WHERE _id = " + id, null);
        try {
            if (cursor.moveToFirst())
                return extraeAsignatura(cursor);
            else
                throw new SQLException("Error al acceder al elemento _id = " + id);
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null) cursor.close();
        }
    }


    public int annade(String nombre) {
        int _id = -1;
        getWritableDatabase().execSQL("INSERT INTO asignatura (nombre) VALUES ('" + nombre + "')");
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT _id FROM asignatura order by _id", null);
        if (c.moveToNext()) _id = c.getInt(0);
        c.close();
        return _id;

    }


    public int nuevo() {
        return 0;
    }

    public void borrar(int id) {
        getWritableDatabase().execSQL("DELETE FROM asignatura WHERE _id = " + id);
    }

    public int tamanno() {
        return extraeCursor().getCount();
    }

    public void actualiza(int id, String nombre) {
        getWritableDatabase().execSQL("UPDATE asignatura SET" +
                "   nombre = '" + nombre +
                " WHERE _id = " + id);
    }

    public static String extraeAsignatura(Cursor cursor) {
        return cursor.getString(1);
    }

    public Cursor extraeCursor() {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(contexto);
        String consulta = "SELECT * FROM asignatura";
        SQLiteDatabase bd = getReadableDatabase();
        return bd.rawQuery(consulta, null);
    }

}