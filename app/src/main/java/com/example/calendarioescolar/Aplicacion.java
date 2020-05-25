package com.example.calendarioescolar;

import android.app.Application;
import android.view.View;

import com.example.calendarioescolar.Adaptadores.AdaptadorAgendaBD;
import com.example.calendarioescolar.Fragmentos.HomeFragment;
import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.AsignaturasBD;


public class Aplicacion extends Application {


    public AgendaBD agendaBD;
    public AdaptadorAgendaBD adaptador;
    public AdaptadorAgendaBD adaptadorHome;
    public AsignaturasBD asigBD;
    public int agendaCursor;
    public HomeFragment home;

    @Override
    public void onCreate() {
        super.onCreate();

        agendaBD = new AgendaBD(this);
        asigBD = new AsignaturasBD(this);
        agendaCursor = 4;
        adaptador = new AdaptadorAgendaBD(agendaBD, R.layout.elemento_agenda, agendaBD.extraeCursor(4));
        adaptador.setTiempo(0);


        adaptadorHome = new AdaptadorAgendaBD(agendaBD, R.layout.elemento_agenda_home, agendaBD.extraeCursor(2));

    }

}