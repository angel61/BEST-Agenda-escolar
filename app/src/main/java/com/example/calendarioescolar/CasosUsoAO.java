package com.example.calendarioescolar;

import android.app.Activity;
import android.content.Intent;

import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.agenda_object;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CasosUsoAO {
    private Activity actividad;
    private AgendaBD agBD;
    private AdaptadorAgendaBD adaptador;
    private Aplicacion apli;


    public CasosUsoAO(Activity actividad, AgendaBD agBD,
                      AdaptadorAgendaBD adaptador,Aplicacion apli) {
        this.actividad = actividad;
        this.agBD = agBD;
        this.adaptador = adaptador;
        this.apli=apli;
    }


    public void mostrar(int pos, int codidoSolicitud, int tab) {
        if (adaptador.getCursor().getCount() > 0) {
            Intent i = new Intent(actividad, AgendaObjectActivity.class);
            i.putExtra("pos", pos);
            i.putExtra("tab", tab);
            actividad.startActivityForResult(i, codidoSolicitud);
        } else {
        }
    }

    public void editar(int pos, int codidoSolicitud, int tab) {
        Intent i = new Intent(actividad, EditarObjectAgendaActivity.class);
        i.putExtra("pos", pos);
        i.putExtra("tab", tab);
        actividad.startActivityForResult(i, codidoSolicitud);
    }

    public void borrar(final int id) {
        agBD.borrar(id);
        adaptador.setCursor(agBD.extraeCursor(adaptador.getTiempo()));
        adaptador.notifyDataSetChanged();
        actividad.finish();
    }

    public void borrarSinFinish(final int id) {
        agBD.borrar(id);
        adaptador.setCursor(agBD.extraeCursor(adaptador.getTiempo()));
        adaptador.notifyDataSetChanged();
    }

    public void guardar(int id, agenda_object nuevoagObject) {
        agBD.actualiza(id, nuevoagObject);
        apli.adaptador.setCursor(agBD.extraeCursor(0));
        apli.adaptador.notifyDataSetChanged();
        apli.adaptadorPa.setCursor(agBD.extraeCursor(1));
        apli.adaptadorPa.notifyDataSetChanged();
    }

    public void compartir(agenda_object AO) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,
                AO.getTitulo() + " - " + (new SimpleDateFormat("dd/MM/yyyy").format(new Date(AO.getFecha()))));
        actividad.startActivity(i);
    }

    public void nuevo() {
        int id = agBD.nuevo();
        Intent i = new Intent(actividad, EditarObjectAgendaActivity.class);
        i.putExtra("_id", id);
        actividad.startActivityForResult(i, 8);
    }
}

