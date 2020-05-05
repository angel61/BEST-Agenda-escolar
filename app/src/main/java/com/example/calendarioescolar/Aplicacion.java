package com.example.calendarioescolar;

import android.app.Application;

import com.example.calendarioescolar.Modelo.AgendaBD;



public class Aplicacion extends Application {


    public AgendaBD agendaBD;
    public AdaptadorAgendaBD adaptador;
    public AdaptadorAgendaBD adaptadorPa;

    @Override
    public void onCreate() {
        super.onCreate();

        agendaBD = new AgendaBD(this);
        adaptador = new AdaptadorAgendaBD(agendaBD, agendaBD.extraeCursor(0));
        adaptador.setTiempo(0);
        adaptadorPa = new AdaptadorAgendaBD(agendaBD, agendaBD.extraeCursor(1));
        adaptadorPa.setTiempo(1);
    }

}