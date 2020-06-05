package com.example.calendarioescolar.CasosDeUso;

import android.app.Activity;
import android.content.Intent;

import com.example.calendarioescolar.Actividades.AgendaObjectActivity;
import com.example.calendarioescolar.Actividades.EditarObjectAgendaActivity;
import com.example.calendarioescolar.Adaptadores.AdaptadorAgendaBD;
import com.example.calendarioescolar.Aplicacion;
import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.agenda_object;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase encargada de cumplir los casos de uso del modulo de agenda
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 */
public class CasosUsoAO {
    private Activity actividad;
    private AgendaBD agBD;
    private AdaptadorAgendaBD adaptador;
    private Aplicacion aplicacion;

    /**
     * Constructor de la clase que obtiene como parametros la actividad, las clase que controla la base de datos y el adaptador del recyclerView
     *
     * @param actividad Actividad que va a ser manejada
     * @param agBD      Clase que controla la base de datos
     * @param adaptador Adaptador del recyclerView
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public CasosUsoAO(Activity actividad, AgendaBD agBD,
                      AdaptadorAgendaBD adaptador) {
        this.actividad = actividad;
        this.agBD = agBD;
        this.adaptador = adaptador;
        aplicacion = (Aplicacion) actividad.getApplication();
    }


    /**
     * Metodo utilizado para mostrar el elemento de la agenda en  una actividad
     *
     * @param pos             Posicion del elemento de la agenda que va a mostrarse en otra actividad
     * @param codidoSolicitud Codigo de solicitud que se va a utilizar al lanzar la actividad
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void mostrar(int pos, int codidoSolicitud) {
        if (adaptador.getCursor().getCount() > 0) {
            Intent i = new Intent(actividad, AgendaObjectActivity.class);
            i.putExtra("pos", pos);
            i.putExtra("codR", codidoSolicitud);
            actividad.startActivityForResult(i, codidoSolicitud);
        } else {
        }
    }


    /**
     * Se encarga de conseguir el elemento de la agenda y mostrar su informacion en el formulario para editar
     *
     * @param pos             Posicion del elemento de la agenda que va a editarse en otra actividad
     * @param codidoSolicitud Codigo de solicitud que se va a utilizar al lanzar la actividad
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void editar(int pos, int codidoSolicitud) {
        Intent i = new Intent(actividad, EditarObjectAgendaActivity.class);
        i.putExtra("pos", pos);
        actividad.startActivityForResult(i, codidoSolicitud);
    }


    /**
     * Este metodo elimina el elemendo de la agenda mediante su id
     *
     * @param id ID del elemento de la agenda que va a eliminarse
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void borrar(final int id) {
        agBD.borrar(id);
        adaptador.setCursor(agBD.extraeCursor(aplicacion.agendaCursor));
        adaptador.notifyDataSetChanged();
        aplicacion.adaptadorHome.setCursor(agBD.extraeCursor(2));
        aplicacion.adaptadorHome.notifyDataSetChanged();
        aplicacion.home.sinContenido();
        actividad.finish();
    }


    /**
     * Se encarga de guardar los cambios de un elemento de la agenda
     *
     * @param id            ID del elemento de la agenda que va a eliminarse
     * @param nuevoagObject Elemento con los datos que se van a guardar en la posicion del ID
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void guardar(int id, agenda_object nuevoagObject) {
        agBD.actualiza(id, nuevoagObject);

        adaptador.setCursor(agBD.extraeCursor(aplicacion.agendaCursor));
        adaptador.notifyDataSetChanged();
        aplicacion.adaptadorHome.setCursor(agBD.extraeCursor(2));
        aplicacion.adaptadorHome.notifyDataSetChanged();
        aplicacion.home.sinContenido();
    }

    /**
     * Se encarga de compartir el elemnto que fue recibido como parametro
     *
     * @param AO Elemento de la agenda el cual va a ser compartido
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void compartir(agenda_object AO) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,
                AO.getTitulo() + " - " + (new SimpleDateFormat("dd/MM/yyyy").format(new Date(AO.getFecha()))));
        actividad.startActivity(i);
    }


    /**
     * Se encarga de crear un elemento nuevo en la agenda
     *
     * @param tipo El tipo de elemento de la agenda del cual se quiere crear el elemento
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void nuevo(int tipo) {
        int id = agBD.nuevo(tipo);
        System.out.println(id);
        Intent i = new Intent(actividad, EditarObjectAgendaActivity.class);
        i.putExtra("_id", id);
        i.putExtra("tab", 0);
        actividad.startActivityForResult(i, 8);
    }
}

