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
import androidx.recyclerview.widget.DividerItemDecoration;
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

    public AgendaBD agendaBD;
    public AdaptadorAgendaBD adaptador;
    private CasosUsoAO casosUsoAO;
    private RecyclerView recycler;
    private FloatingActionButton fab;
    private RippleDrawable rippleDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adaptador = ((Aplicacion) getApplication()).adaptador;

        agendaBD = ((Aplicacion) getApplication()).agendaBD;

        casosUsoAO=new CasosUsoAO(this,agendaBD,adaptador);

        recycler = findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recycler.setAdapter(adaptador);
        fab = findViewById(R.id.fab);
        inicializarListeners();

    }
    private void inicializarListeners() {


        final GestureDetector GestureDetector =
                new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTapEvent(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                });

        recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                try {
                    View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                    if (child != null) {




                        if (GestureDetector.onTouchEvent(motionEvent)) {

                            rippleDrawable = (RippleDrawable) child.getBackground();
                            rippleDrawable.setHotspot(motionEvent.getX(), motionEvent.getY());
                            rippleDrawable.setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});

                            int pos = recyclerView.getChildAdapterPosition(child);
                            casosUsoAO.mostrar(pos, 1);
                            return true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                casosUsoAO.nuevo();
            }
        });
    }


}