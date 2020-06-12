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

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Clase para controlar la actividad de la interfaz de activity_elemento_agenda, sus elementos
 * y el control de eventos.
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 * @see androidx.appcompat.app.AppCompatActivity
 */
public class AgendaObjectActivity extends AppCompatActivity {

    public AgendaBD agBD;
    private int pos;
    private agenda_object agendaObject;
    public final static int RESULTADO_EDITAR = 1;

    private Toolbar toolbar;
    private AdaptadorAgendaBD adaptador;
    private Bundle extras;
    public int _id = -1;
    private Button btnEditar;
    private Button btnborrar;
    private CasosUsoAO casosUso;
    private int home_agenda;
    public String titulo;

    /**
     * Establece la interfaz de la actividad y lanza los metodos que realizan funciones esenciales de la actividad
     *
     * @param savedInstanceState objeto Bundle que contiene el estado de la actividad.
     * @author Angel Lopez Palacios
     * @version 1.0
     * @see AgendaObjectActivity#iniciar
     * @see AgendaObjectActivity#actualizarVistas
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elemento_agenda);
        iniciar();
        actualizarVistas();

    }

    /**
     * Se encarga de extraer la informacion que va a utilizar la actividad
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     * @see CasosUsoAO
     * @see AgendaBD
     * @see AdaptadorAgendaBD
     * @see agenda_object
     */
    private void iniciar() {
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
        titulo = agendaObject.getTitulo();
        setTitle(titulo);
    }

    /**
     * Se encarga de mostrar el menu personalizado en la actividad
     *
     * @param menu objeto Menu que va a ser inicializado.
     * @return
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_elemento_agenda, menu);
        return true;
    }


    /**
     * Se encarga de dar funcionalidad a las opciones del menu
     *
     * @param item objeto MenuItem que contiene el elemento que ha sido pulsado del menu.
     * @return boolean
     * @author Angel Lopez Palacios
     * @version 1.0
     * @see CasosUsoAO
     * @see AlertDialog
     */
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
            case R.id.accion_compartir:
                casosUso.compartir(agendaObject);
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Su uso esta destinado a mostrar la informacion obtenida en los elementos de la interfaz.
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    private void actualizarVistas() {
        titulo = agendaObject.getTitulo();
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

    /**
     * Este evento se encarga de volver a cargar la actividad despues de editar el elemento de la agenda.
     *
     * @param requestCode numero de solicitud enviado desde esta actividad.
     * @param resultCode  numero de resultado recivido desde la actividad {@link EditarObjectAgendaActivity}
     * @param data        objeto Intent el cual se recive la intencion.
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULTADO_EDITAR) {
            casosUso.mostrar(pos, 1);
            finish();
        }
    }
}
