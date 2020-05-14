package com.example.calendarioescolar;

import android.app.Application;

import com.example.calendarioescolar.Adaptadores.AdaptadorAgendaBD;
import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.AsignaturasBD;
import com.github.tlaabs.timetableview.TimetableView;


public class Aplicacion extends Application {


    public AgendaBD agendaBD;
    public AdaptadorAgendaBD adaptador;
    public AdaptadorAgendaBD adaptadorHome;
    public AsignaturasBD asigBD;

    @Override
    public void onCreate() {
        super.onCreate();

        agendaBD = new AgendaBD(this);
        asigBD = new AsignaturasBD(this);
        adaptador = new AdaptadorAgendaBD(agendaBD,R.layout.elemento_agenda, agendaBD.extraeCursor());
        adaptador.setTiempo(0);


        adaptadorHome = new AdaptadorAgendaBD(agendaBD,R.layout.elemento_agenda_home, agendaBD.extraeCursorHome());

    }

}