package com.example.calendarioescolar.Modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

public class AgendaBD extends SQLiteOpenHelper
        implements RepositorioAgenda {

    Context contexto;


    public AgendaBD(Context contexto) {
        super(contexto, "recordatorios", null, 1);
        this.contexto = contexto;
    }


    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("CREATE TABLE agenda (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT, " +
                "comentario TEXT, " +
                "tipo INTEGER, " +
                "asignatura TEXT, " +
                "fecha BIGINT)");
        bd.execSQL("INSERT INTO agenda VALUES (null, " +
                "'Prueba1', " +
                "'Esto es una prueba para comprobar si funciona correctamente', " +
                TipoAgenda.RECORDATORIO.ordinal() + ", '', "+System.currentTimeMillis()+")");
        bd.execSQL("INSERT INTO agenda VALUES (null, " +
                "'Prueba2', " +
                "'Esto es una prueba para comprobar si funciona correctamente', " +
                TipoAgenda.EJERCICIOS.ordinal() + ", 'Mates', "+System.currentTimeMillis()+")");
        bd.execSQL("INSERT INTO agenda VALUES (null, " +
                "'Prueba3', " +
                "'Esto es una prueba para comprobar si funciona correctamente', " +
                TipoAgenda.TRABAJO.ordinal() + ", 'Lengua', "+System.currentTimeMillis()+")");
        bd.execSQL("INSERT INTO agenda VALUES (null, " +
                "'Prueba4', " +
                "'Esto es una prueba para comprobar si funciona correctamente', " +
                TipoAgenda.EXAMEN.ordinal() + ", 'Tecnologia', "+System.currentTimeMillis()+")");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
    }

    @Override
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


    @Override
    public void annade(agenda_object agendaobject) {

    }


    @Override
    public int nuevo() {
        int _id = -1;
        getWritableDatabase().execSQL("INSERT INTO agenda (titulo, " +
                "comentario, tipo, asignatura, fecha" +
                ") VALUES ('', '',  0, '', null)");
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT _id FROM agenda WHERE fecha = null", null);
        if (c.moveToNext()) _id = c.getInt(0);
        c.close();
        return _id;
    }

    @Override
    public void borrar(int id) {
        getWritableDatabase().execSQL("DELETE FROM agenda WHERE _id = " + id);
    }

    @Override
    public int tamanno() {
        return 0;
    }

    @Override
    public void actualiza(int id, agenda_object agendaobject) {
        getWritableDatabase().execSQL("UPDATE agenda SET" +
                "   titulo = '" + agendaobject.getTitulo() +
                "', comentario = '" + agendaobject.getComentario() +
                "', tipo = " + agendaobject.getTipoAg().ordinal() +
                " , asignatura = '" + agendaobject.getAsig()+
                "', fecha = " + agendaobject.getFecha() +
                " WHERE _id = " + id);
    }

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

    public Cursor extraeCursor(int num) {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(contexto);
        String consulta;
        if(num==0){
            switch (pref.getString("orden", "0")) {
                case "0":
                    consulta = "SELECT * FROM agenda WHERE fecha>="+System.currentTimeMillis()+" ORDER BY fecha";
                    break;
                case "1":
                    consulta = "SELECT * FROM agenda WHERE fecha>="+System.currentTimeMillis()+" ORDER BY fecha DESC";
                    break;
                default:
                    consulta = "SELECT * FROM agenda WHERE fecha>="+System.currentTimeMillis()+" ORDER BY fecha";
                    break;
            }}else if(num==1){
            switch (pref.getString("orden", "0")) {
                case "0":
                    consulta = "SELECT * FROM agenda WHERE fecha<"+System.currentTimeMillis()+" ORDER BY fecha DESC";
                    break;
                case "1":
                    consulta = "SELECT * FROM agenda WHERE fecha<"+System.currentTimeMillis()+" ORDER BY fecha DESC";
                    break;
                default:
                    consulta = "SELECT * FROM agenda WHERE  fecha<"+System.currentTimeMillis()+" ORDER BY fecha";
                    break;
            }}else{
            switch (pref.getString("orden", "0")) {
                case "0":
                    consulta = "SELECT * FROM agenda ";
                    break;
                case "1":
                    consulta = "SELECT * FROM agenda ORDER BY fecha DESC";
                    break;
                default:
                    consulta = "SELECT * FROM agenda ORDER BY fecha";
                    break;
            }

        }
        consulta += " LIMIT " + pref.getString("maximo", "12");
        SQLiteDatabase bd = getReadableDatabase();
        return bd.rawQuery(consulta, null);
    }
}