package com.example.calendarioescolar;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;

import com.example.calendarioescolar.Modelo.AgendaBD;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabs;
    private ViewPager vp;
    private ViewPagerAdapter vpa;
    public AgendaBD agendaBD;
    public AdaptadorAgendaBD adaptador;
    public AdaptadorAgendaBD adaptadorPa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adaptador = ((Aplicacion) getApplication()).adaptador;
        adaptadorPa = ((Aplicacion) getApplication()).adaptadorPa;

        agendaBD = ((Aplicacion) getApplication()).agendaBD;



        vp = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
        vpa= new ViewPagerAdapter(getSupportFragmentManager());

        vpa.AddFragment(new FragmentPresent(),"Proximas");
        vpa.AddFragment(new FragmentPast(),"Pasadas");

        vp.setAdapter(vpa);
        tabs.setupWithViewPager(vp);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}