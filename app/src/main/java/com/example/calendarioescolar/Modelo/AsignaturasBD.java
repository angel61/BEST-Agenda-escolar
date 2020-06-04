package com.example.calendarioescolar.Modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

/**
 * Clase que crea la tabla de base de datos "asignatura" si no existe y que contiene
 * operaciones basicas con la base de datos SQLite
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 */
public class AsignaturasBD extends SQLiteOpenHelper {

    Context contexto;

    /**
     * Constructor de la clase
     *
     * @param contexto Contexto
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public AsignaturasBD(Context contexto) {
        super(contexto, "asignaturas", null, 1);
        this.contexto = contexto;
    }


    /**
     * Crea la tabla de base de datos "asignatura" si no esta creada ya.
     *
     * @param bd base datos
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("CREATE TABLE asignatura (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT PRIMARY KEY)");
    }


    /**
     * Actualizar base de datos a una nueva version
     *
     * @param db         base de datos
     * @param oldVersion Version vieja de la base de datos.
     * @param newVersion Version nueva de la base de datos.
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
    }


    /**
     * Recibe identificador y devuelve la asignatura correspondiente a él.
     *
     * @param id identificador de la asignatura
     * @return String
     * @author Angel Lopez Palacios
     * @version 1.0
     */
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


    /**
     * Crea una nueva asignatura y devuelve su identificador correspondiente, si no existe una con el mismo nombre.
     *
     * @param nombre Nombre de la signatura
     * @return int
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public int annade(String nombre) {
        Cursor cursor = getReadableDatabase().rawQuery(
            "SELECT nombre FROM asignatura WHERE upper(nombre) = '" + nombre.toUpperCase()+"'", null);

        int _id = -1;

        if(cursor.getCount()<=0) {
            getWritableDatabase().execSQL("INSERT INTO asignatura (nombre) VALUES ('" + nombre + "')");
            Cursor c = getReadableDatabase().rawQuery(
                    "SELECT _id FROM asignatura order by _id", null);
            if (c.moveToNext()) _id = c.getInt(0);
            c.close();
        }
        return _id;

    }


    /**
     * Borrar la asignatura a la que corresponde el id pasado por parametro
     *
     * @param id ID correspondiente al identificador de la asignatura
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void borrar(int id) {
        getWritableDatabase().execSQL("DELETE FROM asignatura WHERE _id = " + id);
    }


    /**
     * Extrae la asignatura de un cursor
     *
     * @param cursor El cursor del cual se va a extraer la asignatura
     * @return String
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public static String extraeAsignatura(Cursor cursor) {
        return cursor.getString(1);
    }


    /**
     * Extrae el cursor
     *
     * @return Cursor
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public Cursor extraeCursor() {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(contexto);
        String consulta = "SELECT * FROM asignatura";
        SQLiteDatabase bd = getReadableDatabase();
        return bd.rawQuery(consulta, null);
    }

}