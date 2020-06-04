package com.example.calendarioescolar.CasosDeUso;

import android.app.Activity;
import android.content.Intent;

import com.example.calendarioescolar.Actividades.EditarHorarioActivity;
import com.example.calendarioescolar.Aplicacion;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.TimetableView;

import java.util.ArrayList;

public class CasosUsoHorario {
    private Activity actividad;
    private Aplicacion aplicacion;

    public CasosUsoHorario(Activity actividad) {
    this.actividad = actividad;
    aplicacion = (Aplicacion) actividad.getApplication();
}
    public void guardarAnnadir(Schedule schedule){
        Intent i = new Intent();
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        schedules.add(schedule);
        i.putExtra("schedules", schedules);
        actividad.setResult(EditarHorarioActivity.RESULT_OK_ADD, i);
        actividad.finish();
    }

    public void guardarEditar(Schedule schedule, int editIdx) {Intent i = new Intent();
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        schedules.add(schedule);
        i.putExtra("idx", editIdx);
        i.putExtra("schedules", schedules);
        actividad.setResult(EditarHorarioActivity.RESULT_OK_EDIT, i);
        actividad.finish();
    }
    public void borrar(int _id) {
        Intent i = new Intent();
        i.putExtra("idx", _id);
        actividad.setResult(EditarHorarioActivity.RESULT_OK_DELETE, i);
        actividad.finish();
    }
}
