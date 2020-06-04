package com.example.calendarioescolar;

import android.app.Application;

import com.example.calendarioescolar.Adaptadores.AdaptadorAgendaBD;
import com.example.calendarioescolar.Fragmentos.HomeFragment;
import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.AsignaturasBD;

/**
 * Esta clase es utilizada para compartir informacion entre clases
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 */
public class Aplicacion extends Application {


    public AgendaBD agendaBD;
    public AdaptadorAgendaBD adaptador;
    public AdaptadorAgendaBD adaptadorHome;
    public AsignaturasBD asigBD;
    public int agendaCursor;
    public HomeFragment home;


    /**
     * Al iniciar esta clase inicializa la informacion que van a compartir las actividades
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     */
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