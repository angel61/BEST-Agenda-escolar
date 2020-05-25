package com.example.calendarioescolar.Actividades;

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
import com.example.calendarioescolar.Fragmentos.HorarioFragment;
import com.example.calendarioescolar.Modelo.AsignaturasBD;
import com.example.calendarioescolar.R;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
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
        setContentView(R.layout.activity_editar_horario);
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
        mode = i.getIntExtra("mode", HorarioFragment.REQUEST_ADD);

        if (mode == HorarioFragment.REQUEST_EDIT) {
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

                array = casosUsoAsignatura.arrayAsignaturas(array);

                final String[] a = array.toArray(new String[0]);
                final int[] aux = new int[1];
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditarHorarioActivity.this);
                builder.setTitle("Seleccionar una asignatura")
                        .setSingleChoiceItems(a,0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                aux[0] =which;
                            }
                        });
                builder.setPositiveButton("Seleccionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        asignatura.getEditText().setText(a[aux[0]]);
                    }
                });
                builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        casosUsoAsignatura.eliminarAsig(aux[0]);
                        asignatura.getEditText().setText("");
                    }
                });
                builder.setNeutralButton("Añadir", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        casosUsoAsignatura.dialogoAnnadirAsignatura();
                    }
                });
                builder.show();
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
                if (mode == HorarioFragment.REQUEST_ADD) {
                    if (asignatura.getEditText().getText().toString().length() > 0 && hInicio.getEditText().getText().length() > 0 && hFin.getEditText().getText().length() > 0) {
                        Intent i = new Intent();
                        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                        schedules.add(schedule);
                        i.putExtra("schedules", schedules);
                        setResult(RESULT_OK_ADD, i);
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Faltan campos por rellenar")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (asignatura.getEditText().getText().toString().length() <= 0) {
                                            asignatura.setError("Campo vacio");
                                            asignatura.setErrorEnabled(true);
                                        }
                                        if (hInicio.getEditText().getText().toString().length() <= 0) {
                                            hInicio.setError("Campo vacio");
                                            hInicio.setErrorEnabled(true);
                                        }
                                        if (hFin.getEditText().getText().toString().length() <= 0) {
                                            hFin.setError("Campo vacio");
                                            hFin.setErrorEnabled(true);
                                        }
                                    }
                                })
                                .show();
                    }
                } else if (mode == HorarioFragment.REQUEST_EDIT) {
                    Intent i = new Intent();
                    ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                    schedules.add(schedule);
                    i.putExtra("idx", editIdx);
                    i.putExtra("schedules", schedules);
                    setResult(RESULT_OK_EDIT, i);
                    finish();
                }
                ((Aplicacion)getApplication()).home.incializarListView();
                return true;
            case R.id.accion_borrar:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Deseas eliminar este elemento del horario?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                casosUsoAsignatura.borrar(editIdx);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                ((Aplicacion)getApplication()).home.incializarListView();
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

        int horaInicio = schedule.getStartTime().getHour();
        int mInicio = schedule.getStartTime().getMinute();
        int horaFin = schedule.getEndTime().getHour();
        int mFin = schedule.getEndTime().getMinute();
        Calendar Inicio = Calendar.getInstance();
        Inicio.set(Calendar.MINUTE, mInicio);
        Inicio.set(Calendar.HOUR_OF_DAY, horaInicio);
        SimpleDateFormat sdm = new SimpleDateFormat("HH:mm");
        String inicioStr = sdm.format(Inicio.getTime());

        Calendar Fin = Calendar.getInstance();
        Fin.set(Calendar.MINUTE, mFin);
        Inicio.set(Calendar.HOUR_OF_DAY, horaFin);
        String finStr = sdm.format(Fin.getTime());

        hInicio.getEditText().setText(inicioStr);

        hFin.getEditText().setText(finStr);
    }

}
