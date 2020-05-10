package com.example.calendarioescolar.Presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_elemento_agenda);
        extras = getIntent().getExtras();
        pos = extras.getInt("pos", 0);
        agBD = ((Aplicacion) getApplication()).agendaBD;
        adaptador = ((Aplicacion) getApplication()).adaptador;
        _id = adaptador.idPosicion(pos);
        agendaObject = adaptador.agendaPosicion(pos);
        casosUso=new CasosUsoAO(this,agBD,adaptador);
        actualizarVistas ();
        listeners();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detalle_ao, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_editar:
                casosUso.editar(pos, RESULTADO_EDITAR);
                return true;
            case R.id.accion_borrar:
                casosUso.borrar(_id);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void listeners() {
    }

    private void actualizarVistas (){
        fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        setTitle(agendaObject.getTitulo());

       /* TextView tvTitulo=findViewById(R.id.titleOb);
        tvTitulo.setText(agendaObject.getTitulo());

        btnborrar=findViewById(R.id.btnBorrar);
        btnEditar=findViewById(R.id.btnEditar);*/
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
