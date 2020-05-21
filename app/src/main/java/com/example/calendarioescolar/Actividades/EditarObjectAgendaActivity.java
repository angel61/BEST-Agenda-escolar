package com.example.calendarioescolar.Actividades;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.calendarioescolar.Adaptadores.AdaptadorAgendaBD;
import com.example.calendarioescolar.Aplicacion;
import com.example.calendarioescolar.CasosDeUso.CasosUsoAO;
import com.example.calendarioescolar.CasosDeUso.CasosUsoAsignatura;
import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.AsignaturasBD;
import com.example.calendarioescolar.Modelo.agenda_object;
import com.example.calendarioescolar.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;


public class EditarObjectAgendaActivity extends AppCompatActivity {
    public AgendaBD agendaBD;
    private CasosUsoAO casosUso;
    private int pos;
    private agenda_object agendaObject;

    private TextInputLayout titulo;
    private TextInputLayout comentario;
    private TextInputLayout asignatura;
    private TextInputEditText asignaturaText;
    private TextView hora;
    private CalendarView calendario;

    private AdaptadorAgendaBD adaptador;
    private int _id;
    private Bundle extras;
    private Toolbar tool;
    private CasosUsoAsignatura casosUsoAsignatura;
    private AsignaturasBD asBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_agenda);

        extras = getIntent().getExtras();
        pos = extras.getInt("pos", 0);
        _id = extras.getInt("_id", 0);
        agendaBD = ((Aplicacion) getApplication()).agendaBD;
        adaptador = ((Aplicacion) getApplication()).adaptador;
        if (_id == 0) {
            _id = adaptador.idPosicion(pos);
        }
        casosUso = new CasosUsoAO(this, agendaBD, adaptador);
        agendaObject = agendaBD.elemento(_id);
        _id = agendaObject.getId();
        asBD = ((Aplicacion) getApplication()).asigBD;
        casosUsoAsignatura = new CasosUsoAsignatura(this, asBD);
        actualizaVistas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_editar_agenda, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_guardar:
                String tit = titulo.getEditText().getText().toString();
                String asig = asignatura.getEditText().getText().toString();
                if (tit.length() > 0 && (asig.length() > 0 || agendaObject.getTipoAg().getRecurso() == R.drawable.recordatorio)) {
                    agendaObject.setTitulo(tit);
                    agendaObject.setComentario(comentario.getEditText().getText().toString());
                    agendaObject.setAsig(asig);
                    casosUso.guardar(_id, agendaObject);
                    finish();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Faltan campos por rellenar")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    if(asignatura.getEditText().getText().toString().length()<=0){
                                        asignatura.setError("Campo vacio");
                                        asignatura.setErrorEnabled(true);
                                    }
                                    if(titulo.getEditText().getText().toString().length()<=0){
                                        titulo.setError("Campo vacio");
                                        titulo.setErrorEnabled(true);
                                    }
                                }
                            })
                            .show();
                }
                return true;
            case android.R.id.home:

                if (extras.getInt("_id", 0) != 0) agendaBD.borrar(_id);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void actualizaVistas() {
        titulo = findViewById(R.id.tituloe);
        titulo.getEditText().setText(agendaObject.getTitulo());
        comentario = findViewById(R.id.comentarioe);
        comentario.getEditText().setText(agendaObject.getComentario());
        asignatura = findViewById(R.id.asignaturae);
        asignaturaText = findViewById(R.id.asignaturaTIET);
        listaAsignaturas();
        if (agendaObject.getTipoAg().getTexto().equals("Recordatorio")) {
            asignatura.setVisibility(View.GONE);
        } else {
            asignatura.getEditText().setText(agendaObject.getAsig());
        }
        calendario = findViewById(R.id.calendarioe);
        calendario.setDate(agendaObject.getFecha());
        Calendar horaDate = Calendar.getInstance();
        horaDate.setTimeInMillis(agendaObject.getFecha());
        hora = findViewById(R.id.hora);
        hora.setText(horaDate.get(Calendar.HOUR_OF_DAY) + ":" + horaDate.get(Calendar.MINUTE));

        CalendarView.OnDateChangeListener list = new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.YEAR, year);
                seleccionarHora(cal.getTimeInMillis());
            }
        };
        calendario.setOnDateChangeListener(list);

        tool = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void listaAsignaturas() {
        asignatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> array = new ArrayList<String>();
                array.add("+ Añadir asignatura");

                array = casosUsoAsignatura.arrayAsignaturas(array);

                final String[] a = array.toArray(new String[0]);
                final boolean annadir = false;
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditarObjectAgendaActivity.this);
                builder.setTitle("Seleccionar una asignatura")
                        .setItems(a, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    casosUsoAsignatura.dialogoAnnadirAsignatura();
                                } else {
                                    System.out.println(which);
                                    asignatura.getEditText().setText(a[which]);
                                }
                            }
                        }).show();
            }
        });
        asignaturaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asignatura.performClick();
            }
        });
        asignaturaText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    asignatura.performClick();
                }
            }
        });
        titulo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(titulo.getEditText().getText().toString().length()>0) {
                    titulo.setErrorEnabled(false);
                }
                return false;
            }
        });
    }

    private void seleccionarHora(long milis) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milis);
        cal.set(Calendar.SECOND, 0);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minutes);
                hora.setText(hourOfDay + ":" + minutes);
                agendaObject.setFecha(cal.getTimeInMillis());
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (extras.getInt("_id", 0) != 0) agendaBD.borrar(_id);
        finish();
    }
}
