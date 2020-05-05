package com.example.calendarioescolar;

import android.database.Cursor;

import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.RepositorioAgenda;
import com.example.calendarioescolar.Modelo.agenda_object;

public class AdaptadorAgendaBD extends AdaptadorAgenda {

    protected Cursor cursor;

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    private int tiempo;
    public AdaptadorAgendaBD(RepositorioAgenda repAgenda, Cursor cursor) {
        super(repAgenda);
        this.cursor = cursor;
    }


    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }


    public agenda_object agendaPosicion(int posicion) {
        cursor.moveToPosition(posicion);
        return AgendaBD.extraeAgenda(cursor);
    }

    public int idPosicion(int posicion) {
        cursor.moveToPosition(posicion);
        return cursor.getInt(0);
    }


    public int posicionId(int id) {
        int pos = 0;
        int b = -1;
        cursor.moveToPosition(pos);
        while (pos != getItemCount()) {
            if (id == cursor.getInt(0)) {
                b = pos;
                break;
            } else {
                pos++;
                cursor.moveToPosition(pos);
            }
        }
        return b;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        agenda_object agenda = agendaPosicion(posicion);
        holder.personaliza(agenda);
        holder.itemView.setTag(new Integer(posicion));
    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}