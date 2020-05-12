package com.example.calendarioescolar.Presentacion;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.calendarioescolar.R;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private int fragment = 0;
    private FloatingActionButton fab;
    public AgendaBD agendaBD;
    public AdaptadorAgendaBD adaptador;
    private CasosUsoAO casosUsoAO;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;
    private TimetableView timetable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        adaptador = ((Aplicacion) getApplication()).adaptador;

        agendaBD = ((Aplicacion) getApplication()).agendaBD;

        casosUsoAO = new CasosUsoAO(this, agendaBD, adaptador);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Selecciona el tipo de actividad")
                        .setItems(R.array.tipo_agenda, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                casosUsoAO.nuevo(which);
                            }
                        }).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_agenda, R.id.nav_horario)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
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
                }else if(desti.equals("Agenda")){
                    fragment=2;
                    fab.setVisibility(View.VISIBLE);
                }
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
            }
        });
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (fragment == 1) {
            getMenuInflater().inflate(R.menu.horario, menu);
        } else {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


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
