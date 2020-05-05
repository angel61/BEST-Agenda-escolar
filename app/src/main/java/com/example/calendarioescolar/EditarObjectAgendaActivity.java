package com.example.calendarioescolar;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.agenda_object;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EditarObjectAgendaActivity extends AppCompatActivity {
    public AgendaBD agendaBD;
    private CasosUsoAO casosUso;
    private int pos;
    private agenda_object agendaObject;

    private TextInputLayout titulo;
    private TextInputLayout comentario;
    private TextInputLayout asignatura;
    private TextView hora;
    private CalendarView calendario;

    private AdaptadorAgendaBD adaptador;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private int _id;
    private int tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_layout);
        Bundle extras = getIntent().getExtras();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pos = extras.getInt("pos", 0);
        tab = extras.getInt("tab", 0);
        agendaBD = ((Aplicacion) getApplication()).agendaBD;
        if(tab==0) {
            adaptador = ((Aplicacion) getApplication()).adaptador;
        } else {
            adaptador = ((Aplicacion) getApplication()).adaptadorPa;
        }
        casosUso = new CasosUsoAO(this, agendaBD, adaptador,((Aplicacion) getApplication()));
        agendaObject = adaptador.agendaPosicion(pos);
        _id=agendaObject.getId();
        actualizaVistas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editar_agenda_object, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_guardar:
                agendaObject.setTitulo(titulo.getEditText().getText().toString());
                agendaObject.setComentario(comentario.getEditText().getText().toString());
                agendaObject.setAsig(asignatura.getEditText().getText().toString());
                casosUso.guardar(_id, agendaObject);
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
        asignatura=findViewById(R.id.asignaturae);
        if(agendaObject.getTipoAg().getTexto().equals("Recordatorio")){
            asignatura.setVisibility(View.GONE);
        }else{
            asignatura.getEditText().setText(agendaObject.getAsig());
        }
        calendario=findViewById(R.id.calendarioe);
        calendario.setDate(agendaObject.getFecha());
        Calendar horaDate=Calendar.getInstance();
        horaDate.setTimeInMillis(agendaObject.getFecha());
        hora=findViewById(R.id.hora);
        hora.setText(horaDate.get(Calendar.HOUR_OF_DAY)+":"+horaDate.get(Calendar.MINUTE));

        CalendarView.OnDateChangeListener list=new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar cal= Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                cal.set(Calendar.MONTH,month);
                cal.set(Calendar.YEAR,year);
                seleccionarHora(cal.getTimeInMillis());
            }
        };
        calendario.setOnDateChangeListener(list);

    }

    private void seleccionarHora(long milis) {
        final Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis(milis);
        cal.set(Calendar.SECOND, 0);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minutes);
                hora.setText(hourOfDay+":"+minutes);
                agendaObject.setFecha(cal.getTimeInMillis());
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (_id == -1) agendaBD.borrar(_id);
        finish();
    }
}
