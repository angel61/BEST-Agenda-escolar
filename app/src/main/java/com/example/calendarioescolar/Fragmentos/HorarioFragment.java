package com.example.calendarioescolar.Fragmentos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.calendarioescolar.Actividades.EditarHorarioActivity;
import com.example.calendarioescolar.R;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.TimetableView;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Clase que extiende de Fragment utilizada para mostrar la interfaz de la pantalla principal del horario
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 * @see Fragment
 */
public class HorarioFragment extends Fragment {

    private Context context;
    private Activity actividad;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;
    private View root;
    private TimetableView timetable;


    /**
     * Se encarga de cargar la interfaz que se va ha mostrar
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     * @author Angel Lopez Palacios
     * @version 1.0
     * @see HorarioFragment#iniciar()
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_horario, container, false);
        setHasOptionsMenu(true);
        iniciar();
        return root;
    }

    /**
     * Se encarga de inicializar los componentes del fragmento
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     * @see HorarioFragment#iniciarListeners() ()
     */
    private void iniciar() {
        actividad = getActivity();
        context = getActivity();

        timetable = root.findViewById(R.id.timetable);
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_WEEK) != 1 && cal.get(Calendar.DAY_OF_WEEK) != 7)
            timetable.setHeaderHighlight(cal.get(Calendar.DAY_OF_WEEK) - 1);
        iniciarListeners();
    }


    /**
     * Se encarga de inicializar los listeners de los componentes del fragmento
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    private void iniciarListeners() {

        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                Intent i = new Intent(context, EditarHorarioActivity.class);
                i.putExtra("mode", REQUEST_EDIT);
                i.putExtra("idx", idx);
                i.putExtra("schedules", schedules);
                startActivityForResult(i, REQUEST_EDIT);
            }
        });

        cargarTabla();
    }


    private void cargarTabla() {
        timetable.removeAll();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(actividad);
        String datos = pref.getString("timetable_demo", "");
        if (datos == null && datos.equals("")) return;
        if (datos != null && !datos.equals("")) timetable.load(datos);
    }


    /**
     * Evento que es lanzado cuando se selecciona alguna opcion del menu
     *
     * @param item objeto item del menu
     * @return boolean
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_annadir:
                Intent i = new Intent(actividad, EditarHorarioActivity.class);
                i.putExtra("mode", REQUEST_ADD);
                startActivityForResult(i, REQUEST_ADD);
                return true;
            case R.id.accion_limpiar:


                AlertDialog.Builder builder = new AlertDialog.Builder(actividad);
                builder.setMessage("¿Deseas borrar el horario?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                timetable.removeAll();
                                guardarPreferencias(timetable.createSaveData());
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return true;

        }
        return false;
    }


    /**
     * En el resultado de una actividad segun el codigo del resultado se ejcutaran algunas ordenes
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADD:
                if (resultCode == EditarHorarioActivity.RESULT_OK_ADD) {
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    timetable.add(item);
                    guardarPreferencias(timetable.createSaveData());
                }
                break;
            case REQUEST_EDIT:
                if (resultCode == EditarHorarioActivity.RESULT_OK_EDIT) {
                    int idx = data.getIntExtra("idx", -1);
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    timetable.edit(idx, item);
                    guardarPreferencias(timetable.createSaveData());
                } else if (resultCode == EditarHorarioActivity.RESULT_OK_DELETE) {
                    int idx = data.getIntExtra("idx", -1);
                    timetable.remove(idx);
                    guardarPreferencias(timetable.createSaveData());
                }
                break;
        }
    }

    /**
     * Se encarga de guardar el horairo como preferencias
     *
     * @param data
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    private void guardarPreferencias(String data) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(actividad);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("timetable_demo", data);
        editor.commit();
    }
}