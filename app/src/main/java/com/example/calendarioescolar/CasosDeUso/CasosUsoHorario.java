package com.example.calendarioescolar.CasosDeUso;

import android.app.Activity;
import android.content.Intent;

import com.example.calendarioescolar.Actividades.EditarHorarioActivity;
import com.example.calendarioescolar.Aplicacion;
import com.github.tlaabs.timetableview.Schedule;

import java.util.ArrayList;


/**
 * Clase encargada de cumplir los casos de uso del modulo de horario
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 */
public class CasosUsoHorario {
    private Activity actividad;
    private Aplicacion aplicacion;


    /**
     * Constructor de la clase que obtiene como parametro la actividad
     *
     * @param actividad Actividad que va a ser manejada
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public CasosUsoHorario(Activity actividad) {
        this.actividad = actividad;
        aplicacion = (Aplicacion) actividad.getApplication();
    }

    /**
     * Se encarga de guardar el elemento del horario cuando es añadido
     *
     * @param schedule Elemento del horario nuevo
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void guardarAnnadir(Schedule schedule) {
        Intent i = new Intent();
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        schedules.add(schedule);
        i.putExtra("schedules", schedules);
        actividad.setResult(EditarHorarioActivity.RESULT_OK_ADD, i);
        actividad.finish();
    }


    /**
     * Se encarga de guardar el elemento del horario cuando es editado
     *
     * @param schedule Elemento del horario a editar
     * @param editIdx  ID del elemento
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void guardarEditar(Schedule schedule, int editIdx) {
        Intent i = new Intent();
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        schedules.add(schedule);
        i.putExtra("idx", editIdx);
        i.putExtra("schedules", schedules);
        actividad.setResult(EditarHorarioActivity.RESULT_OK_EDIT, i);
        actividad.finish();
    }

    /**
     * Se encarga de eliminar el elemento del horario
     *
     * @param _id ID del elemento a eliminar
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void borrar(int _id) {
        Intent i = new Intent();
        i.putExtra("idx", _id);
        actividad.setResult(EditarHorarioActivity.RESULT_OK_DELETE, i);
        actividad.finish();
    }
}
