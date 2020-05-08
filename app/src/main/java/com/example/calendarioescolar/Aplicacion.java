package com.example.calendarioescolar;

import android.app.Application;

import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.AsignaturasBD;


public class Aplicacion extends Application {


    public AgendaBD agendaBD;
    public AdaptadorAgendaBD adaptador;
    public AsignaturasBD asigBD;

    @Override
    public void onCreate() {
        super.onCreate();

        agendaBD = new AgendaBD(this);
        asigBD = new AsignaturasBD(this);
        adaptador = new AdaptadorAgendaBD(agendaBD, agendaBD.extraeCursor());
        adaptador.setTiempo(0);

    }

}