package com.example.calendarioescolar;

import android.app.Application;

import com.example.calendarioescolar.Modelo.AgendaBD;



public class Aplicacion extends Application {


    public AgendaBD agendaBD;
    public AdaptadorAgendaBD adaptador;

    @Override
    public void onCreate() {
        super.onCreate();

        agendaBD = new AgendaBD(this);
        adaptador = new AdaptadorAgendaBD(agendaBD, agendaBD.extraeCursor());
        adaptador.setTiempo(0);
    }

}