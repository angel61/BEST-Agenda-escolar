package com.example.calendarioescolar.Presentacion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.calendarioescolar.R;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.TimetableView;

import java.util.ArrayList;
import java.util.Calendar;

public class HorarioActivity extends AppCompatActivity {
    private Context context;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;

    private Toolbar toolbar;
    private TimetableView timetable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horario_layout);

        iniciar();
    }

    private void iniciar() {
        this.context = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        timetable = findViewById(R.id.timetable);
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_WEEK) != 1 && cal.get(Calendar.DAY_OF_WEEK) != 7)
            timetable.setHeaderHighlight(cal.get(Calendar.DAY_OF_WEEK) - 1);
        iniciarListeners();
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.horario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_annadir:
                Intent i = new Intent(this, EditarHorarioActivity.class);
                i.putExtra("mode", REQUEST_ADD);
                startActivityForResult(i, REQUEST_ADD);
                return true;
            case R.id.accion_limpiar:
                timetable.removeAll();
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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


    private void guardarPreferencias(String data) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("timetable_demo", data);
        editor.commit();
    }


    private void cargarTabla() {
        timetable.removeAll();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String datos = pref.getString("timetable_demo", "");
        if (datos == null && datos.equals("")) return;
        if (datos != null && !datos.equals("")) timetable.load(datos);
    }
}
