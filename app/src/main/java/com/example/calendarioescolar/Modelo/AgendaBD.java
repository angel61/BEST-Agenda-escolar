package com.example.calendarioescolar.Modelo;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

/**
 * Clase que crea  la tabla de base de datos "agenda" si no existe y que contiene
 * operaciones basicas con la base de datos SQLite
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 */
public class AgendaBD extends SQLiteOpenHelper {

    Context contexto;

    /**
     * Constructor de la clase
     *
     * @param contexto Contexto
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public AgendaBD(Context contexto) {
        super(contexto, "recordatorios", null, 1);
        this.contexto = contexto;
    }


    /**
     * Crea la tabla de base de datos "agenda" si no esta creada ya.
     *
     * @param bd base datos
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("CREATE TABLE agenda (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT, " +
                "comentario TEXT, " +
                "tipo INTEGER, " +
                "asignatura TEXT, " +
                "fecha BIGINT)");
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
     * Recibe identificador y devuelve el elemento de la agenda correspondiente a él.
     *
     * @param id identificador del elemento de la agenda
     * @return agenda_object
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public agenda_object elemento(int id) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM agenda WHERE _id = " + id, null);
        try {
            if (cursor.moveToNext())
                return extraeAgenda(cursor);
            else
                throw new SQLException("Error al acceder al elemento _id = " + id);
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null) cursor.close();
        }
    }


    /**
     * Crea un nuevo elemento vacio y devuelve su identificador correspondiente
     *
     * @return int
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public int nuevo(int tipo) {
        int _id = -1;
        long fecha = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(fecha);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        fecha = cal.getTimeInMillis();

        getWritableDatabase().execSQL("INSERT INTO agenda (titulo, " +
                "comentario, tipo, asignatura, fecha" +
                ") VALUES ('', '',  " + tipo + ", '', " + fecha + ")");
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT _id FROM agenda WHERE fecha = " + fecha, null);
        if (c.moveToNext()) _id = c.getInt(0);
        c.close();
        return _id;
    }


    /**
     * Borrar el elemento al que corresponde el id pasado por parametro
     *
     * @param id ID correspondiente al identificador del elemento
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void borrar(int id) {
        getWritableDatabase().execSQL("DELETE FROM agenda WHERE _id = " + id);
    }


    /**
     * Recibe el elemento que va actualizar y el id que lo identifica en la base de datos
     * para poder actualizar.
     *
     * @param id           identificador del lugar
     * @param agendaobject Elemento que recibe con los nuevos datos.
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void actualiza(int id, agenda_object agendaobject) {
        getWritableDatabase().execSQL("UPDATE agenda SET" +
                "   titulo = '" + agendaobject.getTitulo() +
                "', comentario = '" + agendaobject.getComentario() +
                "', tipo = " + agendaobject.getTipoAg().ordinal() +
                " , asignatura = '" + agendaobject.getAsig() +
                "', fecha = " + agendaobject.getFecha() +
                " WHERE _id = " + id);
    }


    /**
     * Extrae el elemento de un cursor
     *
     * @param cursor El cursor del cual se va a extraer el elemento
     * @return agenda_object
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public static agenda_object extraeAgenda(Cursor cursor) {
        agenda_object agendaobject = new agenda_object();
        agendaobject.setTitulo(cursor.getString(1));
        agendaobject.setComentario(cursor.getString(2));
        agendaobject.setTipoAg(TipoAgenda.values()[cursor.getInt(3)]);
        agendaobject.setAsig(cursor.getString(4));
        agendaobject.setFecha(cursor.getLong(5));
        agendaobject.setId(cursor.getInt(0));
        return agendaobject;
    }


    /**
     * Extrae el cursor segun el numero recibido como parametro.
     *
     * @param i Numero usado para extraer elementos de la agenda segun el filtro
     * @return Cursor
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public Cursor extraeCursor(int i) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        Long hoy = cal.getTimeInMillis();
        String consulta = "";
        switch (i) {
            case 0:
                consulta = "SELECT * FROM agenda WHERE fecha<" + System.currentTimeMillis() + " ORDER BY fecha";
                break;
            case 1:
                cal.add(Calendar.DAY_OF_YEAR, -1);
                Long ayer = cal.getTimeInMillis();
                consulta = "SELECT * FROM agenda WHERE fecha>=" + ayer
                        + " AND fecha<" + hoy + " ORDER BY fecha";
                break;
            case 2:
                cal.add(Calendar.DAY_OF_YEAR, 1);
                Long mannana = cal.getTimeInMillis();
                consulta = "SELECT * FROM agenda WHERE fecha>=" + hoy
                        + " AND fecha<" + mannana + " ORDER BY fecha";
                break;
            case 3:
                cal.add(Calendar.DAY_OF_YEAR, 1);
                Long manna = cal.getTimeInMillis();
                cal.add(Calendar.DAY_OF_YEAR, 1);
                Long pasmanna = cal.getTimeInMillis();
                consulta = "SELECT * FROM agenda WHERE fecha>=" + manna
                        + " AND fecha<" + pasmanna + " ORDER BY fecha";
                break;
            case 4:
                consulta = "SELECT * FROM agenda WHERE fecha>=" + System.currentTimeMillis() + " ORDER BY fecha";
                break;
            case 5:
                consulta = "SELECT * FROM agenda ORDER BY fecha";
                break;
        }
        SQLiteDatabase bd = getReadableDatabase();
        return bd.rawQuery(consulta, null);
    }


    /**
     * @return int
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public int tamanno() {
        return 0;
    }
}