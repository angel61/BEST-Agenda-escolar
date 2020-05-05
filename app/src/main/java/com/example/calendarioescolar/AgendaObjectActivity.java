package com.example.calendarioescolar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.agenda_object;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class AgendaObjectActivity extends Activity {

    public AgendaBD agBD;
    private int pos;
    private agenda_object agendaObject;
    final static int RESULTADO_EDITAR = 1;

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private AdaptadorAgendaBD adaptador;
    private Toolbar toolb;
    private Bundle extras;
    public int _id = -1;
    private Button btnEditar;
    private Button btnborrar;
    private CasosUsoAO casosUso;
    private int tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_elemento_agenda);
        extras = getIntent().getExtras();
        pos = extras.getInt("pos", 0);
        tab = extras.getInt("tab", 0);
        agBD = ((Aplicacion) getApplication()).agendaBD;
        if(tab==0) {
            adaptador = ((Aplicacion) getApplication()).adaptador;
        } else {
            adaptador = ((Aplicacion) getApplication()).adaptadorPa;
        }
        _id = adaptador.idPosicion(pos);
        agendaObject = adaptador.agendaPosicion(pos);
        casosUso=new CasosUsoAO(this,agBD,adaptador,((Aplicacion) getApplication()));
        actualizarVistas ();
        listeners();

    }

    private void listeners() {
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casosUso.editar(pos, RESULTADO_EDITAR,tab);
            }
        });
        btnborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void actualizarVistas (){
        fab = findViewById(R.id.fab);
        TextView tvComentario=findViewById(R.id.tv_comentario);
        tvComentario.setText(agendaObject.getComentario());

        TextView tvAsignatura = findViewById(R.id.tv_asignatura);
        if(agendaObject.getAsig().length()>0) {
            tvAsignatura.setText(agendaObject.getAsig());
        }else{
            LinearLayout ll=findViewById(R.id.ll_asignatura);
            ll.setVisibility(LinearLayout.GONE);
        }

        TextView tvFecha=findViewById(R.id.tv_Fecha);
        tvFecha.setText(new SimpleDateFormat("HH:mm dd/MM/yy").format(new Date(agendaObject.getFecha())));

        TextView tvTipo=findViewById(R.id.tv_Tipo);
        tvTipo.setText(agendaObject.getTipoAg().getTexto());

        TextView tvTitulo=findViewById(R.id.titleOb);
        tvTitulo.setText(agendaObject.getTitulo());

        btnborrar=findViewById(R.id.btnBorrar);
        btnEditar=findViewById(R.id.btnEditar);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULTADO_EDITAR) {
            agendaObject = agBD.elemento(_id);
            actualizarVistas();
        }
    }
}
