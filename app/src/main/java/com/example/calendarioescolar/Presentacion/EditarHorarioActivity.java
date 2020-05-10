package com.example.calendarioescolar.Presentacion;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.calendarioescolar.Aplicacion;
import com.example.calendarioescolar.CasosDeUso.CasosUsoAsignatura;
import com.example.calendarioescolar.Modelo.AsignaturasBD;
import com.example.calendarioescolar.R;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class EditarHorarioActivity extends AppCompatActivity {
    public static final int RESULT_OK_ADD = 1;
    public static final int RESULT_OK_EDIT = 2;
    public static final int RESULT_OK_DELETE = 3;

    private Context context;

    private TextInputLayout asignatura;
    private TextInputEditText asignaturaText;
    private Spinner day;
    private TextInputLayout hInicio;
    private TextInputEditText hInicioText;
    private TextInputLayout hFin;
    private TextInputEditText hFinText;


    private int mode;

    private Schedule schedule;
    private int editIdx;


    private CasosUsoAsignatura casosUsoAsignatura;
    private AsignaturasBD asBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_horario_layout);
        iniciar();
    }

    private void iniciar() {
        this.context = this;

        asBD = ((Aplicacion) getApplication()).asigBD;
        casosUsoAsignatura = new CasosUsoAsignatura(this, asBD);

        asignatura = findViewById(R.id.asignaturae);
        asignaturaText = findViewById(R.id.asignaturaeTIET);
        day = findViewById(R.id.dias_semana);
        hInicio = findViewById(R.id.hora_inicio);
        hFin = findViewById(R.id.hora_fin);
        hInicioText = findViewById(R.id.h_inicioTXT);
        hFinText = findViewById(R.id.h_finTXT);
        Toolbar tool = findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Calendar cal = Calendar.getInstance();
        int hora = cal.get(Calendar.HOUR_OF_DAY);

        schedule = new Schedule();
        schedule.setStartTime(new Time(hora, 0));
        schedule.setEndTime(new Time(hora + 1, 0));

        iniciarListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent i = getIntent();
        mode = i.getIntExtra("mode", HorarioActivity.REQUEST_ADD);

        if (mode == HorarioActivity.REQUEST_EDIT) {
            cargarDatos();
            getMenuInflater().inflate(R.menu.editar_horario, menu);
        } else {
            getMenuInflater().inflate(R.menu.annadir_horario, menu);
        }
        return true;
    }


    private void iniciarListeners() {
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
        asignatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> array = new ArrayList<String>();
                array.add("+ Añadir asignatura");

                array = casosUsoAsignatura.arrayAsignaturas(array);

                final String[] a = array.toArray(new String[0]);
                final boolean annadir = false;
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Seleccionar una asignatura")
                        .setItems(a, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    casosUsoAsignatura.dialogoAnnadirAsignatura();
                                } else {
                                    asignatura.getEditText().setText(a[which]);
                                }
                            }
                        }).show();
            }
        });

        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                schedule.setDay(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Calendar cal = Calendar.getInstance();
        hInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context, listener, schedule.getStartTime().getHour(), schedule.getStartTime().getMinute(), true);
                dialog.show();
            }

            private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    if (minute > 9) {
                        hInicio.getEditText().setText(hourOfDay + ":" + minute);
                    } else {
                        hInicio.getEditText().setText(hourOfDay + ":0" + minute);
                    }
                    schedule.getStartTime().setHour(hourOfDay);
                    schedule.getStartTime().setMinute(minute);
                }
            };
        });
        hFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context, listener, schedule.getEndTime().getHour(), schedule.getEndTime().getMinute(), true);
                dialog.show();
            }

            private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (minute > 9) {
                        hFin.getEditText().setText(hourOfDay + ":" + minute);
                    } else {
                        hFin.getEditText().setText(hourOfDay + ":0" + minute);
                    }
                    schedule.getEndTime().setHour(hourOfDay);
                    schedule.getEndTime().setMinute(minute);
                }
            };
        });


        hInicioText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hInicio.performClick();
            }
        });
        hInicioText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hInicio.performClick();
                }
            }
        });

        hFinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hFin.performClick();
            }
        });
        hFinText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hFinText.performClick();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_guardar:
                schedule.setClassTitle(asignatura.getEditText().getText().toString());
                if (mode == HorarioActivity.REQUEST_ADD) {
                    Intent i = new Intent();
                    ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                    schedules.add(schedule);
                    i.putExtra("schedules", schedules);
                    setResult(RESULT_OK_ADD, i);
                    finish();
                } else if (mode == HorarioActivity.REQUEST_EDIT) {
                    Intent i = new Intent();
                    ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                    schedules.add(schedule);
                    i.putExtra("idx", editIdx);
                    i.putExtra("schedules", schedules);
                    setResult(RESULT_OK_EDIT, i);
                    finish();
                }
                return true;
            case R.id.accion_borrar:

                Intent i = new Intent();
                i.putExtra("idx", editIdx);
                setResult(RESULT_OK_DELETE, i);
                finish();
                return true;
            case android.R.id.home:

                finish();
                return true;
            default:
                return true;
        }
    }


    private void cargarDatos() {
        Intent i = getIntent();
        editIdx = i.getIntExtra("idx", -1);
        ArrayList<Schedule> schedules = (ArrayList<Schedule>) i.getSerializableExtra("schedules");
        schedule = schedules.get(0);
        asignatura.getEditText().setText(schedule.getClassTitle());
        day.setSelection(schedule.getDay());

        int mInicio = schedule.getStartTime().getMinute();
        int mFin = schedule.getEndTime().getMinute();
        if (mInicio > 9) {
            hInicio.getEditText().setText(schedule.getStartTime().getHour() + ":" + mInicio);
        } else {
            hInicio.getEditText().setText(schedule.getStartTime().getHour() + ":0" + mInicio);
        }
        if (mFin > 9) {
            hFin.getEditText().setText(schedule.getEndTime().getHour() + ":" + mFin);
        } else {
            hFin.getEditText().setText(schedule.getEndTime().getHour() + ":0" + mFin);
        }
    }

}
