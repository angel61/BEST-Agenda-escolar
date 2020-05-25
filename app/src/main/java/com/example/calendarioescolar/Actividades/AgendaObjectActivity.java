package com.example.calendarioescolar.Actividades;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.calendarioescolar.Adaptadores.AdaptadorAgendaBD;
import com.example.calendarioescolar.Aplicacion;
import com.example.calendarioescolar.CasosDeUso.CasosUsoAO;
import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.agenda_object;
import com.example.calendarioescolar.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class AgendaObjectActivity extends AppCompatActivity {

    public AgendaBD agBD;
    private int pos;
    private agenda_object agendaObject;
    public final static int RESULTADO_EDITAR = 1;

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private AdaptadorAgendaBD adaptador;
    private Toolbar toolb;
    private Bundle extras;
    public int _id = -1;
    private Button btnEditar;
    private Button btnborrar;
    private CasosUsoAO casosUso;
    private int home_agenda;
    public String titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elemento_agenda);
        extras = getIntent().getExtras();
        pos = extras.getInt("pos", 0);
        home_agenda = extras.getInt("codR", 0);
        agBD = ((Aplicacion) getApplication()).agendaBD;
        if (home_agenda == 1) {
            adaptador = ((Aplicacion) getApplication()).adaptador;
        } else {
            adaptador = ((Aplicacion) getApplication()).adaptadorHome;
        }
        _id = adaptador.idPosicion(pos);
        agendaObject = adaptador.agendaPosicion(pos);
        casosUso = new CasosUsoAO(this, agBD, adaptador);
        titulo=agendaObject.getTitulo();
        setTitle(titulo);
        actualizarVistas();
        listeners();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_elemento_agenda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_editar:
                casosUso.editar(pos, RESULTADO_EDITAR);
                return true;
            case R.id.accion_borrar:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Deseas eliminar la actividad?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                casosUso.borrar(_id);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void listeners() {
    }

    private void actualizarVistas() {
        titulo=agendaObject.getTitulo();
        fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tvComentario = findViewById(R.id.tv_comentario);
        tvComentario.setText(agendaObject.getComentario());
        if (agendaObject.getComentario().trim().length() <= 0) {
            LinearLayout ll = findViewById(R.id.ll_comentario);
            ll.setVisibility(LinearLayout.GONE);
        }

        TextView tvAsignatura = findViewById(R.id.tv_asignatura);
        if (agendaObject.getAsig().length() > 0) {
            tvAsignatura.setText(agendaObject.getAsig());
        } else {
            LinearLayout ll = findViewById(R.id.ll_asignatura);
            ll.setVisibility(LinearLayout.GONE);
        }

        TextView tvFecha = findViewById(R.id.tv_Fecha);
        tvFecha.setText(new SimpleDateFormat("HH:mm dd/MM/yy").format(new Date(agendaObject.getFecha())));

        TextView tvTipo = findViewById(R.id.tv_Tipo);
        tvTipo.setText(agendaObject.getTipoAg().getTexto());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        casosUso.mostrar(pos, 1);
        finish();
    }
}
