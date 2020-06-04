package com.example.calendarioescolar.Actividades;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.calendarioescolar.Adaptadores.AdaptadorAgendaBD;
import com.example.calendarioescolar.Aplicacion;
import com.example.calendarioescolar.CasosDeUso.CasosUsoAO;
import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.TipoAgenda;
import com.example.calendarioescolar.R;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

/**
 * Clase principal en la que se muestran los fragments y se inicializa el navigation drawer.
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 * @see androidx.appcompat.app.AppCompatActivity
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private int fragment = 0;
    private FloatingActionButton fab;
    public AgendaBD agendaBD;
    public AdaptadorAgendaBD adaptador;
    private CasosUsoAO casosUsoAO;
    private TimetableView timetable;
    private NavController navController;


    /**
     * El argumento Bundle
     * contiene el estado ya guardado de la actividad.
     * Si la actividad nunca ha existido, el valor del objeto Bundle es nulo.
     * <p>
     * muestra la configuración básica de la actividad, como declarar
     * la interfaz de usuario (definida en un archivo XML de diseño),
     * definir las variables de miembro y configurar parte de la IU
     * </p>
     *
     * @param savedInstanceState objeto Bundle que contiene el estado de la actividad.
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iniciar();
        iniciarListeners();
    }

    /**
     * El metodo inicializa los componentes de la actividad.
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void iniciar() {


        adaptador = ((Aplicacion) getApplication()).adaptador;

        agendaBD = ((Aplicacion) getApplication()).agendaBD;

        casosUsoAO = new CasosUsoAO(this, agendaBD, adaptador);

        fab = findViewById(R.id.fab);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_agenda, R.id.nav_horario)
                .setDrawerLayout(drawer)
                .build();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    /**
     * Se encarga de inicializar los listeners
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void iniciarListeners() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final TipoAgenda[] items = {
                        TipoAgenda.RECORDATORIO,
                        TipoAgenda.EJERCICIOS,
                        TipoAgenda.TRABAJO,
                        TipoAgenda.EXAMEN
                };
                ArrayAdapter<TipoAgenda> arrayAdapter = new ArrayAdapter<TipoAgenda>(
                        MainActivity.this,
                        android.R.layout.select_dialog_item,
                        android.R.id.text1,
                        items) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        TextView tv = (TextView) v.findViewById(android.R.id.text1);
                        tv.setText(items[position].getTexto());

                        int dp36 = (int) (36 * getResources().getDisplayMetrics().density + 0.5f);
                        Drawable img = getDrawable(items[position].getRecurso());
                        img.setBounds(0, 0, dp36, dp36);
                        tv.setCompoundDrawables(img, null, null, null);

                        int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                        tv.setCompoundDrawablePadding(dp5);

                        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                        return v;
                    }
                };

                ListAdapter adapter = arrayAdapter;

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Selecciona el tipo de actividad")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                casosUsoAO.nuevo(item);
                            }
                        }).show();
            }
        });
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                CharSequence desti = destination.getLabel();
                System.out.println(destination.getLabel());
                fragment = 0;
                fab.setVisibility(View.GONE);
                if (desti.equals("Horario")) {
                    fragment = 1;
                    timetable = findViewById(R.id.timetable);
                } else if (desti.equals("Agenda")) {
                    fragment = 2;
                    fab.setVisibility(View.VISIBLE);
                }
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
            }
        });
    }

    /**
     * Se encarga de mostrar el menu personalizado en la actividad
     *
     * @param menu objeto Menu que va a ser inicializado.
     * @return boolean
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (fragment == 1) {
            getMenuInflater().inflate(R.menu.horario, menu);
        } else if (fragment == 2) {
            getMenuInflater().inflate(R.menu.agenda, menu);
        } else {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    /**
     * Se encarga de mostrar el navigation drawer
     *
     * @return boolean
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
                return false;
            case R.id.accion_limpiar:
                return false;

        }
        return false;
    }
}
