package com.example.calendarioescolar.CasosDeUso;

import android.app.Activity;
import android.content.Intent;

import com.example.calendarioescolar.Adaptadores.AdaptadorAgendaBD;
import com.example.calendarioescolar.Aplicacion;
import com.example.calendarioescolar.Presentacion.AgendaObjectActivity;
import com.example.calendarioescolar.Presentacion.EditarObjectAgendaActivity;
import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.agenda_object;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CasosUsoAO {
    private Activity actividad;
    private AgendaBD agBD;
    private AdaptadorAgendaBD adaptador;
    private Aplicacion aplicacion;

    public CasosUsoAO(Activity actividad, AgendaBD agBD,
                      AdaptadorAgendaBD adaptador) {
        this.actividad = actividad;
        this.agBD = agBD;
        this.adaptador = adaptador;
        aplicacion=(Aplicacion)actividad.getApplication();
    }


    public void mostrar(int pos, int codidoSolicitud) {
        if (adaptador.getCursor().getCount() > 0) {
            Intent i = new Intent(actividad, AgendaObjectActivity.class);
            i.putExtra("pos", pos);
            actividad.startActivityForResult(i, codidoSolicitud);
        } else {
        }
    }

    public void editar(int pos, int codidoSolicitud) {
        Intent i = new Intent(actividad, EditarObjectAgendaActivity.class);
        i.putExtra("pos", pos);
        actividad.startActivityForResult(i, codidoSolicitud);
    }

    public void borrar(final int id) {
        agBD.borrar(id);
        adaptador.setCursor(agBD.extraeCursor(aplicacion.agendaCursor));
        adaptador.notifyDataSetChanged();
        aplicacion.adaptadorHome.setCursor(agBD.extraeCursor(2));
        aplicacion.adaptadorHome.notifyDataSetChanged();
        actividad.finish();
    }

    public void borrarSinFinish(final int id) {
        agBD.borrar(id);
        adaptador.setCursor(agBD.extraeCursor(aplicacion.agendaCursor));
        adaptador.notifyDataSetChanged();
        aplicacion.adaptadorHome.setCursor(agBD.extraeCursor(2));
        aplicacion.adaptadorHome.notifyDataSetChanged();
    }

    public void guardar(int id, agenda_object nuevoagObject) {
        agBD.actualiza(id, nuevoagObject);

        adaptador.setCursor(agBD.extraeCursor(aplicacion.agendaCursor));
        adaptador.notifyDataSetChanged();
        aplicacion.adaptadorHome.setCursor(agBD.extraeCursor(2));
        aplicacion.adaptadorHome.notifyDataSetChanged();
    }

    public void compartir(agenda_object AO) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,
                AO.getTitulo() + " - " + (new SimpleDateFormat("dd/MM/yyyy").format(new Date(AO.getFecha()))));
        actividad.startActivity(i);
    }

    public void nuevo(int tipo) {
        int id = agBD.nuevo(tipo);
        System.out.println(id);
        Intent i = new Intent(actividad, EditarObjectAgendaActivity.class);
        i.putExtra("_id", id);
        i.putExtra("tab", 0);
        actividad.startActivityForResult(i, 8);
    }
}

