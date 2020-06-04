package com.example.calendarioescolar.Adaptadores;

import android.database.Cursor;

import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.RepositorioAgenda;
import com.example.calendarioescolar.Modelo.agenda_object;

/**
 * Clase para adaptar la base de datos a nuestra app para que se guarden tanto los cambios
 * que realicemos en los elementos de la agenda como cuando creamos un nuevo lugar
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 */
public class AdaptadorAgendaBD extends AdaptadorAgenda {

    protected Cursor cursor;

    private int tiempo;

    /**
     * Constructor para inicializar el cursor de la clase
     *
     * @param repAgenda interfaz RepositorioAgenda
     * @param cursor
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public AdaptadorAgendaBD(RepositorioAgenda repAgenda, int idV, Cursor cursor) {
        super(repAgenda, idV);
        this.cursor = cursor;
    }


    /**
     * Devuelve el cursor que esta utilizando el adaptador
     *
     * @return Cursor
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public Cursor getCursor() {
        return cursor;
    }


    /**
     * Establece el cursor que va a utilizar el adaptador
     *
     * @param cursor utilizado para extraer la informacion que va a utilizar el adaptador
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    /**
     * Devuelve el elemento de la agenda a partir de la posición pasada como parametro
     *
     * @param posicion Posicion del elemento al cual se quiere acceder
     * @return agenda_object
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public agenda_object agendaPosicion(int posicion) {
        cursor.moveToPosition(posicion);
        return AgendaBD.extraeAgenda(cursor);
    }


    /**
     * Devuelve el ID del elemento de la agenda dependiendo de la posición en la que se encuentre en la base de datos
     *
     * @param posicion Posicion pasada como parametro para obtener la id de dicha posicion
     * @return id del lugar
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public int idPosicion(int posicion) {
        cursor.moveToPosition(posicion);
        return cursor.getInt(0);
    }


    /**
     * Retorna la posición a partir del ID que ha sido pasado como parametro de la base de datos "agenda"
     *
     * @param id ID del elemento de la agenda del cual se quiere obtener la posicion del cursor
     * @return posición del lugar
     * @author Angel Lopez Palacios
     * @version 1.0
     */
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


    /**
     * Método que actualiza los ViewHolder a partir de la posicion del elemento
     *
     * @param holder
     * @param posicion
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        agenda_object agenda = agendaPosicion(posicion);
        holder.personaliza(agenda);
        holder.itemView.setTag(new Integer(posicion));
    }


    /**
     * Devuelve el número total de elementos en el conjunto de datos
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }


    /**
     * Pone al elemento el tiempo
     *
     * @param tiempo
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }
}